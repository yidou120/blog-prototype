package com.edou.blog.controller;

import com.edou.blog.domain.Blog;
import com.edou.blog.domain.Catalog;
import com.edou.blog.domain.User;
import com.edou.blog.domain.Vote;
import com.edou.blog.service.BlogService;
import com.edou.blog.service.CatalogService;
import com.edou.blog.service.UserService;
import com.edou.blog.util.ConstraintViolationExceptionHandler;
import com.edou.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName UserspaceController
 * @Description 用户主页控制器
 * @Author 中森明菜
 * @Date 2020/3/18 17:44
 * @Version 1.0
 */
@Controller
@RequestMapping("/u")
public class UserspaceController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CatalogService catalogService;

    //返回个人设置页面
    @GetMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView profile(@PathVariable("username") String username, Model model){
        User user = (User)userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        return new ModelAndView("/userspace/profile","userModel",model);
    }

    //保存个人设置
    @PostMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public String saveProfile(@PathVariable("username") String username, User user){
        //根据userId查询用户原始信息
        User originUser = userService.getUserById(user.getId());
        //保存设置的邮箱 姓名信息
        originUser.setEmail(user.getEmail());
        originUser.setName(user.getName());
        //进行密码校验 判断密码是否更改
        String rawPassword = originUser.getPassword();
        String newPassword = user.getPassword();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodeNewPassword = passwordEncoder.encode(newPassword);
        boolean matches = passwordEncoder.matches(rawPassword, encodeNewPassword);
        if(!matches){
            originUser.setEncodePassword(newPassword);
        }
        //保存
        userService.saveUser(originUser);
        return "redirect:/u/"+username+"/profile";
    }

    //返回编辑头像页面
    @GetMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView avatar(@PathVariable("username") String username,Model model){
        User user = (User)userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        return new ModelAndView("/userspace/avatar","userModel",model);
    }

    //保存头像
    @PostMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveAvatar(@PathVariable("username") String username,@RequestBody User user){
        //获取图片url
        String avatarUrl = user.getAvatar();
        //根据id查询user
        User originUser = userService.getUserById(user.getId());
        //设置url
        originUser.setAvatar(avatarUrl);
        //保存
        userService.saveUser(originUser);
        //返回
        return ResponseEntity.ok().body(new Response(true,"处理成功",avatarUrl));
    }

    //返回用户个人主页
    @GetMapping("/{username}")
    public String userSpace(@PathVariable("username") String username,Model model) {
        User user = (User)userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        return "redirect:/u/"+username+"/blogs";
    }

    //可以根据分类，最新最热，关键词查找用户文章列表
    @GetMapping("/{username}/blogs")
    public String listBlogsByOrder(@PathVariable("username") String username,
                                   @RequestParam(value="order",required=false,defaultValue="new") String order,
                                   @RequestParam(value="catalog",required=false ) Long catalogId,
                                   @RequestParam(value="keyword",required=false ) String keyword,
                                   @RequestParam(value = "async",required = false) boolean async,
                                   @RequestParam(value = "pageIndex",required = false,defaultValue = "0") int pageIndex,
                                   @RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize,
                                   Model model) {

        //根据用户名查询用户
        User user = (User)userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        //是否是博主本人标志
        User principal = null;
        boolean isBlogOwner = false;
        if(Objects.nonNull(SecurityContextHolder.getContext().getAuthentication())&&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated()&&
                !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")
        ){
            principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(Objects.nonNull(principal) && username.equals(principal.getUsername())){
                isBlogOwner = true;
            }
        }

        Page<Blog> page = null;
        if(Objects.nonNull(catalogId)&&catalogId>0){
            Catalog catalog = catalogService.getCatalogById(catalogId);
//            Pageable pageable = new PageRequest(pageIndex,pageSize);
            Pageable pageable = PageRequest.of(pageIndex,pageSize);
            page = blogService.listBlogsByCatalog(catalog,pageable);
            order = "";
        } else if(order.equals("hot")){
            Sort sort = new Sort(Sort.Direction.DESC,"readSize","commentSize","voteSize");
            Pageable pageable = PageRequest.of(pageIndex,pageSize,sort);
//            Pageable pageable = new PageRequest(pageIndex,pageSize,sort);
            page = blogService.listBlogsByTitleVoteAndSort(user,keyword,pageable);
        } else if(order.equals("new")){
            Pageable pageable = PageRequest.of(pageIndex,pageSize);
//            Pageable pageable = new PageRequest(pageIndex,pageSize);
            page = blogService.listBlogsByTitleVote(user,keyword,pageable);
        }
        List<Blog> content = page.getContent();
        model.addAttribute("isCatalogsOwner",isBlogOwner);
        model.addAttribute("user",user);
        model.addAttribute("keyword",keyword);
        model.addAttribute("catalogId",catalogId);
        model.addAttribute("order",order);
        model.addAttribute("page",page);
        model.addAttribute("blogList",content);
        return (async==true?"/userspace/u :: #mainContainerRepleace":"/userspace/u");
    }


    //获取博文展示页面
    @GetMapping("/{username}/blogs/{id}")
    public String getBlogById(@PathVariable("username") String username,
                              @PathVariable("id") Long id,
                              Model model){
        Blog blog = blogService.getBlogById(id);
        User principal = null;
        //每次访问阅读量自增1
        blogService.readingIncrease(id);


        // 判断操作用户是否是博客的所有者
        boolean isBlogOwner = false;
        if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal !=null && username.equals(principal.getUsername())) {
                isBlogOwner = true;
            }
        }

        //判断当前用户是否点赞本篇博客
        Vote currentVote = null;
        List<Vote> votes = blog.getVotes();

        //如果当前没有用户登录 说明没有点赞权限
        if(Objects.nonNull(principal)){
            for(Vote vote:votes){
                if(vote.getUser().getUsername().equals(principal.getUsername())){
                    currentVote = vote;
                    break;
                }
            }
        }
        model.addAttribute("isBlogOwner", isBlogOwner);
        model.addAttribute("blogModel",blogService.getBlogById(id));
        model.addAttribute("currentVote",currentVote);
        return "/userspace/blog";
    }

    //返回新建博客页面
    @GetMapping("/{username}/blogs/edit")
    public ModelAndView editBlog(@PathVariable("username") String username,Model model) {
        // 获取用户分类列表
        User user = (User)userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogs = catalogService.listCatalogs(user);
        model.addAttribute("blog",new Blog(null,null,null));
        model.addAttribute("catalogs", catalogs);
        return new ModelAndView("/userspace/blogedit","blogModel",model);
    }

    //返回编辑博文页面
    @GetMapping("/{username}/blogs/edit/{id}")
    public ModelAndView editBlog(@PathVariable("username") String username,@PathVariable("id") Long id, Model model) {
        // 获取用户分类列表
        User user = (User)userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogs = catalogService.listCatalogs(user);

        model.addAttribute("blog", blogService.getBlogById(id));
        model.addAttribute("catalogs", catalogs);
        return new ModelAndView("/userspace/blogedit", "blogModel", model);
    }

    //删除博文
    @DeleteMapping("/{username}/blogs/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> deleteBlog(@PathVariable("username") String username,
                                           @PathVariable("id") Long id){
        try {
            blogService.removeBlog(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }
        String redirectUrl = "/u/"+username+"/blogs";
        return ResponseEntity.ok().body(new Response(true,"处理成功",redirectUrl));
    }

    //保存博文
    @PostMapping("/{username}/blogs/edit")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveBlog(@PathVariable("username") String username,
                                             @RequestBody Blog blog){
        // 对 Catalog 进行空处理
        if (blog.getCatalog().getId() == null) {
            return ResponseEntity.ok().body(new Response(false,"未选择分类"));
        }
        //判断是更新还是新增
        try {
            if(Objects.isNull(blog.getId())){
                //新增
                User user = (User)userDetailsService.loadUserByUsername(username);
                blog.setUser(user);
                blogService.saveBlog(blog);
            }else{
                //更新
                Blog originBlog = blogService.getBlogById(blog.getId());
                originBlog.setTitle(blog.getTitle());
                originBlog.setContent(blog.getContent());
                originBlog.setSummary(blog.getSummary());
                originBlog.setCatalog(blog.getCatalog());
                originBlog.setTags(blog.getTags());
                blogService.saveBlog(originBlog);
            }
        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }
        String redirectUrl = "/u/"+username+"/blogs/"+blog.getId();
        return ResponseEntity.ok().body(new Response(true,"处理成功",redirectUrl));
    }

}

package com.edou.blog.controller;

import com.edou.blog.domain.User;
import com.edou.blog.service.UserService;
import com.edou.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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

    //返回个人设置页面
    @GetMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView profile(@PathVariable("username") String username, Model model){
        UserDetails user = userDetailsService.loadUserByUsername(username);
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
        UserDetails user = userDetailsService.loadUserByUsername(username);
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
    public String userSpace(@PathVariable("username") String username) {
        System.out.println("username" + username);
        return "u";
    }

    //可以根据分类，最新最热，关键词查找用户文章列表
    @GetMapping("/{username}/blogs")
    public String listBlogsByOrder(@PathVariable("username") String username,
                                   @RequestParam(value="order",required=false,defaultValue="new") String order,
                                   @RequestParam(value="category",required=false ) Long category,
                                   @RequestParam(value="keyword",required=false ) String keyword) {

        if (category != null) {

            System.out.print("category:" +category );
            System.out.print("selflink:" + "redirect:/u/"+ username +"/blogs?category="+category);
            return "/u";

        } else if (keyword != null && keyword.isEmpty() == false) {

            System.out.print("keyword:" +keyword );
            System.out.print("selflink:" + "redirect:/u/"+ username +"/blogs?keyword="+keyword);
            return "/u";
        }

        System.out.print("order:" +order);
        System.out.print("selflink:" + "redirect:/u/"+ username +"/blogs?order="+order);
        return "/u";
    }

    //根据文章id显示用户具体的文章页面
    @GetMapping("/{username}/blogs/{id}")
    public String listBlogsByOrder(@PathVariable("id") Long id) {

        System.out.print("blogId:" + id);
        return "/blog";
    }

    //新建一个博客
    @GetMapping("/{username}/blogs/edit")
    public String editBlog() {

        return "/blogedit";
    }
}

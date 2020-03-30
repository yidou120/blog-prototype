package com.edou.blog.controller;

import com.edou.blog.domain.Authority;
import com.edou.blog.domain.User;
import com.edou.blog.repository.UserRepository;
import com.edou.blog.repository.UserRepositoryByDataBase;
import com.edou.blog.service.AuthorityService;
import com.edou.blog.service.UserService;
import com.edou.blog.util.ConstraintViolationExceptionHandler;
import com.edou.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @ClassName BlogController
 * @Description 用户管理controller
 * @Author 中森明菜
 * @Date 2020/3/8 14:03
 * @Version 1.0
 */
@RestController
@RequestMapping("/users")
@PreAuthorize("hasAuthority('ROLE_ADMIN')") // 指定角色权限才能操作方法
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    AuthorityService authorityService;

    //查询所有用户 获取所有用户列表 参数 async pageIndex pageSize pageName
    @GetMapping
    public ModelAndView list(@RequestParam(value="async",required=false) boolean async,
                             @RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
                             @RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
                             @RequestParam(value="name",required=false,defaultValue="") String name,
                             Model model){
        Pageable pageable = PageRequest.of(pageIndex,pageSize);
//        Pageable pageable = new PageRequest(pageIndex, pageSize);
        Page<User> page = userService.listUsersByNameLike(name, pageable);
        List<User> list = page.getContent();	// 当前所在页面数据列表

        model.addAttribute("page", page);
        model.addAttribute("userList", list);
        return new ModelAndView(async==true?"users/list :: #mainContainerRepleace":"users/list", "userModel", model);
    }

    //返回添加用户页面
    @GetMapping("/add")
    public ModelAndView add(Model model){
        model.addAttribute("user",new User(null,null,null,null));
        return new ModelAndView("/users/add","userModel",model);
    }

    //新建用户
    @PostMapping
    public ResponseEntity<Response> create(User user,Long authorityId){
        //根据authorityId查询角色权限信息
        List<Authority> authorityList = new ArrayList<>();
        Authority authority = authorityService.getAuthorityById(authorityId);
        authorityList.add(authority);
        //设置到用户信息中
        user.setAuthorities(authorityList);
        //判断用户id是否为空
        if(Objects.isNull(user.getId())){
            //为空 设置密码
            user.setEncodePassword(user.getPassword()); //加密密码
        }else{
            //如果不为空，就是更新
            // 判断密码是否做了变更
            User originalUser = userService.getUserById(user.getId());
            String rawPassword = originalUser.getPassword();
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodePasswd = encoder.encode(user.getPassword());
            boolean isMatch = encoder.matches(rawPassword, encodePasswd);
            if (!isMatch) {
                user.setEncodePassword(user.getPassword());
            }else {
                user.setPassword(user.getPassword());
            }
        }
        //保存
        try {
            userService.saveUser(user);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }
        return ResponseEntity.ok().body(new Response(true,"处理成功",user));
    }

    //删除用户
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable("id")Long id, Model model){
        try {
            userService.removeUser(id);
        } catch (Exception e) {
            return ok().body(new Response(false,e.getMessage()));
        }
        return ok().body(new Response(true,"删除成功"));
    }

    //返回修改用户界面 以及数据回写
    @GetMapping("/edit/{id}")
    public ModelAndView modifyForm(@PathVariable("id") Long id,Model model){
        User user = userService.getUserById(id);
        model.addAttribute("user",user);
        return new ModelAndView("/users/edit","userModel",model);
    }
}

package com.edou.blog.controller;

import com.edou.blog.domain.Authority;
import com.edou.blog.domain.User;
import com.edou.blog.service.AuthorityService;
import com.edou.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MainController
 * @Description 主页控制器
 * @Author 中森明菜
 * @Date 2020/3/18 17:43
 * @Version 1.0
 */
@Controller
public class MainController {

    private static final Long ROLE_USER_AUTHORITY_ID = 2L;

    @Autowired
    UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        model.addAttribute("errorMsg", "登陆失败，用户名或者密码错误！");
        return "login";
    }

    //返回注册页面
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    //提交注册信息 返回到登录页面
    @PostMapping("/register")
    public String register(User user){
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authorityService.getAuthorityById(ROLE_USER_AUTHORITY_ID));
        user.setAuthorities(authorities);
        user.setEncodePassword(user.getPassword());
        userService.saveUser(user);
        return "redirect:/login";
    }

    @GetMapping("/search")
    public String search() {
        return "search";
    }
}

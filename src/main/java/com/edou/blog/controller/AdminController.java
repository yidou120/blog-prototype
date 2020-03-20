package com.edou.blog.controller;

import com.edou.blog.vo.Menu;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName AdminController
 * @Description 用户控制器
 * @Author 中森明菜
 * @Date 2020/3/18 17:39
 * @Version 1.0
 */
@Controller
@RequestMapping("/admins")
public class AdminController {

    //获取后台管理主页面
    @GetMapping
    public ModelAndView listUsers(Model model){
        List<Menu> list = new ArrayList<>();
        list.add(new Menu("用户管理", "/users"));
        list.add(new Menu("角色管理", "/roles"));
        list.add(new Menu("博客管理", "/blogs"));
        list.add(new Menu("评论管理", "/commits"));
        model.addAttribute("list",list);
        return new ModelAndView("admins/index","menuList",model);
    }
}

package com.edou.blog.controller;

import com.edou.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

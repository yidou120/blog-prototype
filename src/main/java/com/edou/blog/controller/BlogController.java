package com.edou.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName BlogController
 * @Description 博客主页控制器
 * @Author 中森明菜
 * @Date 2020/3/18 17:41
 * @Version 1.0
 */
@Controller
@RequestMapping("/blogs")
public class BlogController {

    //博客首页 显示博文列表 可根据最新最热，关键词tag显示博文
    @GetMapping
    public String listBlogs(@RequestParam(value="order",required=false,defaultValue="new") String order,
                            @RequestParam(value="tag",required=false) Long tag) {
        System.out.print("order:" +order + ";tag:" +tag );
        return "redirect:/index?order="+order+"&tag="+tag;
    }
}

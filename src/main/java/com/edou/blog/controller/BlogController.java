package com.edou.blog.controller;

import com.edou.blog.domain.User;
import com.edou.blog.domain.es.EsBlog;
import com.edou.blog.service.EsBlogService;
import com.edou.blog.vo.TagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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

    @Autowired
    private EsBlogService esBlogService;

    //博客首页 显示博文列表 可根据最新最热，关键词tag显示博文
    @GetMapping
    public String listBlogs(@RequestParam(value="order",required=false,defaultValue="new") String order,
                            @RequestParam(value="keyword",required=false,defaultValue="" ) String keyword,
                            @RequestParam(value="async",required=false) boolean async,
                            @RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
                            @RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
                            Model model) {
        Page<EsBlog> esBlogs = null;
        List<EsBlog> content = null;
        boolean isEmpty = true;
        try {
            if(order.equals("hot")){
                Sort sort = new Sort(Sort.Direction.DESC,"readSize","commentSize","voteSize","createTime");
                Pageable pageable = PageRequest.of(pageIndex,pageSize,sort);
//                Pageable pageable = new PageRequest(pageIndex,pageSize,sort);
                esBlogs = esBlogService.listHotestEsBlogs(keyword, pageable);
            }
            if(order.equals("new")){
                Sort sort = new Sort(Sort.Direction.DESC,"createTime");
                Pageable pageable = PageRequest.of(pageIndex,pageSize,sort);
//                Pageable pageable = new PageRequest(pageIndex,pageSize,sort);
                esBlogs = esBlogService.listNewestEsBlogs(keyword, pageable);
            }
            isEmpty = false;
        } catch (Exception e) {
            Pageable pageable = PageRequest.of(pageIndex,pageSize);
//            Pageable pageable = new PageRequest(pageIndex, pageSize);
            esBlogs = esBlogService.listEsBlogs(pageable);
        }
        content = esBlogs.getContent();
        model.addAttribute("order", order);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", esBlogs);
        System.out.println(esBlogs.getTotalPages()+" "+esBlogs.getTotalElements()+" "+esBlogs.getNumber());
        model.addAttribute("blogList", content);

        // 首次访问页面才加载
        if (!async && !isEmpty) {
            List<EsBlog> newest = esBlogService.listTop5NewestEsBlogs();
            model.addAttribute("newest", newest);
            List<EsBlog> hotest = esBlogService.listTop5HotestEsBlogs();
            model.addAttribute("hotest", hotest);
            List<TagVO> tags = esBlogService.listTop30Tags();
            model.addAttribute("tags", tags);
            List<User> users = esBlogService.listTop12Users();
            model.addAttribute("users", users);
        }

        return (async==true?"/index :: #mainContainerRepleace":"/index");
    }
}

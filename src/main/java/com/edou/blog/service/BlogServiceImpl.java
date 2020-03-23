package com.edou.blog.service;

import com.edou.blog.domain.Blog;
import com.edou.blog.domain.User;
import com.edou.blog.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

/**
 * @ClassName BlogServiceImpl
 * @Description 博客service层
 * @Author 中森明菜
 * @Date 2020/3/22 15:20
 * @Version 1.0
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    BlogRepository blogRepository;

    //保存博客
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    //删除博客
    @Transactional
    @Override
    public void removeBlog(Long id) {
        blogRepository.delete(id);
    }

    //更新博客
    @Transactional
    @Override
    public Blog updateBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    //根据id查询博客
    @Override
    public Blog getBlogById(Long id) {
        Blog blog = blogRepository.findOne(id);
        return blog;
    }

    //默认根据title最新查询
    @Override
    public Page<Blog> listBlogsByTitleVote(User user, String title, Pageable pageable) {
        if(Objects.isNull(title)){
            title = "";
        }
//        title = "%" + title + "%";
        //如果是用Containing模糊查询不需要手动加% 如果用Like模糊查询需要自己加%并且使用concat()函数进行拼接concat('%',?1,'%')
        String tags = title;
        return blogRepository.findByUserAndTitleContainingOrTagsContainingAndUserOrderByCreateTimeDesc(user,title,tags,user,pageable);
    }

    //根据最热查询
    @Override
    public Page<Blog> listBlogsByTitleVoteAndSort(User user, String title, Pageable pageable) {
        title = "%" + title + "%";
        return blogRepository.findByUserAndTitleLike(user,title,pageable);
    }

    //阅读量自增
    @Override
    public void readingIncrease(Long id) {
        Blog blog = blogRepository.findOne(id);
        blog.setReadSize(blog.getReadSize()+1);
        blogRepository.save(blog);
    }
}

package com.edou.blog.service;

import com.edou.blog.domain.*;
import com.edou.blog.domain.es.EsBlog;
import com.edou.blog.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

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

    @Autowired
    EsBlogService esBlogService;

    //保存博客
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        boolean isNew = Objects.isNull(blog.getId());
        EsBlog esBlog = null;
        Blog returnBlog = blogRepository.save(blog);
        if(isNew){
            esBlog = new EsBlog(returnBlog);
        }else{
            esBlog = esBlogService.getEsBlogByBlogId(blog.getId());
            esBlog.update(returnBlog);
        }
        esBlogService.updateEsBlog(esBlog);
        return returnBlog;
    }

    //删除博客
    @Transactional
    @Override
    public void removeBlog(Long id) {
//        blogRepository.delete(id);
        blogRepository.deleteById(id);
        EsBlog esBlog = esBlogService.getEsBlogByBlogId(id);
        esBlogService.removeEsBlog(esBlog.getId());
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
        Optional<Blog> optional = blogRepository.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
//        Blog blog = blogRepository.findOne(id);
//        return blog;
        return null;
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
        Blog blog = null;
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if(optionalBlog.isPresent()){
            blog = optionalBlog.get();
        }
//        Blog blog = blogRepository.findOne(id);
        blog.setReadSize(blog.getReadSize()+1);
        this.saveBlog(blog);
    }

    //点赞
    @Override
    public Blog createVote(Long blogId) {
        Blog blogOrigin = null;
        Optional<Blog> optionalBlog = blogRepository.findById(blogId);
        if(optionalBlog.isPresent()){
            blogOrigin = optionalBlog.get();
        }
//        Blog blogOrigin = blogRepository.findOne(blogId);
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Vote vote = new Vote(user);
        boolean isExist = blogOrigin.addVote(vote);
        if(isExist){
            throw new IllegalArgumentException("该用户已经点过赞了");
        }
        return this.saveBlog(blogOrigin);
    }

    //取消点赞
    @Override
    public void removeVote(Long blogId, Long voteId) {
        Blog blogOrigin = null;
        Optional<Blog> optionalBlog = blogRepository.findById(blogId);
        if(optionalBlog.isPresent()){
            blogOrigin = optionalBlog.get();
        }
//        Blog blogOrigin = blogRepository.findOne(blogId);
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        blogOrigin.removeVote(voteId);
        this.saveBlog(blogOrigin);
    }

    //发表评论
    @Override
    public Blog createComment(Long blogId, String commentContent) {
        Blog blog = null;
        Optional<Blog> optionalBlog = blogRepository.findById(blogId);
        if(optionalBlog.isPresent()){
            blog = optionalBlog.get();
        }
//        Blog blog = blogRepository.findOne(blogId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = new Comment(user,commentContent);
        blog.addComment(comment);
        return this.saveBlog(blog);
    }

    //删除评论
    @Override
    public void removeComment(Long blogId, Long commentId) {
        Blog blog = null;
        Optional<Blog> optionalBlog = blogRepository.findById(blogId);
        if(optionalBlog.isPresent()){
            blog = optionalBlog.get();
        }
//        Blog blog = blogRepository.findOne(blogId);
        blog.removeComment(commentId);
        this.saveBlog(blog);
    }

    //根据分类名查询博客列表
    @Override
    public Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable) {
        Page<Blog> page = blogRepository.findByCatalog(catalog, pageable);
        return page;
    }
}

package com.edou.blog.repository;

import com.edou.blog.domain.Blog;
import com.edou.blog.domain.Catalog;
import com.edou.blog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog,Long> {
    //根据用户名 title tag 模糊查询 分页查询用户列表 降序
    Page<Blog> findByUserAndTitleContainingOrTagsContainingAndUserOrderByCreateTimeDesc(User user,String title,String tags,User user2,Pageable pageable);
    //根据用户名 title 分页查询用户列表
    Page<Blog> findByUserAndTitleContaining(User user,String title,Pageable pageable);
    //根据分类查询博客列表
    Page<Blog> findByCatalog(Catalog catalog,Pageable pageable);
}

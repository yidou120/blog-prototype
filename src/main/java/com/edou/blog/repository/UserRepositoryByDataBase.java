package com.edou.blog.repository;

import com.edou.blog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.List;

public interface UserRepositoryByDataBase extends JpaRepository<User,Long> {
    //根据姓名模糊查询 分页查询用户列表
    Page<User> findByNameLike(String name, Pageable pageable);
    //根据用户名查询
    User findByUsername(String username);
    //根据用户名集合查询
    List<User> findByUsernameIn(Collection<String> usernames);
}

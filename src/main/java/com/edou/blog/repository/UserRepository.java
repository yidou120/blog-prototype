package com.edou.blog.repository;

import com.edou.blog.domain.User;

import java.util.List;

/**
 * @ClassName UserRepository
 * @Description user 持久层接口
 * @Author 中森明菜
 * @Date 2020/3/8 19:51
 * @Version 1.0
 */
public interface UserRepository {
    User saveOrUpdateUser(User user);
    void deleteUser(Long id);
    User getUserById(Long id);
    List<User> listUsers();
}

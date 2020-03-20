package com.edou.blog.service;

import com.edou.blog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/*
 * @Description //用户业务层接口
 * @Date 2020/3/19 12:13
 **/
public interface UserService {
    /**
     * 保存用户
     * @param user
     * @return
     */
    User saveUser(User user);

    /**
     * 删除用户
     * @param id
     * @return
     */
    void removeUser(Long id);

    /**
     * 删除列表里面的用户
     * @param users
     * @return
     */
    void removeUsersInBatch(List<User> users);

    /**
     * 更新用户
     * @param user
     * @return
     */
    User updateUser(User user);

    /**
     * 根据id获取用户
     * @param id
     * @return
     */
    User getUserById(Long id);

    /**
     * 获取用户列表
     * @return
     */
    List<User> listUsers();

    /**
     * 根据姓名进行分页模糊查询
     * @param name
     * @param pageable
     * @return
     */
    Page<User> listUsersByNameLike(String name, Pageable pageable);

    //根据用户名查找
    UserDetails loadUserByUsername(String username);
}

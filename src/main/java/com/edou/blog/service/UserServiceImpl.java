package com.edou.blog.service;

import com.edou.blog.domain.User;
import com.edou.blog.repository.UserRepositoryByDataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName UserServiceImpl
 * @Description 用户业务层实现类
 * @Author 中森明菜
 * @Date 2020/3/19 12:16
 * @Version 1.0
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    UserRepositoryByDataBase userRepositoryByDataBase;

    @Override
    public User saveUser(User user) {
        return userRepositoryByDataBase.save(user);
    }

    @Override
    public void removeUser(Long id) {
        userRepositoryByDataBase.delete(id);
    }

    @Override
    public void removeUsersInBatch(List<User> users) {
        userRepositoryByDataBase.deleteInBatch(users);
    }

    @Override
    public User updateUser(User user) {
        return userRepositoryByDataBase.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepositoryByDataBase.findOne(id);
    }

    @Override
    public List<User> listUsers() {
        return userRepositoryByDataBase.findAll();
    }

    @Override
    public Page<User> listUsersByNameLike(String name, Pageable pageable) {
        name = "%"+ name+"%";
        return userRepositoryByDataBase.findByNameLike(name,pageable);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepositoryByDataBase.findByUsername(username);
    }
}

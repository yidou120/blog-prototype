package com.edou.blog.repository;

import com.edou.blog.domain.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @ClassName UserRepositoryImpl
 * @Description UserRepository实现类
 * @Author 中森明菜
 * @Date 2020/3/9 11:11
 * @Version 1.0
 */
@Repository
public class UserRepositoryImpl implements UserRepository {
    //用于自增主键
    private static AtomicLong count = new AtomicLong();

    //用户存放数据
    private final ConcurrentMap<Long,User> userMap = new ConcurrentHashMap<Long,User>();

    /*public UserRepositoryImpl() {
        User user = new User();
        user.setEmail("xxx@qq.com");
        user.setName("Way Lau");
        this.saveOrUpdateUser(user);
    }*/

    //保存或者更新用户信息
    @Override
    public User saveOrUpdateUser(User user) {
        //取出用户id
        Long id = user.getId();
        if(Objects.isNull(id)){
            //如果用户id为空 自增主键 新增用户
            id = count.incrementAndGet();
            user.setId(id);
        }
        //不为空 更新用户
        userMap.put(id,user);
        return user;
    }

    //删除用户
    @Override
    public void deleteUser(Long id) {
        userMap.remove(id);
    }

    //根据id查询用户
    @Override
    public User getUserById(Long id) {
        User user = (User)userMap.get(id);
        return user;
    }

    //查询所有用户
    @Override
    public List<User> listUsers() {
        return new ArrayList<User>(this.userMap.values());
    }
}

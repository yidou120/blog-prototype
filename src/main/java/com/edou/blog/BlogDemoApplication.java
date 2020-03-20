package com.edou.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @ClassName BlogDemoApplication
 * @Description 启动类
 * @Author 中森明菜
 * @Date 2020/3/8 14:02
 * @Version 1.0
 */
@SpringBootApplication
public class BlogDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogDemoApplication.class,args);
    }
}

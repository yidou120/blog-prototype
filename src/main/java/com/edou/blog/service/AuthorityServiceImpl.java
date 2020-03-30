package com.edou.blog.service;

import com.edou.blog.domain.Authority;
import com.edou.blog.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @ClassName AuthorityServiceImpl
 * @Description 认真授权信息业务类
 * @Author 中森明菜
 * @Date 2020/3/19 17:03
 * @Version 1.0
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {
    @Autowired
    AuthorityRepository authorityRepository;

    @Override
    public Authority getAuthorityById(Long id) {
        Optional<Authority> optionalAuthority = authorityRepository.findById(id);
        if(optionalAuthority.isPresent()){
            return optionalAuthority.get();
        }
//        return authorityRepository.findOne(id);
        return null;
    }
}

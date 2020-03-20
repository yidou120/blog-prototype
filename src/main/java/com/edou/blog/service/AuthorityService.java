package com.edou.blog.service;

import com.edou.blog.domain.Authority;

public interface AuthorityService {

    /**
     * 根据id获取 Authority
     * @param id
     * @return
     */
    Authority getAuthorityById(Long id);
}

package com.edou.blog.repository;

import com.edou.blog.domain.Catalog;
import com.edou.blog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
 * @Description //分类实体对应的持久层接口
 * @Date 2020/3/25 12:16
 * @param null
 *@return
 **/
public interface CatalogRepository extends JpaRepository<Catalog,Long> {

    /**
     * 显示所有分类列表 显示某个用户下的所有博客分类
     * @param user
     * @return
     */
    List<Catalog> findAllByUser(User user);

    /**
     * 根据用户 分类名称查询 判断分类是否重复
     * @param user
     * @param name
     * @return
     */
    List<Catalog> findByUserAndName(User user,String name);
}

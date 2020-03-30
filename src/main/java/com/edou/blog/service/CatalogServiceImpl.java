package com.edou.blog.service;

import com.edou.blog.domain.Catalog;
import com.edou.blog.domain.User;
import com.edou.blog.repository.BlogRepository;
import com.edou.blog.repository.CatalogRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @ClassName CatalogServiceImpl
 * @Description 分类业务实现类
 * @Author 中森明菜
 * @Date 2020/3/25 12:39
 * @Version 1.0
 */
@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    CatalogRepository catalogRepository;

    //保存
    @Override
    public Catalog saveCatalog(Catalog catalog) {
        //判断是否已存在此分类
        List<Catalog> catalogList = catalogRepository.findByUserAndName(catalog.getUser(), catalog.getName());
        if(CollectionUtils.isEmpty(catalogList)){
            return catalogRepository.save(catalog);
        }
        throw new IllegalArgumentException("该分类已经存在了");
    }

    //删除
    @Override
    public void removeCatalog(Long id) {
        catalogRepository.deleteById(id);
//        catalogRepository.delete(id);
    }

    //查找
    @Override
    public Catalog getCatalogById(Long id) {
        Optional<Catalog> optional = catalogRepository.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
//        return catalogRepository.findOne(id);
    }

    //所有分类列表
    @Override
    public List<Catalog> listCatalogs(User user) {
        return catalogRepository.findAllByUser(user);
    }
}

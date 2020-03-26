package com.edou.blog.vo;

import com.edou.blog.domain.Catalog;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @ClassName CatalogVO
 * @Description 分类值对象
 * @Author 中森明菜
 * @Date 2020/3/25 17:56
 * @Version 1.0
 */
@NoArgsConstructor
public class CatalogVO implements Serializable {
    private static final long serialVersionUID = 2134244712049785848L;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private Catalog catalog;
}

package com.edou.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName Menu
 * @Description 菜单 值对象.
 * @Author 中森明菜
 * @Date 2020/3/19 13:18
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Menu implements Serializable {
    private static final long serialVersionUID = 5600222571575526950L;
    private String name;
    private String url;
}

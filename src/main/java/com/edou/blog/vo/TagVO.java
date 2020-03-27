package com.edou.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @ClassName TagVO
 * @Description 标签值对象
 * @Author 中森明菜
 * @Date 2020/3/26 18:10
 * @Version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
public class TagVO implements Serializable {

    private static final long serialVersionUID = 2884005480338583975L;

    private String name;
    private Long count;

}

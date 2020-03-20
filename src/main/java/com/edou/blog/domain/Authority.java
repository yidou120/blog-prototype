package com.edou.blog.domain;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @ClassName Authority
 * @Description Authority实体
 * @Author 中森明菜
 * @Date 2020/3/19 15:53
 * @Version 1.0
 */
@Entity
@Data
public class Authority implements GrantedAuthority, Serializable {

    private static final long serialVersionUID = 7152277148113890555L;

    @Id // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    private Long id; // 用户的唯一标识

    @Column(nullable = false) // 映射为字段，值不能为空
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}

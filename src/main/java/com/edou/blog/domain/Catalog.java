package com.edou.blog.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @ClassName Catalog
 * @Description 分类实体对象
 * @Author 中森明菜
 * @Date 2020/3/25 12:11
 * @Version 1.0
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Catalog implements Serializable {

    private static final long serialVersionUID = 4140084019698192358L;

    @Getter
    @Setter
    @Id // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    private Long id; // 用户的唯一标识

    @Getter
    @Setter
//    @NotEmpty(message = "名称不能为空")
    @javax.validation.constraints.NotEmpty(message = "名称不能为空")
    @Size(min=2, max=30)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String name;

    @Getter
    @Setter
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    public Catalog(User user, String name) {
        this.name = name;
        this.user = user;
    }

}

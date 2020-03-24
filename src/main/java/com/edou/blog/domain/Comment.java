package com.edou.blog.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @ClassName Comment
 * @Description 评论实体
 * @Author 中森明菜
 * @Date 2020/3/23 17:03
 * @Version 1.0
 */
@Entity // 实体
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment implements Serializable {

    private static final long serialVersionUID = 7797613209246177372L;

    @Getter
    @Setter
    @Id // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    private Long id; // 用户的唯一标识

    @Getter
    @Setter
    @NotEmpty(message = "评论内容不能为空")
    @Size(min=2, max=500)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String content;

    @Getter
    @Setter
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Getter
    @Column(nullable = false) // 映射为字段，值不能为空
    @org.hibernate.annotations.CreationTimestamp  // 由数据库自动创建时间
    private Timestamp createTime;

    public Comment(User user, String content) {
        this.content = content;
        this.user = user;
    }

}

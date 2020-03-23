package com.edou.blog.domain;

import com.github.rjeschke.txtmark.Processor;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @ClassName Blog
 * @Description 博客实体类
 * @Author 中森明菜
 * @Date 2020/3/22 12:23
 * @Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity // 实体
public class Blog implements Serializable {
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    @Id // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    private Long id; // 用户的唯一标识

    @Getter
    @Setter
    @NotEmpty(message = "标题不能为空")
    @Size(min=2, max=50)
    @Column(nullable = false, length = 50) // 映射为字段，值不能为空
    private String title;

    @Getter
    @Setter
    @NotEmpty(message = "摘要不能为空")
    @Size(min=2, max=300)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String summary;

    @Getter
    @Lob  // 大对象，映射 MySQL 的 Long Text 类型
    @Basic(fetch=FetchType.LAZY) // 懒加载
    @NotEmpty(message = "内容不能为空")
    @Size(min=2)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String content;

    @Getter
    @Lob  // 大对象，映射 MySQL 的 Long Text 类型
    @Basic(fetch=FetchType.LAZY) // 懒加载
    @NotEmpty(message = "内容不能为空")
    @Size(min=2)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String htmlContent; // 将 md 转为 html

    @Getter
    @Setter
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Getter
    @Column(nullable = false) // 映射为字段，值不能为空
    @org.hibernate.annotations.CreationTimestamp  // 由数据库自动创建时间
    private Timestamp createTime;

    @Getter
    @Setter
    @Column(name="readSize")
    private Integer readSize = 0; // 访问量、阅读量

    @Getter
    @Setter
    @Column(name="commentSize")
    private Integer commentSize = 0;  // 评论量

    @Getter
    @Setter
    @Column(name = "tags", length = 100)
    private String tags;

    @Getter
    @Setter
    @Column(name="voteSize")
    private Integer voteSize = 0;

    public Blog(String title, String summary,String content) {
        this.title = title;
        this.summary = summary;
        this.content = content;
    }


    public void setContent(String content) {
        this.content = content;
        this.htmlContent = Processor.process(content);
    }

}
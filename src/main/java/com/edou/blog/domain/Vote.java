package com.edou.blog.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @ClassName Vote
 * @Description 点赞实体对象
 * @Author 中森明菜
 * @Date 2020/3/24 11:51
 * @Version 1.0
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote implements Serializable {

    private static final long serialVersionUID = 3568303643861253480L;

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @OneToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    @Column(nullable = false)
    @CreationTimestamp
    private Timestamp createTime;

    public Vote(User user) {
        this.user = user;
    }
}

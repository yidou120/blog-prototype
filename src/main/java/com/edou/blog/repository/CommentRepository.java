package com.edou.blog.repository;

import com.edou.blog.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName CommentRepository
 * @Description 评论持久层接口
 * @Author 中森明菜
 * @Date 2020/3/23 17:32
 * @Version 1.0
 */
public interface CommentRepository extends JpaRepository<Comment,Long> {
}

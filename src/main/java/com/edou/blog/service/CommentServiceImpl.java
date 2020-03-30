package com.edou.blog.service;

import com.edou.blog.domain.Comment;
import com.edou.blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @ClassName CommentServiceImpl
 * @Description 评论业务层实现类
 * @Author 中森明菜
 * @Date 2020/3/23 17:31
 * @Version 1.0
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;

    //根据评论id查询评论
    @Override
    public Comment getCommentById(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if(optionalComment.isPresent()){
            return optionalComment.get();
        }
//        return commentRepository.findOne(id);
        return null;
    }

    //根据评论id删除评论
    @Transactional
    @Override
    public void removeComment(Long id) {
//        commentRepository.delete(id);
        commentRepository.deleteById(id);
    }
}

package com.edou.blog.controller;

import com.edou.blog.domain.Blog;
import com.edou.blog.domain.User;
import com.edou.blog.service.BlogService;
import com.edou.blog.service.VoteService;
import com.edou.blog.util.ConstraintViolationExceptionHandler;
import com.edou.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.ConstraintViolationException;
import java.util.Objects;

/**
 * @ClassName VoteController
 * @Description 点赞控制层
 * @Author 中森明菜
 * @Date 2020/3/24 16:47
 * @Version 1.0
 */
@Controller
@RequestMapping("/votes")
public class VoteController {

    @Autowired
    VoteService voteService;

    @Autowired
    BlogService blogService;

    //点赞
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> vote(Long blogId){
        try {
            blogService.createVote(blogId);
        } catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true, "点赞成功", null));
    }

    //取消点赞
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> cancelVote(@PathVariable("id") Long id, Long blogId){
        boolean isOwner = false;
        User user = voteService.getVoteById(id).getUser();

        // 判断操作用户是否是点赞的所有者
        if (Objects.nonNull(SecurityContextHolder.getContext().getAuthentication()) && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (Objects.nonNull(principal) && user.getUsername().equals(principal.getUsername())) {
                isOwner = true;
            }
        }

        if (!isOwner) {
            return ResponseEntity.ok().body(new Response(false, "没有操作权限"));
        }

        try {
            blogService.removeVote(blogId, id);
            voteService.removeVote(id);
        } catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true, "取消点赞成功", null));

    }
}

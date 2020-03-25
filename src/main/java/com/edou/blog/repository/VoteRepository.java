package com.edou.blog.repository;

import com.edou.blog.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
/*
 * @Description //点赞实体对应的持久层
 * @Date 2020/3/24 12:06
 * @param null
 *@return
 **/
public interface VoteRepository extends JpaRepository<Vote,Long> {
}

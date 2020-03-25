package com.edou.blog.service;

import com.edou.blog.domain.Vote;
/*
 * @Description //点赞service接口
 * @Date 2020/3/24 12:07
 * @param null
 *@return
 **/
public interface VoteService {
    /**
     * 根据id获取 Vote
     * @param id
     * @return
     */
    Vote getVoteById(Long id);
    /**
     * 删除Vote
     * @param id
     * @return
     */
    void removeVote(Long id);
}

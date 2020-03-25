package com.edou.blog.service;

import com.edou.blog.domain.Vote;
import com.edou.blog.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName VoteServiceImpl
 * @Description 点赞service业务实现类
 * @Author 中森明菜
 * @Date 2020/3/24 12:07
 * @Version 1.0
 */
@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteRepository voteRepository;

    //根据id获取Vote
    @Override
    public Vote getVoteById(Long id) {
        return voteRepository.findOne(id);
    }

    //根据id删除Vote
    @Override
    public void removeVote(Long id) {
        voteRepository.delete(id);
    }
}

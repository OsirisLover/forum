package com.example.forum2.service;


import com.example.forum2.model.ArticleReply;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IArticleReplyService {
    /**
     * 添加回复记录
     * @param articleReply 回复记录
     */
    // 用事务管理
    @Transactional
    void create (ArticleReply articleReply);


    /**
     * 获取帖子下的所有回复
     * @param articleId 帖子Id
     * @return 回复列表
     */
    List<ArticleReply> selectByArticleId(Long articleId);
}

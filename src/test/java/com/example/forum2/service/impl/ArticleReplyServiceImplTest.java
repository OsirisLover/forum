package com.example.forum2.service.impl;

import com.example.forum2.model.ArticleReply;
import com.example.forum2.service.IArticleReplyService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleReplyServiceImplTest {

    @Resource
    IArticleReplyService iArticleReplyService;
    @Test
    void selectByArticleId() {
        List<ArticleReply> articleReplies = iArticleReplyService.selectByArticleId(1l);
        for (ArticleReply articleReply : articleReplies) {
            System.out.println(articleReply);
        }
    }

    @Test
    void create() {
        ArticleReply articleReply=new ArticleReply();
        articleReply.setArticleId(1l);
        articleReply.setPostUserId(2l);
        articleReply.setContent("测试帖子回复接口3");
        iArticleReplyService.create(articleReply);
        System.out.println("没有报错，写入成功");
    }
}
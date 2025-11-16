package com.example.forum2.service.impl;

import com.example.forum2.model.Article;
import com.example.forum2.service.IArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest

class ArticleServiceImplTest {


    @Resource
    IArticleService iArticleService;
    @Test
    void create() {
        Article article=new Article();
        article.setBoardId(2l);
        article.setUserId(6l);
        article.setTitle("帖子写入");
        article.setContent("测试帖子写入测试帖子写入测试帖子写入测测试帖子写入试帖子写入");

        iArticleService.create(article);


    }
    @Test
    void selectAll() {
        List<Article> articles = iArticleService.selectAll();
        for (Article article: articles
             ) {
            System.out.println(article);

        }
    }

    @Test
    void selectByBoardId() {

        List<Article> articles = iArticleService.selectByBoardId(2l);
        for (Article article: articles
        ) {
            System.out.println(article);

        }
    }

    @Test
    void selectDetailById() {
        Article article = iArticleService.selectDetailById(1l);
        System.out.println(article);
    }

    @Test
    void modify() {
        iArticleService.modify(3l,"修改的内容");
    }

    @Test
    void deleteById() {
        iArticleService.deleteById(7l);
    }

    @Test
    void likeById() {
        iArticleService.likeById(1l);
    }

    @Test
    void visitById() {
        iArticleService.visitById(1l);
    }

    @Test
    void selectByUserId() {
        List<Article> articles = iArticleService.selectByUserId(7l);
        for (Article article: articles
        ) {
            System.out.println(article);

        }
    }
}
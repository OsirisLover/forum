package com.example.forum2.controller;


import com.example.forum2.common.AppResult;
import com.example.forum2.common.ResultCode;
import com.example.forum2.config.AppConfig;
import com.example.forum2.model.Article;
import com.example.forum2.model.ArticleReply;
import com.example.forum2.model.User;
import com.example.forum2.service.IArticleReplyService;
import com.example.forum2.service.IArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


@Slf4j
@Api(tags = "帖子回复接口")
@RestController
@RequestMapping("/articleReply")
public class ArticleReplyController {

    @Resource
    IArticleReplyService iArticleReplyService;

    @Resource
    IArticleService iArticleService;

    @ApiOperation("帖子回复列表")
    @GetMapping("/getReplies")
    public AppResult<List<ArticleReply>> getReplies(
            @ApiParam(value = "帖子Id") @RequestParam("articleId") @NonNull Long articleId) {
        List<ArticleReply> result = iArticleReplyService.selectByArticleId(articleId);

        return AppResult.success(result);
    }

    @ApiOperation("回复帖子")
    @PostMapping("/reply")
    public AppResult reply(HttpServletRequest rep,
                           @ApiParam(value = "帖子Id") @RequestParam("articleId") @NonNull Long articleId,
                           @ApiParam(value = "回复内容") @RequestParam("content") @NonNull String content) {
        //对用户参数进行校验
        HttpSession session = rep.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        if (user == null || user.getState() == 1) {
            return AppResult.failed(ResultCode.FAILED_USER_BANNED.toString());
        }
        if (user.getDeleteState() == 1) {
            return AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS.toString());
        }
        //对帖子状态进行校验
        Article article = iArticleService.selectById(articleId);
        if (article == null || article.getDeleteState() == 1) {
            return AppResult.failed("帖子不存在.");
        }
        if (article.getState() == 1) {
            return AppResult.failed("已被封贴, 不能进行回复.");
        }
        //进行数据封装
        ArticleReply articleReply = new ArticleReply();
        articleReply.setArticleId(articleId);
        articleReply.setPostUserId(user.getId());
        articleReply.setContent(content);

        iArticleReplyService.create(articleReply);

        return AppResult.success("回复成功");


    }
}

package com.example.forum2.service.impl;

import com.example.forum2.common.AppResult;
import com.example.forum2.common.ResultCode;
import com.example.forum2.dao.ArticleMapper;
import com.example.forum2.dao.ArticleReplyMapper;
import com.example.forum2.dao.UserMapper;
import com.example.forum2.explication.ApplicationException;
import com.example.forum2.model.Article;
import com.example.forum2.model.ArticleReply;
import com.example.forum2.model.User;
import com.example.forum2.service.IArticleReplyService;
import com.example.forum2.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Service
@Slf4j
public class ArticleReplyServiceImpl implements IArticleReplyService {

    @Resource
    ArticleReplyMapper articleReplyMapper;
    @Resource
    ArticleMapper articleMapper;

    @Resource
    UserMapper userMapper;



    @Transactional
    @Override
    public void create(ArticleReply articleReply) {
        //对参数进行校验
        if(articleReply==null||articleReply.getPostUserId()==null||
                articleReply.getArticleId()==null|| StringUtils.isEmpty(articleReply.getContent())){
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        //对帖子进行状态校验
        Article article = articleMapper.selectByPrimaryKey(articleReply.getArticleId());
        if(article==null||article.getState()==1||article.getDeleteState()==1){
            // 记录日志
            log.warn(ResultCode.FAILED_STATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_STATE));
        }

        User user = userMapper.selectByPrimaryKey(articleReply.getPostUserId());
        if(user==null||user.getDeleteState()==1||user.getState()==1){
            // 记录日志
            log.warn(ResultCode.FAILED_STATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_STATE));
        }
        //继续封装articleReply
        articleReply.setLikeCount(0);
        articleReply.setState((byte) 0);
        articleReply.setDeleteState((byte) 0);
        Date date=new Date();
        articleReply.setCreateTime(date);
        articleReply.setUpdateTime(date);


        //校验没有问题，插入帖子回复表
        int articleReplyRow = articleReplyMapper.insertSelective(articleReply);
        if(articleReplyRow!=1){
            log.warn(ResultCode.FAILED_UPDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_UPDATE));
        }
        //更新帖子表
        Article updateArticle=new Article();
        updateArticle.setId(article.getId());
        updateArticle.setReplyCount(article.getReplyCount()+1);
        int articleRow = articleMapper.updateByPrimaryKeySelective(updateArticle);
        if(articleRow!=1){
            log.warn(ResultCode.FAILED_UPDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_UPDATE));
        }
        log.info("回复发表成功,帖子id："+articleReply.getArticleId()+"回复id:"+articleReply.getId());

    }

    @Override
    public List<ArticleReply> selectByArticleId( Long articleId) {

        // 非空校验
        if (articleId == null) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        List<ArticleReply> articleReplies = articleReplyMapper.selectByArticleId(articleId);

        return articleReplies;


    }
}

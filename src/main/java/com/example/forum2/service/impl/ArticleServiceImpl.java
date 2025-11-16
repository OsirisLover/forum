package com.example.forum2.service.impl;

import com.example.forum2.common.AppResult;
import com.example.forum2.common.ResultCode;
import com.example.forum2.dao.ArticleMapper;
import com.example.forum2.dao.BoardMapper;
import com.example.forum2.dao.UserMapper;
import com.example.forum2.explication.ApplicationException;
import com.example.forum2.model.Article;
import com.example.forum2.service.IArticleService;

import com.example.forum2.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ArticleServiceImpl implements IArticleService {

    @Resource
    ArticleMapper articleMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    BoardMapper boardMapper;



    @Override
    public void create(Article article) {
        if (article == null) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        //继续设置初始值
        article.setVisitCount(0);
        article.setReplyCount(0);
        article.setLikeCount(0);

        article.setState((byte) 0);
        article.setDeleteState((byte) 0);
        Date date = new Date();
        article.setCreateTime(date);
        article.setUpdateTime(date);

        // 调用DAO层，写入数据库
        int articleRow = articleMapper.insertSelective(article);
        if (articleRow != 1) {
            log.warn("新增帖子失败");
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        // 更新用户发帖数
        int userRow = userMapper.upArticleCountById(article.getUserId());
        if (userRow != 1) {
            log.warn("发贴时更新用户发帖数失败.");
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        // 更新版块帖子数量
        int boardRow = boardMapper.upArticleCountById(article.getBoardId());
        if (boardRow != 1) {
            log.warn("发贴时更新版块帖子数失败.");
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        log.info("写入帖子成功");
    }


    @Override
    public List<Article> selectAll() {
        return articleMapper.selectAll();
    }

    @Override
    public List<Article> selectByBoardId(Long boardId) {
        if (boardId == null) {
            log.info(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        return articleMapper.selectByBoardId(boardId);
    }

    @Override
    public List<Article> selectByUserId(Long userId) {
        if (userId == null) {
            log.info(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        return articleMapper.selectByUserId(userId);
    }


    @Override
    public Article selectDetailById(Long id) {

        if (id == null) {
            log.info(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        Article article = articleMapper.selectDetailById(id);
        return  article;

    }

    @Override
    public Article selectById(Long id) {
        // 非空校验
        if (id == null) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 返回结果
        return articleMapper.selectByPrimaryKey(id);
    }

    @Override
    public void modify(Long id, String content) {
        //开局第一步先对参数进行校验
        if(id==null|| StringUtils.isEmpty(content)){
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        //通过id查看文章状态是否正常
        Article article = articleMapper.selectByPrimaryKey(id);
        if (article == null || article.getDeleteState() == 1) {
            log.warn(ResultCode.FAILED_NOT_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }
        if (article.getState() == 1) {
            log.warn(ResultCode.FAILED_STATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_STATE));
        }

        //对修改后的帖子进行封装
        Article updateArticle=new Article();
        updateArticle.setId(id);
        updateArticle.setContent(content);
        updateArticle.setUpdateTime(new Date());

        int updateRow = articleMapper.updateByPrimaryKeySelective(updateArticle);
        if (updateRow != 1) {
            log.warn("更新帖子失败.");
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_UPDATE));
        }
        log.info("修改帖子成功");

    }



    @Override
    public void deleteById(Long id) {
        if(id==null){
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        Article article = articleMapper.selectByPrimaryKey(id);

        Article deleteArticle=new Article();
        deleteArticle.setId(id);
        deleteArticle.setDeleteState((byte) 1);

        int deleteRow = articleMapper.updateByPrimaryKeySelective(deleteArticle);

        if (deleteRow != 1) {
            log.warn("删除帖子失败.");
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_DELETE));
        }


        // 更新用户发帖数
        int userRow = userMapper.downArticleCountById(article.getUserId());
        if (userRow != 1) {
            log.warn("删贴时更新用户发帖数失败.");
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        // 更新版块帖子数量
        int boardRow = boardMapper.downArticleCountById(article.getBoardId());
        if (boardRow != 1) {
            log.warn("发贴时更新版块帖子数失败.");
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        log.info("删除帖子成功");
    }

    @Override
    public void likeById(Long id) {
        if(id==null){
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        Article article = articleMapper.selectByPrimaryKey(id);
        Article likeArticle=new Article();
        likeArticle.setId(id);
        likeArticle.setLikeCount(article.getLikeCount()+1);

        int likeRow = articleMapper.updateByPrimaryKeySelective(likeArticle);

        if (likeRow != 1) {
            log.warn("点赞帖子失败.");
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_UPDATE));
        }
        log.info("帖子点赞成功");
    }

    @Override
    public void visitById(Long id) {
        if(id==null){
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        Article article = articleMapper.selectByPrimaryKey(id);
        Article visitArticle=new Article();
        visitArticle.setId(id);
        visitArticle.setVisitCount(article.getVisitCount()+1);

        int visitRow = articleMapper.updateByPrimaryKeySelective(visitArticle);

        if (visitRow != 1) {
            log.warn("更改帖子查看数量失败.");
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_UPDATE));
        }
        log.info("更改帖子查看数量成功");
    }
}
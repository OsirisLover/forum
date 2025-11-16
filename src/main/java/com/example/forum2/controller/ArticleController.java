package com.example.forum2.controller;

import com.example.forum2.common.AppResult;
import com.example.forum2.common.ResultCode;
import com.example.forum2.config.AppConfig;
import com.example.forum2.model.Article;
import com.example.forum2.model.Board;
import com.example.forum2.model.User;
import com.example.forum2.service.IArticleService;
import com.example.forum2.service.IBoardService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/article")
@Api(tags = "帖子接口")
public class ArticleController {

    @Resource
    private  IBoardService iBoardService;
    @Resource
    private IArticleService iArticleService;
    @ApiOperation(value = "新增帖子")
    @PostMapping("/create")
    public AppResult create(HttpServletRequest req,
                            @ApiParam(value ="版块Id") @RequestParam(value = "boardId") @NonNull Long boardId,
                            @ApiParam(value ="文章标题") @RequestParam(value="title") @NonNull String title,
                            @ApiParam(value ="文章内容") @RequestParam(value="content")@NonNull String content ){
        //不需要进行参数校验

        // 从登录的用户的信息里面获取userid，只需要去校验一下用户的状态是否有发言权,
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
                if(user.getState()==1){
                    log.warn("用户已经被禁言");
                    return AppResult.failed(ResultCode.FAILED);
                }

        //校验板块是否被禁言
        Board board = iBoardService.selectByPrimaryKey(boardId);
                if(board==null||board.getState()==1){
                    log.warn("板块已经被禁言");
                    return AppResult.failed(ResultCode.FAILED);
                }
                //对参数进行封装
        Article article=new Article();
        article.setBoardId(boardId);
        article.setUserId(user.getId());
        article.setTitle(title);
        article.setContent(content);

         iArticleService.create(article);
        //更新当前用户
        user.setArticleCount(user.getArticleCount()+1);
        session.setAttribute(AppConfig.USER_SESSION,user);
        log.info("用户Id:"+user.getId()+" 写入帖子成功");
        return AppResult.success();
    }

    /**
     *
     * @param boardId
     * @return 帖子列表
     */
    @ApiOperation(value = "获取所有帖子")
    @GetMapping("/getAllByBoardId")
    public AppResult<List<Article>> getAllByBoardId(
            @ApiParam(value = "板块Id") @RequestParam(name = "boardId",required = false) Long boardId){
        List<Article> articles=null;

        //没有参数的情况下获取所有的帖子
        if(boardId==null){
           articles = iArticleService.selectAll();
            log.info("获取首页帖子列表成功");
        }else{
            articles = iArticleService.selectByBoardId(boardId);

            log.info("查询版块帖子列表, boardId = " + boardId);
        }
        //有参数的情况下同各国穿的参数获取相应板块的帖子列表
       if(articles==null){
           articles=new ArrayList<>();
       }

        return AppResult.success(articles);
    }
    /**
     * 根据用户Id获取帖子列表
     * @param userId 用户Id
     * @return 帖子列表
     */
    // API 描述
    @ApiOperation("获取用户所有帖子")
    // 指定接口URL映射
    @GetMapping("/getByUserId")
    public AppResult<List<Article>> getAllByUserId (HttpServletRequest request,
                                                    @ApiParam(value = "用户Id") @RequestParam(value = "userId", required = false) Long userId) {
        // 根据参数中的userId是否为空决定返回哪个用户的帖子列表
        // 1. userId 为空，获取当前登录用户Id
        // 2. userId 不为空，指定id的用户
        if (userId == null) {
            // 获取Session中的用户信息
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(AppConfig.USER_SESSION);
            // 获取当前登录用户的Id
            userId = user.getId();
        }
        // 查询用户的帖子列表
        List<Article> result = iArticleService.selectByUserId(userId);
        log.info("查询用户帖子列表, userId = " + userId);
        // 返回结果
        return AppResult.success(result);
    }
    /**
     *
     * @param rep
     * @param id 帖子id
     * @return 帖子详情
     */
    @ApiOperation(value = "获取帖子详情")
    @GetMapping("/getById")
    public AppResult<Article> getById(HttpServletRequest rep,
                                      @ApiParam(value = "帖子Id") @RequestParam(name = "id") @NonNull Long id){

        if(id==null){
             log.warn(ResultCode.FAILED.toString());
             return AppResult.failed(ResultCode.FAILED);
        }
        // 先获取当前登录用户的信息
        HttpSession session = rep.getSession();
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 获取查询的帖子信息
        Article article = iArticleService.selectDetailById(id);
        if(article==null){
            log.warn(ResultCode.FAILED_NOT_EXISTS.toString());
            return AppResult.failed(ResultCode.FAILED_NOT_EXISTS);
        }
        iArticleService.visitById(id);
        article.setVisitCount(article.getVisitCount()+1);
        //查看当前用户是否是帖子的作者
        if(user.getId()==article.getUserId()){
            article.setOwn(true);
        }
    return AppResult.success(article);
    }

    /**
     * 编辑帖子
     * @param id 帖子Id
     * @param content 帖子内容
     * @return
     */
    @ApiOperation("编辑帖子")
    @PostMapping("/modify")
    public AppResult modify(HttpServletRequest request,
                            @ApiParam("帖子Id") @RequestParam("id") @NonNull Long id,
                            @ApiParam("帖子内容") @RequestParam("content") @NonNull String content) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 校验用户状态
        if (user.getState() == 1) {
            return AppResult.failed(ResultCode.FAILED_USER_BANNED.toString());
        }
        // 校验帖子作者与当前登录用户是否为同一个人
        Article article = iArticleService.selectById(id);
        if (article == null || article.getState() == 1 || article.getDeleteState() == 1) {
            return AppResult.failed("帖子不存在或状态异常");
        }
        if (article.getUserId() != user.getId()) {
            return AppResult.failed("只能修改自己发布的帖子.");
        }
        // 调用service层
        iArticleService.modify(id, content);
        // 返回成功
        return AppResult.success();
    }

    /**
     * 编辑帖子
     * @param id 帖子Id

     * @return
     */
    @ApiOperation("删除帖子")
    @GetMapping("/delete")
    public AppResult delete(HttpServletRequest request,
                            @ApiParam("帖子Id") @RequestParam("id") @NonNull Long id) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 校验用户状态
        if (user.getState() == 1) {
            return AppResult.failed(ResultCode.FAILED_USER_BANNED.toString());
        }
        // 校验帖子作者与当前登录用户是否为同一个人
        Article article = iArticleService.selectById(id);
        if (article == null ||  article.getDeleteState() == 1) {
            return AppResult.failed("帖子不存在或状态异常");
        }
        if (article.getUserId() != user.getId()) {
            return AppResult.failed("只能删除自己发布的帖子.");
        }
        // 调用service层
        iArticleService.deleteById(id);
        //更新当前用户
        user.setArticleCount(user.getArticleCount()-1);
        session.setAttribute(AppConfig.USER_SESSION,user);
        log.info("用户Id:"+user.getId()+" 删除帖子成功");
        // 返回成功
        return AppResult.success();
    }

    /**
     * 点赞帖子
     * @param id 帖子Id

     * @return
     */
    @ApiOperation("点赞帖子")
    @GetMapping("/like")
    public AppResult like(HttpServletRequest request,
                            @ApiParam("帖子Id") @RequestParam("id") @NonNull Long id) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 校验用户状态
        if (user.getState() == 1) {
            return AppResult.failed(ResultCode.FAILED_USER_BANNED.toString());
        }
        // 校验帖子状态
        Article article = iArticleService.selectById(id);
        if (article == null ||  article.getDeleteState() == 1) {
            return AppResult.failed("帖子不存在或状态异常");
        }

        // 调用service层
        iArticleService.likeById(id);
        // 返回成功
        return AppResult.success();
    }


}

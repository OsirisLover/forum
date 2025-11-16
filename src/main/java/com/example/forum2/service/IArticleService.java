package com.example.forum2.service;

import com.example.forum2.model.Article;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IArticleService {
    /**
     * 新增帖字
     * @param article 帖子对象
     */
 //由于包含多个表的操作,需要开启事务
    @Transactional
    void create(Article article);

    /**
     * 获取所有帖子
     * @return 帖子列表
     */
    List<Article> selectAll();

    /**
     * 按版块Id获取版块下的所有帖子
     * @param boardId 版块Id
     * @return 帖子列表
     */
    List<Article> selectByBoardId(Long boardId);

    /**
     * 按用户Id获取版块下的所有帖子
     * @param userId 用户Id
     * @return 帖子列表
     */
    List<Article> selectByUserId(Long userId);


    /**
     * 获取帖子详情
     * @param id 帖子Id
     * @return 帖子信息
     */

    Article selectDetailById(Long id);

    /**
     * 查询帖子信息
     * @param id 帖子Id
     * @return
     */
    Article selectById (Long id);

    /**
     * 编辑帖子
     * @param id 帖子Id
     * @param content 帖子内容
     */
    void modify (Long id, String content);

    /**
     * 删除帖子
     * @param id 帖子Id
     */
    @Transactional
    void deleteById (Long id);

    /**
     * 点赞帖子
     * @param id 帖子Id
     */
    void likeById (Long id);

    /**
     * 帖子查看数量
     * @param id 帖子Id
     */
    void visitById (Long id);


}

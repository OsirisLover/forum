package com.example.forum2.model;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
@Data
@ToString
public class ArticleReply {
    private Long id;

    private Long articleId;

    private Long postUserId;

    private Long replyId;

    private Long replyUserId;

    private String content;

    private Integer likeCount;

    private Byte state;

    private Byte deleteState;

    private Date createTime;

    private Date updateTime;

    private User user;

}
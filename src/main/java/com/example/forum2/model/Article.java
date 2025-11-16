package com.example.forum2.model;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
@Data
@ToString
public class Article {
    private Long id;

    private Long boardId;

    private Long userId;

    private String title;

    private Integer visitCount;

    private Integer replyCount;

    private Integer likeCount;

    private Byte state;

    private Byte deleteState;

    private Date createTime;

    private Date updateTime;

    private String content;

    private boolean isOwn;

    private  User user;

    private Board board;


}
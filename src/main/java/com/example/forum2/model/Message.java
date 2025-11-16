package com.example.forum2.model;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
@Data
@ToString
public class Message {
    private Long id;

    private Long postUserId;

    private Long receiveUserId;

    private String content;

    private Byte state;

    private Byte deleteState;

    private Date createTime;

    private Date updateTime;


}
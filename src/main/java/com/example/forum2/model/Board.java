package com.example.forum2.model;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
@Data
@ToString
public class Board {
    private Long id;

    private String name;

    private Integer articleCount;

    private Integer sort;

    private Byte state;

    private Byte deleteState;

    private Date createTime;

    private Date updateTime;


}
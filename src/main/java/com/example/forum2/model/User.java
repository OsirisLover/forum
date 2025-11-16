package com.example.forum2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
@Data
@ToString
public class User {
    private Long id;

    private String username;
    @JsonIgnore
    private String password;

    private String nickname;
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private String phoneNum;
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private String email;

    private Byte gender;
    @JsonIgnore
    private String salt;
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private String avatarUrl;

    private Integer articleCount;

    private Byte isAdmin;

    private String remark;

    private Byte state;
    @JsonIgnore
    private Byte deleteState;

    private Date createTime;

    private Date updateTime;


}
package com.example.forum2.service;


import com.example.forum2.model.User;

public interface IUserService {

     /**
      * 普通用户注册
      * @param user
      */
     void createNormal(User user);

     /**
      * 用户登录
      * @param username
      * @param password
      */
     User login(String username,String password);

     /**
      * 根据用户Id查询用户信息
      * @param id 用户Id
      * @return
      */
     User selectById (Long id);

     /**
      * 修改用户信息
      * @param id 用户id
      * @param gender 性别
      * @param nickname 昵称
      * @param phoneNum 电话号码
      * @param email 邮箱地址
      * @param remark 个人简介
      */
     void modifyInfo(Long id, Byte gender, String nickname, String phoneNum, String email, String remark);

     /**
      * 修改用户密码
      * @param id 用户Id
      * @param newPassword 新密码
      * @param oldPassword 老密码
      */
     void modifyPassword (Long id, String newPassword, String oldPassword);
}

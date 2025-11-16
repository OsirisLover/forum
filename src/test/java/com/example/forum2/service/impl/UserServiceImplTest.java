package com.example.forum2.service.impl;

import com.example.forum2.model.User;
import com.example.forum2.service.IUserService;
import com.example.forum2.utils.MD5Utils;
import com.example.forum2.utils.UUIDUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class UserServiceImplTest {
    @Resource
    IUserService iUserService;
    @Test
    void createNormal() {
        User user=new User();
        user.setUsername("admin");
        user.setNickname("程序员");
        user.setGender((byte) 2);
        String password="pjh011005";
        String salt= UUIDUtils.UUID_32();
        user.setSalt(salt);
        String ciphertext= MD5Utils.md5AndSalt(password,salt);
        user.setPassword(ciphertext);
        iUserService.createNormal(user);
    }

    @Test
    void login() {

        String username="admin";
        String password="pjh011005";
        User login = iUserService.login(username, password);
        System.out.println(login);
    }

    @Test
    void modify() {
        iUserService.modifyInfo(8l,null,"修改昵称",null,null,null);
    }
}
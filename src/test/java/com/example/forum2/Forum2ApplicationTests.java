package com.example.forum2;

import com.example.forum2.dao.BoardMapper;
import com.example.forum2.dao.UserMapper;
import com.example.forum2.model.Board;
import com.example.forum2.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
class Forum2ApplicationTests {

    @Resource
    UserMapper userMapper;
    @Test
    public void testSelectByUsername(){
        User user = userMapper.selectByUsername("pjh");
        System.out.println(user);
    }
    @Resource
    BoardMapper boardMapper;
    @Test
    public void testMapper(){
        for(long l=1;l<5;l++){
            Board board = boardMapper.selectByPrimaryKey(l);
            System.out.println(board);
        }
    }
    @Resource
    DataSource dataSource;

    @Test
    public void testDB () throws SQLException {
        System.out.println("dataSource = " + dataSource.getClass());
        System.out.println("Connection = " + dataSource.getConnection());
        System.out.println("Connection class = " + dataSource.getConnection().getClass());
    }

    @Test
    void contextLoads() {
        System.out.println("基于spring前后端分离的论坛项目");
    }
    public static void main(String[] args){
        System.out.println("Hello");
    }
}

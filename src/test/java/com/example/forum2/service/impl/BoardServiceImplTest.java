package com.example.forum2.service.impl;

import com.example.forum2.model.Board;
import com.example.forum2.service.IBoardService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

@SpringBootTest
class BoardServiceImplTest {
    @Resource
    IBoardService iBoardService;
    @Test
    void selectTopByNum() {
        List<Board> boards = iBoardService.selectTopByNum(9);
        for (Board b: boards
             ) {
            System.out.println(b);
        }

    }
}
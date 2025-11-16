package com.example.forum2.service.impl;

import com.example.forum2.common.AppResult;
import com.example.forum2.common.ResultCode;
import com.example.forum2.dao.BoardMapper;
import com.example.forum2.explication.ApplicationException;
import com.example.forum2.model.Board;
import com.example.forum2.service.IBoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BoardServiceImpl implements IBoardService {
    @Resource
    BoardMapper boardMapper;
    @Override
    public List<Board> selectTopByNum(Integer num) {
        //参数校验
        if(num<=0){
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));

        }
        List<Board> boards = boardMapper.selectTopByNum(num);
        if(boards==null){
            return new ArrayList<>();
        }
        return boards;
    }

    public Board selectByPrimaryKey(Long id){
        if(id<=0){
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));

        }
        Board board = boardMapper.selectByPrimaryKey(id);

        return board;

    }
}

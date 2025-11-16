package com.example.forum2.controller;


import com.example.forum2.common.AppResult;
import com.example.forum2.model.Board;
import com.example.forum2.service.IBoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
@Slf4j
@Api(tags = "板块接口")
@RestController
@RequestMapping("/board")
public class BoardController {

    @Resource
    private IBoardService iBoardService;

    @Value("${forum.index.board-num}")
    private  Integer indexBoardNum;


    @ApiOperation(value = "主页中显示的版块")
    @GetMapping("/topList")
    public AppResult<List<Board>> getTop(){
        log.info("从配置文件中读取到的版块数量为：" + indexBoardNum);
        List<Board> result = iBoardService.selectTopByNum(indexBoardNum);

        return AppResult.success(result);
    }



    @ApiOperation(value = "获取板块详情")
    @GetMapping("/getById")
    public AppResult<Board> getById(@ApiParam(value = "板块Id") @RequestParam("id" ) @NonNull Long id){
        Board board = iBoardService.selectByPrimaryKey(id);
        return AppResult.success(board);
    }


}

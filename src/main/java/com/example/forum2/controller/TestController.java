package com.example.forum2.controller;

import com.example.forum2.common.AppResult;
import com.example.forum2.explication.ApplicationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequestMapping("/test")
@RestController
@Api(tags = "TestController测试接口")
public class TestController {


    @GetMapping("/t1")
    @ApiOperation(value = "初始化环境测试")
    public String testHello () {
        return "hello, Spring boot";
    }



    @GetMapping("/t2")
    @ApiOperation(value = "自定义异常测试")
    public Object t2() {
        throw new ApplicationException("ApplicationException异常");
    }


    @GetMapping("/t3")
    @ApiOperation(value = "异常测试")
    public Object t3() throws Exception {
        throw new Exception("Exception异常");
    }

    @GetMapping("/t4")
    @ApiOperation(value = "传参测试")
    public Object t4(@ApiParam(value = "姓名",required = true) String name)  {

        return AppResult.success(name+"你好！JAVA高级工程师");

    }
}

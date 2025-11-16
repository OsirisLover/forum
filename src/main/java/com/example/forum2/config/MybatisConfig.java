package com.example.forum2.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author 比特就业课
 * @Date 2023-05-21
 */
// 配置类
@Configuration
// 指定Mybatis的扫描路径
@MapperScan("com/example/forum2/dao")
public class MybatisConfig {
}

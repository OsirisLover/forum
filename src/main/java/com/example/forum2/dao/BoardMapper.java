package com.example.forum2.dao;

import com.example.forum2.model.Board;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {
    int insert(Board row);

    int insertSelective(Board row);

    Board selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Board row);

    int updateByPrimaryKey(Board row);

    List<Board> selectTopByNum(@Param("num") Integer num);

    int upArticleCountById(@Param("id") Long id);

    int downArticleCountById(@Param("id") Long id);
}
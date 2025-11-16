package com.example.forum2.service;


import com.example.forum2.model.Board;



import java.util.List;



public interface IBoardService {


    List<Board> selectTopByNum(Integer num);
    Board selectByPrimaryKey(Long id);


}

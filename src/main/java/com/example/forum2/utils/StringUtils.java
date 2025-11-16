package com.example.forum2.utils;

public class StringUtils {

    public static  boolean isEmpty(String value){
        return value==null||value.length()==0;
    }
    public static  boolean isEmpty(Byte value){
        return value==null;
    }
    public static  boolean isEmpty(Integer value){
        return value==null;
    }
}

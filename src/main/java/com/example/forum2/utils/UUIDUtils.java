package com.example.forum2.utils;

import java.util.UUID;

public class UUIDUtils {

    /**
     *
     * @return 随机生成36位扰动字符串
     */
    public static String UUID_36(){
       return  UUID.randomUUID().toString();
    }

    /**
     *
     * @return  随机生成32位扰动字符串
     */
    public static String UUID_32(){
        return  UUID.randomUUID().toString().replace("-","");
    }


}

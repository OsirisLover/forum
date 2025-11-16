package com.example.forum2.utils;


import org.apache.commons.codec.digest.DigestUtils;

public class MD5Utils {

    public static String md5(String value){

        return DigestUtils.md5Hex(value);
    }

    public static String md5AddSalt(String value,String salt){
        return DigestUtils.md5Hex(value+salt);
    }

    public static String md5AndSalt(String value,String salt){
        return DigestUtils.md5Hex(DigestUtils.md5Hex(value)+salt);
    }

    /**
     *
     * @param value 原文
     * @param salt  扰动字符串
     * @param ciphertext 密文
     * @return
     */
    public static boolean compareToCiphertext(String value,String salt,String ciphertext){
        return MD5Utils.md5AndSalt(value,salt).equals(ciphertext);
    }
}

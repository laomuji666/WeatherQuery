package com.lmjproject.weatherquery.Http;

import java.io.UnsupportedEncodingException;

//编码类
public class UrlEncode {
    //将文本转化为utf8
    public static String toUtf8(String str){
        //%E8%B0%B7%E5%9F%8E
        String req = null;
        try {
            req = java.net.URLEncoder.encode(str, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return req;
    }
}

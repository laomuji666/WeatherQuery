package com.lmjproject.weatherquery.Http;

import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Http {
    private static final String TAG="HttpTAG";

    //通过一个文本查询城市
    public static String getCity(String autocomplete){
        String autocompleteUtf8=UrlEncode.toUtf8(autocomplete);
        String limit=String.valueOf(System.currentTimeMillis());
        String url="http://www.nmc.cn/f/rest/autocomplete?q="+autocompleteUtf8+"&limit=10&timestamp="+limit;
        return sendGet(url);
    }

    //通过getCity返回的文本查询天气
    public static List<String> getWeather(String cityStr){
        //http://www.nmc.cn/publish/forecast/AHB/wuhan.html
        //57494|武汉|430000|湖北省|wuhan|AHB
        //台站号,城市名称,邮政编码,所属省份,拼音,A湖北
        //http://www.nmc.cn/publish/forecast/AHB/wuhan.html
        String[] split = cityStr.split("\\|");
        //若返回的字符数组不符合格式则返回null
        if (split.length!=6){
            return null;
        }
        String url="http://www.nmc.cn/publish/forecast/"+split[5]+"/"+split[4]+".html";
        //分割符
        final String sBegin="<div class=\"date\">";
        final String sEnd=
                "      </div> \n" +
                "     </div> ";
        final String ssBegin="\">\n";
        final String ssEnd="</div> ";
        String string=sendGet(url);
        List<String>weatherList=new ArrayList<>();
        int iBegin=-1,iEnd=-1;
        int iiBegin=-1,iiEnd=-1;
        while ( (iBegin=string.indexOf(sBegin,iBegin))!=-1 && (iEnd=string.indexOf(sEnd,iBegin))!=-1 ){
            String sub=string.substring(iBegin,iEnd);
            //sub=sub.replace(" ","");
            iBegin=iEnd;//向后循环
            //今天,星期五,天气,摄氏度,风向,风大小
            StringBuilder builder=new StringBuilder();
            while ( (iiBegin=sub.indexOf(ssBegin,iiBegin))!=-1 && (iiEnd=sub.indexOf(ssEnd,iiBegin))!=-1 ){
                builder.append(sub.substring(iiBegin+11,iiEnd-8));
                builder.append("|");
                iiBegin=iiEnd;
            }
            weatherList.add(builder.toString());
        }
        for (String s:weatherList){
            Log.d(TAG, s);
        }
        return weatherList;
    }

    //发送get请求
    private static String sendGet(String url){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(url).build();
        Response response=null;
        String string=null;
        try {
            response = client.newCall(request).execute();
            string = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (response!=null){
                response.close();
            }
        }
        return string;
    }
}

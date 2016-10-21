package edu.feicui.news.news.net;

import android.content.Context;

import java.util.Map;

/**
 * 网络请求类
 * Created by jiaXian on 2016/9/22.
 */
public class MyHttp {

    public static void get(Context context,String url, Map<String,String> prams,OnResultFinishListener mListener){
        //进行网络请求
        Request request=new Request();
        request.params=prams;
        request.type=Constant.GET;
        request.url=url+Utils.getUrl(prams,Constant.GET);
        //请求
        NetAsync async=new NetAsync(context,mListener);
        async.execute(request);


    }
    public void post(Context context,String url, Map<String,String> prams){
        Request request=new Request();
        request.params=prams;
        request.type=Constant.GET;
        request.url=url;


    }
}

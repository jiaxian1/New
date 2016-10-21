package edu.feicui.news.news.net;

import java.util.Map;

/**
 * 请求所用数据
 * Created by Administrator on 2016/9/22.
 */
public class Request {
    public String url;//请求路径
    public Map<String,String> params;//参数
    public int type;//类型  10：get   11：post
}

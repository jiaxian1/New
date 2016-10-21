package edu.feicui.news.news.entity;

import java.io.Serializable;

/**
 * 新闻实体
 * Created by jiaXian on 2016/9/27.
 */
public class NewsInfo implements Serializable{
    public String summary;//新闻的摘要
    public String icon;//图标
    public String stamp;//时间戳
    public String title;//标题
    public int nid;//新闻id
    public String link;//连接
    public int type;//类型标识


}

package edu.feicui.news.news.entity;

import java.util.ArrayList;

/**
 * 用户信息
 * Created by jiaXian on 2016/10/8.
 */
public class UserInfo {
    public String uid;//用户id
    public String portrait;//用户头像
    public int integration;//用户积分
    public int comnum;//跟帖数量
    public ArrayList<LoginLog> loginlog;//登陆日志

}

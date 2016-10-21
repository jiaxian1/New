package edu.feicui.news.news.entity;

import java.io.Serializable;

/**
 * 登陆信息
 * Created by jiaXian on 2016/10/8.
 */
public class LoginInfo implements Serializable {
    public int result;//登陆状态  0：正常登陆
    public String explain;//登陆说明
    public String token;//用户令牌
}

package edu.feicui.news.news;

import edu.feicui.news.news.entity.Login;
import edu.feicui.news.news.entity.LoginInfo;
import edu.feicui.news.news.entity.NewsInfo;
import edu.feicui.news.news.entity.RegisterInfo;
import edu.feicui.news.news.entity.UserInfo;

/**
 * Created by jiaXian on 2016/10/12.
 */
public class AppInfo {

    public static LoginInfo login;//登陆响应信息
    public static NewsInfo news;//新闻响应信息
    public static UserInfo user;//用户响应信息
    public static RegisterInfo register;//注册响应信息
    public static boolean isLogin;//登陆状态
    public static int commentNum;//评论数量
    public static String versionCode;//评论数量
    public static String name;//登陆之后名字
    public static String password;//登陆之后密码
    public static String token;//用户令牌
    public static boolean flag;//用户令牌
}

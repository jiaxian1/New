package edu.feicui.news.news;

/**
 *
 * Created by jiaXian on 2016/9/27.
 */
public class ServerUrl {
    /**
     * 根链接
     */
    public static final String BASE_URL="http://118.244.212.82:9092/newsClient/";
    /**
     * 新闻列表数据
     * //news_sort?ver=版本号&imei=手机标识符
     */
    public static final String NEWS_SORT=BASE_URL+"/news_sort";
    /**
     * 新闻分类
     */
    public static final String NEWS_LIST=BASE_URL+"/news_list";
    /**
     *用户注册
     */
    public static final String USER_REGISTER=BASE_URL+"/user_register";
    /**
     *用户登陆
     */
    public static final String USER_LOGIN=BASE_URL+"/user_login";
    /**
     * 忘记密码
     */
    public static  final String USER_FORGETPASSWORD=BASE_URL+"/user_forgetpass";
    /**
     * 用户中心
     */
    public static  final String USER_HOME=BASE_URL+"/user_home";
    /**
     * 版本升级
     * update?imei=唯一识别号&pkg=包名&ver=版本
     */
    public static  final String UPDATE=BASE_URL+"/update";
    /**
     * 头像上传
     * user_image?token=用户令牌& portrait =头像
     */
    public static  final String USER_IMAGE=BASE_URL+"/user_image";
    /**
     * 评论数量
     * cmt_num?ver=版本号& nid=新闻编号
     */
    public static  final String COMMENT_NUMBER=BASE_URL+"/cmt_num";
    /**
     * 显示评论
     * cmt_list ?ver=版本号&nid=新闻id&type=1&stamp=yyyyMMdd&cid=评论id&dir=0&cnt=20
     */
    public static  final String COMMENT_LIST=BASE_URL+"/cmt_list";
    /**
     * 发布评论
     * cmt_commit?ver=版本号&nid=新闻编号&token=用户令牌&imei=手机标识符&ctx=评论内容
     */
    public static  final String PUBLISH_COMMENT=BASE_URL+"/cmt_commit";




}

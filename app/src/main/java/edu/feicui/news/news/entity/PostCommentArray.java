package edu.feicui.news.news.entity;

/**
 * 发布评论数组实体
 * Created by jiaXian on 2016/10/9.
 */
public class PostCommentArray {
    public String message;
    public int status;
    public PostCommentInfo data; //
    /*************************************************************************************************************
     *  此处着重注意！！！！实体建错  会报这样的异常JsonSyntaxException，IllegalStateException  注意数组是方括号
     ***************************************************************************************************************/
}

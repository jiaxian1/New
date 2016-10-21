package edu.feicui.news.news.net;

/**
 *
 * Created by jiaXian on 2016/9/23.
 */
public interface OnResultFinishListener {
    void  success(Response response);
    void failed(Response response);
}

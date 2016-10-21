package edu.feicui.news.news.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

import edu.feicui.news.news.AppInfo;
import edu.feicui.news.news.R;
import edu.feicui.news.news.ServerUrl;
import edu.feicui.news.news.entity.CommentNumInfo;
import edu.feicui.news.news.entity.NewsInfo;
import edu.feicui.news.news.net.MyHttp;
import edu.feicui.news.news.net.OnResultFinishListener;
import edu.feicui.news.news.net.Response;

/**
 * 新闻详情界面
 * Created by jiaXian on 2016/10/17.
 */
public class NewsDetailsActivity extends BaseActivity{
    View mView;
    /**
     * 用来加载请求到的网络数据
     */
    WebView mWebView;
    /**
     * 添加收藏的PopupWindow
     */
    PopupWindow popupWindow;
    /**
     * 添加收藏的popup的按钮(点开之后显示的)
     */
    Button mBtnPopup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);//加载新闻详细界面
    }

    @Override
    void initView() {
        //使用布局填充器 将popup布局加载进来
        mView=getLayoutInflater().inflate(R.layout.view_detailsnews_add_favorite_popup,null);
        //设置头上的东西
        mImgLeft.setImageResource(R.mipmap.back);
        mImgRight.setVisibility(View.GONE);//将右边的图片隐藏
        mLinLayoutHead= (LinearLayout) findViewById(R.id.ll_base_head);//根布局
        //将评论数量和加入收藏按钮显示出来
        mBtnCommentNumber.setVisibility(View.VISIBLE);
        mBtnAddFavorite.setVisibility(View.VISIBLE);
        mBtnCommentNumber= (Button) findViewById(R.id.btn_comment_number);//找到评论数量按钮
        //找到加入收藏的按钮(四个小点点)
        mBtnAddFavorite= (Button)findViewById(R.id.btn_add_favorite_base);//找到加入收藏按钮
        //注意：--------这里是用mView点的(点开四个小点点之后的按钮)
        mBtnPopup= (Button)mView.findViewById(R.id.btn_add_favorite_news_pop);
        //找到WebView
        mWebView= (WebView) findViewById(R.id.web_news_details);
        //绑定点击事件
        mBtnCommentNumber.setOnClickListener(this);//评论数量按钮
        mBtnAddFavorite.setOnClickListener(this);//加入收藏按钮
        mBtnPopup.setOnClickListener(this);
        mImgLeft.setOnClickListener(this);
        Intent intent=getIntent();//拿到intent对象
        NewsInfo news= (NewsInfo) intent.getSerializableExtra("news");//从Fragment接受传递的对象
        //加载链接
        mWebView.loadUrl(news.link);
        //添加一些设置
        WebSettings settings=mWebView.getSettings();
        //支持JavaScript写的网页
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        //可以按任意比例缩放
        settings.setLoadWithOverviewMode(true);
        //支持缩放功能
        settings.setSupportZoom(true);
        //显示缩放视图
        settings.setBuiltInZoomControls(true);
        if (AppInfo.isLogin){ //如果是登陆状态
//            Glide.with(this).load(AppInfo.user.portrait).into(mIvPortraits);//将头像拿过来
//            mTvMyName.setText(AppInfo.user.uid);//将头像拿过来
        }
        getHttpDataCommentNum();//调用跟帖数量接口
        Log.e("aaa", "initView: "+AppInfo.news.nid);
        mBtnCommentNumber.setText("跟帖数量："+AppInfo.commentNum);//AppInfo.commentNum 是int值
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_comment_number://跟帖数量
                Intent intent=new Intent(this,CommentActivity.class);//跳转评论列表界面(Activity)
                startActivity(intent);
                break;
            case R.id.btn_add_favorite_base://加入收藏(四个点点)
                //1---拿到PopupWindow的对象
                popupWindow=new PopupWindow(mView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //2----设置外部可点击：
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                //3----进行展示
                //1.基于某一个控件去显示PopWindow
//                popupWindow.showAsDropDown(mView,0,0);//三个参数 一个View  x，y偏移量
                //2.基于屏幕取显示PopWindow
                popupWindow.showAtLocation(mLinLayoutHead, Gravity.TOP,500,180);
                break;
            case R.id.btn_add_favorite_news_pop://加入收藏字样的按钮(点击四个小点弹出来的popupWindow)
                popupWindow.dismiss();
                Toast.makeText(this, "收藏成功，在主界面左侧滑中查看", Toast.LENGTH_SHORT).show();
//                SQLiteDatabase
                break;
            case R.id.iv_base_left://点击头上的左边箭头
                finish();//直接结束此界面  返回上一个界面
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //返回键 可以返回  不会直接结束
        if (keyCode==KeyEvent.KEYCODE_BACK&&mWebView.canGoBack()){//按键监听  返回  并且  网页能够返回
            mWebView.goBack();//返回
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * cmt_num?ver=版本号& nid=新闻编号
     * 跟贴数量
     */
    public void getHttpDataCommentNum(){
        Map<String,String> params=new HashMap<>();
        params.put("ver","0000000");//版本号
        params.put("nid",AppInfo.news.nid+"");//新闻编号
        MyHttp.get(this, ServerUrl.COMMENT_NUMBER, params,
                new OnResultFinishListener() {
                    @Override
                    public void success(Response response) {//请求成功
                        Gson gson=new Gson();
                        CommentNumInfo commentNumInfo=gson.fromJson((String) response.result,CommentNumInfo.class);
                        Log.e("aaa", "success: "+commentNumInfo.data);
                        AppInfo.commentNum=commentNumInfo.data;
                    }
                    @Override
                    public void failed(Response response) {//请求失败

                    }
                });
    }
}

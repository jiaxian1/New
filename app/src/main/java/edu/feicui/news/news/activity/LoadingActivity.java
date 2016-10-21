package edu.feicui.news.news.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

import edu.feicui.news.news.AppInfo;
import edu.feicui.news.news.R;
import edu.feicui.news.news.activity.fragment.fragment.LoginFragment;
import edu.feicui.news.news.util.Constant;
import edu.feicui.news.news.util.ReadDataBaseUtil;

/**
 * 加载界面
 * Created by jiaXian on 2016/10/17.
 */
public class LoadingActivity extends BaseActivity{
    Button mBtnIntentHome;
    LoginFragment loginFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
//        String[]str=ReadDataBaseUtil.read();//读数据
//        AppInfo.name=str[0];//保存登陆名到静态实体
//        AppInfo.password=str[1];//保存登陆密码到静态实体
//        loginFragment.getHttpDataLogin();//调用一下接口  拿到token
//        AppInfo.token=loginFragment.loginArray.data.token;//保存token到静态实体
        //读、、、
    }
    @Override
    void initView() {
        mBtnIntentHome= (Button) findViewById(R.id.btn_loading_intent_home);
        mBtnIntentHome.setOnClickListener(this);
        //设置头  不让其显示
        mLinLayoutHead.setVisibility(View.GONE);


    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

}

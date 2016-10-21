package edu.feicui.news.news.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.feicui.news.news.AppInfo;
import edu.feicui.news.news.R;
import edu.feicui.news.news.ServerUrl;
import edu.feicui.news.news.activity.fragment.fragment.LoginFragment;
import edu.feicui.news.news.adapter.LoginLogAdapter;
import edu.feicui.news.news.entity.HeadUpLoadingArray;
import edu.feicui.news.news.entity.LoginLog;
import edu.feicui.news.news.entity.UserArray;
import edu.feicui.news.news.net.MyHttp;
import edu.feicui.news.news.net.OnResultFinishListener;
import edu.feicui.news.news.net.Response;

/**
 * 我的帐户界面
 * Created by jiaXian on 2016/10/17.
 */
public class MyAccountActivity extends BaseActivity {
    /**
     * 用户中心的信息
     */
    UserArray userArray;
    /**
     * 登陆日志的RecyclerView列表
     */
    RecyclerView mRecyclerView;
    /**
     * 登陆日志列表的适配器
     */
    LoginLogAdapter loginLogAdapter;
    /**
     * 头像
     */
    ImageView mIvMyaccountPortraits;

    View mView;
    /**
     * 调用相机
     */
    LinearLayout mLLCallCamera;
    /**
     * 选择照片
     */
    LinearLayout mLLSelectPhoto;
    /**
     * 我的帐户名
     */
    TextView mMyName;
    /**
     * 用户积分
     */
    TextView mIntegral;
    /**
     * 评论数量
     */
    TextView mCommentCount;
    PopupWindow popupWindow;
    /**
     * 根布局
     */
    LinearLayout mLLMyaccount;
    /**
     * 退出登陆按钮
     */
    Button mBtnLogout;
    /**
     * 登陆界面碎片
     */
    LoginFragment loginFragment;
    /**
     * 声明SharedPreferences
     */
    SharedPreferences preferencesLogin;
    /**
     * 选择相机的权限请求码
     */
    static final int CAMERA_PERMISSION = 200;
    /**
     * 跳转相机的请求码
     */
    static final int OPEN_CAMERA = 201;
    /**
     * 打开图库的请求码
     */
    static final int OPEN_PICK = 202;
    /**
     * 通过打开相机拍到的图片存储文件夹路径
     */
    public static final String DIR_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + //根路径
            File.separator + "DailyNews";//分隔符  和文件夹名
    /**
     * 图片存储路径
     */
    public static final String PHOTO_FILE_PATH = DIR_PATH + File.separator + "myportraits.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount);
    }

    @Override
    void initView() {
        //初始化登陆界面碎片
        loginFragment = new LoginFragment();
        //使用布局填充器 将popup布局加载进来
        mView = getLayoutInflater().inflate(R.layout.view_myaccount_portraits_pop, null);
        //找到我的头像
        mIvMyaccountPortraits = (ImageView) findViewById(R.id.iv_myaccount_portraits);
        //找到  调用相机和选择照片  这里用View来找到控件
        mLLCallCamera = (LinearLayout) mView.findViewById(R.id.ll_call_camera);//调用相机
        mLLSelectPhoto = (LinearLayout) mView.findViewById(R.id.ll_select_photo);//选择照片
        mLLMyaccount = (LinearLayout) findViewById(R.id.ll_myaccount);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyv_login_log);//登陆日志的列表
        mMyName = (TextView) findViewById(R.id.tv_myaccount_name);//找到我的账户名控件
        mIntegral = (TextView) findViewById(R.id.tv_myaccount_integral);//找到帐户积分
        mCommentCount = (TextView) findViewById(R.id.tv_myaccount_comment_count);//找到跟帖数量
        mBtnLogout = (Button) findViewById(R.id.btn_myacccount_logout);//找到退出登陆按钮
        //绑定点击事件
        mIvMyaccountPortraits.setOnClickListener(this);//我的头像
        mLLCallCamera.setOnClickListener(this);//调用相机
        mLLSelectPhoto.setOnClickListener(this);//选择图片
        mTvTittle.setText("我的账号");
        mImgLeft.setImageResource(R.mipmap.back);
        mImgRight.setVisibility(View.GONE);
        mImgLeft.setOnClickListener(this);
        mBtnLogout.setOnClickListener(this);
        if (AppInfo.login.token == null) { //判断用户指令是否为空
            return;
        } else {
            getHttpDataUserHome();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_myaccount_portraits://点击我的头像  弹出popupWindow  来选择
                //1---拿到PopupWindow的对象
                popupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //2----设置外部可点击：
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                //3----进行展示
                //1.基于某一个控件去显示PopWindow
//                popupWindow.showAsDropDown(mView,0,0);//三个参数 一个View  x，y偏移量
                //2.基于屏幕取显示PopWindow
                popupWindow.showAtLocation(mLLMyaccount, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.ll_call_camera://打开相机
                popupWindow.dismiss();//关闭popupWindow
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//判断版本是否大于23
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&//相机有权限
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {//sdk有了权限
                        goToCamear();//调用跳转相机的方法
                    } else {//没有权限  申请
                        /**
                         * @NonNull String[] permissions  (这里是相机)权限
                         * int requestCode                 请求码(这里是相机的CAMERA_PERMISSION)
                         * new String[]{Manifest.permission.CAMERA}  数组  表示 可以一次请求多个
                         */
                        requestPermissions(new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION);//申请 权限
                    }
                } else {//版本小于23
                    goToCamear();//直接调用跳转相机的方法
                }
                Toast.makeText(this, "请拍照", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_select_photo://选择图库
                popupWindow.dismiss();//关闭popupWindow
                Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent1, OPEN_PICK);//有结果的跳转
                Toast.makeText(this, "请选择照片", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_base_left://左边的箭头  返回
                finish();//结束我的帐户中心
                break;
            case R.id.btn_myacccount_logout://退出登陆按钮
                AppInfo.isLogin = false;//将登陆状态设置为false
//                  AppInfo.token=null;
                //将记住密码选框设置为false
                AppInfo.flag = false;
                //向LoginFragment传递数据
                Intent intent2 = new Intent();
                intent2.putExtra("flags", AppInfo.flag);
                startActivity(intent2);
                //跳转到主界面
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);

                break;
        }
    }

    /**
     * 跳转系统相机
     */
    public void goToCamear() {
        //MediaStore.ACTION_IMAGE_CAPTURE 相当于action
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//跳转打开相机
        //传递此次拍照图片存储的路径
        //指定一个路径去存放 图片   手机SD卡路径
        File fileDir = new File(DIR_PATH);
        if (!fileDir.exists()) {//如果不存在
            fileDir.mkdirs();//创建此文件夹以及父类文件夹
        }
        File file = new File(PHOTO_FILE_PATH);
        //向相机传递文件路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, OPEN_CAMERA);//开始跳转  OPEN_CAMERA 跳转相机请求码
    }

    /**
     * 拿到回传数据的结果
     *
     * @param requestCode 请求码   区分返回结果的 请求
     * @param resultCode  结果码   区分结果是否成功
     * @param data        回传的数据数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case OPEN_CAMERA://请求相机的结果
                Toast.makeText(this, "requestCode=" + requestCode + "--resultCode==" + resultCode, Toast.LENGTH_SHORT).show();
                if (resultCode == RESULT_OK) {
                    //读取图片  路径
                    Bitmap bitmap = BitmapFactory.decodeFile(PHOTO_FILE_PATH);//通过路径拿到图片
                    mIvMyaccountPortraits.setImageBitmap(bitmap);//将图片展示出来
                    getHttpDataPortrait();//上传头像
                }
                break;
            case OPEN_PICK://选择图库拿到的结果
                if (resultCode == RESULT_OK) {  //如果有数据
                    Uri uri = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String path = cursor.getString(columIndex);
                    Log.e("aaa", "onActivityResult: " + path);
                    try {
                        Bitmap bit = BitmapFactory.decodeStream(new FileInputStream(path));
                        mIvMyaccountPortraits.setImageBitmap(bit);//将拿到的图片展示出来
                        getHttpDataPortrait();//上传头像
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
    /**
     * @param requestCode  请求码
     * @param permissions  所请求的权限
     * @param grantResults 所请求权限的结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {//通过请求码来区分
            case CAMERA_PERMISSION://相机请求
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {  //同意相机权限
                    goToCamear();//调用跳转相机的方法
                } else {//拒绝相机权限
                    Toast.makeText(this, "打开相机需要赋予权限 权限管理-->应用-->权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 用户中心
     * user_home?ver=版本号&imei=手机标识符&token=用户令牌
     */
    public void getHttpDataUserHome() {
        Map<String, String> params = new HashMap<>();
        params.put("ver", "0000000");//版本
        params.put("imei", "868564020877953");//手机标识符  从手机获取的  在拨号处  打出  *#06# 的字样  会拿自动弹出MEID和IMEI
        params.put("token", AppInfo.login.token);//用户令牌   登陆的时候的参数 直接拿来使用（从LoginFragment传递过来）
        MyHttp.get(this, ServerUrl.USER_HOME, params,
                new OnResultFinishListener() {
                    @Override
                    public void success(Response response) {//获取用户信息成功
                        Gson gson = new Gson();
                        userArray = gson.fromJson((String) response.result, UserArray.class);
                        Toast.makeText(MyAccountActivity.this, userArray.message, Toast.LENGTH_SHORT).show();
                        AppInfo.user = userArray.data;//赋值
                        if (AppInfo.isLogin) {
                            userArray.data = AppInfo.user;
                        }
                        mMyName.setText(userArray.data.uid);//将我的账户名展示出来
                        mIntegral.setText("积分：" + userArray.data.integration);//将积分展示出来
                        mCommentCount.setText("跟帖统计数：" + userArray.data.comnum);//将跟帖数量展示出来
                        ArrayList<LoginLog> list = userArray.data.loginlog;//数据源
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(MyAccountActivity.this));//给RecyclerView列表设置布局管理器
                        loginLogAdapter = new LoginLogAdapter(list, MyAccountActivity.this);//初始化登陆日志RecyclerView列表的 适配器
                        mRecyclerView.setAdapter(loginLogAdapter);//绑定适配器
                    }

                    @Override
                    public void failed(Response response) {//获取用户信息失败
                        Toast.makeText(MyAccountActivity.this, "获取用户中心失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, HomeActivity.class);//我的帐户中心结束，直接跳回HomeActivity
        startActivity(intent);
    }

    /**
     * user_image?token=用户令牌& portrait =头像
     * 头像上传   说服务器有问题
     */
    public void getHttpDataPortrait() {
        Map<String, String> params = new HashMap<>();
        params.put("token", AppInfo.login.token);//用户令牌
        params.put("portrait", "file");//头像
        MyHttp.get(this, ServerUrl.USER_IMAGE, params,
                new OnResultFinishListener() {
                    @Override
                    public void success(Response response) {//上传头像成功
                        Gson gson = new Gson();
                        HeadUpLoadingArray headUpLoadingArray = gson.fromJson((String) response.result, HeadUpLoadingArray.class);
                        Toast.makeText(MyAccountActivity.this, headUpLoadingArray.data.explain, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failed(Response response) {//上传头像失败

                    }
                });

    }
}

package edu.feicui.news.news.activity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import edu.feicui.news.news.AppInfo;
import edu.feicui.news.news.R;
import edu.feicui.news.news.ServerUrl;
import edu.feicui.news.news.XList.XListView;
import edu.feicui.news.news.activity.fragment.fragment.FavoriteFragment;
import edu.feicui.news.news.activity.fragment.fragment.FindPasswordFragment;
import edu.feicui.news.news.activity.fragment.fragment.LoginFragment;
import edu.feicui.news.news.activity.fragment.fragment.NewsFragment;
import edu.feicui.news.news.activity.fragment.fragment.RegisterFragment;
import edu.feicui.news.news.adapter.NewsListAdapter;
import edu.feicui.news.news.entity.UpDateInfo;
import edu.feicui.news.news.net.MyHttp;
import edu.feicui.news.news.net.OnResultFinishListener;
import edu.feicui.news.news.net.Response;
import edu.feicui.news.news.util.Constant;
import edu.feicui.news.news.util.ReadDataBaseUtil;
import edu.feicui.news.news.util.SQLDateBaseUtil;
import edu.zx.slidingmenu.SlidingMenu;

/**
 * 主界面
 * Created by jiaXian on 2016/10/17.
 */
public class HomeActivity extends BaseActivity {
    /**
     * 左右滑动
     */
    SlidingMenu slidingMenu;
    /**
     * 新闻列表的Fragment
     */
    NewsFragment mNewsFragment;
    /**
     * 登陆界面的Fragment
     */
    LoginFragment mLoginFragment;
    /**
     * 收藏列表的Fragment
     */
    FavoriteFragment mCollectFragment;
    /**
     * 注册界面的Fragment
     */
    RegisterFragment mRegisterFragment;
    /**
     * 忘记密码的Fragment
     */
    FindPasswordFragment mFindPasswordFragment;
    /**
     * 新闻的ListView
     */
    XListView mXListNews;
    /**
     * 新闻列表适配器
     */
    NewsListAdapter mAdapter;
    /**
     * 左边抽屉的每个子线性布局
     */
    LinearLayout mLinLayViewLeftNews;
    LinearLayout mLinLayViewLeftFavorite;
    LinearLayout mLinLayViewLeftLocal;
    LinearLayout mLinLayViewLeftComment;
    LinearLayout mLinLayViewLeftPhoto;
    /**
     * 右边抽屉的登陆按钮(包括立即登陆的字和上面的图)
     */
    LinearLayout mLinLayRightLogin;
    /**
     * 版本更新
     */
    Button mBtnUpDateVersion;
    /**
     * 包名
     */
    String packageName;
    /**
     * 当前应用的版本号
     */
    int versionThisCode;
    /**
     * 通过网络请求获得的最新版本号
     */
    int versionThatCode;
    /**
     * 碎片管理器
     */
    FragmentManager fragmentManager;
    /**
     * 下载的id
     */
    long downloadId;
    /**
     * 下载管理器
     */
    DownloadManager downloadManager;
    /**
     * 下载接收器  接受系统的下载完成广播
     */
    DownLoadReceiver receiver;
    /**
     * 声明SharedPreferences
     */
    SharedPreferences preferences;
    /**
     * /头像
     */
    ImageView mIvPortraits;
    /**
     * 账户名
     */
    TextView mTvMyName;
    /**
     * 数据库工具
     */
    SQLDateBaseUtil sqlDateBaseUtil;
    /**
     * 分享到微信好友
     */
    ImageView mIvWeChat;
    /**
     * 分享到微信朋友圈
     */
    ImageView mIvWeChatMoments;
    /**
     * 分享到新浪微博
     */
    ImageView mIvSinaMicroblog;
    /**
     * 分享到qq好友
     */
    ImageView mIvQQ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);//加载布局
    }

    @Override
    void initView() {
        //初始化ShareSDK
        ShareSDK.initSDK(this,"sharesdk的appkey");
        //1.初始化SlidingMenu
        slidingMenu = new SlidingMenu(this);
        //2.将SlidingMenu与当前Activity进行绑定
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //布局填充器
        LayoutInflater inflater = getLayoutInflater();
        View leftView = inflater.inflate(R.layout.include_home_drawer_left, null);//使用布局填充器将xml文件转化成View
        slidingMenu.setMenu(leftView);//设置左边的View
        View rightView = inflater.inflate(R.layout.include_home_drawer_right, null);//使用布局填充器将xml文件转化成View
        slidingMenu.setSecondaryMenu(rightView);//设置又变得View
        //3.并设置模式
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        //4.设置边界距离  防止菜单栏完全遮盖主内容
        slidingMenu.setBehindOffset(300);//设置边界距离
        //设置触摸模式  全屏滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //初始化每个碎片
        mNewsFragment = new NewsFragment();
        mLoginFragment = new LoginFragment();
        mCollectFragment = new FavoriteFragment();
        mRegisterFragment = new RegisterFragment();
        mFindPasswordFragment = new FindPasswordFragment();
        //拿到FragmentManager的对象
        fragmentManager = getSupportFragmentManager();
        //找到新闻列表的ListView
        mXListNews = (XListView) findViewById(R.id.lv_home_news_fragment);
        mAdapter = new NewsListAdapter(this, null, R.layout.item_home_news);
        //找到左边抽屉的每个子线性布局
        mLinLayViewLeftNews = (LinearLayout) leftView.findViewById(R.id.ll_drawer_left_news);
        mLinLayViewLeftFavorite = (LinearLayout) leftView.findViewById(R.id.ll_drawer_left_favorite);
        mLinLayViewLeftLocal = (LinearLayout) leftView.findViewById(R.id.ll_drawer_left_local);
        mLinLayViewLeftComment = (LinearLayout) leftView.findViewById(R.id.ll_drawer_left_comment);
        mLinLayViewLeftPhoto = (LinearLayout) leftView.findViewById(R.id.ll_drawer_left_photo);
        //找到右边抽屉的登陆按钮(包括立即登陆的字和上面的图)
        mLinLayRightLogin = (LinearLayout) rightView.findViewById(R.id.ll_home_include_right_login);
        //找到更新版本的按钮
        mBtnUpDateVersion = (Button) rightView.findViewById(R.id.btn_update_version);
        //找到我的头像
        mIvPortraits = (ImageView) rightView.findViewById(R.id.biz_pc_main_info_profile_avatar_bg_dark);
        //找到我的账户名
        mTvMyName = (TextView) rightView.findViewById(R.id.tv_include_bottom_login_name);
        //找到分享到微信好友
        mIvWeChat= (ImageView) findViewById(R.id.iv_include_home_drawer_right_wechat);
        //找到分享到微信朋友圈
        mIvWeChatMoments= (ImageView) findViewById(R.id.iv_include_home_drawer_right_wechat_moments);
        //找到分享新浪微博
        mIvSinaMicroblog= (ImageView) findViewById(R.id.iv_include_home_drawer_right_microblog);
        //找到分享到qq
        mIvQQ= (ImageView) findViewById(R.id.iv_include_home_drawer_right_qq);
        //给左边的每个子线性布局绑定点击事件
        mLinLayViewLeftNews.setOnClickListener(this);
        mLinLayViewLeftFavorite.setOnClickListener(this);
        mLinLayViewLeftLocal.setOnClickListener(this);
        mLinLayViewLeftComment.setOnClickListener(this);
        mLinLayViewLeftPhoto.setOnClickListener(this);
        //给右边抽屉的登陆按钮绑定点击事件
        mLinLayRightLogin.setOnClickListener(this);
        mBtnUpDateVersion.setOnClickListener(this);
        //给第三方分享绑定点击事件
        mIvWeChat.setOnClickListener(this);
        mIvWeChatMoments.setOnClickListener(this);
        mIvSinaMicroblog.setOnClickListener(this);
        mIvQQ.setOnClickListener(this);
        if (AppInfo.isLogin) {  //如果是登陆状态
            Glide.with(this).load(AppInfo.user.portrait).into(mIvPortraits);//将头像拿过来
            mTvMyName.setText(AppInfo.user.uid);//将用户名拿过来
        }
        addFragment();//调用添加碎片的方法
        getHttpDataUpDateVersion();//调用 版本更新的接口  应该在点击版本更新之前  因为  点击版本更新的时候需要去判断  版本号等信息
        getAppInfo();//调用拿到当前应用信息的方法
        //初始化数据库工具
        sqlDateBaseUtil = new SQLDateBaseUtil(this);
        sqlDateBaseUtil.insertLoginInfo();
        sqlDateBaseUtil.selectLoginInfo();
    }

    /**
     * 添加碎片方法
     */
    public void addFragment() {
        /**
         * 首先添加碎片
         */
        FragmentTransaction newsTransaction = fragmentManager.beginTransaction();//开启事务
        newsTransaction
                .add(R.id.ll_home_content, mNewsFragment)//将新闻列表的碎片添加到HomeActivity中
                .add(R.id.ll_home_content, mCollectFragment)//将收藏列表的碎片添加到HomeActivity中
                .add(R.id.ll_home_content, mLoginFragment)//将登陆界面的碎片添加到HomeActivity中
                .add(R.id.ll_home_content, mRegisterFragment)//将注册界面的碎片添加到HomeActivity中
                .add(R.id.ll_home_content, mFindPasswordFragment)//将找回密码界面的碎片添加到HomeActivity中
                .show(mNewsFragment)
                .commit();//提交生效
    }

    /**
     * 拿到当前应用的各种信息
     */
    public void getAppInfo() {
        //首先拿到PackageManager的对象
        PackageManager packageManager = getPackageManager();
        //拿到安装的包
        List<PackageInfo> lists = packageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo : lists) {
            //通过PackageInfo拿到包名  版本号
            packageName = packageInfo.packageName;//拿到当前应用的包名
            versionThisCode = packageInfo.versionCode;//拿到当前应用的版本号
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_base_left://点击左边的base图片
                slidingMenu.showMenu();
                break;
            case R.id.iv_base_right://点击右边的base图片
                slidingMenu.showSecondaryMenu();
                break;
            case R.id.ll_drawer_left_news://点击新闻
                Toast.makeText(HomeActivity.this, "新闻", Toast.LENGTH_SHORT).show();
                //调用切换碎片的方法  第一个是展示的碎片  后面的用可变数组传参数 都是隐藏的碎片
                getFragment(mNewsFragment, mCollectFragment, mLoginFragment, mRegisterFragment, mFindPasswordFragment);
                slidingMenu.showContent();//将左边的抽屉布局关闭
                break;
            case R.id.ll_drawer_left_favorite:
                Toast.makeText(HomeActivity.this, "收藏", Toast.LENGTH_SHORT).show();
                //调用切换碎片的方法  第一个是展示的碎片  后面的用可变数组传参数 都是隐藏的碎片
                getFragment(mCollectFragment, mNewsFragment, mLoginFragment, mRegisterFragment, mFindPasswordFragment);
                slidingMenu.showContent();//将左边的抽屉布局关闭
                break;
            case R.id.ll_drawer_left_local:
                Toast.makeText(HomeActivity.this, "本地", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_drawer_left_comment:
                Toast.makeText(HomeActivity.this, "评论", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_drawer_left_photo:
                Toast.makeText(HomeActivity.this, "图片", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_home_include_right_login://找到右边抽屉的登陆按钮(包括立即登陆的字和上面的图)
                if (AppInfo.isLogin) {  //判断  注意：这里退出登陆后  将其设置为false
                    Intent intent = new Intent(this, MyAccountActivity.class);//跳转到我的账户中心
                    startActivity(intent);
                } else {
                    //调用切换碎片的方法  第一个是展示的碎片  后面的用可变数组传参数 都是隐藏的碎片
                    getFragment(mLoginFragment, mCollectFragment, mNewsFragment, mRegisterFragment, mFindPasswordFragment);
                    slidingMenu.showContent();//将右边的抽屉布局关闭
                    mTvTittle.setText("登陆");
                }
                break;
            case R.id.btn_login_register://注册
                //调用切换碎片的方法  第一个是展示的碎片  后面的用可变数组传参数 都是隐藏的碎片
                getFragment(mRegisterFragment, mLoginFragment, mCollectFragment, mNewsFragment, mFindPasswordFragment);
                slidingMenu.showContent();//将右边的抽屉布局关闭
                break;
            case R.id.btn_login_forget_password://忘记密码
                //调用切换碎片的方法  第一个是展示的碎片  后面的用可变数组传参数 都是隐藏的碎片
                getFragment(mFindPasswordFragment, mRegisterFragment, mLoginFragment, mCollectFragment, mNewsFragment);
                slidingMenu.showContent();//将右边的抽屉布局关闭
                break;
            case R.id.btn_update_version://点击更新版本
                //判断当前版本是否和最新版本一样
                if (versionThisCode != versionThatCode) {//如果不一样
                    //点击触发下载
                    //这里的下载连接  最好在网上找一个外网的  否则 关闭服务器之后就不能更新了
                    upDateAPK("http://192.168.199.239:8080/b.apk", "down.apk");
                    //就更新
                } else {//一样
                    Toast.makeText(HomeActivity.this, "此版本已经是最新版本", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_include_home_drawer_right_wechat://点击微信好友分享
                showShare();
                break;
            case R.id.iv_include_home_drawer_right_wechat_moments://点击微信朋友圈分享
                showShare();
                break;
            case R.id.iv_include_home_drawer_right_microblog://点击微博分享
                showShare();
                break;
            case R.id.iv_include_home_drawer_right_qq://点击qq分享
                showShare();
                break;

        }
    }

    /**
     * 碎片替换方法
     *
     * @param showFragment 只展示一个碎片
     * @param hideFragment 隐藏其他的碎片
     */
    public void getFragment(Fragment showFragment, Fragment... hideFragment) {
        FragmentTransaction TransactionNext = fragmentManager.beginTransaction();//开启事务
        TransactionNext
                .show(showFragment)//---------展示注册
                .hide(hideFragment[0])//隐藏新闻
                .hide(hideFragment[1])//隐藏收藏
                .hide(hideFragment[2])//隐藏登陆
                .hide(hideFragment[3])//隐藏找回密码
                .commit();//提交生效
    }

    /**
     * 替换碎片  暴露给外面使用
     */
    public void replaceFragment(int flag) {
        switch (flag) {
            case 0://注册
                //调用切换碎片的方法  第一个是展示的碎片  后面的用可变数组传参数 都是隐藏的碎片
                getFragment(mRegisterFragment, mLoginFragment, mCollectFragment, mNewsFragment, mFindPasswordFragment);
                slidingMenu.showContent();//将右边的抽屉布局关闭
                mTvTittle.setText("注册");
                break;
            case 1://找回密码
                //调用切换碎片的方法  第一个是展示的碎片  后面的用可变数组传参数 都是隐藏的碎片
                getFragment(mFindPasswordFragment, mRegisterFragment, mLoginFragment, mCollectFragment, mNewsFragment);
                slidingMenu.showContent();//将右边的抽屉布局关闭
                mTvTittle.setText("忘记密码");
                break;
            case 2://登陆
                //调用切换碎片的方法  第一个是展示的碎片  后面的用可变数组传参数 都是隐藏的碎片
                getFragment(mLoginFragment, mCollectFragment, mNewsFragment, mRegisterFragment, mFindPasswordFragment);
                slidingMenu.showContent();//将右边的抽屉布局关闭
                mTvTittle.setText("登陆");
                mLoginFragment.flag = false;
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //判断菜单是否正在显示
        if (slidingMenu.isMenuShowing() || slidingMenu.isSecondaryMenuShowing()) {//判断两个  如果有一个在显示  则返回的时候显示中间的
            slidingMenu.showContent();//展示中间的内容
            return;
        }
        super.onBackPressed();//否则  就直接结束  finish
    }

    /**
     * 版本更新
     * update?imei=唯一识别号&pkg=包名&ver=版本
     */
    public void getHttpDataUpDateVersion() {
        Map<String, String> params = new HashMap<>();
        params.put("imei", "868564020877953");//手机标识符
        params.put("pkg", packageName);//包名
        params.put("ver", "0000000");
        MyHttp.get(this, ServerUrl.UPDATE, params,
                new OnResultFinishListener() {
                    @Override
                    public void success(Response response) {//
                        Gson gson = new Gson();
                        UpDateInfo upDateInfo = gson.fromJson((String) response.result, UpDateInfo.class);
                        AppInfo.versionCode = upDateInfo.version;
                        //通过网络请求拿到的版本号是String类型的  将其强转为int类型
                        versionThatCode = Integer.parseInt(AppInfo.versionCode);
                    }

                    @Override
                    public void failed(Response response) {

                    }
                });
    }


    /**
     * 更新APK方法
     */
    public void upDateAPK(String downloadUri, String downloadPath) {
        //1.拿到DownloadManager的对象
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        //2.需要创建一个下载请求
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUri));
        //3.做一些额外的设置
        //网络要求    WIFI和MOBILE 都可以下载
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        //设置通知栏是否显示
        request.setShowRunningNotification(true);
        //设置下载存放路径
        request.setDestinationInExternalFilesDir(this, null, downloadPath);
        //4.下载
        Toast.makeText(this, "加入请求队列...", Toast.LENGTH_SHORT).show();
        downloadId = downloadManager.enqueue(request);//将请求加到下载队列
    }

    /**
     * 分享
     */
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //广播注册
        receiver = new DownLoadReceiver();//初始化接收器
        //Action  过滤   调频
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);//下载完成时
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //广播反注册
        unregisterReceiver(receiver);

    }

    /**
     * 下载完成监听广播
     */
    public class DownLoadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
            //需要接收到下载的一个ID
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadId == id) {//如果下载的id和
                //打开此apk  隐式意图
                //路径
                Uri uri = downloadManager.getUriForDownloadedFile(downloadId);
                //   file:///storage/emulated/0/Android/data/edu.feicui.testmenu.testslidingmenu/files/down-4.apk
                Log.e("aaa", "onReceive: -----------------------" + uri);
/*******************************************************************************************
 *      这部分  是 转化路径的   有的手机的uri  不是路径  需要以下的代码取转换
 *      有的手机的uri直接是路径 不需要转化  直接可以使用
 //                String[] filePathColumn={MediaStore.Images.Media.DATA};
 //                Cursor cursor=getContentResolver().query(uri,filePathColumn,null,null,null);
 //                cursor.moveToFirst();
 //                int columIndex=cursor.getColumnIndex(filePathColumn[0]);
 //                String path=cursor.getString(columIndex);
 //                Log.e("aaa", "onReceive: "+path);
 ******************************************************************************************/
                Intent intent1 = new Intent();
                intent1.setAction(Intent.ACTION_VIEW);
                //指定文件路径   和打开的文件类型
                intent1.setDataAndType(uri, "application/vnd.android.package-archive");
                startActivity(intent1);
            }
        }
    }

}

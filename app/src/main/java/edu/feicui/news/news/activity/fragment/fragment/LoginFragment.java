package edu.feicui.news.news.activity.fragment.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.feicui.news.news.AppInfo;
import edu.feicui.news.news.R;
import edu.feicui.news.news.ServerUrl;
import edu.feicui.news.news.activity.HomeActivity;
import edu.feicui.news.news.activity.MyAccountActivity;
import edu.feicui.news.news.entity.LoginArray;
import edu.feicui.news.news.net.MyHttp;
import edu.feicui.news.news.net.OnResultFinishListener;
import edu.feicui.news.news.net.Response;
import edu.feicui.news.news.util.SQLDateBaseUtil;

/**
 * 登陆碎片
 * Created by jiaXian on 2016/10/10.
 */
public class LoginFragment extends Fragment implements TextWatcher, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    /**
     * 登陆状态
     */
    boolean isLogin;
    View mView;
    /**
     * 调用接口解析到的登陆响应信息
     */
    public LoginArray loginArray;
    HomeActivity activity;
    /**
     * 注册按钮
     */
    Button mBtnRegister;
    /**
     * 忘记密码按钮
     */
    Button mBtnForgetPassword;
    /**
     * 数据库工具
     */
    SQLDateBaseUtil sqlDateBaseUtil;
    /**
     * 登陆按钮
     */
    Button mBtnLogin;
    /**
     * 记住密码
     */
    CheckBox mRemberPassword;
    /**
     * 默认  false
     */
    public boolean flag;
    /**
     * 登陆昵称
     */
    TextInputLayout mTextInputLayoutLoginName;
    TextInputEditText mTextInputEditTextLoginName;
    /**
     * 登陆密码
     */
    TextInputLayout mTextInputLayoutLoginPassword;
    TextInputEditText mTextInputEditTextLoginPassword;
    /**
     * 声明SharedPreferences
     */
    SharedPreferences preferencesLogin;
    /**
     * SharedPreferences方法参数
     */
    static String PREFERENCE_NAME_LOGIN = "preference_settings_login";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    /**
     * 自己写的  用起来和Activity的一样  比较方便
     *
     * @param id
     * @return
     */
    public View findViewById(int id) {
        return mView.findViewById(id);
    }

    /**************************************************************************
     * *****************        初始化 和 加载控件     *************************
     **************************************************************************/
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {//view已经被创建
        super.onViewCreated(view, savedInstanceState);
        mView = getView();//拿到View
        activity = (HomeActivity) getActivity();//拿到Activity
        //找到控件
        mBtnRegister = (Button) mView.findViewById(R.id.btn_login_register);//注册
        mBtnForgetPassword = (Button) findViewById(R.id.btn_login_forget_password);//忘记密码
        mBtnLogin = (Button) findViewById(R.id.btn_login_login);//登陆
        mTextInputLayoutLoginName = (TextInputLayout) findViewById(R.id.tvlay_login_name);//登陆昵称外面
        mTextInputEditTextLoginName = (TextInputEditText) findViewById(R.id.tvedt_login_name);//登陆昵称里面
        mTextInputLayoutLoginPassword = (TextInputLayout) findViewById(R.id.tvlay_login_password);//登陆密码外面
        mTextInputEditTextLoginPassword = (TextInputEditText) findViewById(R.id.tvedt_login_password);//登陆密码里面
        mRemberPassword = (CheckBox) findViewById(R.id.ch_rember_password);//找到记住密码的选择框
        //绑定监听事件
        mTextInputEditTextLoginName.addTextChangedListener(this);//登陆昵称文本改变监听
        mTextInputEditTextLoginPassword.addTextChangedListener(this);//登陆密码文本改变监听
        mBtnRegister.setOnClickListener(this);//注册
        mBtnForgetPassword.setOnClickListener(this);//忘记密码
        mBtnLogin.setOnClickListener(this);//登陆
        mRemberPassword.setOnCheckedChangeListener(this);//给记住密码的选框绑定状态改变事件
        preferencesLogin = activity.getSharedPreferences(PREFERENCE_NAME_LOGIN, Context.MODE_PRIVATE);

        if (preferencesLogin.getBoolean("ok", AppInfo.flag)) {// 默认是false 不是第一次进入  拿到了即使拿到的 //第一次进入时将选框选中  则flag=true  不选中为false
            boolean a=preferencesLogin.getBoolean("ok", AppInfo.flag);
            mTextInputEditTextLoginName.setText(preferencesLogin.getString("name", ""));
            mTextInputEditTextLoginPassword.setText(preferencesLogin.getString("password", ""));
//            mTextInputEditTextLoginName.append(preferencesLogin.getString("name",""));
//            mTextInputEditTextLoginPassword.append(preferencesLogin.getString("password",""));
            mRemberPassword.setChecked(true);//将选择状态设置
        }

    }

    /**************************************************************************
     * ***************** 对TextInputEditText的监听事件 *************************
     **************************************************************************/

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mTextInputEditTextLoginPassword.hasFocus()) { //判断焦点
            if ((mTextInputEditTextLoginPassword.length() >= 6) && (mTextInputEditTextLoginPassword.length() <= 12)) {//判断长度
                mTextInputEditTextLoginPassword.setError(null);


            } else {
                mTextInputEditTextLoginPassword.setError("密码的长度在6-12位");
            }
        } else if (mTextInputEditTextLoginName.hasFocus()) {//判断焦点
            Pattern p = Pattern.compile("[a-zA-Z0-9_]{6,24}");//用正则取匹配
            Matcher m = p.matcher(s);
            boolean name = m.matches();
            if (!name) {
                mTextInputEditTextLoginName.setError("昵称格式不正确");
            } else {
                mTextInputEditTextLoginName.setError(null);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_register://注册
                //调用 HomeActivity中的方法  通知HomeActivity去进行fragment的切换
                activity.replaceFragment(0);
                break;
            case R.id.btn_login_forget_password://忘记密码
                Log.e("aaa", "onClick: " + mTextInputEditTextLoginName.getText().toString());
                //调用 HomeActivity中的方法  通知HomeActivity去进行fragment的切换
                activity.replaceFragment(1);
                break;
            case R.id.btn_login_login://登陆
                //注意：***************这里的账号名和密码不是null 而是一个空的字符串  要用equals来判断
                if (mTextInputEditTextLoginName.getText().toString().equals("") ||
                        mTextInputEditTextLoginPassword.getText().toString().equals("")) {  //判断如果密码或者用户名为空，则不让其点击登陆按钮
                    Toast.makeText(activity, "密码/账号不能为空", Toast.LENGTH_SHORT).show();
                    Log.e("aaa", "onClick: " + AppInfo.token);
                    activity.replaceFragment(2);//通知HomeActivity  打开登陆的碎片(在此界面的效果是 只弹出吐丝窗口)
                } else { //不为空
                    getHttpDataLogin();//调用登陆的接口
                    break;
                }
        }
    }

    /**
     * user_login?ver=版本号&uid=用户名&pwd=密码&device=0
     * 登陆
     */
    public void getHttpDataLogin() {
        Map<String, String> params = new HashMap<>();
        params.put("ver", "0000000");//版本
        params.put("uid", mTextInputEditTextLoginName.getText().toString());//用户名
        params.put("pwd", mTextInputEditTextLoginPassword.getText().toString());//密码
        params.put("device", "0");//登陆设备   0为手机客户端   1为PC网页端
        MyHttp.get(activity, ServerUrl.USER_LOGIN, params,
                new OnResultFinishListener() {
                    @Override
                    public void success(Response response) {//登陆成功
                        Gson gson = new Gson();
                        loginArray = gson.fromJson((String) response.result, LoginArray.class);
                        AppInfo.login = loginArray.data;//将登陆的信息保存在静态类中
                        Log.e("aaa", "success: " + loginArray.data.token);
                        //应该在请求网络成功之后再跳转  因为请求网络是在子线程中做的
                        // 如果请求网络之间就跳转  跳转的时候请求网络可能还没有成功 而跳转过去要使用请求网络拿到的数据 从而造成空指针
                        if (loginArray.message.equals("OK")) {
                            isLogin = true;//登陆成功之后 将登陆状态置为true
                            AppInfo.isLogin=isLogin;
                            AppInfo.token=loginArray.data.token;
                            SharedPreferences.Editor editor = preferencesLogin.edit();
                            editor.putBoolean("ok", AppInfo.flag);//第一次进入时将选框选中  则flag=true  不选中为false
                            editor.putString("name", mTextInputEditTextLoginName.getText().toString());
                            editor.putString("password", mTextInputEditTextLoginPassword.getText().toString());
                            editor.commit();
                            Log.e("aaa", "success:================ " + AppInfo.token);
                            Intent intent = new Intent(activity, MyAccountActivity.class);//跳转到我的账户中心
                            startActivity(intent);
                        }
//                        AppInfo.isLogin = isLogin;//将登陆状态保存
//                        AppInfo.name = mTextInputEditTextLoginName.getText().toString();//将登陆名保存到静态量中
//                        AppInfo.password = mTextInputEditTextLoginPassword.getText().toString();//将登陆密码保存到静态量中
//                        AppInfo.token = loginArray.data.token;
//                        sqlDateBaseUtil.insertLoginInfo();//将保存到静态量中的信息保存到数据库
//                        sqlDateBaseUtil = new SQLDateBaseUtil(activity);
//                        sqlDateBaseUtil.insertLoginInfo();
//                        sqlDateBaseUtil.selectLoginInfo();
                        Toast.makeText(activity, loginArray.data.explain, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failed(Response response) {//登陆失败
                        Toast.makeText(activity, "登陆失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        Intent intent=activity.getIntent();
        boolean flags = intent.getBooleanExtra("flags", false);
        isChecked=flags;
        flag = isChecked;//是选框的选中状态
        AppInfo.flag=isChecked;
    }
}

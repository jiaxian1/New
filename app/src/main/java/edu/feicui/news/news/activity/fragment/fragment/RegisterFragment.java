package edu.feicui.news.news.activity.fragment.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
import edu.feicui.news.news.entity.RegisterArray;
import edu.feicui.news.news.net.MyHttp;
import edu.feicui.news.news.net.OnResultFinishListener;
import edu.feicui.news.news.net.Response;

/**
 * 注册的碎片
 * Created by jiaXian on 2016/10/10.
 */
public class RegisterFragment extends Fragment implements TextWatcher, View.OnClickListener {

    View mView;
    HomeActivity activity;
    /**
     * 服务条款
     */
    CheckBox mCheckTermOfServers;
    /**
     * 立即注册按钮
     */
    Button mBtnRegister;
    /**
     * 注册邮箱
     */
    TextInputLayout mTextInputLayoutRegisterEmail;
    TextInputEditText mTextInputEditTextRegisterEmail;
    /**
     * 注册昵称
     */
    TextInputLayout mTextInputLayoutRegisterName;
    TextInputEditText mTextInputEditTextRegisterName;
    /**
     * 注册密码
     */
    TextInputLayout mTextInputLayoutRegisterPassword;
    TextInputEditText mTextInputEditTextRegisterPassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = getView();//拿到View
        activity = (HomeActivity) getActivity();//拿到Activity
        //找到各控件
        mTextInputLayoutRegisterEmail = (TextInputLayout) findViewById(R.id.tvlay_register_email);//注册邮箱的外面
        mTextInputEditTextRegisterEmail = (TextInputEditText) findViewById(R.id.tvedt_register_email);//注册邮箱的里面
        mTextInputLayoutRegisterName = (TextInputLayout) findViewById(R.id.tvlay_register_name);//注册昵称的外面
        mTextInputEditTextRegisterName = (TextInputEditText) findViewById(R.id.tvedt_register_name);//注册昵称的里面
        mTextInputLayoutRegisterPassword = (TextInputLayout) findViewById(R.id.tvlay_register_password);//注册密码的外面
        mTextInputEditTextRegisterPassword = (TextInputEditText) findViewById(R.id.tvedt_register_password);//注册密码的里面
        mBtnRegister = (Button) findViewById(R.id.btn_register_now);//立即注册按钮
        mCheckTermOfServers = (CheckBox) findViewById(R.id.ch_terms_of_servers);//服务条款
        //绑定监听事件
        mTextInputEditTextRegisterEmail.addTextChangedListener(this);//注册邮箱
        mTextInputEditTextRegisterName.addTextChangedListener(this);//注册昵称
        mTextInputEditTextRegisterPassword.addTextChangedListener(this);//注册密码
        mBtnRegister.setOnClickListener(this);//立即注册按钮
    }

    /**
     * 验证邮箱的方法
     *
     * @return
     */
    public boolean validateEmail(String email) {
        Pattern p = Pattern.compile("[a-zA-Z0-9]{1,12}+@([a-zA-Z0-9]{1,12})+(\\.[a-zA-Z0-9]{1,12})");
        Matcher m = p.matcher(mTextInputEditTextRegisterEmail.getText().toString());
        boolean isemail = m.matches();
        if (!isemail) {
            mTextInputLayoutRegisterEmail.setError("邮箱格式不正确");
        } else {
            mTextInputLayoutRegisterEmail.setError(null);
        }
        return isemail;
    }

    /**
     * 验证密码的方法  （6-12位字符）
     *
     * @return
     */
    public boolean validatePassword(String password) {
        boolean isPassword = (password.length() >= 6) && (password.length() <= 12);
        if ((password.length() >= 6) && (password.length() <= 12)) {
            mTextInputEditTextRegisterPassword.setError(null);
        } else {
            mTextInputEditTextRegisterPassword.setError("密码的长度在6-12位");
        }
        return isPassword;
    }

    /**
     * 验证昵称的方法  （6-12位字符）
     *
     * @return
     */
    public boolean validateName(String name) {
        Pattern p = Pattern.compile("[a-zA-Z0-9_]{6,24}");
        Matcher m = p.matcher(mTextInputEditTextRegisterName.getText().toString());
        boolean isName = m.matches();
        if (!isName) {
            mTextInputLayoutRegisterName.setError("昵称格式不正确");
        } else {
            mTextInputLayoutRegisterName.setError(null);
        }
        return isName;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mTextInputEditTextRegisterEmail.hasFocus()) {
            validateEmail(mTextInputEditTextRegisterEmail.getText().toString());//进行邮箱验证
        } else if (mTextInputEditTextRegisterPassword.hasFocus()) {
            validatePassword(mTextInputEditTextRegisterPassword.getText().toString());//进行密码验证
        } else if (mTextInputEditTextRegisterName.hasFocus()) {
            validateName(mTextInputEditTextRegisterName.getText().toString());//进行昵称验证
        }
    }

    @Override
    public void onClick(View v) {
        if (mCheckTermOfServers.isChecked()) {
            if (mTextInputEditTextRegisterEmail.getText().toString().equals("") ||//判断 邮件是否为空
                    mTextInputEditTextRegisterPassword.getText().toString().equals("") ||//判断密码是否为空
                    mTextInputEditTextRegisterName.getText().toString().equals("")) {//判断昵称是否为空
                //三个 之中由一个为空 就不精细操作
                Toast.makeText(activity, "邮箱/密码/昵称不能为空", Toast.LENGTH_SHORT).show();
                activity.replaceFragment(0);//通知HomeActivity 打开注册的碎片(在这里的效果是 不做操作 只弹出吐丝窗口)
            } else {//不为空
                getHttpDataRegister();//调用注册的方法
            }
        } else {
            Toast.makeText(activity, "请同意服务条款", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 注册
     * user_register?ver=版本号&uid=用户名&email=邮箱&pwd=登陆密码
     */
    public void getHttpDataRegister() {
        Map<String, String> params = new HashMap<>();
        params.put("ver", "0000000");//版本号
        params.put("uid", mTextInputEditTextRegisterName.getText().toString());//用户名
        params.put("email", mTextInputEditTextRegisterEmail.getText().toString());//邮箱
        params.put("pwd", mTextInputEditTextRegisterPassword.getText().toString());//密码
        MyHttp.get(activity, ServerUrl.USER_REGISTER, params,
                new OnResultFinishListener() {
                    @Override
                    public void success(Response response) {//成功
                        Gson gson = new Gson();
                        RegisterArray registerArray = gson.fromJson((String) response.result, RegisterArray.class);
                        AppInfo.register=registerArray.data;
                        if (registerArray.status == 0) {//判断是否正常响应
                            Toast.makeText(activity, registerArray.data.explain, Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void failed(Response response) {//失败
                        Toast.makeText(activity, "注册失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

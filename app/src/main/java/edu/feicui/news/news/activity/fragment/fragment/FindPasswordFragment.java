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
import android.widget.Toast;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.feicui.news.news.R;
import edu.feicui.news.news.ServerUrl;
import edu.feicui.news.news.activity.HomeActivity;
import edu.feicui.news.news.entity.FindPasswordArray;
import edu.feicui.news.news.net.MyHttp;
import edu.feicui.news.news.net.OnResultFinishListener;
import edu.feicui.news.news.net.Response;

/**
 * 找回密码碎片
 * Created by jiaXian on 2016/10/10.
 */
public class FindPasswordFragment extends Fragment implements TextWatcher, View.OnClickListener {
    HomeActivity activity;
    View mView;
    /**
     * 忘记密码的邮箱
     */
    TextInputLayout mTextInputLayoutForgetPasswordEmail;
    TextInputEditText mTextInputEditTextForgetPasswordEmail;
    /**
     * 忘记密码点击发送邮件按钮
     */
    Button mBtnForgetPasswordSendEmail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_findpassword, container, false);
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
        mView = getView();//初始化View
        activity = (HomeActivity) getActivity();//初始化activity  强转为HomeActivity
        //找到忘记密码的邮箱
        mTextInputLayoutForgetPasswordEmail = (TextInputLayout) findViewById(R.id.tvlay_forget_password_email);//外面
        mTextInputEditTextForgetPasswordEmail = (TextInputEditText) findViewById(R.id.tvedt_forget_password_email);//里面
        //找到  忘记密码 点击发送邮件的按钮
        mBtnForgetPasswordSendEmail = (Button) findViewById(R.id.btn_forget_password_send_email);
        //绑定监听事件
        mTextInputEditTextForgetPasswordEmail.addTextChangedListener(this);//忘记密码的邮箱
        mBtnForgetPasswordSendEmail.setOnClickListener(this);//发送邮件的确认按钮
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mTextInputEditTextForgetPasswordEmail.hasFocus()) { //判断是否拿到焦点
            Pattern p = Pattern.compile("[a-zA-Z0-9]{1,12}+@([a-zA-Z0-9]{1,12})+(\\.[a-zA-Z0-9]{1,12})");//用正则来匹配邮箱
            Matcher m = p.matcher(s);
            boolean email = m.matches();
            if (!email) {
                mTextInputLayoutForgetPasswordEmail.setError("邮箱格式不正确");
            } else {
                mTextInputLayoutForgetPasswordEmail.setError(null);//设置错误
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (mTextInputEditTextForgetPasswordEmail.getText().toString().equals("")) {//判断  邮箱是否为空
            Toast.makeText(activity, "邮箱不能为空", Toast.LENGTH_SHORT).show();
            activity.replaceFragment(1);//通知HomeActivity  打开找回密码的碎片(在此界面的效果是 只弹出吐丝窗口)
        } else {//不为空  则调用方法
            getHttpDataSendEmail();//调用发送邮箱的方法
        }
    }
    /**
     * 忘记密码 发送邮箱
     * user_forgetpass?ver=版本号&email=邮箱
     */
    public void getHttpDataSendEmail() {
        Map<String, String> pamars = new HashMap<>();
        pamars.put("ver", "0000000");//版本号
        pamars.put("email", mTextInputEditTextForgetPasswordEmail.getText().toString());//邮箱
        MyHttp.get(activity, ServerUrl.USER_FORGETPASSWORD, pamars,
                new OnResultFinishListener() {
                    @Override
                    public void success(Response response) {//发送成功
                        Gson gson = new Gson();
                        FindPasswordArray findPasswordArray = gson.fromJson((String) response.result, FindPasswordArray.class);
                        Toast.makeText(activity, findPasswordArray.data.explain, Toast.LENGTH_SHORT).show();
                        //切换Fragment
                    }
                    @Override
                    public void failed(Response response) {//发送失败
                        Toast.makeText(activity, "发送失败", Toast.LENGTH_SHORT).show();

                    }
                });
    }
}

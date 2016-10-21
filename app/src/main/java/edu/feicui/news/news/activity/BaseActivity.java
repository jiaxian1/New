package edu.feicui.news.news.activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import edu.feicui.news.news.R;
import edu.feicui.news.news.util.SQLDateBaseUtil;

/**
 * Created by jiaXian on 2016/10/17.
 */
public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {
    public String TAG = this.getClass().getSimpleName();
    /**
     * 谁继承它，它就是谁的布局
     */
    RelativeLayout mRelContent;
    //头
    ImageView mImgLeft;
    TextView mTvTittle;
    ImageView mImgRight;
    LinearLayout mLinLayoutHead;
    /**
     * 跟帖数量
     */
    Button mBtnCommentNumber;
    /**
     * 加入收藏
     */
    Button mBtnAddFavorite;
    /**
     * 布局填充器
     */
    LayoutInflater mInflater;
    /**************************************************************************
     ******************     初始化 以及  加载控件     *************************
     **************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: ");
        super.setContentView(R.layout.base_layout);
        //装下面的布局
        mRelContent=(RelativeLayout) findViewById(R.id.rl_base_content);
        //头上的文本
        mTvTittle=(TextView) findViewById(R.id.tv_base_tittle);
        mInflater=getLayoutInflater();
        mLinLayoutHead = (LinearLayout) findViewById(R.id.ll_base_head);
        //找到左边的base图片
        mImgLeft= (ImageView) findViewById(R.id.iv_base_left);
        //找到右边的base图片
        mImgRight= (ImageView) findViewById(R.id.iv_base_right);
        //找到评论数量按钮
        mBtnCommentNumber= (Button) findViewById(R.id.btn_comment_number);
        //找到加入收藏按钮
        mBtnAddFavorite= (Button) findViewById(R.id.btn_add_favorite_base);
        mImgLeft.setOnClickListener(this);
        mImgRight.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Log.e(TAG, "onStart: " );
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.e(TAG, "onResume: ");
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Log.e(TAG, "onPause: ");
    }
    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        Log.e(TAG, "onRestart: ");
    }
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        Log.e(TAG, "onStop: ");
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }

    /**************************************************************************
     ************* 骨架 模版设计 在加载布局之后自动调用initView ***************
     **************************************************************************/
    public void setContentView(int id) {
        //将子Activity  传入的布局id  加载到base_layout 的 mRelContent
        mInflater.inflate(id, mRelContent);
        initView();
    }

    /**
     * 骨架 模版设计 在加载布局之后自动调用initView
     */
    public void setContentView(View view) {
        //将子Activity  传入的布局id  加载到base_layout 的 mRelContent
        mRelContent.addView(view);
        initView();
    }
    /**
     * 用于子类初始化工作
     */
    abstract void initView();
    /**
     * @param listener
     *            绑定的监听器
     * @param view
     *            所要绑定事件的view
     */
    public void setOnClickListener(View.OnClickListener listener, View... view) {
        // 给每一个View绑定点击事件
        if (listener == null) {
            return;
        }
        for (View view2 : view) {
            view2.setOnClickListener(listener);
        }
    }
    /**
     *
     * @param listener
     *            绑定的监听器
     * @param id
     *            所要绑定事件的id
     */
    protected void setOnClickListener(View.OnClickListener listener, int... id) {
        // 给每一个View绑定点击事件
        if (listener == null) {
            return;
        }
        for (int i : id) {
            findViewById(i).setOnClickListener(listener);
        }
        TextView tv = new TextView(this);
    }

}

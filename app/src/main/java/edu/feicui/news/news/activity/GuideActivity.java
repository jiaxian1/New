package edu.feicui.news.news.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import edu.feicui.news.news.R;
import edu.feicui.news.news.adapter.GuidePagerAdapter;

/**
 * 引导界面
 * Created by jiaXian on 2016/10/17.
 */
public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener{
    /**
     * ViewPager滑动的三张图片
     */
    public int[] mPicture={R.mipmap.small,R.mipmap.welcome,R.mipmap.wy};

    /**
     * 第一个点
     */
    TextView mTvFirstPoint;
    /**
     * 第二个点
     */
    TextView mTvSecondPoint;
    /**
     * 第三个点
     */
    TextView mTvThridPoint;
    /**
     * 图片资源数组
     */
    TextView[] mTvArry=new TextView[3];
    /**
     * SharedPreferences方法参数
     */
    static String PREFERENCE_NAME="preference_settings";
    /**
     * 声明SharedPreferences
     */
    SharedPreferences preferencesGuide;

    /**
     * 直接跳过按钮
     */
    Button mButton;
    /**
     * 设置背景色上面的
     */
    RelativeLayout mRelativeLayout;
    /**
     * 设置背景色底下的
     */
    RelativeLayout mRelativeLayoutBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        if (!getDataGuide()){//如果不是第一次
            Intent intent=new Intent(this,LoadingActivity.class);//则直接跳转到加载
            // 界面
            startActivity(intent);//开始跳转
            finish();//将GuideActivity直接finish掉  不再做下面的操作
        }

    }
    /**
     * 操作SharedPreferences中的数据 返回是否是第一次进入的状态
     * @return
     */
    private boolean getDataGuide(){
        preferencesGuide =getSharedPreferences(PREFERENCE_NAME,MODE_PRIVATE);
        boolean isFirst= preferencesGuide.getBoolean("is_first",true);
        return isFirst;
    }

    /**
     * 重写的父类BaseActivity的方法  用来初始化控件和绑定事件
     */
    public void initView() {
        //1.初始化控件
        ViewPager pager= (ViewPager) findViewById(R.id.vp_guide);//找到ViewPager
        //分别找到三个点和直接跳过按钮的控件
        mTvFirstPoint= (TextView) findViewById(R.id.tv_guide_first_point);
        mTvSecondPoint= (TextView) findViewById(R.id.tv_guide_second_point);
        mTvThridPoint= (TextView) findViewById(R.id.tv_guide_third_point);
        mButton= (Button) findViewById(R.id.btn_guide_pass);
        mRelativeLayout= (RelativeLayout) findViewById(R.id.rl_guide_back);
        mRelativeLayoutBottom= (RelativeLayout) findViewById(R.id.rl_guide_back_bottom);
        //将三个TextView的点分别给TextView数组的三个 元素
        mTvArry[0]=mTvFirstPoint;
        mTvArry[1]=mTvSecondPoint;
        mTvArry[2]=mTvThridPoint;
        //2.数据源
        ArrayList<ImageView> listImage=new ArrayList<ImageView>();
        for (int i = 0; i <mPicture.length ; i++) {
            ImageView img=new ImageView(this);
            img.setImageResource(mPicture[i]);
            listImage.add(img);//将三张图片资源添加到ImageView的集合里面
        }
        //适配器PagerAdapter  抽象类
        GuidePagerAdapter adapter=new GuidePagerAdapter(listImage);//new的时候直接传其构造方法里面的参数
        pager.setAdapter(adapter);//绑定适配器
        //给pager绑定监听事件
        pager.addOnPageChangeListener(this);
        mButton.setOnClickListener(this);
        //设置头  让其不显示
        mLinLayoutHead.setVisibility(View.GONE);
    }
    /**
     * @param position          操作页面的下标
     * @param positionOffset    滑动的偏移百分比[0,1)
     * @param positionOffsetPixels   滑动的偏移像素
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 所选择的页面  切换图片
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        for (TextView tv:mTvArry) {
            mButton.setVisibility(View.GONE);
            tv.setTextColor(getResources().getColor(R.color.colorGray));
        }
        mTvArry[position].setTextColor(getResources().getColor(R.color.colorAccent));
        if (position==0){
            mRelativeLayout.setBackgroundColor(getResources().getColor(R.color.colorYellow));
//            mRelativeLayoutBottom.setBackgroundColor(getResources().getColor(R.color.colorYellow));
        }
        if (position==1){
            mRelativeLayout.setBackgroundColor(getResources().getColor(R.color.colorYellowdeep));
//            mRelativeLayoutBottom.setBackgroundColor(getResources().getColor(R.color.colorYellowdeep));
        }
        if (position==2){

            mRelativeLayout.setBackgroundColor(getResources().getColor(R.color.colorPink));
//            mRelativeLayoutBottom.setBackgroundColor(getResources().getColor(R.color.colorPink));
            mButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 页面滚动状态的监听
     *    1.滑动
     *    2.弹性运动  复位
     *    0.停止
     * @param state
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        SharedPreferences.Editor edt= preferencesGuide.edit();
        edt.putBoolean("is_first",false);
//        edt.putBoolean("is_first_login",false);
        edt.commit();//提交生效
        Intent intent=new Intent(this,LoadingActivity.class);//跳到加载界面
        startActivity(intent);
        //动画
        overridePendingTransition(R.anim.loading_alpha_animation, R.anim.loading_translate_animation);
        finish();
        getPreferences(0);
        getSharedPreferences(getLocalClassName(),0);
    }
}

package edu.feicui.news.news.activity.fragment.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import edu.feicui.news.news.AppInfo;
import edu.feicui.news.news.R;
import edu.feicui.news.news.ServerUrl;
import edu.feicui.news.news.XList.XListView;
import edu.feicui.news.news.activity.NewsDetailsActivity;
import edu.feicui.news.news.adapter.NewsListAdapter;
import edu.feicui.news.news.entity.NewsArray;
import edu.feicui.news.news.entity.NewsInfo;
import edu.feicui.news.news.entity.NewsSortArray;
import edu.feicui.news.news.net.MyHttp;
import edu.feicui.news.news.net.OnResultFinishListener;
import edu.feicui.news.news.net.Response;
import edu.feicui.news.news.util.SQLDateBaseUtil;

/**
 * 新闻的Fragment
 * Created by jiaXian on 2016/9/28.
 */
public class NewsFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener, XListView.IXListViewListener {
    /**
     * 别人写好的 拿过来用 是对ListView的封装 带有上拉加载和下拉刷新的
     */
    XListView mXListNews;
    /**
     * 接受从布局填充器转化过来的View
     */
    View mView;
    /**
     * activity
     */
    FragmentActivity activity;
    /**
     * 新闻列表的适配器
     */
    NewsListAdapter mNewsListAdapter;
    /**
     * 军事
     */
    Button mTvMilitary;
    /**
     * 社会
     */
    Button mTvSociety;
    /**
     * 股票
     */
    Button mTvStock;
    /**
     * 基金
     */
    Button mTvFund;
    /**
     * 手机
     */
    Button mTvPhone;
    /**
     * 英超
     */
    Button mTvEPL;
    /**
     * NBA
     */
    Button mTvNBA;


    /**
     * dir=1  刷新
     * dir=2  加载
     */
    int dir = 1;
    /**
     * 最后一条的id   用于上拉加载
     */
    int nid = 1;
    /**
     * 分类的号
     * 1：军事   2：社会   3：股票  4：基金  5：手机  6：  7：英超  8：NBA
     */
    int subid = 1;
    /**
     * 数据库工具
     */
    SQLDateBaseUtil sqlDateBaseUtil;

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
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
     * *****************        初始化 和 加载控件     ************************
     **************************************************************************/
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mView = getView();//拿到View
        activity = getActivity();//拿到Activity
        mXListNews = (XListView) findViewById(R.id.lv_home_news_fragment);
        //找到新闻分类的各个按钮
        mTvMilitary = (Button) findViewById(R.id.btn_news_sort_military);//军事
        mTvSociety = (Button) findViewById(R.id.btn_news_sort_society);//社会
        mTvStock = (Button) findViewById(R.id.btn_news_sort_stock);//股票
        mTvFund = (Button) findViewById(R.id.btn_news_sort_fund);//基金
        mTvPhone = (Button) findViewById(R.id.btn_news_sort_phone);//手机
        mTvEPL = (Button) findViewById(R.id.btn_news_sort_epl);//英超
        mTvNBA = (Button) findViewById(R.id.btn_news_sort_nba);//NBA
        //初始化新闻列表的适配器
        mNewsListAdapter = new NewsListAdapter(activity, null, R.layout.item_home_news);
        mXListNews.setAdapter(mNewsListAdapter);//给列表绑定适配器
        //可以进行上拉加载
        mXListNews.setPullLoadEnable(true);
        //可以进行下拉刷新
        mXListNews.setPullRefreshEnable(true);
        //给新闻列表绑定事件
        mXListNews.setXListViewListener(this);
        // 给新闻列表的子条目绑定点击事件
        mXListNews.setOnItemClickListener(this);
        //给分类 绑定点击事件
        mTvMilitary.setOnClickListener(this);//军事
        mTvSociety.setOnClickListener(this);//社会
        mTvStock.setOnClickListener(this);//股票
        mTvFund.setOnClickListener(this);//基金
        mTvPhone.setOnClickListener(this);//手机
        mTvEPL.setOnClickListener(this);//英超
        mTvNBA.setOnClickListener(this);//NBA
        //调用获取新闻列表的方法
        getHttpDataNews();
        //初始化数据库工具
        sqlDateBaseUtil=new SQLDateBaseUtil(activity);

    }
    /**
     * 新闻分类点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_news_sort_military://军事
                subid = 1;
                //  注意：这里必须清空之前的数据 否则  之前的数据不会消失 它只会在后面追加上
                mNewsListAdapter.mList.clear();
                //调用获取新闻列表的方法
                getHttpDataNews();
                break;
            case R.id.btn_news_sort_society://社会
                subid = 2;
                //清空之前的数据
                mNewsListAdapter.mList.clear();
                //调用获取新闻列表的方法
                getHttpDataNews();
                break;
            case R.id.btn_news_sort_stock://股票
                subid = 3;
                //清空之前的数据
                mNewsListAdapter.mList.clear();
                //调用获取新闻列表的方法
                getHttpDataNews();
                break;
            case R.id.btn_news_sort_fund://基金
                subid = 4;
                //清空之前的数据
                mNewsListAdapter.mList.clear();
                //调用获取新闻列表的方法
                getHttpDataNews();
                break;
            case R.id.btn_news_sort_phone://手机
                subid = 5;
                //清空之前的数据
                mNewsListAdapter.mList.clear();
                //调用获取新闻列表的方法
                getHttpDataNews();
                break;
            case R.id.btn_news_sort_epl://英超
                subid = 7;
                //清空之前的数据
                mNewsListAdapter.mList.clear();
                //调用获取新闻列表的方法
                getHttpDataNews();
                break;
            case R.id.btn_news_sort_nba://NBA
                subid = 8;
                //清空之前的数据
                mNewsListAdapter.mList.clear();
                //调用获取新闻列表的方法
                getHttpDataNews();
                break;
        }
    }

    /**************************************************************************
     ************** 获取新闻列表 调用接口 请求网络 解析 ***********************
     **************************************************************************/
    /**
     * 获取新闻列表
     * ver=版本号&subid=分类名&dir=1&nid=新闻id&stamp=20140321&cnt=20
     * ver=0000000&subid=1&dir=1&nid=1&stamp=20140321000000&cnt=20
     */
    public void getHttpDataNews() {
        Map<String, String> params = new HashMap<>();
        params.put("ver", "0000000");//版本号
        params.put("subid", subid + "");//分类的号
        params.put("dir", "" + dir);//方向
        params.put("nid", "" + nid);//新闻id
        params.put("stamp", "20140321000000");//时间
        params.put("cnt", "20");//数目
        MyHttp.get(activity, ServerUrl.NEWS_LIST, params,
                new OnResultFinishListener() {//自己写的接口回调
                    @Override
                    public void success(Response response) {//成功
                        //解析  给适配器添加数据  刷新界面
                        Gson gson = new Gson();
                        NewsArray newsArray = gson.fromJson((String) response.result, NewsArray.class);
                        Log.e("aaa", "success: "+subid);
                        if (newsArray.status == 0) {//判断是否正常响应
                            /**
                             * 有数据  进行添加并且刷新
                             */
                            if (newsArray.data != null && newsArray.data.size() > 0) {
                                mNewsListAdapter.mList.addAll(newsArray.data);
                                mNewsListAdapter.notifyDataSetChanged();//刷新数据
                            }
                        }
                        //关闭上拉加载以及下拉刷新框
                        mXListNews.stopLoadMore();
                        mXListNews.stopRefresh();
                    }
                    @Override
                    public void failed(Response response) {//失败
                        Toast.makeText(activity, "加载失败！", Toast.LENGTH_SHORT).show();
                        //关闭上拉加载以及下拉刷新框
                        mXListNews.stopLoadMore();
                        mXListNews.stopRefresh();
                    }
                });
    }
    @Override
    public void onRefresh() {
        //进行下拉刷新操作
        dir = 1;
        //清空之前的数据
        mNewsListAdapter.mList.clear();
        //再调用获取新闻列表的方法
        getHttpDataNews();
    }
    @Override
    public void onLoadMore() {
        //上拉加载
        dir = 2;
        //拿到最后一条的id
        if (mNewsListAdapter.mList.size() > 0) {
            NewsInfo news = mNewsListAdapter.mList.get(mNewsListAdapter.mList.size() - 1);//拿到当前的最后一条id
            nid = news.nid;//将拿到的nid给nid
        }
        //再调用获取新闻列表的方法
        getHttpDataNews();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(activity, NewsDetailsActivity.class);//用Intent 传递News的对象news 在新闻详情界面拿到数据
        NewsInfo news = mNewsListAdapter.mList.get(position - 1);//拿到数据
        intent.putExtra("news", news);//传递数据
        startActivity(intent);
        AppInfo.news = news;//将新闻的id保存在静态量之中
        sqlDateBaseUtil.insertNews();//插入
        sqlDateBaseUtil.selectNews();//查询
    }


    /**
     * news_sort?ver=版本号&imei=手机标识符
     * 新闻分类
     */
    public void getHttpDataNewsSort() {
        final Map<String, String> params = new HashMap<>();
        params.put("ver", "0000000");//版本号
        params.put("imei", "868564020877953");//用户标识符
        MyHttp.get(activity, ServerUrl.NEWS_LIST, params,
                new OnResultFinishListener() {//自己写的接口回调
                    @Override
                    public void success(Response response) {//成功
                        //解析  给适配器添加数据  刷新界面
                        Gson gson = new Gson();
                        NewsSortArray newsSortArray = gson.fromJson((String) response.result, NewsSortArray.class);
                    }

                    @Override
                    public void failed(Response response) {//失败
                        Toast.makeText(activity, "加载失败！", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
/**
 * * XListView   步骤使用：
 * 1.导入XListView所需代码
 * 2.布局中使用代替列表
 * 3.代码中进行设置
 * 设置可以进行上拉加载
 * 可以进行下拉刷新
 * 4.绑定  刷新 加载  事件监听
 * 实现  刷新以及加载方法
 * 注意：刷新或者加载结束   需要停止刷新以及加载
 *
 *
 *
 * 新闻分类接口  解析结果
 */


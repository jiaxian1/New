package edu.feicui.news.news.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import edu.feicui.news.news.AppInfo;
import edu.feicui.news.news.R;
import edu.feicui.news.news.ServerUrl;
import edu.feicui.news.news.XList.XListView;
import edu.feicui.news.news.adapter.CommentListAdapter;
import edu.feicui.news.news.entity.CommentListArray;
import edu.feicui.news.news.entity.CommentListInfo;
import edu.feicui.news.news.entity.PostCommentArray;
import edu.feicui.news.news.net.MyHttp;
import edu.feicui.news.news.net.OnResultFinishListener;
import edu.feicui.news.news.net.Response;

/**
 * 评论
 * Created by jiaXian on 2016/10/12.
 */
public class CommentActivity extends BaseActivity implements XListView.IXListViewListener, TextWatcher {
    /**
     * 别人写好的 拿过来用 是对ListView的封装 带有上拉加载和下拉刷新的
     */
    XListView mXListComment;
    /**
     * 评论列表的适配器
     */
    CommentListAdapter mCommentListAdapter;
    /**
     * 发布评论的输入框
     */
    EditText mEdtPostComment;
    /**
     * dir=1  刷新
     * dir=2  加载
     */
    int dir = 1;
    /**
     * 最后一条的id   用于上拉加载
     */
    int cid = 1;
    /**
     * 发布评论
     */
    ImageView mIvPostComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
    }
    @Override
    void initView() {
        mIvPostComment= (ImageView) findViewById(R.id.iv_send_comment);//点击发布评论
        mEdtPostComment= (EditText) findViewById(R.id.edt_post_comment);//找到发布评论的输入框
        mXListComment = (XListView) findViewById(R.id.lv_comment_list);//找到控件
        mCommentListAdapter=new CommentListAdapter(this,null,R.layout.item_xlistview_comment);//初始化适配器
        mXListComment.setAdapter(mCommentListAdapter);//绑定适配器
        //可以进行上拉加载
        mXListComment.setPullLoadEnable(true);
        //可以进行下拉刷新
        mXListComment.setPullRefreshEnable(true);
        //给新闻列表绑定事件
        mXListComment.setXListViewListener(this);
        //给输入框绑定文字改变监听事件
        mEdtPostComment.addTextChangedListener(this);
        //给点击发布评论绑定监听事件
        mIvPostComment.setOnClickListener(this);
        getHttpDataCommentList();//获取评论列表

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_send_comment://点击发布评论
                    if (!AppInfo.isLogin){//没有登陆
                        Toast.makeText(CommentActivity.this, "请登陆账号，再进行评论", Toast.LENGTH_SHORT).show();
                    }else {
                        getHttpDataPostComment();//发布评论
                    }
                Log.e("aaa", "onClick: "+AppInfo.news.nid);
                break;
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mEdtPostComment.getText().toString();
    }
    /**
     * 显示评论
     * cmt_list ?ver=版本号&nid=新闻id&type=1&stamp=yyyyMMdd&cid=评论id&dir=0&cnt=20
     */
    public void getHttpDataCommentList(){
        Map<String,String> params=new HashMap<>();
        params.put("ver","0000000");//版本号
        params.put("nid", AppInfo.news.nid+"");//新闻id  在新闻列表的子条目点击事件中保存在静态类AppInfo中  这里可以直接用类名拿到
        params.put("type","1");
        params.put("stamp","20140321000000");//评论时间
        params.put("cid",""+cid);//评论id
        params.put("dir",""+dir);//刷新列表的方向
        params.put("cnt","20");//数目
        MyHttp.get(this, ServerUrl.COMMENT_LIST, params,
                new OnResultFinishListener() {
                    @Override
                    public void success(Response response) {//获取评论列表成功
                        Gson gson = new Gson();
                        CommentListArray commentListArray = gson.fromJson((String) response.result, CommentListArray.class);
                        if (commentListArray.status==0){//判断是否正常响应
                            /**
                             * 有数据  进行添加并且刷新
                             */
                            if (commentListArray.data != null && commentListArray.data.size() > 0) {
                                mCommentListAdapter.mList.addAll(commentListArray.data);
                                mCommentListAdapter.notifyDataSetChanged();//刷新数据
                            }
                        }
                        Toast.makeText(CommentActivity.this,"加载成功！",Toast.LENGTH_SHORT).show();
                        //关闭上拉加载以及下拉刷新框
                        mXListComment.stopLoadMore();
                        mXListComment.stopRefresh();
                    }
                    @Override
                    public void failed(Response response) {//获取评论列表失败
                        Toast.makeText(CommentActivity.this,"加载失败！",Toast.LENGTH_SHORT).show();
                        //关闭上拉加载以及下拉刷新框
                        mXListComment.stopLoadMore();
                        mXListComment.stopRefresh();
                    }
                });
    }

    /**
     * 发布评论
     * cmt_commit?ver=版本号&nid=新闻编号&token=用户令牌&imei=手机标识符&ctx=评论内容
     */
    public void getHttpDataPostComment(){
        Map<String,String> pamars=new HashMap<>();
        pamars.put("ver","0000000");//版本号
        pamars.put("nid", AppInfo.news.nid+"");//新闻编号
        pamars.put("token",AppInfo.login.token);//用户令牌
        pamars.put("imei","868564020877953");//用户标识符
        pamars.put("ctx",mEdtPostComment.getText().toString());//评论内容
        MyHttp.get(this, ServerUrl.PUBLISH_COMMENT, pamars,
                new OnResultFinishListener() {
                    @Override
                    public void success(Response response) {//发布评论成功
                        Gson gson = new Gson();
                        PostCommentArray postCommentArray = gson.fromJson((String) response.result, PostCommentArray.class);
                        Toast.makeText(CommentActivity.this,postCommentArray.message, Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void failed(Response response) {//发布评论失败
                        Toast.makeText(CommentActivity.this,"发布失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public void onRefresh() {
        //进行下拉刷新操作
        dir = 1;
        //清空之前的数据
        mCommentListAdapter.mList.clear();
        //再调用获取评论列表的方法
        getHttpDataCommentList();

    }
    @Override
    public void onLoadMore() {
        //上拉加载
        dir = 2;
        //拿到最后一条的id
        if (mCommentListAdapter.mList.size() > 0) {
            CommentListInfo comments = mCommentListAdapter.mList.get(mCommentListAdapter.mList.size()-1);//拿到当前的最后一条id
            cid = comments.cid;//将拿到的cid给cid
        }
        //再调用获取评论列表的方法
        getHttpDataCommentList();
    }


}

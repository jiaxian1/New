package edu.feicui.news.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import edu.feicui.news.news.R;
import edu.feicui.news.news.entity.LoginLog;

/**
 * 登陆日志的RecycleView列表的适配器
 * Created by jiaXian on 2016/10/11.
 */
public class LoginLogAdapter extends RecyclerView.Adapter<LoginLogAdapter.MyHolder>{
    ArrayList<LoginLog> mList;//数据源
    Context mContext;
    LayoutInflater mInflate;//声明布局填充器
    /**
     *  使用构造方法用来传递数据源
     */
    public LoginLogAdapter(ArrayList<LoginLog> mList, Context mContext){
        this.mList=mList;
        this.mContext=mContext;
        mInflate= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//初始化布局填充器的对象
    }

    /**
     * 创建ViewHolder 以及子条目的View  并且将两者绑定
     *
     * @param parent   父容器    记载此适配器的RecyclerView
     * @param viewType 类型
     * @return
     */
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //1.初始化子条目的View   注意:需要指定父容器
        View view =mInflate.inflate(R.layout.item_login_log,parent,false);
        //2.初始化ViewHolder同时  进行子条目视图与ViewHolder的绑定
        MyHolder holder = new MyHolder(view);
        return holder;
    }
    /**
     * 对指定条目上的控件进行  渲染
     * @param holder   就是之前返回的Holder
     * @param position 下标
     */
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.mTvTime.setText(mList.get(position).time);//设置时间
        holder.mTvAdrress.setText(mList.get(position).address);//设置地址
        holder.mTvDevice.setText(mList.get(position).device);//设置设备
    }
    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }
    /**
     * 绑定Holder和View  以及存储控件
     */
    public class MyHolder extends RecyclerView.ViewHolder{
        View itemView;
        public TextView mTvTime;
        public TextView mTvAdrress;
        public TextView mTvDevice;
        public MyHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            //找到子条目的各个控件
            mTvTime= (TextView) itemView.findViewById(R.id.tv_item_login_log_time);//时间
            mTvAdrress= (TextView) itemView.findViewById(R.id.tv_item_login_log_address);//地址
            mTvDevice= (TextView) itemView.findViewById(R.id.tv_item_login_log_device);//设备
        }
    }

}

package edu.feicui.news.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 *
 * Created by jiaXian on 2016/10/17.
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {
    Context mContext;
    public ArrayList<T> mList;
    LayoutInflater mInflate;
    int mLayoutId;
    public MyBaseAdapter( Context mContext,ArrayList<T> mList,int layoutId){
        this.mContext=mContext;
        this.mList=mList;
        mLayoutId=layoutId;
        mInflate= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //这里一定要判断一下 ，否则后面会报空指针异常
        if (this.mList==null){
            this.mList=new ArrayList<>();
        }
    }
    @Override
    public int getCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView==null){
            convertView=mInflate.inflate(mLayoutId,null);
            holder=new Holder();
            convertView.setTag(holder);
        }else{
            holder= (Holder) convertView.getTag();
        }
        /**
         * 将条目View的渲染具体过程交给子类去做
         * 因为只有子类知道具体的条目View或者控件
         */
        putView(holder,convertView,position,mList.get(position));
        return convertView;
    }
    /**
     *
     * @param holder         对应条目的Holder
     * @param convertView    对应条目的VIEW
     * @param position       对应条目的位置
     * @param t               对应条目的数据
     */
    public abstract void putView(Holder holder,View convertView,int position,T t);

    class Holder{

    }
}

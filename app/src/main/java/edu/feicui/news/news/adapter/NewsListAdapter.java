package edu.feicui.news.news.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import edu.feicui.news.news.R;
import edu.feicui.news.news.entity.NewsInfo;

/**
 * 新闻列表适配器
 * Created by jiaXian on 2016/10/17.
 */
public class NewsListAdapter extends MyBaseAdapter<NewsInfo>{
    public NewsListAdapter(Context mContext, ArrayList<NewsInfo> mList, int layoutId) {
        super(mContext, mList, layoutId);

    }
    @Override
    public void putView(Holder holder, View convertView, int position, NewsInfo news) {
        //设置标题
        TextView title= (TextView) convertView.findViewById(R.id.tv_item_home_news_title);
        title.setText(news.title);
        //设置图标
        ImageView img= (ImageView) convertView.findViewById(R.id.iv_item_home_news_icon);
        Glide.with(mContext).load(news.icon).into(img);
        //设置摘要
        TextView summary= (TextView) convertView.findViewById(R.id.tv_item_home_news_summary);
        summary.setText(news.summary);
        //设置时间
        TextView time= (TextView) convertView.findViewById(R.id.tv_item_home_news_time);
        time.setText(news.stamp);
    }
}

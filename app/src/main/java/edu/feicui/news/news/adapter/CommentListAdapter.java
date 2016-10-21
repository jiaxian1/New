package edu.feicui.news.news.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

import edu.feicui.news.news.R;
import edu.feicui.news.news.entity.CommentListInfo;

/**
 * 评论列表适配器
 * Created by jiaXian on 2016/10/12.
 */
public class CommentListAdapter extends MyBaseAdapter<CommentListInfo>{

    public CommentListAdapter(Context mContext, ArrayList<CommentListInfo> mList, int layoutId) {
        super(mContext, mList, layoutId);
    }

    @Override
    public void putView(Holder holder, View convertView, int position, CommentListInfo commentListInfo) {
        //设置头像
        ImageView mIvPortrait= (ImageView) convertView.findViewById(R.id.iv_item_comment_list_portrait);
        Glide.with(mContext).load(commentListInfo.portrait).into(mIvPortrait);
        //设置用户名
        TextView mMyName= (TextView) convertView.findViewById(R.id.tv_item_comment_list_name);
        mMyName.setText(commentListInfo.uid);
        //设置评论时间
        TextView mCommentTime= (TextView) convertView.findViewById(R.id.tv_item_comment_list_time);
        mCommentTime.setText(commentListInfo.stamp);
        //设置评论
        TextView mTvComment= (TextView) convertView.findViewById(R.id.tv_item_comment_list_content);
        mTvComment.setText(commentListInfo.content);
    }
}

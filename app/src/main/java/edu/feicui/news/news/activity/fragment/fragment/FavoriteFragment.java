package edu.feicui.news.news.activity.fragment.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.feicui.news.news.R;

/**
 * 收藏的Fragment
 * Created by jiaXian on 2016/9/28.
 */
public class FavoriteFragment extends Fragment {
    RecyclerView mRecyclerView;
    View mView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_news,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView=getView();//初始化View
        mRecyclerView= (RecyclerView) mView.findViewById(R.id.recv_home_favorite_news_fragment);//RecyclerView找到控件

    }
}

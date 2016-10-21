package edu.feicui.news.news.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * 引导界面的适配器
 * Created by jiaXian on 2016/10/17.
 */
public class GuidePagerAdapter extends PagerAdapter{
    ArrayList<ImageView> mListImage;
    Context mContext;
    public GuidePagerAdapter(ArrayList<ImageView> mListImage){
        this.mListImage=mListImage;
    }

    /**
     * Pager的数量
     * @return  判断如果为空 则返回0
     */
    @Override
    public int getCount() {
        return mListImage==null?0:mListImage.size();
    }

    /**
     *  暴露给外部使用的  用来查看View和Object是否相等
     * @param view
     * @param object   instantiateItem的返回值
     * @return   只有返回值为true时才会展示效果
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    /**
     * 初始化子条目
     * @param container    容器 就是使用适配器的ViewPager
     * @param position    View的下标
     * @return    要添加的视图
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView=mListImage.get(position%mListImage.size());
        container.addView(imageView);
        return imageView;
    }

    /**
     * 销毁子条目
     * @param container  容器 就是使用适配器的ViewPager
     * @param position   要移除的container的下标
     * @param object     instantiateItem的返回值
     *            注意：super.destroyItem(container, position, object);必须删掉
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mListImage.get(position));

    }
}

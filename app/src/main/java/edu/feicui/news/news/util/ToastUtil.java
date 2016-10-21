package edu.feicui.news.news.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类
 * Created by jiaXian on 2016/10/14.
 */
public class ToastUtil {
    public static void showToastLong(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
    public static void showToastShort(Context context,String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}

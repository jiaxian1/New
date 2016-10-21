package edu.feicui.news.news.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Created by jiaXian on 2016/10/18.
 */
public class ReadDataBaseUtil {
    /**
     * 路径
     */
    public static final String DB_PATH = "data/data/edu.feicui.news.news/databases/login.db";
    static File file=new File(Constant.DB_PATH_LOGIN);
    public static String[] read() {
        if (file.exists() && file.canRead()) { //判断数据库是否存在 是否可读
            //打开数据库
            SQLiteDatabase database = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = database.query(Constant.LOGIN_TABLE, null, null, null, null, null, null);
            while (cursor.moveToNext()) { //移向下一条
                //若getColumnIndex 返回值为-1，则不去查询 没有找到时 此方法会返回-1
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                String token = cursor.getString(cursor.getColumnIndex("token"));
                String[] str={name,password,token};
                return str;
            }
        }
        return null;
    }

}

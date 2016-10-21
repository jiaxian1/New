package edu.feicui.news.news.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/10/17.
 */
public class MySQLOpenHelper extends SQLiteOpenHelper {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "newsinfo_db";
    public static final int DB_VERSION = 1;

    /**
     *
     * @param context  事件发生的一个场景
     */
    public MySQLOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 可见创建表
		/*
		 * 建新闻的数据库
		 * sql语句 create table 表名(列名 类型,列名 类型....)
		 * sql语句  相当于一条指令
		 */
        String sqlNews = "create table news(summary String,icon String,stamp String,title String,nid integer,link String,type integer)";
		/*
		 * execSQL执行SQL语句 除过查询(select)语句
		 */
        db.execSQL(sqlNews);
        /*************************************************************************************************
         * 这里要注意  一个版本数据库只能建一次  如果数据库已经存在 要修改  必须升级版本之后  才可以修改
         * 外面可以卸载软件 重新新建
         **************************************************************************************************/
        String sqlLogin = "create table login(name text,password text,token text)";
        db.execSQL(sqlLogin);
    }
    /**
     * 用于数据版本更新
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

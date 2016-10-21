package edu.feicui.news.news.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import edu.feicui.news.news.AppInfo;
/**
 * 数据库
 * Created by jiaXian on 2016/10/17.
 */
public class SQLDateBaseUtil {
    SQLiteDatabase base;
    public SQLDateBaseUtil(Context context) { //构造方法
        //需要SQLiteDatabase  从SQLiteOpenHelper中获取
        SQLiteOpenHelper helper = new MySQLOpenHelper(context);//先拿到SQLiteOpenHelper的对象
        //拿到SQLiteDatabase的对象
//        base = helper.getWritableDatabase();
//        base=helper.getReadableDatabase();//读写   耗时长      当数据库满了的时候 会返回一个只读的数据库操作对象
        base = helper.getWritableDatabase();//读写  一般选取它  当数据库满了的时候 可能会出现异常  少数据不会有异常
    }

    /**
     * 插入(增加)新闻信息
     */
    public void insertNews() {
        /**
         * 1.sql insert into 表名(列名,列名....)values(值,值,ֵ...)
         * 2.使用SQLiteDateBase提供的方法
         */
        // 使用第一种方法
//        String sql = "insert into user(summary,icon,stamp,title,nid,link,type)values(4,\"张三\",19)";
//        base.execSQL(sql);
		/*
		 * table 表名
		 * nullColumnHack 默认值 可以为null
		 * values 插入的数据
		 */
        // 使用第二种方法
        ContentValues values = new ContentValues();
        values.put("summary", AppInfo.news.summary);
        values.put("icon", AppInfo.news.icon);
        values.put("stamp", AppInfo.news.stamp);
        values.put("title", AppInfo.news.title);
        values.put("nid", "" + AppInfo.news.nid);
        values.put("link", AppInfo.news.link);
        values.put("type", "" + AppInfo.news.type);
        base.insert("news", null, values);
        Log.e("abc", "" + values);
    }

    /**
     * 查询新闻信息
     */
    public void selectNews() {
		/*
		 * 1.sql语句 select(列名,列名....)(*)from表名
		 * 2.使用SQLiteDateBase提供的方法
		 */
//		 String sql = "select*from news";
		/*
		 * 游标 Cursor
		 * sql sql语句
		 * selectionArgs 执行语句  null 代表所以
		 */
//		 Cursor cursor=base.rawQuery(sql, null);//返回游标
        /**
         * table 表名
         * columns 要查询的列 null(查询所有) 条件：new String[]{"_id","name"}
         * selection 查询条件 null(没有条件)
         * selectionArgs 条件的值  null
         * groupBy 分组相关的
         * having 分组相关的
         * orderBy 排序 null(默认顺序：插入的顺序) asc(顺序) desc(倒序)
         */
        Cursor cursor = base.query("news", null,// new
                // String[]{"_id","name","age"}
                // "_id=? or age=?", //
                // selection null
                null,
                // new String[] { "1", "18" },
                null, null, null, null);

        // �α��λ��
        // cursor.moveToFirst();
        while (cursor.moveToNext()) {//移向下一条
            //若getColumnIndex 返回值为-1，则不去查询 没有找到时 此方法会返回-1
            String summary = cursor.getString(cursor.getColumnIndex("summary"));
            String icon = cursor.getString(cursor.getColumnIndex("icon"));
            String stamp = cursor.getString(cursor.getColumnIndex("stamp"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            int nid = cursor.getInt(cursor.getColumnIndex("nid"));//列的下标
            String link = cursor.getString(cursor.getColumnIndex("link"));
            int type = cursor.getInt(cursor.getColumnIndex("type"));
            Log.e("aaa", "summary=" + summary + "icon=" + icon + "stamp=" +
                    stamp + "title=" + title + "nid=" + nid + "link=" + link + "type=" + type);
        }
    }

    /**
     * 保存登陆信息
     */
    public void insertLoginInfo() {
        ContentValues values = new ContentValues();
        values.put("name", AppInfo.name);
        values.put("password", AppInfo.password);
        values.put("token", AppInfo.token);
        base.insert("login", null, values);
        Log.e("abc", "" + values);
    }
    public String name;
    public String password;
    public String token;
    /**
     * 查询登陆信息
     */
    public void selectLoginInfo() {
        Cursor cursor = base.query("login", null, null, null, null, null, null);
        while (cursor.moveToNext()) { //移向下一条
            //若getColumnIndex 返回值为-1，则不去查询 没有找到时 此方法会返回-1
            name = cursor.getString(cursor.getColumnIndex("name"));
            password = cursor.getString(cursor.getColumnIndex("password"));
            token = cursor.getString(cursor.getColumnIndex("token"));
           Log.e("aaa", "name=" + name + "password=" + password + "token=" + token);
        }
    }
}

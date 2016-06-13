package com.hong.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hong on 2016/5/1.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static String NAME = "hong.db";

    private static final int START_VERSION = 1;
    private static final int HISTORY_VERSION = 2;
    private static final int CURRENT_VERSION = 3;


    public static final String TABLE_ID = "_id";//通用的主键
    public static final String TABLE_NEWS_NAME = "news";//新闻表名

    public static final String TABLE_NEWS_TITLE = "title";// 新闻标题
    public static final String TABLE_NEWS_SUMMARY = "summary";// 新闻摘要

    public DBHelper(Context context) {
        super(context, NAME, null, CURRENT_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        onUpgrade(db, START_VERSION, CURRENT_VERSION);

     /*   db.execSQL("CREATE TABLE " + TABLE_NEWS_NAME + " (" + //
                TABLE_ID + " integer primary key autoincrement, " + //
                TABLE_NEWS_TITLE + " varchar(50), " + //
                TABLE_NEWS_SUMMARY + " VARCHAR(200))"//
        );*/

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        //希望：不需要总是考虑老用户，每次升级数据库操作是什么

        //数据库版本维护的通用方法
        switch (oldVersion) {

            case START_VERSION:
                db.execSQL("CREATE TABLE " + TABLE_NEWS_NAME + " (" + //
                        TABLE_ID + " integer primary key autoincrement, " + //
                        TABLE_NEWS_TITLE + " varchar(50), " + //
                        TABLE_NEWS_SUMMARY + " VARCHAR(200))"//
                );
            case HISTORY_VERSION:
                db.execSQL("CREATE TABLE " + TABLE_NEWS_NAME + " (" + //
                        TABLE_ID + " integer primary key autoincrement, " + //
                        TABLE_NEWS_TITLE + " varchar(50), " + //
                        TABLE_NEWS_SUMMARY + " VARCHAR(200))"//
                );
            case 3:
                //更新表

            case 4:
                //删除表
                break;


        }


    }
}

package com.biner.ripplesweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jb294 on 2016/2/19.
 */
public class MyDBOpenHelper extends SQLiteOpenHelper {
    public MyDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MyDBOpenHelper(Context context, String dbName, Object factory, int version) {
        super(context,dbName, (SQLiteDatabase.CursorFactory) factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table citylist(id text primary key," +
                "province text," +
                "city text)");
        db.execSQL("create table todayweather(id text primary key ," +
                "cityName text," +
                "tmp text," +
                "cond text," +
                "date text," +
                "comf text," +
                "drsg text," +
                "flu text," +
                "sport text," +
                "trav text," +
                "uv text)");
        db.execSQL("create table futureweather(id text," +
                "date text," +
                "cond_d text," +
                "cond_n text," +
                "tmp text," +
                "wind text," +
                "primary key(id,date))");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

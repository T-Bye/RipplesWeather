package com.biner.ripplesweather.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.biner.ripplesweather.db.MyDBOpenHelper;
import com.biner.ripplesweather.model.City;
import com.biner.ripplesweather.model.FutureWeather;
import com.biner.ripplesweather.model.TodayWeather;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jb294 on 2016/2/19.
 */
public class DBUtil {
    public static final String DB_NAME="weather";
    public static final int VERSION=1;
    private static DBUtil dbUtil;
    private static SQLiteDatabase db;

    private DBUtil(Context context){
        MyDBOpenHelper myDBOpenHelper=new MyDBOpenHelper(context,DB_NAME,null,VERSION);
        db=myDBOpenHelper.getWritableDatabase();
    }

    public synchronized static DBUtil getInstance(Context context){
        if(dbUtil==null){
            dbUtil=new DBUtil(context);
        }
        return dbUtil;
    }
    public static void saveCity(City city){
        db.execSQL("insert into citylist values (?,?,?)",new String[]{city.getId(),city.getProvinceName(),city.getCityName()});
    }
    public static void saveTodayWeather(TodayWeather todayWeather){
        db.execSQL("replace into todayweather values (?,?,?,?,?,?,?,?,?,?,?) ",
                new String[]{todayWeather.getCityId(),todayWeather.getCityName(),todayWeather.getTmp(),todayWeather.getCond(),
                        todayWeather.getUpdate(),todayWeather.getSuggestion_comf(),todayWeather.getSuggestion_drsg(),
                        todayWeather.getSuggestion_flu(),todayWeather.getSuggestion_sport(),
                        todayWeather.getSuggestion_trav(),todayWeather.getSuggestion_uv()});

    }
    public static void saveFutureWeather(FutureWeather futureWeather){
            db.execSQL("replace  into futureweather values (?,?,?,?,?,?) ",
                    new String[]{futureWeather.getCityId(), futureWeather.getData(), futureWeather.getCond_d(), futureWeather.getCond_n(), futureWeather.getTmp(),futureWeather.getWind()});

    }
    public static void deleteFutureWeather(String cityid){
        db.execSQL("delete from futureweather where id=?",new String[]{cityid});
    }
    public static List<City> loadCity(){
        List<City> list=new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from citylist",null);
        if (cursor.moveToFirst()){
            do {
                City city=new City();
                city.setId(cursor.getString(cursor.getColumnIndex("id")));
                city.setProvinceName(cursor.getString(cursor.getColumnIndex("province")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city")));
                list.add(city);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return list;


    }
    public static List<TodayWeather> queryTodayWeather(String cityid){
        List<TodayWeather> list=new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from todayweather where id=?",new String[]{cityid});
        if (cursor.moveToFirst()){
            do {
                TodayWeather todayWeather =new TodayWeather();
                todayWeather.setCityId(cursor.getString(cursor.getColumnIndex("id")));
                todayWeather.setCityName(cursor.getString(cursor.getColumnIndex("cityName")));
                todayWeather.setTmp(cursor.getString(cursor.getColumnIndex("tmp")));
                todayWeather.setCond(cursor.getString(cursor.getColumnIndex("cond")));
                todayWeather.setUpdate(cursor.getString(cursor.getColumnIndex("date")));
                todayWeather.setSuggestion_comf(cursor.getString(cursor.getColumnIndex("comf")));
                todayWeather.setSuggestion_drsg(cursor.getString(cursor.getColumnIndex("drsg")));
                todayWeather.setSuggestion_flu(cursor.getString(cursor.getColumnIndex("flu")));
                todayWeather.setSuggestion_sport(cursor.getString(cursor.getColumnIndex("sport")));
                todayWeather.setSuggestion_trav(cursor.getString(cursor.getColumnIndex("trav")));
                todayWeather.setSuggestion_uv(cursor.getString(cursor.getColumnIndex("uv")));
                list.add(todayWeather);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;


    }
    public static List<FutureWeather> queryFutureWeather(String cityid){
        List<FutureWeather> list=new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from futureweather where id=?",new String[]{cityid});
        if (cursor.moveToFirst()){
            do {
                FutureWeather futureWeather =new FutureWeather() ;
                futureWeather.setCityId(cursor.getString(cursor.getColumnIndex("id")));
                futureWeather.setData(cursor.getString(cursor.getColumnIndex("date")));
                futureWeather.setCond_d(cursor.getString(cursor.getColumnIndex("cond_d")));
                futureWeather.setCond_n(cursor.getString(cursor.getColumnIndex("cond_n")));
                futureWeather.setTmp(cursor.getString(cursor.getColumnIndex("tmp")));
                futureWeather.setWind(cursor.getString(cursor.getColumnIndex("wind")));
                list.add(futureWeather);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return list;


    }
    public static List<FutureWeather> queryFutureWeather(){
        List<FutureWeather> list=new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from futureweather ",null);
        if (cursor.moveToFirst()){
            do {
                FutureWeather futureWeather =new FutureWeather() ;
                futureWeather.setCityId(cursor.getString(cursor.getColumnIndex("id")));
                futureWeather.setData(cursor.getString(cursor.getColumnIndex("date")));
                futureWeather.setCond_d(cursor.getString(cursor.getColumnIndex("cond_d")));
                futureWeather.setCond_n(cursor.getString(cursor.getColumnIndex("cond_n")));
                futureWeather.setTmp(cursor.getString(cursor.getColumnIndex("tmp")));
                futureWeather.setWind(cursor.getString(cursor.getColumnIndex("wind")));
                list.add(futureWeather);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return list;


    }
    public static List<TodayWeather> queryTodayWeather(){
        List<TodayWeather> list=new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from todayweather ",null);
        if (cursor.moveToFirst()){
            do {
                TodayWeather todayWeather =new TodayWeather();
                todayWeather.setCityId(cursor.getString(cursor.getColumnIndex("id")));
                todayWeather.setCityName(cursor.getString(cursor.getColumnIndex("cityName")));
                todayWeather.setTmp(cursor.getString(cursor.getColumnIndex("tmp")));
                todayWeather.setCond(cursor.getString(cursor.getColumnIndex("cond")));
                todayWeather.setUpdate(cursor.getString(cursor.getColumnIndex("date")));
                todayWeather.setSuggestion_comf(cursor.getString(cursor.getColumnIndex("comf")));
                todayWeather.setSuggestion_drsg(cursor.getString(cursor.getColumnIndex("drsg")));
                todayWeather.setSuggestion_flu(cursor.getString(cursor.getColumnIndex("flu")));
                todayWeather.setSuggestion_sport(cursor.getString(cursor.getColumnIndex("sport")));
                todayWeather.setSuggestion_trav(cursor.getString(cursor.getColumnIndex("trav")));
                todayWeather.setSuggestion_uv(cursor.getString(cursor.getColumnIndex("uv")));
                list.add(todayWeather);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    public static boolean deleteCity(String cityid){
        db.execSQL("delete from todayweather where id=?",new String[]{cityid});
        db.execSQL("delete from futureweather where id=?",new String[]{cityid});
        return true;
    }




}

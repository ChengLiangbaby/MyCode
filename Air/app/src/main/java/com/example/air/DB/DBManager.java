package com.example.air.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    public static SQLiteDatabase database;
    public static void initDB(Context context){
        DBHelper dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }
    public static List<String> QueryAllCityName(){
        Cursor cursor = database.query("Info", null, null, null, null, null, null);
        List<String> cityList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String city = cursor.getString(cursor.getColumnIndex("city"));
            cityList.add(city);
        }
        return cityList;
    }
    //根据城市名称，替换信息内容
    public static  int UpDateInfoByCity(String city,String content){
        ContentValues values = new ContentValues();
        values.put("content",content);
        return database.update("Info",values,"city=?",new String[]{city});

    }
    public static long AddCityInfo(String city,String content){
        ContentValues values = new ContentValues();
        values.put("content",content);
        values.put("city",city);
        return database.insert("Info",null,values);
    }
    public static String QueryInfoByCity(String city){
        Cursor cursor = database.query("Info", null, "city=?", new String[]{city}, null, null, null);
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            String content = cursor.getString(cursor.getColumnIndex("content"));
            return content;
        }
return null;
    }
    //存储天气最多存储五个天气
    public static int getCityCount(){
        Cursor cursor = database.query("Info", null, null, null, null, null, null);
        int count = cursor.getCount();
        return count;
    }
    public static List<DataBaseBean> QueryAllInfo(){
        Cursor cursor = database.query("Info", null, null, null, null, null, null);
List<DataBaseBean> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String city =cursor.getString(cursor.getColumnIndex("city"));
            String contenr = cursor.getString(cursor.getColumnIndex("content"));
            DataBaseBean dataBaseBean = new DataBaseBean(id, city, contenr);
            list.add(dataBaseBean);
        }
        return list;
    }
    public static int deleteInfoByCity(String city){
        return database.delete("Info","city=?",new String[]{city});
    }
    public static  void  deleteAllInfo(){
        String sql = "delete from Info";
        database.execSQL(sql);
    }
}

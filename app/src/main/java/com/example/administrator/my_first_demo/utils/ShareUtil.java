package com.example.administrator.my_first_demo.utils;

import android.content.Context;
import android.content.SharedPreferences;

/*
 *项目名：com.example.administrator.my_first_demo.utils
 *创建者： LJW
 *创建时间：2018/6/3 0003
 * 描述:sharepreference
 */public class ShareUtil {
    public static final String NAME = "config";

    //键值
    public static void putString(Context mcontext, String key, String value) {
        SharedPreferences sp = mcontext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }
    //键默认值
    public static String getString(Context mcontext, String key, String defValue) {
        SharedPreferences sp = mcontext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);

    }

    //键值
    public static void putInt(Context mcontext, String key, int value) {
        SharedPreferences sp = mcontext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }
    //键默认值
    public static int getInt(Context mcontext, String key, int defValue) {
        SharedPreferences sp = mcontext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getInt(key,defValue);

    }

    //键值
    public static void putBoolean(Context mcontext, String key, boolean value) {
        SharedPreferences sp = mcontext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }
    //键默认值
    public static boolean getBoolean(Context mcontext, String key, boolean defValue) {
        SharedPreferences sp = mcontext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);

    }
//删除 单个
    public static void deleShare(Context mcontext,String key){
        SharedPreferences sp= mcontext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }
    //删除 全部
    public static void deleAll(Context mcontext){
        SharedPreferences sp= mcontext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }

//    private void test(Context context) {
//        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
//        sp.getString("key", "未获取值");
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString("key", "value");
//        editor.commit();
//
//    }
}

package com.example.administrator.my_first_demo.utils;

import android.util.Log;

/*
 *项目名：com.example.administrator.my_first_demo.utils
 *创建者： LJW
 *创建时间：2018/6/3 0003
 * 描述：Tag的封装
 */public class L {
     //开关
    public static boolean DEBUG =true;
    //TAG
    public static final String TAG ="SmartButler:";
    //五个等级
    public static void d (String text){
        Log.d(TAG,text);
    }
    public static void i (String text){
        Log.i(TAG,text);
    }
    public static void w (String text){
        Log.w(TAG,text);
    }

}

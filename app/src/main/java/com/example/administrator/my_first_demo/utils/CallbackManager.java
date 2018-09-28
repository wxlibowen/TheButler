package com.example.administrator.my_first_demo.utils;

public class CallbackManager {
    private static MyCallback myCallback;

    public static void setCallback(MyCallback callback) {
        myCallback = callback;
    }
    public static   void doCallback(){
        myCallback.changeAdapter();
    }
}

package com.example.administrator.my_first_demo.base;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;


import com.example.administrator.my_first_demo.utils.StaticClass;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.mob.MobSDK;
import com.squareup.leakcanary.LeakCanary;

import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.bmob.v3.Bmob;
import cn.jpush.android.api.JPushInterface;


/*
 *项目名：com.example.administrator.my_first_demo.application
 *创建者： LJW
 *创建时间：2018/6/3 0003
 */
public class BaseApplication extends MultiDexApplication {
    //监控全局内存泄漏
    private RefWatcher refWatcher;
    //全局上下文
    private static Context context;
    //一次退出所有Activity
    private static List<Activity> mList=new ArrayList<>();


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        context=getApplicationContext();
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //极光推送相关
        JPushInterface.setDebugMode(true);//正式版的时候设置false，关闭调试
        JPushInterface.init(this);
        //建议添加tag标签，发送消息的之后就可以指定tag标签来发送了
        Set<String> set = new HashSet<>();
        set.add("andfixdemo");//名字任意，可多添加几个,能区别就好了
        JPushInterface.setTags(this, set, null);//设置标签
//        //内存泄漏,只能监控activity内存泄漏
//        if (LeakCanary.isInAnalyzerProcess(this)) {//1
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
        refWatcher = setupLeakCanary();

        //初始化Bugly
        CrashReport.initCrashReport(getApplicationContext(), StaticClass.BUGLY_APP_ID, true);
        //初始化Bmob
        //第一：默认初始化
        Bmob.initialize(this, StaticClass.BOMB_APPID);
        // 讯飞语音的初始化
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + "=" + StaticClass.VOICE_KEY);
        //初始化短信登录
        MobSDK.init(getApplicationContext());
    }

    private RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher(Context context) {
        BaseApplication leakApplication = (BaseApplication) context.getApplicationContext();
        return leakApplication.refWatcher;
    }
    public static Context getContext(){
        return context;
    }
    public static void addActivity(Activity activity){
            mList.add(activity);
    }
    public static void removeAllActivity(){
        for (Activity activity : mList){
            activity.finish();
        }
    }

}

package com.example.administrator.my_first_demo.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.my_first_demo.MainActivity;
import com.example.administrator.my_first_demo.R;
import com.example.administrator.my_first_demo.utils.ShareUtil;
import com.example.administrator.my_first_demo.utils.StaticClass;
import com.example.administrator.my_first_demo.utils.UtilTools;

import java.lang.ref.WeakReference;

/*
 *项目名：com.example.administrator.my_first_demo.ui
 *创建者： LJW
 *创建时间：2018/6/3 0003
 *描述：闪屏页
 * 延时2秒；判断程序是否第一次运行；自定义字体；Activity全屏主题。
 */
public class SplashActivity extends Activity implements View.OnClickListener {

    /**
     * 1.延时2000ms
     * 2.判断程序是否第一次运行
     * 3.自定义字体
     * 4.Activity全屏主题
     */

    private TextView tv_splash;
    private Handler handler = new Handler();

    //倒计时相关
    private TextView skip;
    MyCountDownTimer mc;
    SplashActivity splashActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //无title
        setContentView(R.layout.activity_splash);
        initView();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //延迟两秒执行这里，执行在主线程。hander在哪个线程new，线程就在对应地方执行。
                startMainActivity();
            }
        }, 6000);
    }

    private void startMainActivity() {

        if (ifFirst()) {
            //跳转引导页
            Intent intent = new Intent(this, GuideActivity.class);
            startActivity(intent);
            //关闭当前页面
            finish();
        } else {
            //跳转到登录页面
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }


    //初始化View
    private void initView() {
        tv_splash = findViewById(R.id.tv_splash);
        //设置字体
        UtilTools.setFont(this, tv_splash);
        skip = findViewById(R.id.skip);
        skip.setOnClickListener(this);
        splashActivity = new SplashActivity();
        mc = new MyCountDownTimer(4000, 1000);
        mc.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.skip:
                startMainActivity();
                break;
            default:
        }
    }

    //判断程序是否是第一次运行
    private boolean ifFirst() {
        //第一次把他设为true
       boolean isFirst = ShareUtil.getBoolean(this, "isf", true);
        if (isFirst) {
            //之后把这个设为false
            ShareUtil.putBoolean(this, "isf", false);
            //是第一次运行
            return true;
        } else {
            return false;
        }
    }

    //当程序在加载界面，用户按退出时还会进入主界面；处理办法
    @Override
    protected void onDestroy() {
        //倒计时的内存泄漏避免方法
        if (mc != null) {
            mc.cancel();
            mc = null;
        }
        //把所有的消息和回调移除
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

//    //禁止返回键
//    @Override
//    public void onBackPressed() {
////        super.onBackPressed();
//    }

    /**
     * 继承 CountDownTimer
     * 重写 父类的方法 onTick() 、 onFinish()
     */
    class MyCountDownTimer extends CountDownTimer {
        /**
         * @param millisInFuture    表示以毫秒为单位 倒计时的总数
         *                          <p>
         *                          例如 millisInFuture=1000 表示1秒
         * @param countDownInterval 表示 间隔 多少微秒 调用一次 onTick 方法
         *                          <p>
         *                          例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {

            if (!splashActivity.isFinishing()) {
                skip.setText("正在跳转");
            }

        }

        public void onTick(long millisUntilFinished) {
            skip.setText("跳转" + (millisUntilFinished / 1000));
        }
    }
}

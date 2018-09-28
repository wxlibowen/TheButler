package com.example.administrator.my_first_demo.ui;
/*
 *项目名：com.example.administrator.my_first_demo.ui
 *创建者： LJW
 *创建时间：2018/7/31 0031
 *描述：推送详情界面
 */


import android.app.job.JobInfo;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.my_first_demo.R;

import cn.jpush.android.api.JPushInterface;

public class JushpushActivity extends BaseActivity {
    private TextView jpush_message;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jpush);
        jpush_message=findViewById(R.id.jpush_tv_message);
        Intent it =getIntent();
        if (it!=null){
            Bundle bundle =getIntent().getExtras();
            String title ="";
            String content ="";
            if (bundle!=null){
                title =bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                content =bundle.getString(JPushInterface.EXTRA_ALERT);
            }
            jpush_message.setText("Title:"+title+" "+"Content:"+content);
        }
//        addContentView(jpush_message,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
    }
}

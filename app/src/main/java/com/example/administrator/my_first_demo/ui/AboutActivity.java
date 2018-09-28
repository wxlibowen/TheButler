package com.example.administrator.my_first_demo.ui;
/*
 *项目名：com.example.administrator.my_first_demo.ui
 *创建者： LJW
 *创建时间：2018/8/2 0002
 *描述：
 */


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.administrator.my_first_demo.R;
import com.example.administrator.my_first_demo.utils.UtilTools;

import java.util.ArrayList;
import java.util.List;

public class AboutActivity extends BaseActivity {
    private ListView mListView;
    private List<String> mList = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //去除阴影
        getSupportActionBar().setElevation(0);
        initView();
    }

    //初始化
    private void initView() {
        mListView = findViewById(R.id.mListView);

        mList.add("应用名称：" + getString((R.string.app_name)));
        mList.add("版本号：" + UtilTools.getVersion(this));
        mList.add("官网：www.lixiao.com");
        mList.add("作者：李小白");

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, mList);
//设置适配器
        mListView.setAdapter(mAdapter);
    }

}

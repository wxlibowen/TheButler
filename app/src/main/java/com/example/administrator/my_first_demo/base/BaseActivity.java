package com.example.administrator.my_first_demo.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

/*
 *项目名：com.example.administrator.my_first_demo.ui
 *创建者： LJW
 *创建时间：2018/6/3 0003
 */
 //主要应用：统一应用，统一接口，统一方法
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //显示返回键
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
//    //菜单栏操作
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case android.R.id.home:
//                finish();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
    public  void startToActivity(Class clazz,boolean needToClose){
           startActivity(new Intent(this,clazz));
           if (needToClose){
               finish();
           }
    }
    public  void startToActivity(Class clazz){
        startActivity(new Intent(this,clazz));
    }




}

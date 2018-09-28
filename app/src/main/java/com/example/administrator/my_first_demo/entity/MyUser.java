package com.example.administrator.my_first_demo.entity;

import cn.bmob.v3.BmobUser;

/*
 *项目名：com.example.administrator.my_first_demo.entity
 *创建者： LJW
 *创建时间：2018/6/4 0004
 * 描述：用户属性
 */public class MyUser extends BmobUser {
     private int age;
     private boolean sex;
     private String desc;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

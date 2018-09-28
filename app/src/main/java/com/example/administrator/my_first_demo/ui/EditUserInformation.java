package com.example.administrator.my_first_demo.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.my_first_demo.R;
import com.example.administrator.my_first_demo.entity.MyUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/*
 *项目名：com.example.administrator.my_first_demo.ui
 *创建者： LJW
 *创建时间：2018/6/14 0014
 */public class EditUserInformation extends BaseActivity implements View.OnClickListener {
    //个人信息
    private TextView edit_user;
    private EditText et_username;
    private EditText et_sex;
    private EditText et_age;
    private EditText et_desc;
    //确认修改
    private Button btn_update_ok;
    //删除所有信息按钮
    ImageView deleusername, delesex, deleage, deledesc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editinformation);
        initView();
    }

    private void initView() {
        et_username = findViewById(R.id.et_username);
        et_sex = findViewById(R.id.et_sex);
        et_age = findViewById(R.id.et_age);
        et_desc = findViewById(R.id.et_desc);
        edit_user = findViewById(R.id.edit_user);
        btn_update_ok = findViewById(R.id.btn_update_ok);
        btn_update_ok.setOnClickListener(this);
        //删除
        deleusername = findViewById(R.id.file_delete_username);
        deleusername.setOnClickListener(this);
        deleage = findViewById(R.id.delete_age);
        deleage.setOnClickListener(this);
        delesex = findViewById(R.id.delete_sex);
        delesex.setOnClickListener(this);
        deledesc = findViewById(R.id.delete_desc);
        deledesc.setOnClickListener(this);
        //设置具体的值
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        et_username.setText(userInfo.getUsername());
        et_age.setText(userInfo.getAge() + "");
        et_sex.setText(userInfo.isSex() ? getString(R.string.text_boy) : getString(R.string.text_girl));
        et_desc.setText(userInfo.getDesc());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update_ok:
                AlertDialog.Builder dialogd = new AlertDialog.Builder(this);
                dialogd.setTitle("您真的确定修改个人信息吗？");
                dialogd.setMessage("您的信息不一般哦，请慎重考虑");
                dialogd.setCancelable(false);
                dialogd.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateFile();
                    }
                });
                dialogd.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialogd.show();
                break;
            //清空输入框
            case R.id.file_delete_username:
                et_username.setText("");
                break;
            case R.id.delete_age:
                et_age.setText("");
                break;
            case R.id.delete_sex:
                et_sex.setText("");
                break;
            case R.id.delete_desc:
                et_desc.setText("");
                break;

        }
    }
//更新bomb库
    private void updateFile() {
        //1.拿到输入框的值
        String username = et_username.getText().toString();
        String age = et_age.getText().toString();
        String sex = et_sex.getText().toString();
        String desc = et_desc.getText().toString();
        //2.判断是否为空
        if (!TextUtils.isEmpty(username) & !TextUtils.isEmpty(age) & !TextUtils.isEmpty(sex)) {
            //3.更新属性
            MyUser user = new MyUser();
            user.setUsername(username);
            user.setAge(Integer.parseInt(age));
            //性别
            if (sex.equals(getString(R.string.text_boy))) {
                user.setSex(true);
            } else {
                user.setSex(false);
            }
            //简介
            if (!TextUtils.isEmpty(desc)) {
                user.setDesc(desc);
            } else {
                user.setDesc("这个人很懒，什么都没有留下");
            }
            BmobUser bmobUser = BmobUser.getCurrentUser();
            user.update(bmobUser.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
//                        //修改成功
//                        setEnabled(false);
//                        btn_update_ok.setVisibility(View.GONE);
                        Toast.makeText(EditUserInformation.this, "修改成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditUserInformation.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(EditUserInformation.this, "输入不能为空", Toast.LENGTH_SHORT).show();
        }
    }
}

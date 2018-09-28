package com.example.administrator.my_first_demo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.my_first_demo.R;
import com.example.administrator.my_first_demo.entity.MyUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/*
 *项目名：com.example.administrator.my_first_demo.ui
 *创建者： LJW
 *创建时间：2018/6/8 0008
 * 描述：email修改密码
 */public class EmailForgetPassword extends BaseActivity implements View.OnClickListener{
     private Button emailpassword;
     private EditText etEmail;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);
        emailpassword =findViewById(R.id.btn_forget_password);
        etEmail =findViewById(R.id.et_sendemail);
        emailpassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_forget_password:
                //获取输入框的邮箱
                final String email = etEmail.getText().toString().trim();
                //判断是否为空
                if (!TextUtils.isEmpty(email)) {
                    //发送邮件
                    MyUser.resetPasswordByEmail(email, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(EmailForgetPassword.this, "邮箱已经发送至：" + email, Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(EmailForgetPassword.this, "发送失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(EmailForgetPassword.this, "输入邮箱不能为空", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}

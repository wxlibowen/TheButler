package com.example.administrator.my_first_demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.example.administrator.my_first_demo.MainActivity;
import com.example.administrator.my_first_demo.R;
import com.example.administrator.my_first_demo.base.BaseActivity;
import com.example.administrator.my_first_demo.entity.MyUser;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

/*
 *项目名：com.example.administrator.my_first_demo.ui
 *创建者： LJW
 *创建时间：2018/6/14 0014
 */public class RegisterPhoneAcitvity extends BaseActivity {
     private EditText phoneinput ,phonesmsinput;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerforphone);
        initView();

    }

    private void initView() {
        phoneinput =findViewById(R.id.ph_et_phone);
        phonesmsinput =findViewById(R.id.ph_et_phoneinput);
        String phone =phoneinput.getText().toString().trim();
        BmobSMS.requestSMSCode(phone,"生活助手", new QueryListener<Integer>() {

            @Override
            public void done(Integer smsId,BmobException ex) {
                if(ex==null){//验证码发送成功
                  phonesmsinput.setText(smsId);
                }
            }
        });
        BmobUser.signOrLoginByMobilePhone("11位手机号码", "验证码", new LogInListener<MyUser>() {

            @Override
            public void done(MyUser user, BmobException e) {
                if(user!=null){
                 startActivity(new Intent(RegisterPhoneAcitvity.this, MainActivity.class));
                }
            }
        });
    }

}

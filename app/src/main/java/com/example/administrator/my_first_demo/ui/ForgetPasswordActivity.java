package com.example.administrator.my_first_demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.my_first_demo.R;
import com.example.administrator.my_first_demo.entity.MyUser;
import com.example.administrator.my_first_demo.utils.L;
import com.example.administrator.my_first_demo.utils.StaticClass;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/*
 *项目名：com.example.administrator.my_first_demo.ui
 *创建者： LJW
 *创建时间：2018/6/4 0004
 * 描述：忘记密码
 */public class ForgetPasswordActivity extends BaseActivity {
    @BindView(R.id.et_now)
    EditText etNow;
    @BindView(R.id.et_new)
    EditText etNew;
    @BindView(R.id.et_new_password)
    EditText etNewPassword;
    @BindView(R.id.btn_update_password)
    Button btnUpdatePassword;
    @BindView(R.id.tv_sendemail)
    TextView tvSendemail;
//    private EditText etNow;
//    private EditText etNew;
//    private EditText etNewPassword;
//    private Button btnUpdatePassword;
//    private TextView tvSendemail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_update_password, R.id.tv_sendemail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_update_password:
                //获取输入框的值
                String now = etNow.getText().toString().trim();
                String newpw = etNew.getText().toString().trim();
                String new_password = etNewPassword.getText().toString().trim();
                //判断是否为空
                if (!TextUtils.isEmpty(now)
                        & !TextUtils.isEmpty(newpw)
                        & !TextUtils.isEmpty(new_password)) {
                    //判断两次输入密码是否一致
                    if (newpw.equals(new_password)) {
                        //重置密码
                        L.i(now);
                        MyUser.updateCurrentUserPassword(now, newpw, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(ForgetPasswordActivity.this, "重置密码成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(ForgetPasswordActivity.this, "重置密码失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            //邮箱验证
            case R.id.tv_sendemail:
                startActivity(new Intent(this, EmailForgetPassword.class));
                break;
        }
    }

}

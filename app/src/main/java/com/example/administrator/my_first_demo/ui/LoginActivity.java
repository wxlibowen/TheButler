package com.example.administrator.my_first_demo.ui;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.my_first_demo.MainActivity;
import com.example.administrator.my_first_demo.R;
import com.example.administrator.my_first_demo.base.BaseActivity;
import com.example.administrator.my_first_demo.entity.MyUser;
import com.example.administrator.my_first_demo.utils.ShareUtil;
import com.example.administrator.my_first_demo.view.CustomDialog;
import com.example.administrator.my_first_demo.view.CustomVideoView;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;


import org.json.JSONException;
import org.json.JSONObject;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import de.hdodenhof.circleimageview.CircleImageView;

/*
 *项目名：com.example.administrator.my_first_demo.ui
 *创建者： LJW
 *创建时间：2018/6/4 0004
 * 描述：登录界面
 */public class LoginActivity extends BaseActivity implements View.OnClickListener {
    //注册按钮
    private TextView registered;
    private EditText et_name, et_password;
    private Button btnlogin;
    //记住密码
    private CheckBox rememberPassword;
    //忘记密码
    private TextView forgetpassword;
    private CustomDialog dialog;
    //全部删除
    private ImageView deleteUsername, deletePassword;
    //qq,微信，短信登录
    private CircleImageView login_qq, login_sina,login_mess;
    //自定义登录动画
    private CustomVideoView videoview;

    //qq登录相关
    private static final String APP_ID = "1106990785";//官方获取的APPID
    // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    private UserInfo mUserInfo;

//    //微博登录相关
//    //key
//    public static final String APP_KEY_SINA = "2139709210";
//    //新浪AppSecret值
//    public static final String APP_SECRET_SINA = "ad9f372d935b803c0a1c92033a1752b4";
//    /**
//     * WeiboSDKDemo 应用对应的权限，第三方开发者一般不需要这么多，可直接设置成空即可。
//     * 详情请查看 Demo 中对应的注释。
//     */
//    public static final String SCOPE =
//            "email,direct_messages_read,direct_messages_write,"
//                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
//                    + "follow_app_official_microblog," + "invitation_write";
//    //    public static final String REDIRECT_URL = "http://www.sina.com";
//    //注意，这里的回调页应该和官网的回调页一致，不然会报21322错误
//    public static final String REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";
//    private SsoHandler mSsoHandler;
//    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
//    private Oauth2AccessToken mAccessToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        //用全局上下文避免内存泄漏
        mTencent = Tencent.createInstance(APP_ID,this.getApplicationContext());
//        //微博sdk初始化
//        WbSdk.install(this,new AuthInfo(this, APP_KEY_SINA, REDIRECT_URL,
//                SCOPE));
//        //新浪微博
//        mSsoHandler = new SsoHandler(LoginActivity.this);
        initView();
    }

    private void initView() {
        //登录动画
        videoview =findViewById(R.id.videoview);
        videoview.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.sport));
        //播放
        videoview.start();
        //循环播放
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoview.start();
            }
        });

        //qq,微信，短信登录初始化
        login_qq = findViewById(R.id.login_qq);
        login_qq.setOnClickListener(this);
        login_sina = findViewById(R.id.login_sina);
        login_sina.setOnClickListener(this);
        login_mess =findViewById(R.id.login_sms);
        login_mess.setOnClickListener(this);
        deletePassword = findViewById(R.id.delete_password);
        deletePassword.setOnClickListener(this);
        deleteUsername = findViewById(R.id.delete_username);
        deleteUsername.setOnClickListener(this);
        registered = findViewById(R.id.login_tv_registered);
        registered.setOnClickListener(this);
        et_name = findViewById(R.id.et_name);
        et_password = findViewById(R.id.et_password);
        btnlogin = findViewById(R.id.login_btnLogin);
        btnlogin.setOnClickListener(this);
        forgetpassword = findViewById(R.id.tv_forget);
        forgetpassword.setOnClickListener(this);
        rememberPassword = findViewById(R.id.remember_password);
        //自定义对话框
        dialog = new CustomDialog(this, 0, 0, R.layout.dialog_loding, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        //屏幕外点击无效
        dialog.setCancelable(false);

        //设置选中的状态
        boolean isCheck = ShareUtil.getBoolean(this, "rememberPassword", false);
        rememberPassword.setChecked(isCheck);
        if (isCheck) {
            //设置密码
            et_name.setText(ShareUtil.getString(this, "name", ""));
            et_password.setText(ShareUtil.getString(this, "password", ""));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //注册
            case R.id.login_tv_registered:
                startActivity(new Intent(this, RegisteredEmailActivity.class));
                break;
            case R.id.login_btnLogin:
                //获取输入框的值
                String name = et_name.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                //判断是否为空
                if (!TextUtils.isEmpty(name) & !TextUtils.isEmpty(password)) {
                    dialog.show();
                    //登录 这里开始采用bmob
                    final MyUser user = new MyUser();
                    user.setUsername(name);
                    user.setPassword(password);
                    user.login(new SaveListener<MyUser>() {
                        //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                        //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                        @Override
                        public void done(MyUser bmobUser, BmobException e) {
                            dialog.dismiss();
                            if (e == null) {
                                //判断邮箱是否验证成功
//                                if (user.getEmailVerified()) {
                                //跳转
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
//                                } else {
//                                    Toast.makeText(LoginActivity.this, "请前往邮箱验证", Toast.LENGTH_SHORT).show();
//                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "登录失败：账号或者密码错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            //qq登录
            case R.id.login_qq:
                mIUiListener = new BaseUiListener();
                //all表示获取所有权限
                mTencent.login(LoginActivity.this,"all", mIUiListener);
                break;
            //微博登录
            case R.id.login_sina:
//                //授权方式有三种，第一种对客户端授权 第二种对Web短授权，第三种结合前两中方式
//                mSsoHandler.authorize(new SelfWbAuthListener());
                break;
            //微信登录
            case R.id.login_sms:
                startActivity(new Intent(LoginActivity.this,MobSms.class));
                break;
            //忘记密码
            case R.id.tv_forget:
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                break;
            case R.id.delete_username:
                et_name.setText("");
                break;
            case R.id.delete_password:
                et_password.setText("");
                break;
        }
    }

    //假设输入用户名和密码，但是不点击登录，而是直接退出
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //保存状态
        ShareUtil.putBoolean(this, "rememberPassword", rememberPassword.isChecked());
        //是否记住密码
        if (rememberPassword.isChecked()) {
            //记住密码
            ShareUtil.putString(this, "name", et_name.getText().toString().trim());
            ShareUtil.putString(this, "password", et_password.getText().toString().trim());
        } else {
            ShareUtil.deleShare(this, "name");
            ShareUtil.deleShare(this, "password");
        }
    }
    /**qq相关
     * 自定义监听器实现IUiListener接口后，需要实现的3个方法
     *
     */
    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
            Toast.makeText(getApplicationContext(), "授权成功", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//            Log.e(TAG, "response:" + response);
            JSONObject obj = (JSONObject) response;
            try {
                String openID = obj.getString("openid");
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken,expires);
                QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getApplicationContext(),qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
//                        Log.e(TAG,"登录成功"+response.toString());
                    }

                    @Override
                    public void onError(UiError uiError) {
//                        Log.e(TAG,"登录失败"+uiError.toString());
                    }

                    @Override
                    public void onCancel() {
//                        Log.e(TAG,"登录取消");

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel() {
            Toast.makeText(getApplicationContext(), "授权取消", Toast.LENGTH_SHORT).show();

        }

    }

    /**
     * 在调用Login的Activity或者Fragment中重写onActivityResult方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.REQUEST_LOGIN){
            Tencent.onActivityResultData(requestCode,resultCode,data,mIUiListener);
        }
        //新浪 login
//        if(mSsoHandler!=null){
//            mSsoHandler.authorizeCallBack(requestCode,resultCode,data);
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//    //微博登录相关
//    private class SelfWbAuthListener implements com.sina.weibo.sdk.auth.WbAuthListener{
//        @Override
//        public void onSuccess(final Oauth2AccessToken token) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mAccessToken = token;
//                    if (mAccessToken.isSessionValid()) {
//                        // 显示 Token
////                        updateTokenView(false);
//                        // 保存 Token 到 SharedPreferences
////                        AccessTokenKeeper.writeAccessToken(LoginActivity.this, mAccessToken);
//                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
//                        Toast.makeText(LoginActivity.this,"授权成功",Toast.LENGTH_SHORT).show();
//                        //获取个人资料
//                        //https://api.weibo.com/2/users/show.json
////                      OkHttpClient okHttpClient =new OkHttpClient();
////                     FormBody body =new FormBody.Builder().add("access_token",mAccessToken.getToken())
////                             .add("uid",mAccessToken.getUid()).build();
////                        Request request =new Request.Builder().post(body).url("https://api.weibo.com/2/users/show.json").build();
////                        Call call =okHttpClient.newCall(request);
////                        call.enqueue(new Callback() {
////                            @Override
////                            public void onFailure(Call call, IOException e) {
////                                ViseLog.d("获取失败："+e.getMessage());
////                                        e.printStackTrace();
////                            }
////                            @Override
////                            public void onResponse(Call call, Response response) throws IOException {
////                                        ViseLog.d("response:"+response);
////                                        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(String.valueOf(response));
////                                        String headUrl = jsonObject.getString("profile_image_url");
////                            }
////                        });
//                        OkHttpUtils.get()
//                                .url("https://api.weibo.com/2/users/show.json")
//                                .addParams("access_token",mAccessToken.getToken())
//                                .addParams("uid",mAccessToken.getUid())
//                                .build()
//                                .execute(new StringCallback() {
//                                    //注意导包导的是这个包import com.zhy.http.okhttp.callback.StringCallback;
//                                    @Override
//                                    public void onError(okhttp3.Call call, Exception e, int id) {
//                                        L.d("获取失败："+e.getMessage());
//                                        e.printStackTrace();
//                                    }
//                                    @Override
//                                    public void onResponse(String response, int id) {
//                                        L.d("response:"+response);
//                                        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response);
//                                        String headUrl = jsonObject.getString("profile_image_url");
//                                    }
//                                });
//
//                    }
//                }
//            });
//        }

//        @Override
//        public void cancel() {
//            L.d("取消授权---sinal---");
//        }
//
//        @Override
//        public void onFailure(WbConnectErrorMessage errorMessage) {
//            Toast.makeText(LoginActivity.this, errorMessage.getErrorMessage(), Toast.LENGTH_LONG).show();
//        }
//    }
    //返回重启加载
    @Override
    protected void onRestart() {
        super.onRestart();
        initView();
    }

    //防止锁屏或者切出的时候，音乐在播放
    @Override
    protected void onStop() {
        super.onStop();
        videoview.stopPlayback();
    }


}

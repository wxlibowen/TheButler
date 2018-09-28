package com.example.administrator.my_first_demo.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.my_first_demo.R;
import com.example.administrator.my_first_demo.utils.L;
import com.example.administrator.my_first_demo.utils.ShareUtil;
import com.example.administrator.my_first_demo.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
 *项目名：com.example.administrator.my_first_demo.ui
 *创建者： LJW
 *创建时间：2018/6/3 0003
 * 描述：设置
 */public class SettingActivity extends BaseActivity {

    //语音播报
    @BindView(R.id.sw_speak)
    Switch swSpeak;
    //检测更新
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.ll_update)
    LinearLayout llUpdate;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.ll_my_location)
    LinearLayout llMyLocation;
    @BindView(R.id.ll_about)
    LinearLayout llAbout;
    private String versionName;
    private int versioncode;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initView();


    }

    private void initView() {
        boolean isSpeak = ShareUtil.getBoolean(this, "isSpeak", false);
        swSpeak.setChecked(isSpeak);
        getVersionNameCode();
        tvVersion.setText("检测版本：" + versionName);

    }

    @OnClick({R.id.sw_speak, R.id.ll_update,R.id.ll_my_location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sw_speak:
                //切换相反
                swSpeak.setSelected(!swSpeak.isSelected());
                //保存状态
                ShareUtil.putBoolean(this, "isSpeak", swSpeak.isChecked());
                break;
            case R.id.ll_update:
                /**
                 * 1、请求服务器的配置文件，拿到code
                 * 2、比较
                 * 3、dialog提示
                 * 4、跳转到更新界面，并且把url传递过去
                 */
                RxVolley.get(StaticClass.CHECK_UPDATE_URL, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        L.i(t);
                        parsingJson(t);
                    }
                });

                break;
            case R.id.ll_my_location:
                startActivity(new Intent(SettingActivity.this,LocationActivity.class));
                break;
        }
    }

    //解析json
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            int code = jsonObject.getInt("versionCode");
            url =jsonObject.getString("url");
            if (code > versioncode) {
                showUpdateDialog(jsonObject.getString("content"));
            } else {
                Toast.makeText(this, "当前为最新版本", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    //获取版本号
    private void getVersionNameCode() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versioncode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    //弹出升级提示
    private void showUpdateDialog(String content) {
        new AlertDialog.Builder(this).setTitle("有新版本").setMessage("修复多项bug").
                setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent it =new Intent(SettingActivity.this, UpdateActivity.class);
                        it.putExtra("url",url);
                         startActivity(it);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

}

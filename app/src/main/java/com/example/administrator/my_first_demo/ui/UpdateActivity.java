package com.example.administrator.my_first_demo.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.my_first_demo.R;
import com.example.administrator.my_first_demo.utils.L;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.http.VolleyError;
import com.kymjs.rxvolley.toolbox.FileUtils;

import java.io.File;
import java.lang.ref.WeakReference;

/*
 *项目名：com.example.administrator.my_first_demo.ui
 *创建者： LJW
 *创建时间：2018/6/11 0011
 */
public class UpdateActivity extends BaseActivity {
    //正在下载
    public static final int HANDLER_LODING=10001;
    //下载完成
    public static final int HANDLER_OK=10002;
    //下载失败
    public static final int HANDLER_NO=10003;
    private TextView tv_size;
    private String url;
    private String path;

        //进度条
//    private NumberProgressBar number_progress_bar;
    private ProgressBar progressBar;
    //防止内存泄漏采用弱引用
    private CopyHandler mhandler;
    //有个问题尚未解决，这个类没有静态
    private class CopyHandler extends Handler {
        WeakReference<UpdateActivity> mactivity;

        CopyHandler(UpdateActivity activity) {
            this.mactivity = new WeakReference<UpdateActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final UpdateActivity activity = mactivity.get();
            switch (msg.what) {
                case HANDLER_LODING:
                    //实时更新进度
                    Bundle bundle = msg.getData();
                    long transferredBytes = bundle.getLong("transferredBytes");
                    long totalSize = bundle.getLong("totalSize");
                    tv_size.setText(transferredBytes + "/" + totalSize);
                    //设置进度
                    // 30%  --- 100%   number_progress_bar.setpar(30)
//                      5200.0 / 52000.0  10.0%  10 / 100  = 100%
                   progressBar.setProgress((int) (((float) transferredBytes / (float) totalSize) * 100));
                    break;
                case HANDLER_OK:
                    tv_size.setText("下载成功");
                    //启动应用安装
                    startInstallApk();
                    break;
                case HANDLER_NO:
                    tv_size.setText("下载失败");
                    break;
            }
        }
    }
    //    private Handler handler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what){
//                case HANDLER_LODING:
//                    //实时更新进度
//                    Bundle bundle=msg.getData();
//                    long transferredBytes= bundle.getLong("transferredBytes");
//                    long totalSize= bundle.getLong("totalSize");
//                    tv_size.setText(transferredBytes+"/"+totalSize);
//                    break;
//                case HANDLER_OK:
//                    tv_size.setText("下载成功");
//                    //启动应用安装
//                    startInstallApk();
//                    break;
//                case HANDLER_NO:
//                    tv_size.setText("下载失败");
//                    break;
//            }
//        }
//    };
    //启动安装
    private void startInstallApk() {
        Intent i=new Intent();
        i.setAction(Intent.ACTION_VIEW);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setDataAndType(Uri.fromFile(new File(path)),"application/vnd.android.package-archive");
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        initView();
        mhandler = new CopyHandler(this);

    }

    //初始化
    private void initView() {
        tv_size = findViewById(R.id.tv_size);
        //进度条初始化
        progressBar =findViewById(R.id.progressBarHorizontal);
//        number_progress_bar = (NumberProgressBar) findViewById(R.id.number_progress_bar);
//        number_progress_bar.setMax(100);
        path = FileUtils.getSDCardPath() + "/" + System.currentTimeMillis() + ".apk";
        //下载
        url = getIntent().getStringExtra("url");
        if (!TextUtils.isEmpty(url)) {
            //下载
            RxVolley.download(path, url, new ProgressListener() {
                @Override
                public void onProgress(long transferredBytes, long totalSize) {
                    L.i("transferredBytes:" + transferredBytes + "totalSize:" + totalSize);
                    Message msg=new Message();
                    msg.what=HANDLER_LODING;
                    Bundle bundle=new Bundle();
                    bundle.putLong("transferredBytes",transferredBytes);
                    bundle.putLong("totalSize",totalSize);
                    msg.setData(bundle);
                    mhandler.sendMessage(msg);
                }
            }, new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    L.i("成功");
                    mhandler.sendEmptyMessage(HANDLER_OK);
                }

                @Override
                public void onFailure(VolleyError error) {
                    L.i("失败");
                    mhandler.sendEmptyMessage(HANDLER_NO);
                }
            });

        }
    }
}

package com.example.administrator.my_first_demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.example.administrator.my_first_demo.R;
import com.example.administrator.my_first_demo.base.BaseActivity;

/*
 *项目名：com.example.administrator.my_first_demo.ui
 *创建者： LJW
 *创建时间：2018/6/7 0007
 */public class WebViewActivity extends BaseActivity {
     //进度条
     private ProgressBar progressBar;
     //webviw
    private WebView webView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        initView();
    }

    //初始化View
    private void initView() {
        progressBar=findViewById(R.id.mProgressBar);
        webView =findViewById(R.id.mWebView);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String url = intent.getStringExtra("url");
        //设置标题
        getSupportActionBar().setTitle(title);
        //加载网页的逻辑

        //支持JavaScript
        webView.getSettings().setJavaScriptEnabled(true);

        //支持缩放
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        //接口回调

        webView.setWebChromeClient(new WebViewClient());
        //加载网页
        webView.loadUrl(url);

        //本地显示
        webView.setWebViewClient(new android.webkit.WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                //接受此事件
                return true;}
        });
    }
    public class WebViewClient extends WebChromeClient{
        //进度变化的监听
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if(newProgress==100){
                progressBar.setVisibility(View.GONE);
            }
        }
    }
//防止webView内存泄漏的方法
    //让onDetachedFromWindow先走，在主动调用destroy()之前，把webview从它的parent上面移除掉。
    @Override
    protected void onDestroy() {

        if(webView!=null){
            ViewParent parent =webView.getParent();
            if(parent!=null){
                ((ViewGroup)parent).removeView(webView);
            }
            webView.stopLoading();
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.clearView();
            webView.removeAllViews();
            webView.destroy();
        }
        super.onDestroy();
    }
}

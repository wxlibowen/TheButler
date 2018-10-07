package com.example.administrator.my_first_demo.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import com.example.administrator.my_first_demo.R;
import com.example.administrator.my_first_demo.base.BaseActivity;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

/*
 *      项目名：       SmartButler
 *      包  名：       com.example.zhaoy.smartbutler.ui
 *      文件名：       QrCodeActivity
 *      创建者：       zhaoy
 *      创建时间：     2018/7/2 or 10:06
 *      描  述：       生成二维码
 */public class QrCodeActivity extends BaseActivity {
     //我得二维码
    private ImageView iv_qr_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        initView();
    }

    private void initView() {
        iv_qr_code=findViewById(R.id.iv_qr_code);
        int width=getResources().getDisplayMetrics().widthPixels;
        //根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
        Bitmap qrCodeBitmap= EncodingUtils.createQRCode("我是生活助手",width/2,width/2,
                        BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        iv_qr_code.setImageBitmap(qrCodeBitmap);
    }
}

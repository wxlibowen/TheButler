package com.example.administrator.my_first_demo.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/*
 *项目名：com.example.administrator.my_first_demo.utils
 *创建者： LJW
 *创建时间：2018/6/3 0003
 */public class UtilTools {
    public static void setFont(Context mcontext, TextView textView) {
        Typeface font_type = Typeface.createFromAsset(mcontext.getAssets(), "fonts/FONT.TTF");
        textView.setTypeface(font_type);

    }
    //从ImageView中去除图片到保存到shareUtil
    public static void putImageToShare(Context context,ImageView imageView){
        //保存
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        //第一步：将bitmap压缩成字节数组输出流
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        //第二步：利用base64将我们的字节数组输出流转换成String
        byte[] bytes = byteArrayOutputStream.toByteArray();
        String imgString = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
        //第三步：将string保存在shareUtils
        ShareUtil.putString(context, "image_title", imgString);
    }
    //保存Bitmap图片到shareUtil
    public static void putImageToShare(Context context,Bitmap imageBitmap){
        //保存
        //第一步：将bitmap压缩成字节数组输出流
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        //第二步：利用base64将我们的字节数组输出流转换成String
        byte[] bytes = byteArrayOutputStream.toByteArray();
        String imgString = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
        //第三步：将string保存在shareUtils
        ShareUtil.putString(context, "image_title", imgString);
    }
    //读取图片
    public static void getImageToShare(Context context,ImageView imageView){
        //拿到String
        String image_title = ShareUtil.getString(context, "image_title", "");
        if (!image_title.equals("")) {
            //利用base64将我们的String转换
            byte[] decode = Base64.decode(image_title, Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decode);
            //生成bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);

            imageView.setImageBitmap(bitmap);
        }

    }
    public static Bitmap getImageBitmapToShare(Context context){
        //拿到String
        String image_title = ShareUtil.getString(context, "image_title", "");
        if (!image_title.equals("")) {
            //利用base64将我们的String转换
            byte[] decode = Base64.decode(image_title, Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decode);
            //生成bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
            return bitmap;

        }else {
            return null;
        }

    }
    //获取版本号
    public static String getVersion(Context mContext){
        PackageManager pm=mContext.getPackageManager();
        try {
            PackageInfo info=pm.getPackageInfo(mContext.getPackageName(),0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "未知";
        }
    }

}

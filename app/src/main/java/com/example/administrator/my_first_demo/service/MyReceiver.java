package com.example.administrator.my_first_demo.service;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.my_first_demo.MainActivity;
import com.example.administrator.my_first_demo.R;
import com.example.administrator.my_first_demo.ui.JushpushActivity;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            Log.d(TAG, "onReceive - " + intent.getAction());
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                Log.e(TAG, "8.0处理了");
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);//定义通知id
                String channelId = context.getPackageName();//通知渠道id
                String channelName = "消息通知";//"PUSH_NOTIFY_NAME";//通知渠道名
                int importance = NotificationManager.IMPORTANCE_HIGH;//通知级别
                NotificationChannel channel = new NotificationChannel(channelId , channelName, importance);
//                channel.enableLights(true);//设置闪光灯
                channel.setLightColor(Color.RED);
                channel.enableVibration(true);//设置通知出现震动
                channel.setShowBadge(true);
                channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notificationManager.createNotificationChannel(channel);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                Intent notificationIntent = new Intent(context, JushpushActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                String message = bundle.getString(JPushInterface.EXTRA_ALERT);
                String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                String s = bundle.getString(JPushInterface.EXTRA_EXTRA);
                Intent intent0= new Intent(context, MainActivity.class);
                PendingIntent pi = PendingIntent.getActivity(context, 0, intent0, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentTitle(title)//设置通知栏标题
                        .setContentText(message)
                        .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                        .setSmallIcon(R.mipmap.ic_launcher)//设置通知小ICON
                        .setChannelId(channelId)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentIntent(pi);
                Notification notification = builder.build();
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                if (notificationManager != null) {
                    notificationManager.notify(notificationId , notification);
                }
            } else if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                System.out.println("收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                // 自定义消息不会展示在通知栏，完全要开发者写代码去处理

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                System.out.println("收到了通知");
                // 在这里可以做些统计，或者做些其他工作
            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//                Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
//                Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//                Logger.d(TAG, "[MyReceiver] 用户点击打开了通知");

                //打开自定义的Activity
                Intent i = new Intent(context, JushpushActivity.class);
                i.putExtras(bundle);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
//                Logger.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
//                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
//                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
//                Logger.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
            } else {
//                Logger.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {

        }
    }


}
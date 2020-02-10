package com.lmjproject.weatherquery;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class WeatherNotification{
    private static String NOTIFICATION_NAME="com.lmjproject.weatherquery.notification";
    private NotificationManagerCompat notificationManager;
    public NotificationCompat.Builder notification;
    private Context mContext;
    public static int NOTIFICATION_ID=333;
    public WeatherNotification(Context context){
        mContext=context;
        //首先注册通知
        notificationManager=NotificationManagerCompat.from(mContext);
        createNotificationChannel();
        //创建notification
        notification=new NotificationCompat.Builder(mContext,NOTIFICATION_NAME).
                setSmallIcon(R.mipmap.ic_launcher).
                setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.ic_launcher)).
                setContentTitle("WeatherQuery").setColor(Color.parseColor("#87CEEB")).
                setContentText("").setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Intent it =new Intent(mContext,MainActivity.class);
        PendingIntent pit =PendingIntent.getActivity(mContext,0,it,0);
        notification.setContentIntent(pit);//设置点击通知后打开的活动
        //notification.setAutoCancel(true);//设置点击后自动关闭
    }
    public void showNotification(String title,String text){
        notification.setContentTitle(title);
        notification.setContentText(text);
        //notificationManager.notify(NOTIFICATION_ID,notification.build());
        ((WeatherService)mContext).startForeground(WeatherNotification.NOTIFICATION_ID,notification.build());
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String id=NOTIFICATION_NAME;
            String name = NOTIFICATION_NAME;
            String description = NOTIFICATION_NAME;
            NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            channel.setDescription(description);
            //notificationManager.deleteNotificationChannel(NOTIFICATION_NAME);
            notificationManager.createNotificationChannel(channel);
        }

    }


}

package com.hong.rise;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

/**
 * Created by hong on 2016/6/19.
 */

//参考文档：http://blog.csdn.net/loongggdroid/article/details/17616509
public class NotificationDemoActivity extends Activity {
    private static final int NOTIFICATION_FLAG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
    }


    public void notificationMethod(View view) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        switch (view.getId()) {
            case R.id.btn1:
                // default notification，已经过时了

                // 创建一个PendingIntent，和Intent类似，不同的是由于不是马上调用，需要在下拉状态条出发的activity，所以采用的是PendingIntent,即点击Notification跳转启动到哪个Activity
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

                Notification notify1 = new Notification();
                notify1.icon = R.drawable.icon;
                //有通知时，在未下拉的情况下，显示的文本
                notify1.tickerText = "Ticker Text: you have a new message, Please read!";
                notify1.when = System.currentTimeMillis();
                notify1.setLatestEventInfo(this, "Notification Title", "This is the notification message", pendingIntent);
                notify1.number = 1;
                notify1.flags |= Notification.FLAG_AUTO_CANCEL;

                // 通过通知管理器来发起通知。如果id不同，则每click，在statu那里增加一个提示
                manager.notify(NOTIFICATION_FLAG, notify1);
                break;

            //默认通知，API11之后可用
            case R.id.btn2:
                PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

                Notification notify2 = new Notification.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setTicker("Ticket text")
                        .setContentTitle("title")
                        .setContentText("notification content message")
                        .setContentIntent(pendingIntent2)
                        .setNumber(1)
                        .getNotification();
                notify2.flags |= Notification.FLAG_AUTO_CANCEL;
//                manager.notify(NOTIFICATION_FLAG, notify2);
                manager.notify(2, notify2);

                break;

            //默认通知，API16之后可用
            case R.id.btn3:
                PendingIntent pendingIntent3 = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

                Notification notify3 = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    notify3 = new Notification.Builder(this)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setTicker("Ticket text 3")
                            .setContentTitle("title 3")
                            .setContentText("notification content message 3")
                            .setContentIntent(pendingIntent3)
                            .setNumber(1)
                            .build();
                }
                notify3.flags |= Notification.FLAG_AUTO_CANCEL;
//                manager.notify(NOTIFICATION_FLAG, notify3);
                manager.notify(3, notify3);
                break;

            case R.id.btn4:
                Notification myNotify = new Notification();
                myNotify.icon = R.drawable.id_union_pay;
                myNotify.tickerText = "my Ticker text";
                myNotify.when = System.currentTimeMillis();
                myNotify.flags = Notification.FLAG_NO_CLEAR;

                myNotify.defaults |= Notification.DEFAULT_VIBRATE;//设置震动


                RemoteViews rv = new RemoteViews(getPackageName(),R.layout.my_notification);
                rv.setTextViewText(R.id.text_content,"my hello wrold!");

                myNotify.contentView = rv;
                myNotify.contentIntent = PendingIntent.getActivity(this, 0, new Intent(Intent.ACTION_MAIN),0);
//                manager.notify(NOTIFICATION_FLAG,myNotify);
                manager.notify(4,myNotify);
                break;


            case R.id.btn5:
                // 清除id为NOTIFICATION_FLAG的通知
//                manager.cancel(NOTIFICATION_FLAG);

                //清除所有
                manager.cancelAll();
                break;

        }


    }

}

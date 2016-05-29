package com.hong.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by hong on 2016/5/7.
 */
public class LocalServcie extends Service {


    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("start service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onStartCommand");
        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("bind service");

        MyBinder myBinder = new MyBinder();
        System.out.println("LocalServcie: " + myBinder.toString());
        return myBinder;
    }

    private class MyBinder extends Binder implements IServiceBinder {

        @Override
        public void method() {
            inServiceMethod();//调用服务里面的方法
        }
    }


    @Override
    public boolean onUnbind(Intent intent) {

        System.out.println("unbind service");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {

        System.out.println("stop service");

    }


    private void inServiceMethod() {
        System.out.println("inServiceMethod");
    }

}

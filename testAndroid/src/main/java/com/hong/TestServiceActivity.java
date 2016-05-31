package com.hong;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import com.hong.service.IServiceBinder;
import com.hong.service.LocalServcie;

/**
 * Created by hong on 2016/5/7.
 */
public class TestServiceActivity extends Activity {

    private IServiceBinder binder;
    private MyConn myConn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_service);
    }


    public void startService(View view) {
        Intent servcie = new Intent(this, LocalServcie.class);
        startService(servcie);
    }

    public void bindService(View view) {
        Intent servcie = new Intent(this, LocalServcie.class);
        myConn = new MyConn();
        bindService(servcie,myConn, BIND_AUTO_CREATE);

    }

    private class MyConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (IServiceBinder) service;
            System.out.println("TestServiceActivity: "+binder.toString());

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }


    public void callServiceMethod(View view) {
        binder.method();//实质是调用服务里面的方法

    }

    public void unBindService(View view) {

        unbindService(myConn);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            unbindService(myConn);
        } catch (Exception e) {
        }

    }

    public void stopService(View view) {
        Intent servcie = new Intent(this, LocalServcie.class);
        stopService(servcie);
    }


}

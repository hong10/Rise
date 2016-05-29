package com.hong;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;

import com.hong.remoteservice.IPayService;

/**
 * Created by hong on 2016/5/7.
 */
public class TestRemoteServiceActivity extends Activity {
    private IPayService iPayService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_remote_service);
    }

    public void bindRemoteService(View view) {
        Intent service = new Intent();
        service.setAction("com.hong.pay");
        bindService(service,new MyConn() , BIND_AUTO_CREATE);
    }



    public void callRemoteService(View view) {
        try {
//            String str = iPayService.method();
//            Toast.makeText(this, str, Toast.LENGTH_LONG).show();

            iPayService.method();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    private class MyConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iPayService = IPayService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}

package com.hong.studyandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hong.service.BlockSmsAndCallService;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void studyActivity(View view) {
        Intent intent = new Intent(this, StudyActivity.class);
        startActivity(intent);
    }

    public void sendBroadcast(View view) {
        Intent intent = new Intent();
        intent.setAction("com.hong.common.broadcast");
        intent.putExtra("common", "a common broadcast");
        sendBroadcast(intent);
    }

    public void sendOrderBroadcast(View view) {
        Intent intent = new Intent();
        intent.setAction("com.hong.order.broadcast");

        Bundle bundle = new Bundle();
        bundle.putString("a", "aaa");
        intent.putExtras(bundle);
        sendOrderedBroadcast(intent, null);
    }

    public void blockSmsAndCall(View view) {
        Intent intent = new Intent(this, BlockSmsAndCallService.class);
        startService(intent);
    }

    public void handlerDemo1(View view) {
        Intent intent = new Intent(this, HandlerDemo1Activity.class);
        startActivity(intent);
    }

    public void handlerDemo2(View view) {
        Intent intent = new Intent(this, HandlerDemo2Activity.class);
        startActivity(intent);
    }


    public void volleyDemo(View view) {
        Intent intent = new Intent(this, VolleyDemoActivity.class);
        startActivity(intent);
    }

    public void mockLoginByVolley(View view) {
        Intent intent = new Intent(this, MockLoginByVolleyActivity.class);
        startActivity(intent);
    }


}
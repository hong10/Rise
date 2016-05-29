package com.hong.rise;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showBaseImage(View view) {
        Intent intent = new Intent(this, ShowBaseImageActivity.class);
        startActivity(intent);

    }

    public void rotateImage(View view) {
        Intent intent = new Intent(this, RotateImageActivity.class);
        startActivity(intent);

    }

    public void netImage(View view) {
        Intent intent = new Intent(this, LoadPicFromNetActivity.class);
        startActivity(intent);

    }

    public void fakeNetEasyNewsClient(View view) {
        Intent intent = new Intent(this, FakeNetEasyNewsActivity.class);
        startActivity(intent);

    }

    public void loadImageDemo(View view) {
        Intent intent = new Intent(this, LoadImageDemo.class);
        startActivity(intent);
    }

    public void fragmentDemo(View view) {
        Intent intent = new Intent(this, FragmentDemoActivity.class);
        startActivity(intent);
    }

    public void lotteryDemo(View view) {
        Intent intent = new Intent(this,LotteryLaunchActivity.class);
        startActivity(intent);
    }

    public void setVpn(View view) {
        Intent intent = new Intent(this,VpnActivity.class);
        startActivity(intent);

    }
}
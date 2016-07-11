package com.hong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        setContentView(R.layout.activity_meminfo_test);
//        setContentView(R.layout.activity_memory_leak_demo);
    }


    public void testService(View view) {

//        System.out.println("test service");

        Intent intent = new Intent(this, TestServiceActivity.class);
        startActivity(intent);

    }

    public void testRemoteService(View view) {

        Intent intent = new Intent(this, TestRemoteServiceActivity.class);
        startActivity(intent);

    }


    /**
     * memory Leak demo
     *
     * @param view
     */
    public void testMemoryLeak(View view) {

        Intent intent = new Intent(this, MemoryInfoActivity.class);
        startActivity(intent);

    }




}

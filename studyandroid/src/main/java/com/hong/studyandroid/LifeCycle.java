package com.hong.studyandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/5/27.
 */
public class LifeCycle extends Activity {

    private static final String TAG = LifeCycle.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "onCreate");

        TextView tv = new TextView(this);
        tv.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        tv.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        tv.setTextColor(Color.RED);
        tv.setTextSize(25f);
        tv.setText("I am activity");
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(LifeCycle.this).setTitle("title")
                        .setMessage("alert").setPositiveButton("OK", null).show();
            }
        });


        setContentView(tv);


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    public void finish() {
        super.finish();
        Log.i(TAG, "finish");
    }
}

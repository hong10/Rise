package com.hong.studyandroid;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/5/31.
 */
public class HandlerDemo1Activity extends Activity {
    private static final String TAG = HandlerDemo1Activity.class.getSimpleName();
    private ProgressBar progressBar;
    private TextView tvStatus;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            progressBar.setProgress(msg.arg1);

            //重新把进程加入到进程队列中
            handler.post(runnable);

            if (msg.arg1 == 100) {
                tvStatus.setText("update success");
                handler.removeCallbacks(runnable);
            }


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_handlerdemo1);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        tvStatus = (TextView) findViewById(R.id.tv_status);

        Log.i(TAG, "activity thread id: " + Thread.currentThread().getId());
        Log.i(TAG, "activity thread name: " + Thread.currentThread().getName());


    }



    public void checkUpdate(View view) {
        //show progressBar
        progressBar.setVisibility(View.VISIBLE);
        //将线程加入到handler的线程队列中
        handler.post(runnable);

    }

    Runnable runnable = new Runnable() {
        int i = 0;

        @Override
        public void run() {

            i = i + 10;
            Log.i(TAG, "runnable thread id: " + Thread.currentThread().getId());
            Log.i(TAG, "runnable thread name: " + Thread.currentThread().getName());

            Message msg = handler.obtainMessage();
            msg.arg1 = i;

            //延迟1s
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //将消息发送到消息队列
            handler.sendMessage(msg);

//            if (i == 100) {
            //不能用在run方法中
//                handler.removeCallbacks(runnable);
//            }

        }
    };



}

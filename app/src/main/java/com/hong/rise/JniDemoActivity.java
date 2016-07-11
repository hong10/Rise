package com.hong.rise;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hong.rise.view.MonitorView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/6/30.
 */
public class JniDemoActivity extends Activity {
    private static final String TAG = JniDemoActivity.class.getSimpleName();

    public native int getCurrentPressure();

    static {
        System.loadLibrary("pressure");
    }

    private Timer timer;
    private TimerTask task;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int pressure = msg.what;

            //这里可以做判断，进行一些逻辑处理
            /*if(pressure>275){
                //发短信, 发邮件, 播放报警音乐.
                MediaPlayer player = MediaPlayer.create(getApplicationContext(), R.raw.ylzs);
                player.setLooping(false);
                player.setVolume(1.0f, 1.0f);
                player.start();
                if (task != null && timer != null) {
                    task.cancel();
                    timer.cancel();
                    task = null;
                    timer = null;
                }
                TextView tv  = new TextView(getApplicationContext());
                tv.setTextSize(30);
                tv.setText("锅炉快要爆炸了,赶紧跑把");
                setContentView(tv);
                return;
            }*/


            setContentView(new MonitorView(getApplicationContext(),pressure));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni_demo);

        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                int pressure = getCurrentPressure();
                Log.i(TAG, pressure + "");

                Message msg = Message.obtain();
                msg.what = pressure;
                handler.sendMessage(msg);

            }
        };

        timer.schedule(task, 1000, 1000);

    }


    @Override
    protected void onDestroy() {
        if (timer != null && task != null) {
            task.cancel();
            timer.cancel();
            task = null;
            timer = null;
        }

        super.onDestroy();
    }
}

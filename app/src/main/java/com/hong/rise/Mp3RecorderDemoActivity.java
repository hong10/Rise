package com.hong.rise;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hong.mp3record.MP3Recorder;
import com.hong.rise.view.VolumeWaveHelper;
import com.hong.rise.view.VolumeWaveView;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2016/7/15.
 */
public class Mp3RecorderDemoActivity extends Activity {

    private static final String TAG = Mp3RecorderDemoActivity.class.getSimpleName();
    private VolumeWaveHelper helper;
    private VolumeWaveView waveView;
    private Button start, stop;
    private MP3Recorder mRecorder = new MP3Recorder(new File(Environment.getExternalStorageDirectory(), "test.mp3"));
    private int SPACE = 1500;// 间隔取样时间
    private int volume = 2;//默认音量为2

    private int dbf; // wave view显示需要的振幅

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg != null) {
                dbf =  msg.what;

                Log.i(TAG, "dbf: " + dbf);

                helper = new VolumeWaveHelper(waveView, 0f, 1f, 0.5f, 0.5f, dbf / 500 + 0.05f, dbf / 800 + 0.05f);
                helper.start();
            }

        }
    };


    private Runnable updateUI = new Runnable() {
        @Override
        public void run() {

            handler.postDelayed(this, SPACE);

//            volume = mRecorder.getRealVolume();
            volume = mRecorder.getVolume();

            Message msg = Message.obtain();
            msg.what = volume;
            handler.sendMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mp3_recorder);

        waveView = (VolumeWaveView) findViewById(R.id.mp3_recorder_volume_wave);
        start = (Button) findViewById(R.id.mp3_recorder_start);
        stop = (Button) findViewById(R.id.mp3_recorder_stop);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mRecorder.start();

                    handler.postDelayed(updateUI, SPACE);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecorder.stop();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRecorder != null) {
            mRecorder.stop();
        }
    }
}

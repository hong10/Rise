package com.hong.rise;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

/**
 * Created by Administrator on 2016/6/21.
 */
public class BaseRecorderActivity extends Activity {
    private static final String LOG_TAG = "AudioRecordTest";
    //语音文件保存路径
    private String fileName = null;

    //界面控件
    private Button startRecord;
    private Button startPlay;
    private Button stopRecord;
    private Button stopPlay;

    //语音操作对象
    private MediaPlayer mPlayer = null;
    private MediaRecorder mRecorder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base_recorder);

        //set file path
        fileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        fileName += "/audiorecordtest.3gp";

        //开始录音
        startRecord = (Button) findViewById(R.id.startRecord);
        startRecord.setText(R.string.startRecord);
        //绑定监听器
        startRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG, "start recorder");
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setOutputFile(fileName);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                try {
                    mRecorder.prepare();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "prepare() failed");
                    e.printStackTrace();
                }

                mRecorder.start();

            }
        });

        //结束录音
        stopRecord = (Button) findViewById(R.id.stopRecord);
        stopRecord.setText(R.string.stopRecord);
        stopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG, "stop recorder");
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
            }
        });

        //开始播放
        startPlay = (Button) findViewById(R.id.startPlay);
        startPlay.setText(R.string.startPlay);
        //绑定监听器
        startPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG, "start play");
                mPlayer = new MediaPlayer();

                try {
                    mPlayer.setDataSource(fileName);
                    mPlayer.prepare();
                    mPlayer.start();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "play failed");
                    e.printStackTrace();
                }


            }
        });


        //结束播放
        stopPlay = (Button) findViewById(R.id.stopPlay);
        stopPlay.setText(R.string.stopPlay);
        stopPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG, "stop play");
                mPlayer.release();
                mPlayer = null;
            }
        });
    }
}

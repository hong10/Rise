package com.hong.rise;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hong.rise.view.VolumeWaveHelper;
import com.hong.rise.view.VolumeWaveView;

import java.io.IOException;

/**
 * Created by hong on 2016/6/26.
 */

public class GetVolumeDemoActivity extends Activity {
    private static final String TAG = "GetVolumeDemoActivity";
    private VolumeWaveHelper helper;
    private VolumeWaveView waveView;
    private double db;
    private float dbf;


    private MediaRecorder mMediaRecorder;
    public static final int MAX_LENGTH = 1000 * 60 * 10;// 最大录音时长1000*60*10;
    private String filePath = "/dev/null";
    private long startTime;
    private long endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_get_volume);
        waveView = (VolumeWaveView) findViewById(R.id.volume_wave);




//        helper = new VolumeWaveHelper(waveView,0f,1f,0.5f,0.5f,dbf,dbf);
//        helper = new VolumeWaveHelper(waveView,0f,1f,0.5f,0.5f,0.1f,0.1f);
//        helper = new VolumeWaveHelper(waveView);


    }

    @Override
    protected void onResume() {
        super.onResume();
        startRecord();
//        dbf = Float.parseFloat(db+"");
//        Log.i("HONGGGGGGGGGGGGGGGGGGG", db + "");


    }

    @Override
    protected void onPause() {
        super.onPause();
        helper.cancel();
       stopRecord();
    }


    /**
     * 开始录音 使用amr格式
     *
     *            录音文件
     * @return
     */
    public void startRecord() {
        // 开始录音
        /* ①Initial：实例化MediaRecorder对象 */
        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();
        try {
            /* ②setAudioSource/setVedioSource */
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
            /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                        /*
             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            /* ③准备 */
            mMediaRecorder.setOutputFile(filePath);
            mMediaRecorder.setMaxDuration(MAX_LENGTH);
            mMediaRecorder.prepare();
            /* ④开始 */
            mMediaRecorder.start();
            // AudioRecord audioRecord.
            /* 获取开始时间* */
            startTime = System.currentTimeMillis();


            updateMicStatus();
//            mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);


            Log.i("ACTION_START", "startTime" + startTime);


        } catch (IllegalStateException e) {
            Log.i(TAG,
                    "call startAmr(File mRecAudioFile) failed!"
                            + e.getMessage());

        } catch (IOException e) {
            Log.i(TAG,
                    "call startAmr(File mRecAudioFile) failed!"
                            + e.getMessage());
        }
    }

    /**
     * 停止录音
     *
     */
    public long stopRecord() {
        if (mMediaRecorder == null)
            return 0L;
        endTime = System.currentTimeMillis();
        Log.i("ACTION_END", "endTime" + endTime);
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mMediaRecorder = null;
        Log.i("ACTION_LENGTH", "Time" + (endTime - startTime));
        return endTime - startTime;
    }

    private  Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            dbf = (Float) msg.obj;
            
            Log.i(TAG, "dbf: " + dbf);

            helper = new VolumeWaveHelper(waveView,0f,1f,0.5f,0.5f,dbf/800+0.02f,dbf/800+0.02f);
            helper.start();


        }
    };
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {

            updateMicStatus();
            Message msg = Message.obtain();


                msg.obj = dbf;

                mHandler.sendMessage(msg);




        }
    };

    /**
     * 更新话筒状态
     *
     */
    private int BASE = 600;
    private int SPACE = 1000;// 间隔取样时间

//    private void updateMicStatus1() {
//        if (mMediaRecorder != null) {
//            double ratio = (double)mMediaRecorder.getMaxAmplitude() /BASE;
//            double db = 0;// 分贝
//            if (ratio > 1)
//                db = 20 * Math.log10(ratio);
//            Log.d(TAG,"分贝值："+db);
//            mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
//        }
//    }

    private void updateMicStatus() {


        if (mMediaRecorder != null) {
            double ratio = (double)mMediaRecorder.getMaxAmplitude() /BASE;
            double db = 0;// 分贝
            if (ratio > 1)
                db = 20 * Math.log10(ratio);
            Log.d(TAG,"分贝值："+db);

            dbf = Float.parseFloat(db+"");

            Log.d(TAG,"分贝值f："+dbf);




            mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);

        }
    }


}

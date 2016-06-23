package com.hong.rise;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hong.rise.view.SineWave;

import java.util.Random;

/**
 * Created by Administrator on 2016/6/23.
 */
public class SinWaveDemoActivity extends Activity {
    private TextView frequency = null;

    private TextView phase = null;

    private TextView amplifier = null;

    private Button btnwave = null;

    private SineWave sw = null;
    private Runnable runnable = runnable = new Runnable() {
        @Override
        public void run() {

            Message msg = Message.obtain();
            msg.what = 0;
            handler.sendMessage(msg);
        }
    };


    private Handler handler = new Handler() {
        Random r = new Random();

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    float f = r.nextInt(10) + 1;
                    float p = r.nextInt(90) + 1;
                    float a = r.nextInt(300) + 1;

                    frequency.setText(String.valueOf(f));
                    phase.setText(String.valueOf(p));
                    amplifier.setText(String.valueOf(a));

                    sw.Set(a, f, p);

                    handler.post(runnable);
                    break;
                case 1:
                    handler.removeCallbacks(runnable);
                    runnable = null;
                    break;
            }


        }
    };


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        sw = new SineWave(this);

        setContentView(R.layout.activity_sin_wave);


        btnwave = (Button) findViewById(R.id.wave);

        frequency = (TextView) findViewById(R.id.frequency);

        phase = (TextView) findViewById(R.id.phase);

        amplifier = (TextView) findViewById(R.id.amplifier);


        handler.post(runnable);


        btnwave.setOnClickListener(new Button.OnClickListener() {


            @Override

            public void onClick(View arg0) {

                // TODO Auto-generated method stub

                sw.Set(Float.parseFloat(amplifier.getText().toString()), Float.parseFloat(frequency.getText().toString()), Float.parseFloat(phase.getText().toString()));

                Message msg = Message.obtain();
                msg.what = 1;
                handler.sendMessage(msg);

            }

        });


    }


    @Override

    protected void onStart() {

        // TODO Auto-generated method stub

        super.onStart();

        frequency.setText(Float.toString(sw.GetFrequency()));

        phase.setText(Float.toString(sw.GetPhase()));

        amplifier.setText(Float.toString(sw.GetAmplifier()));

    }


    @Override

    public boolean onTouchEvent(MotionEvent event) {

        // TODO Auto-generated method stub

        //float px = event.getX();

        //float py = event.getY();

        //sw.SetXY(px, py);

        return super.onTouchEvent(event);

    }

}

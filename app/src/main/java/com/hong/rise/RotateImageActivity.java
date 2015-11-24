package com.hong.rise;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class RotateImageActivity extends Activity implements OnSeekBarChangeListener {

    private ImageView ivRotate;
    private TextView tvSize, tvRotate;
    private SeekBar sbSize, sbRotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotate_image);

        ivRotate = (ImageView) findViewById(R.id.iv_rotate);
        tvSize = (TextView) findViewById(R.id.tv_size);
        tvRotate = (TextView) findViewById(R.id.tv_rotate);

        sbSize = (SeekBar) findViewById(R.id.sb_size);
        sbRotate = (SeekBar) findViewById(R.id.sb_rotate);




    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}

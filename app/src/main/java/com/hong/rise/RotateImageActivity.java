package com.hong.rise;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class RotateImageActivity extends Activity implements OnSeekBarChangeListener {

    private int minSize = 80;
    private ImageView ivRotate;
    private TextView tvSize, tvRotate;
    private SeekBar sbSize, sbRotate;
    private Matrix matrix = new Matrix();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotate_image);

        ivRotate = (ImageView) findViewById(R.id.iv_rotate);
        tvSize = (TextView) findViewById(R.id.tv_size);
        tvRotate = (TextView) findViewById(R.id.tv_rotate);

        sbSize = (SeekBar) findViewById(R.id.sb_size);
        sbRotate = (SeekBar) findViewById(R.id.sb_rotate);


        //Get screen size, and limit the pic max size, can't overstep the screen size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        sbSize.setMax(dm.widthPixels - minSize);

        sbSize.setOnSeekBarChangeListener(this);
        sbRotate.setOnSeekBarChangeListener(this);


    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (seekBar.getId() == R.id.sb_size) {
            //set pic size
            int newWidth = progress + minSize;
            int newHeight = (int) (newWidth * 3 / 4);
            ivRotate.setLayoutParams(new LinearLayout.LayoutParams(newWidth, newHeight));
            tvSize.setText("width: " + newWidth + ", height: " + newHeight);

        } else if (seekBar.getId() == R.id.sb_rotate) {
            //Get the rotate pic
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.young);
            //set rotate angle
            matrix.setRotate(progress, 30, 60);
            //After rotate, generate new pic
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            //bind new pic to seekbar
            ivRotate.setImageBitmap(bitmap);
            tvRotate.setText(progress + "°");

        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}

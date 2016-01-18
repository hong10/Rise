package com.hong.rise;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hong.rise.utils.HttpUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class LoadPicFromNetActivity extends AppCompatActivity {
    private Button btLoad;
    private TextView tvLoad;
    private ImageView ivNetPic;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bitmap bitmap = null;
            if (msg.what == 0) {
                bitmap = (Bitmap) msg.obj;
                tvLoad.setText("使用obj传递数据");
            } else {
                Bundle bundle = msg.getData();
                bitmap = (Bitmap) bundle.get("bmp");
                tvLoad.setText("使用Bundle传递数据");

            }

            //Set imageView
            ivNetPic.setImageBitmap(bitmap);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_pic_from_net);

        btLoad = (Button) findViewById(R.id.bt_load_pic);
        tvLoad = (TextView) findViewById(R.id.tv_load);
        ivNetPic = (ImageView) findViewById(R.id.iv_netpic);

        btLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Clear text
                tvLoad.setText("");
                ivNetPic.setImageBitmap(null);

                //Create thread
                new Thread() {

                    @Override
                    public void run() {
                        // Get net pic
                        try {
                            InputStream is = HttpUtils.getImageViewInputStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(is);
                            Message msg = new Message();
                            Random random = new Random();
                            int id = random.nextInt(10);
                            if (id / 2 == 0) {
                                msg.what = 0;
                                msg.obj = bitmap;
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("bmp", bitmap);
                                msg.what = 1;
                                msg.setData(bundle);

                            }

                            //Send message
                            handler.sendMessage(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                }.start();


            }
        });
    }
}

package com.hong.rise;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class LoadImageDemo extends AppCompatActivity {

    private ImageView ivDemo;
    //    private String imageUrl = "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg";
    private String imageUrl = "http://d.hiphotos.baidu.com/news/q%3D100/sign=99fd27020bd79123e6e090749d355917/9825bc315c6034a80da2cf8bcc13495409237673.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_image_demo);

        ivDemo = (ImageView) findViewById(R.id.iv_image_demo);

        //显示图片的配置


        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader.getInstance().displayImage(imageUrl, ivDemo, options, new SimpleImageLoadingListener(), new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String s, View view, int i, int i1) {

            }
        });

    }
}

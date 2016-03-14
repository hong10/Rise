package com.hong.rise;

import android.app.Application;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Ashton on 2016/1/21.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        /**
         //创建默认的ImageLoader配置参数
         ImageLoaderConfiguration configuration = ImageLoaderConfiguration
         .createDefault(this);
         */

        //可以打印log的初始化
        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
                .Builder(this)
                .writeDebugLogs()
                .build();

        //Initialize ImageLoader with configuration
        ImageLoader.getInstance().init(configuration);


    }
}

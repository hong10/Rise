package com.hong.rise.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ashton on 2015/12/7.
 */
public class HttpUtils {
    private final static String URL_PATH = "http://ww4.sinaimg.cn/bmiddle/9e58dccejw1e6xow22oc6j20c80gyaav.jpg";


    public static InputStream getImageViewInputStream() throws IOException {
        InputStream inputStream = null;
        URL url = new URL(URL_PATH);

        if (url != null) {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(1000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                inputStream = httpURLConnection.getInputStream();
            }
        }

        return inputStream;
    }
}

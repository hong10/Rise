package com.hong.rise.utils;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.hong.rise.lottery.GlobalParams;

/**
 * Created by Administrator on 2016/3/24.
 */
public class NetUtil {

    public static boolean checkNet(Context context) {
        //是否是wifi
        boolean isWifi = isWifiConnection(context);
        //是否是Mobile网络
        boolean isMobile = isMobileConnection(context);

        if (isMobile) {
            //APN被选中，代理设置中是否有内容，如果有就是wap
            readAPN(context);//判断哪个APN被选中
        }

        if (!isWifi && !isMobile) {
            return false;
        }
        return true;
    }

    private static void readAPN(Context context) {
        // content://telephony/carriers/preferapn
        Uri PREFERAPN_APN_URI = Uri.parse("content://telephony/carriers/preferapn");

        //操作联系人类似
        ContentResolver resolver = context.getContentResolver();
        //判断是哪个APN被选中了
        Cursor cursor = resolver.query(PREFERAPN_APN_URI, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            GlobalParams.PROXY = cursor.getString(cursor.getColumnIndex("proxy"));
            GlobalParams.PORT = cursor.getInt(cursor.getColumnIndex("port"));
        }

    }

    private static boolean isMobileConnection(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (networkInfo != null) {
            return networkInfo.isConnected();
        }
        return false;


    }

    private static boolean isWifiConnection(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (networkInfo != null) {
            return networkInfo.isConnected();
        }
        return false;
    }
}

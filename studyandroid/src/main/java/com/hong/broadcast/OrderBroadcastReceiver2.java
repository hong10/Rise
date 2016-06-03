package com.hong.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Administrator on 2016/5/30.
 */
public class OrderBroadcastReceiver2 extends BroadcastReceiver {
    private static final String TAG = OrderBroadcastReceiver2.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
//        String orderBroadcast2 = intent.getStringExtra("order");
//        String update = intent.getStringExtra("1");

        //是否接受上一个广播接收者修改后的数据
        Bundle bundle = getResultExtras(true);
        String b = bundle.getString("b");
        String a = bundle.getString("a");
        Log.i(TAG, a + " : " + b);
//        Log.i(TAG, update);
    }
}

package com.hong.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Administrator on 2016/5/30.
 */
public class OrderBroadcastReceiver1 extends BroadcastReceiver {

    private static final String TAG = OrderBroadcastReceiver1.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String orderBroadcast1 = intent.getExtras().getString("a");

        Bundle bundle = intent.getExtras();
        bundle.putString("b", "bbb");
        setResultExtras(bundle);

        Log.i(TAG, orderBroadcast1);

    }
}

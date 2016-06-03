package com.hong.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Administrator on 2016/5/30.
 */
public class CommonBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = CommonBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        String broadcast = intent.getStringExtra("common");
        Log.i(TAG, broadcast);
    }
}

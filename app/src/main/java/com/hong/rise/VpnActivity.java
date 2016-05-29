package com.hong.rise;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

/**
 * Created by hong on 2016/5/5.
 */
public class VpnActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        setContentView(R.layout.activity_vpn);
    }

    public void launchVpn(View view) {

    }
}

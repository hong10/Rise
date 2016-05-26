package com.hong.studyandroid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/5/23.
 */
public class ActivityB extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView tv = new TextView(this);
        tv.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        tv.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        tv.setTextColor(Color.RED);

        setContentView(tv);

//        String data = getIntent().getStringExtra("data");
//        Bundle bundle = getIntent().getExtras();
//        String data = bundle.getString("data");
//
//        tv.setText(data);


        //- - - - - - - - - - -
        Intent intentData = new Intent();
        intentData.putExtra("back", "返回的数据");
        setResult(200, intentData);


    }
}

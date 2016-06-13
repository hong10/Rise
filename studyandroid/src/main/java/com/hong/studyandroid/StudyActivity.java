package com.hong.studyandroid;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Administrator on 2016/5/23.
 */
public class StudyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_studyactivity);
    }

    public void transferData(View view) {
        Intent intent = new Intent(this, TransferActivityA.class);
        startActivity(intent);
    }

    public void startSystemApp(View view) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SENDTO");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("sms:10086"));
        startActivity(intent);
    }

    public void lifeCycle(View view) {
        Intent intent = new Intent(this, LifeCycle.class);
        startActivity(intent);
    }

}
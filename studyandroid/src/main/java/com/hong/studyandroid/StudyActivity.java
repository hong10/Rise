package com.hong.studyandroid;

import android.app.Activity;
import android.content.Intent;
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

}

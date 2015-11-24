package com.tencent.cloud.demo.fragment.fclouldfragement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.tencent.cloud.demo.R;

/**
 * Created by redickran on 2015/5/8.
 */
public class StatActivity extends Activity {
    public static final String KEY_STAT_RESULT = "stat_result";
    private TextView mEditTextStat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);

        initialization();
    }

    private void initialization() {
        mEditTextStat = (TextView) findViewById(R.id.textViewStat);

        Intent intent = getIntent();
        String result = intent.getStringExtra(KEY_STAT_RESULT);
        mEditTextStat.setText(result);
    }
}
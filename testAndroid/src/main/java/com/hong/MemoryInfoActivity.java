package com.hong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by hong on 2016/7/11.
 */

public class MemoryInfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meminfo_test);

    }

    public void memoryLeak(View view) {
        EditText etNum = (EditText) findViewById(R.id.et_num);
        Intent intent = new Intent(this, MemoryLeakDemoActivity.class);
        if (!TextUtils.isEmpty(etNum.getText().toString().trim())) {

            intent.putExtra("num", etNum.getText().toString().trim());
            startActivity(intent);
        }else {
            Toast.makeText(this, "请输入系数", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * memory out demo
     *
     * @param view
     */
    public void memoryOut(View view) {
        /*ArrayList<String> list = new ArrayList<String>();
        while (true)
            list.add("a");*/
//        byte[] i = new byte[90 * 1024 * 1024];
        byte[] i = new byte[300 * 1024 * 1024];

    }
}

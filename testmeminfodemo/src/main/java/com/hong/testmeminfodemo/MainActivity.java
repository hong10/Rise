package com.hong.testmeminfodemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    private EditText etNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etNum = (EditText) findViewById(R.id.et_num);
    }

    /**
     * memory Leak demo
     *
     * @param view
     */
    public void memoryLeak(View view) {
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
        byte[] i = new byte[90 * 1024 * 1024];

    }


}

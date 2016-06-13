package com.hong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void testService(View view) {

//        System.out.println("test service");

        Intent intent = new Intent(this, TestServiceActivity.class);
        startActivity(intent);

    }

    public void testRemoteService(View view) {

        Intent intent = new Intent(this, TestRemoteServiceActivity.class);
        startActivity(intent);

    }


    /**
     * memory Leak demo
     *
     * @param view
     */
    public void testMemoryLeak(View view) {
        Toast.makeText(this, "待补充", Toast.LENGTH_LONG).show();
//        EditText etNum = (EditText) findViewById(R.id.et_num);
//        Intent intent = new Intent(this, MemoryLeakDemoActivity.class);
//        if (!TextUtils.isEmpty(etNum.getText().toString().trim())) {
//
//            intent.putExtra("num", etNum.getText().toString().trim());
//            startActivity(intent);
//        }else {
//            Toast.makeText(this, "请输入系数", Toast.LENGTH_SHORT).show();
//        }
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

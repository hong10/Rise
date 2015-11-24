package com.hong.testmeminfodemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MemoryLeakDemoActivity extends AppCompatActivity {
    private static final String TAG = "MemoryLeakDemoActivity";
    private EditText etInput;
    private List<byte[]> list = new ArrayList<byte[]>();
    private int num;
    private int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_leak_demo);
        etInput = (EditText) findViewById(R.id.et_input);

        num = Integer.parseInt(getIntent().getStringExtra("num"));
        Log.i(TAG, num + "");

        etInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //1M需要点击42次就crash
                //2M需要点击22次就crash
                //5M需要点击8次就crash
                //10M需要点击5次就crash
                //list.clear();
                Toast.makeText(getApplicationContext(), "" + count++, Toast.LENGTH_SHORT).show();
                list.add(new byte[num * 1024 * 1024]);

                return false;
            }
        });

    }


    @Override
    public void finish() {
        super.finish();
        count = 1;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void send(View view) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        //if (list != null) {
//            list.clear();
//            Toast.makeText(this,""+list.size(),Toast.LENGTH_LONG).show();
//            System.out.print("list大小"+list.size());
        //}

    }
}

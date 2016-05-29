package com.hong.studyandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/5/23.
 */
public class TransferActivityA extends Activity {
    private TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_data_a);

        tv_result = (TextView) findViewById(R.id.tv_result);
    }

    public void openB(View view) {
        Intent intent = new Intent(this, ActivityB.class);
//        intent.putExtra("data", "通过putExtra传递数据-->来自A的数据");
        Bundle bundle = new Bundle();
        bundle.putString("data", "通过Bundle传递数据-->来自A的数据");
        intent.putExtras(bundle);
        startActivity(intent);
    }


    public void getBackData(View view) {
        Intent intent = new Intent(this, ActivityB.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            String str = data.getStringExtra("back");
            tv_result.setText(str);
        }

    }
}

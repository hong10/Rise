package com.hong.studyandroid;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/6/1.
 */
public class HandlerDemo2Activity extends Activity {
    private Button bt;
    private TextView tv;
    private ProgressBar pb;
    private RelativeLayout rl;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            rl.setVisibility(View.GONE);
            Bundle data = msg.getData();

            String dataStr = data.getString("a") + "\n" + data.getString("b");
            tv.setText(dataStr);


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_handlerdemo2);

        bt = (Button) findViewById(R.id.bt_get_data);
        tv = (TextView) findViewById(R.id.tv_get_data);
        pb = (ProgressBar) findViewById(R.id.pb_handler_demo2);
        rl = (RelativeLayout) findViewById(R.id.rl_handler_demo2);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rl.setVisibility(View.VISIBLE);
                Message msg = handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("a", "aa");
                bundle.putString("b", "bb");
                msg.setData(bundle);
                handler.sendMessageDelayed(msg, 3000);
            }
        });

    }


}

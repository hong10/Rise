package com.hong.rise;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.hong.rise.lottery.view.manager.BottomManager;
import com.hong.rise.lottery.view.manager.TitleManager;
import com.hong.rise.lottery.view.manager.view.FirstUI;
import com.hong.rise.lottery.view.manager.view.SecondUI;

public class LotteryLaunchActivity extends AppCompatActivity {

    private RelativeLayout middle;

    private Handler handler = new Handler(){


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            loadSecondUI();
        }
    };

    private void loadSecondUI() {
        SecondUI secondUI = new SecondUI(this);
        middle.addView(secondUI.getChild());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.il_main);

        init();
    }

    private void init() {
        TitleManager titleManager = TitleManager.getInstance();
        titleManager.init(this);
        titleManager.showUnLoginTitle();

        BottomManager bottomManager = BottomManager.getInstrance();
        bottomManager.init(this);
        bottomManager.showCommonBottom();

        middle = (RelativeLayout) findViewById(R.id.ii_middle);

        //显示第一给界面
        loadFirstUI();

        //延迟2秒加载第二界面
        handler.sendEmptyMessageDelayed(0, 2000);


    }

    private void loadFirstUI() {
        FirstUI firstUI = new FirstUI(this);
        middle.addView(firstUI.getChild());
    }


}

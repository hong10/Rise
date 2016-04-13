package com.hong.rise;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hong.rise.lottery.view.manager.BottomManager;
import com.hong.rise.lottery.view.manager.TitleManager;

public class LotteryLaunchActivity extends AppCompatActivity {

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

    }

}

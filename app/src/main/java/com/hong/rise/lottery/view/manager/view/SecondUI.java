package com.hong.rise.lottery.view.manager.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hong.rise.lottery.ConstantValue;
import com.hong.rise.lottery.view.manager.BaseUI;

/**
 * Created by hong on 2016/4/16.
 */
public class SecondUI extends BaseUI {
    private TextView textView;


    public SecondUI(Context context) {
        super(context);
        init();
    }

    /**
     * 由于第二个界面只初始化了一次，所以init()方法只会调用一次；不用每次切换界面都会初始化界面
     */
    public void init() {

        textView = new TextView(context);

        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(layoutParams);

        textView.setBackgroundColor(Color.RED);
        textView.setText("这是第二个界面");


    }

    @Override
    public void setOnClickListener() {

    }

    @Override
    public View getChild() {

        return textView;
    }

    @Override
    public int getID() {
        return ConstantValue.SECOND_VIEW;
    }

    @Override
    public void onClick(View v) {

    }
}

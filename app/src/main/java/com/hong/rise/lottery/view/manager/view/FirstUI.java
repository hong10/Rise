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
public class FirstUI extends BaseUI {


    public FirstUI(Context context) {
        super(context);
    }

    @Override
    public void init() {

    }

    @Override
    public void setOnClickListener() {

    }

    @Override
    public View getChild() {

        TextView textView = new TextView(context);

        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(layoutParams);

        textView.setBackgroundColor(Color.BLUE);
        textView.setText("这是第一个界面");


        return textView;
    }

    @Override
    public int getID() {
        return ConstantValue.FRIST_VIEW;
    }

    @Override
    public void onClick(View v) {

    }
}

package com.hong.rise.lottery.view.manager.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hong.rise.lottery.view.manager.BaseUI;

/**
 * Created by hong on 2016/4/16.
 */
public class SecondUI extends BaseUI{


    public SecondUI(Context context) {
        super(context);
    }

    @Override
    public View getChild() {

        TextView textView = new TextView(context);

        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(layoutParams);

        textView.setBackgroundColor(Color.RED);
        textView.setText("这是第二个界面");


        return textView;
    }
}

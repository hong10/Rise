package com.hong.rise.lottery.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hong.rise.R;
import com.hong.rise.lottery.ConstantValue;
import com.hong.rise.lottery.view.manager.BaseUI;

/**
 * Created by hong on 2016/4/24.
 */
public class Hall extends BaseUI {

    //第一步：加载layout(布局参数设置)
    //第二步：初始化layout中控件
    //第三步：设置监听

    private TextView ssqIssue;
    private ImageView ssqBet;

    public Hall(Context context) {
        super(context);

    }

    public void setOnClickListener() {
        ssqBet.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {

    }

    public void init() {
        showInMiddle = (LinearLayout) View.inflate(context, R.layout.il_hall, null);
        ssqIssue = (TextView) findViewById(R.id.ii_hall_ssq_summary);
        ssqBet = (ImageView) findViewById(R.id.ii_hall_ssq_bet);
    }
    @Override
    public int getID() {
        return ConstantValue.VIEW_HALL;
    }


}

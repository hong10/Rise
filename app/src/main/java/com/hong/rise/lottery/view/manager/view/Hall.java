package com.hong.rise.lottery.view.manager.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hong.rise.R;
import com.hong.rise.lottery.ConstantValue;
import com.hong.rise.lottery.view.manager.BaseUI;

/**
 * Created by hong on 2016/4/24.
 */
public class Hall extends BaseUI {
    private LinearLayout hall;

    public Hall(Context context) {
        super(context);
        init();
    }

    private void init() {
        hall = (LinearLayout) View.inflate(context, R.layout.il_hall, null);

        //判断控件参数是否为null
        if (hall.getLayoutParams() == null) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            hall.setLayoutParams(params);
        }
    }

    @Override
    public View getChild() {
        return hall;
    }

    @Override
    public int getID() {
        return ConstantValue.VIEW_HALL;
    }
}

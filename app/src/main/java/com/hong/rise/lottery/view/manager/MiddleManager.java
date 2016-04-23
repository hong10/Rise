package com.hong.rise.lottery.view.manager;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.hong.rise.R;

/**
 * Created by hong on 2016/4/23.
 */
public class MiddleManager {

    private static MiddleManager instance = null;

    private MiddleManager(){}

    public static MiddleManager getInstance() {
        if (instance == null) {
            instance = new MiddleManager();
        }
        return instance;
    }

    private RelativeLayout middle;

    public void setMiddle(RelativeLayout middle) {
        this.middle = middle;
    }

    /**
     * 切换界面的通用方法
     *
     * @param ui 要切换的目标界面
     */
    public void changeUI(BaseUI ui) {
        middle.removeAllViews();
        View child = ui.getChild();
        middle.addView(child);
        child.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.second_view_in_change));
    }

    public Context getContext() {
        return middle.getContext();
    }
}

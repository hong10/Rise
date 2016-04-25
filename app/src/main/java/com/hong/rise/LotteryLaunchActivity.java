package com.hong.rise;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.hong.rise.lottery.view.FirstUI;
import com.hong.rise.lottery.view.Hall1;
import com.hong.rise.lottery.view.SecondUI;
import com.hong.rise.lottery.view.manager.BaseUI;
import com.hong.rise.lottery.view.manager.BottomManager;
import com.hong.rise.lottery.view.manager.MiddleManager;
import com.hong.rise.lottery.view.manager.TitleManager;
import com.hong.rise.utils.PromptManager;

public class LotteryLaunchActivity extends Activity {

    private RelativeLayout middle;

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


        //不使用handler,任意点击按钮切换
        MiddleManager middleManager = MiddleManager.getInstance();
        middleManager.setMiddle(middle);

        //建立观察者和被观察者之间的关系（在activty中标题和底部导航添加到观察者的容器里面）
        MiddleManager.getInstance().addObserver(TitleManager.getInstance());
        MiddleManager.getInstance().addObserver(BottomManager.getInstrance());

        middleManager.changeUI(Hall1.class);
//        middleManager.changeUI(FirstUI.class);

/*        //显示第一给界面
        loadFirstUI();

        //延迟2秒加载第二界面
        handler.sendEmptyMessageDelayed(0, 3000);
        handler.sendEmptyMessage(0);*/



    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            boolean result = MiddleManager.getInstance().goBack();
            if (!result) {
                //此时栈中只有一个界面，弹出toast提示
//                Toast.makeText(this, "是否退出应用", Toast.LENGTH_LONG).show();
                PromptManager.showExitSystem(this);
            }
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    //**************  以下是演示界面切换的方法  **********************

    private Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            loadSecondUI();
//            changeUI();
            changeUI(new SecondUI(LotteryLaunchActivity.this));
        }

    };

    private View child1;

    private void loadFirstUI() {
        FirstUI firstUI = new FirstUI(this);
        child1 = firstUI.getChild();
        middle.addView(child1);
//        child1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.first_view_out_change));//这个行代码有bug，第一个界面会显示两次；
        // 解决办法：需要加一个动画完成的监听，当动画完成后，清楚当前界面的view

    }


    private void loadSecondUI() {
        SecondUI secondUI = new SecondUI(this);
        View child = secondUI.getChild();
        middle.addView(child);

        child.startAnimation(AnimationUtils.loadAnimation(this, R.anim.second_view_in_change));
//        FadeUtil.fadeIn(child, 2000, 1000);
    }

    /**
     * 切换界面的通用方法
     *
     * @param ui 要切换的目标界面
     */
    private void changeUI(BaseUI ui) {
        middle.removeAllViews();
        View child = ui.getChild();
        middle.addView(child);
        child.startAnimation(AnimationUtils.loadAnimation(this, R.anim.second_view_in_change));
    }


    /**
     * 最基本切换界面的方法
     */
    private void changeUI() {
//        FadeUtil.fadeOut(child1, 2000);
        middle.removeAllViews();
        loadSecondUI();

    }


}

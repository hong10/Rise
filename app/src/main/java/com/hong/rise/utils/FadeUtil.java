package com.hong.rise.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * 淡入淡出工具类
 * Created by hong on 2016/4/23.
 */
public class FadeUtil {

//    private static Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            View view = (View) msg.obj;
//            ViewGroup parent = (ViewGroup) view.getParent();
//            parent.removeView(view);
//        }
//    };
    /*
    第一个动画：淡出
    第二个动画：淡入

     第一个动画执行需要时间
     第一个动画执行时，第二个动画要等待
     同时第二个动画也要有执行时间
     */

    /**
     * 淡出
     *
     * @param view     控件
     * @param duration 动画要执行的时间
     */
    public static void fadeOut(final View view, long duration) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //模拟器有问题，会crash
                ViewGroup parent = (ViewGroup) view.getParent();
                parent.removeView(view);
//                Message msg = Message.obtain();
//                msg.obj = view;
//                handler.sendMessage(msg);
            }


            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.setAnimation(alphaAnimation);

    }

    /**
     * 淡入
     *
     * @param view     控件
     * @param delay    等待第一个动画执行完成
     * @param duration 动画要执行的时间
     */

    public static void fadeIn(View view, long delay, long duration) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setStartOffset(delay);

        alphaAnimation.setDuration(duration);
        view.setAnimation(alphaAnimation);
    }

}

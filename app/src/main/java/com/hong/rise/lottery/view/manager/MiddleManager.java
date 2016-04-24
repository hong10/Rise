package com.hong.rise.lottery.view.manager;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.hong.rise.R;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by hong on 2016/4/23.
 */
public class MiddleManager {

    private static final String TAG = "MiddleManager";
    private static MiddleManager instance = null;
    private Map<String, BaseUI> VIEWCACHE = new HashMap<String, BaseUI>();//key:唯一的标示BaseUI的子类
    private BaseUI currentUI;
    private LinkedList<String> HISTOYR = new LinkedList<String>();

    public BaseUI getCurrentUI() {
        return currentUI;
    }

    private MiddleManager() {
    }

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


    public void changeUI(Class<? extends BaseUI> targetClazz) {
        // 判断：当前正在展示的界面和切换目标界面是否相同
        if (currentUI != null && currentUI.getClass() == targetClazz) {
            return;
        }

        BaseUI targetUI = null;

        //判断之前有没有创建过——曾经创建过的界面要保存到cache
        String key = targetClazz.getSimpleName();
        if (VIEWCACHE.containsKey(key)) {
            //创建了就重用
            targetUI = VIEWCACHE.get(key);
        } else {
            //没有创建，就创建
            try {
                Constructor<? extends BaseUI> constructor = targetClazz.getConstructor(Context.class);
                targetUI = constructor.newInstance(getContext());
                VIEWCACHE.put(key, targetUI);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("constructor new instance error");
            }
        }

        Log.i(TAG, targetUI.toString());
        middle.removeAllViews();
        View child = targetUI.getChild();
        middle.addView(child);
        child.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.second_view_in_change));

        currentUI = targetUI;
        //将当前界面存入HISTORY栈中
        HISTOYR.addFirst(key);
    }

    /**
     * 切换界面的通用方法
     *
     * @param ui 要切换的目标界面
     */
    public void changeUI1(BaseUI ui) {
        middle.removeAllViews();
        View child = ui.getChild();
        middle.addView(child);
        child.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.second_view_in_change));
    }

    public Context getContext() {
        return middle.getContext();
    }

    /**
     * 返回键的处理（模拟栈）
     *
     * @return true代表返回操作成功，false代表当前栈中只有一个界面
     */
    public boolean goBack() {

        //当栈中只有一个界面时，直接返回false，不进行remove操作
        if (HISTOYR.size() == 1) {
            return false;
        }

        //点击back后，删除栈顶的界面；同时获取删除后，栈顶的界面
        if (HISTOYR.size() > 0) {
            System.out.println("1");

            //点击back后，删除栈顶的界面
            HISTOYR.removeFirst();

            if (HISTOYR.size() > 0) {
                System.out.println("2");

                //获取当前栈顶的界面
                String first = HISTOYR.getFirst();
                BaseUI targetUI = VIEWCACHE.get(first);

                //切换界面的核心方法
                middle.removeAllViews();
                middle.addView(targetUI.getChild());
                currentUI = targetUI;
                return true;
            }

        }

        return false;
    }
}

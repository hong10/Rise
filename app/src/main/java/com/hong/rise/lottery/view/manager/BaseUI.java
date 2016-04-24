package com.hong.rise.lottery.view.manager;

import android.content.Context;
import android.view.View;

/**
 * 所有界面的基类
 * Created by hong on 2016/4/16.
 */
public abstract class BaseUI {
    protected Context context;

    public BaseUI(Context context) {
        this.context = context;
    }

    /**
     *获取要在中间容器中加载的内容
     * @return
     */
    public abstract View getChild();

    /**
     * 获取每个节目唯一标示——作用：当容器联动时，作为比对的依据
     * @return
     */
    public abstract int getID();



}

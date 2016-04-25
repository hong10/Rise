package com.hong.rise.lottery.view.manager;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;

import com.hong.rise.lottery.net.protocal.Message;
import com.hong.rise.utils.NetUtil;
import com.hong.rise.utils.PromptManager;

/**
 * 所有界面的基类
 * Created by hong on 2016/4/16.
 */
public abstract class BaseUI implements View.OnClickListener {
    protected Context context;
    protected ViewGroup showInMiddle;

    public BaseUI(Context context) {
        this.context = context;
        init();
        setOnClickListener();
    }

    /**
     * 初始化控件
     *
     * @return
     */
    public abstract void init();

    /**
     * 设置控件的监听
     *
     * @return
     */
    public abstract void setOnClickListener();

    /**
     * 获取每个节目唯一标示——作用：当容器联动时，作为比对的依据
     *
     * @return
     */
    public abstract int getID();

    /**
     * 获取要在中间容器中加载的内容
     *
     * @return
     */
    public View getChild() {

        //设置参数
        //判断控件参数是否为null
        if (showInMiddle.getLayoutParams() == null) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            showInMiddle.setLayoutParams(params);
        }

        return showInMiddle;
    }
//    public abstract View getChild();

    public View findViewById(int id) {
        return showInMiddle.findViewById(id);
    }


    /**
     * 继承AsyncTask这个类时，需要重写doInBackground()方法；如果不重写该方法，就要将这个类用abstract修饰
     * @param <Params>
     */
    protected abstract class MyHttpTask<Params> extends AsyncTask<Params, Void, Message> {

        /**
         * 类似与Thread.start方法 由于final修饰，无法Override，方法重命名 省略掉网络判断
         *
         * @param params
         * @return
         */

        public final AsyncTask<Params, Void, Message> executeProxy(Params... params) {
            if (NetUtil.checkNet(context)) {
                return super.execute(params);
            } else {
                PromptManager.showNoNetWork(context);
            }
            return null;

        }

    }

}

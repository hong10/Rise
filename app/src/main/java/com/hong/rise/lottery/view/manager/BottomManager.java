package com.hong.rise.lottery.view.manager;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hong.rise.R;
import com.hong.rise.lottery.ConstantValue;

import org.apache.commons.lang3.StringUtils;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2016/4/12.
 */
public class BottomManager implements Observer{
    protected static final String TAG = "BottomManager";
    /******************* 第一步：管理对象的创建(单例模式) ***************************************************/
    // 创建一个静态实例
    private static BottomManager instrance;

    // 构造私有
    private BottomManager() {
    }

    // 提供统一的对外获取实例的入口
    public static BottomManager getInstrance() {
        if (instrance == null) {
            instrance = new BottomManager();
        }
        return instrance;
    }

    /*********************************************************************************************/
    /******************* 第二步：初始化各个导航容器及相关控件设置监听 *********************************/

    /********** 底部菜单容器 **********/
    private RelativeLayout bottomMenuContainer;
    /************ 底部导航 ************/
    private LinearLayout commonBottom;// 购彩通用导航
    private LinearLayout playBottom;// 购彩

    /***************** 导航按钮 ******************/

    /************ 购彩导航底部按钮及提示信息 ************/
    private ImageButton cleanButton;
    private ImageButton addButton;

    private TextView playBottomNotice;

    /************ 通用导航底部按钮 ************/
    private ImageButton homeButton;
    private ImageButton hallButton;
    private ImageButton rechargeButton;
    private ImageButton myselfButton;

    public void init(Activity activity) {
        bottomMenuContainer = (RelativeLayout) activity.findViewById(R.id.ii_bottom);
        commonBottom = (LinearLayout) activity.findViewById(R.id.ii_bottom_common);
        playBottom = (LinearLayout) activity.findViewById(R.id.ii_bottom_game);

        playBottomNotice = (TextView) activity.findViewById(R.id.ii_bottom_game_choose_notice);
        cleanButton = (ImageButton) activity.findViewById(R.id.ii_bottom_game_choose_clean);
        addButton = (ImageButton) activity.findViewById(R.id.ii_bottom_game_choose_ok);

        // 设置监听
        setListener();
    }

    /**
     * 设置监听
     */
    private void setListener() {
        // 清空按钮
        cleanButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.i(TAG, "点击清空按钮");
                BaseUI currentUI = MiddleManager.getInstance().getCurrentUI();

                if (currentUI instanceof PlayGame) {
                    ((PlayGame) currentUI).clear();
                }

            }
        });
        // 选好按钮
        addButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.i(TAG, "点击选好按钮");

                BaseUI currentUI = MiddleManager.getInstance().getCurrentUI();

                if (currentUI instanceof PlayGame) {
                    ((PlayGame) currentUI).done();
                }

            }
        });
    }

    /*********************************************************************************************/
    /****************** 第三步：控制各个导航容器的显示和隐藏 *****************************************/
    /**
     * 转换到通用导航
     */
    public void showCommonBottom() {
        if (bottomMenuContainer.getVisibility() == View.GONE || bottomMenuContainer.getVisibility() == View.INVISIBLE) {
            bottomMenuContainer.setVisibility(View.VISIBLE);
        }
        commonBottom.setVisibility(View.VISIBLE);
        playBottom.setVisibility(View.INVISIBLE);
    }

    /**
     * 转换到购彩
     */
    public void showGameBottom() {
        if (bottomMenuContainer.getVisibility() == View.GONE || bottomMenuContainer.getVisibility() == View.INVISIBLE) {
            bottomMenuContainer.setVisibility(View.VISIBLE);
        }
        commonBottom.setVisibility(View.INVISIBLE);
        playBottom.setVisibility(View.VISIBLE);
    }

    /**
     * 改变底部导航容器显示情况
     */
    public void changeBottomVisiblity(int type) {
        if (bottomMenuContainer.getVisibility() != type)
            bottomMenuContainer.setVisibility(type);
    }

    /*********************************************************************************************/
    /*********************** 第四步：控制玩法导航内容显示 ********************************************/
    /**
     * 设置玩法底部提示信息
     *
     * @param notice
     */
    public void changeGameBottomNotice(String notice) {
        playBottomNotice.setText(notice);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data != null && StringUtils.isNumeric(data.toString())) {
            int id = Integer.parseInt(data.toString());
            switch (id) {
                case ConstantValue.VIEW_HALL:
                    showCommonBottom();
                    break;
                case ConstantValue.FRIST_VIEW:
//                    showCommonBottom();
                    break;
                case ConstantValue.SECOND_VIEW:
                    showGameBottom();
                    break;
                case ConstantValue.VIEW_SSQ:
                    showGameBottom();
                    break;
                case ConstantValue.VIEW_SHOPPING:
                case ConstantValue.VIEW_PREBET:
                    changeBottomVisiblity(View.GONE);
                    break;
            }
        }
    }
    /*********************************************************************************************/

}

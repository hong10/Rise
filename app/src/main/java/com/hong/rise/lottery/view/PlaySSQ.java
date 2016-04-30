package com.hong.rise.lottery.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.hong.rise.R;
import com.hong.rise.lottery.Adapter.PoolAdapter;
import com.hong.rise.lottery.ConstantValue;
import com.hong.rise.lottery.view.custom.MyGridView;
import com.hong.rise.lottery.view.manager.BaseUI;
import com.hong.rise.lottery.view.manager.TitleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hong on 2016/4/26.
 */
public class PlaySSQ extends BaseUI {

    // 通用三步

    // ①标题
    // 判断购彩大厅是否获取到期次信息
    // 如果获取到：拼装标题
    // 否则默认的标题展示

    // ②填充选号容器
    // ③选号：单击+机选红蓝球
    // ④手机摇晃处理
    // 加速度传感器：
    // 方式一：任意一个轴的加速度值在单位时间内（1秒），变动的速率达到设置好的阈值
    // 方式二：获取三个轴的加速度值，记录，当过一段时间之后再次获取三个轴的加速度值，计算增量，将相邻两个点的增量进行汇总，当达到设置好的阈值

    // ①记录第一个数据：三个轴的加速度，为了屏蔽掉不同手机采样的时间间隔，记录第一个点的时间
    // ②当有新的传感器数据传递后，判断时间间隔（用当前时间与第一个采样时间进行比对，如果满足了时间间隔要求，认为是合格的第二个点，否则舍弃该数据包）
    //  进行增量的计算：获取到新的加速度值与第一个点上存储的进行差值运算，获取到一点和二点之间的增量
    // ③以此类推，获取到相邻两个点的增量，一次汇总
    // ④通过汇总值与设定好的阈值比对，如果大于等于，用户摇晃手机，否则继续记录当前的数据（加速度值和时间）

    // ⑤提示信息+清空+选好了

    // 机选
    private Button randomRed;
    private Button randomBlue;
    // 选号容器
    private MyGridView redContainer;
    private GridView blueContainer;

    private PoolAdapter redAdapter;
    private PoolAdapter blueAdapter;

    private List<Integer> redNums;
    private List<Integer> blueNums;

    public PlaySSQ(Context context) {
        super(context);
    }

    @Override
    public void init() {
        showInMiddle = (ViewGroup) View.inflate(context, R.layout.il_playssq, null);

        redContainer = (MyGridView) findViewById(R.id.ii_ssq_red_number_container);
        blueContainer = (GridView) findViewById(R.id.ii_ssq_blue_number_container);
        randomRed = (Button) findViewById(R.id.ii_ssq_random_red);
        randomBlue = (Button) findViewById(R.id.ii_ssq_random_blue);

        redNums = new ArrayList<Integer>();
        blueNums = new ArrayList<Integer>();

        redAdapter = new PoolAdapter(context, 33, R.drawable.id_redball, redNums);
        blueAdapter = new PoolAdapter(context, 16, R.drawable.id_blueball, blueNums);

        redContainer.setAdapter(redAdapter);
        blueContainer.setAdapter(blueAdapter);
    }

    @Override
    public void setOnClickListener() {
        randomRed.setOnClickListener(this);
        randomBlue.setOnClickListener(this);

        //自定义的GridView（选中后头上会冒泡）
        redContainer.setOnActionUpListener(new MyGridView.OnActionUpListener() {
            @Override
            public void onActionUp(View view, int position) {
                if (!redNums.contains(position + 1)) {
                    //如果之前没有被选中
                    //设置ball的背景
                    view.setBackgroundResource(R.drawable.id_redball);
                    redNums.add(position + 1);
                } else {
                    //如果之前选中了
                    //此时将背景色改为默认背景色
                    view.setBackgroundResource(R.drawable.id_defalut_ball);
                    redNums.remove((Object) (position + 1));//redNums.remove(int),这个代码的意思是，移出第一个位置上的元素，会报出越界的异常
                }
            }
        });

/*        redContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!redNums.contains(position + 1)) {
                    //如果之前没有被选中
                    //设置ball的背景
                    view.setBackgroundResource(R.drawable.id_redball);
                    //设置摇晃动画
                    view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.ball_shake));
                    redNums.add(position + 1);

                } else {
                    //如果之前选中了
                    //此时将背景色改为默认背景色
                    view.setBackgroundResource(R.drawable.id_defalut_ball);
                    redNums.remove((Object) (position + 1));//redNums.remove(int),这个代码的意思是，移出第一个位置上的元素，会报出越界的异常
                }

            }
        });*/

        blueContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!blueNums.contains(position + 1)) {
                    //如果之前没有被选中
                    //设置ball的背景
                    view.setBackgroundResource(R.drawable.id_blueball);
                    //设置摇晃动画
                    view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.ball_shake));
                    blueNums.add(position + 1);

                } else {
                    //如果之前选中了
                    //此时将背景色改为默认背景色
                    view.setBackgroundResource(R.drawable.id_defalut_ball);
                    blueNums.remove((Object) (position + 1));//redNums.remove(int),这个代码的意思是，移出第一个位置上的元素，会报出越界的异常
                }
            }
        });

    }

    @Override
    public int getID() {
        return ConstantValue.VIEW_SSQ;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ii_ssq_random_red:

                break;
            case R.id.ii_ssq_random_blue:

                break;
        }
    }

    @Override
    public void onResume() {
        changeTitle();
        super.onResume();
    }

    /**
     * 修改标题
     */
    private void changeTitle() {
        String titleInfo = "";
        // ①标题——界面之间的数据传递(Bundle)
        // 判断购彩大厅是否获取到期次信息
        if (bundle != null) {
            // 如果获取到：拼装标题
            titleInfo = "双色球第" + bundle.getString("issue") + "期";
        } else {
            // 否则默认的标题展示
            titleInfo = "双色球选号";
        }

        TitleManager.getInstance().changeTitle(titleInfo);
    }
}

package com.hong.rise.lottery.Adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hong.rise.R;

import java.text.DecimalFormat;

/**
 * Created by hong on 2016/4/26.
 */
public class PoolAdapter extends BaseAdapter {
    private Context context;
    private int endNum;
    private int selectBgResId;//选中的背景图片的资源id

    public PoolAdapter(Context context, int endNum, int selectBgResId) {
        this.context = context;
        this.endNum = endNum;
        this.selectBgResId = selectBgResId;
    }

    @Override
    public int getCount() {
        return endNum;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView ball = new TextView(context);
        DecimalFormat decimalFormat = new DecimalFormat("00");
        ball.setText(decimalFormat.format(position + 1));
        ball.setTextSize(16);

        ball.setGravity(Gravity.CENTER);

        ball.setBackgroundResource(R.drawable.id_defalut_ball);


        return ball;
    }
}

package com.hong.rise.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.WindowManager;



/**
 * Created by Administrator on 2016/7/5.
 */

public class MonitorView extends View {
    private int pressure;
    private Paint paint;
    private int height;

    public MonitorView(Context context, int pressure) {
        super(context);
        this.pressure = pressure;
        paint = new Paint();

        //获取屏幕的高度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        height = wm.getDefaultDisplay().getHeight() - 60;//去掉通知栏的高度

        paint.setStrokeWidth(5);//画笔宽度
        paint.setAntiAlias(true);//抗锯齿

        if (pressure < 150) {
            paint.setColor(Color.GREEN);
        } else if (pressure < 260) {
            paint.setColor(Color.YELLOW);
        } else {
            paint.setColor(Color.RED);

        }


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(30, height - pressure, 60, height, paint);


    }
}

package com.hong.rise;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FakeNetEasyNewsActivity extends Activity {

    private int cursorWidth, screenWidth, currentIndex;
    private TextView tvRecall, tvCreate, tvOffer;
    private ImageView ivCursor;
    private ViewPager pager;
    private List<View> views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fake_net_easy_news);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;

        cursorWidth = screenWidth / 3;

        intTitle();
        intViewPager();

    }

    private void intViewPager() {
        pager = (ViewPager) findViewById(R.id.pager);
        views = new ArrayList<View>();

        LayoutInflater inflater = getLayoutInflater();
        views.add(inflater.inflate(R.layout.viewpager_page1, null));
        views.add(inflater.inflate(R.layout.viewpager_page2, null));
        views.add(inflater.inflate(R.layout.viewpager_page3, null));

        pager.setAdapter(new MyPagerAdapter(views));
        pager.setCurrentItem(0);
        pager.setOnPageChangeListener(new MyOnPageChangeListener());


    }

    private void intTitle() {
        //init title
        tvRecall = (TextView) findViewById(R.id.tv_recall);
        tvCreate = (TextView) findViewById(R.id.tv_create);
        tvOffer = (TextView) findViewById(R.id.tv_offer);

        tvRecall.setOnClickListener(new MyOnClickListener(0));
        tvCreate.setOnClickListener(new MyOnClickListener(1));
        tvOffer.setOnClickListener(new MyOnClickListener(2));

        ivCursor = (ImageView) findViewById(R.id.iv_cursor);
        //set cursor width
        ivCursor.getLayoutParams().width = cursorWidth;


    }

    private class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            pager.setCurrentItem(index);
        }
    }

    /**
     * MyPagerAdapter
     */
    private class MyPagerAdapter extends PagerAdapter {
        private List<View> views;


        public MyPagerAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position), 0);
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public int getCount() {
            return views.size();
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        // position : target index
        @Override
        public void onPageSelected(int position) {
//            Animation animation = new TranslateAnimation(cursorWidth * currentIndex, currentIndex * position, 0, 0);
            Animation animation = null;

            //init animation
            switch (position) {
                case 0:
                    if (currentIndex == 1) {
                        animation = new TranslateAnimation(cursorWidth, 0, 0, 0);
                    } else if (currentIndex == 2) {
                        animation = new TranslateAnimation(2 * currentIndex,0,  0, 0);
                    }
                    break;
                case 1:
                    if (currentIndex == 0) {
                        animation = new TranslateAnimation(0,cursorWidth,  0, 0);
                    } else if (currentIndex == 2) {
                        animation = new TranslateAnimation(2 * cursorWidth,cursorWidth,  0, 0);
                    }
                    break;
                case 2:
                    if (currentIndex == 0) {
                        animation = new TranslateAnimation(0,2 * cursorWidth,  0, 0);
                    } else if (currentIndex == 1) {
                        animation = new TranslateAnimation(cursorWidth, 2 * cursorWidth, 0, 0);
                    }
                    break;
            }

            currentIndex = position;
            /**
            为什么会有setFillAfter 和 setFillBefore这两个方法：
            是因为有动画链的原因，假定你有一个移动的动画紧跟一个淡出的动画，如果你不把移动的动画的setFillAfter置为true，那么移动动画结束后，View会回到原来的位置淡出，如果setFillAfter置为true， 就会在移动动画结束的位置淡出
             */
            animation.setFillAfter(true);
            animation.setDuration(300);
            ivCursor.startAnimation(animation);

        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }


}

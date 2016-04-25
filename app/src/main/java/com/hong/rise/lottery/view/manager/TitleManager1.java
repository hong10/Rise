package com.hong.rise.lottery.view.manager;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hong.rise.R;
import com.hong.rise.lottery.view.SecondUI;


/**
 * 管理标题容器的工具
 *
 * @author Administrator
 */
public class TitleManager1 {
    // 显示和隐藏

    private static TitleManager1 instance = null;

    private TitleManager1() {
    }

    public static TitleManager1 getInstance() {
        if (instance == null) {
            instance = new TitleManager1();
        }
        return instance;
    }

    private RelativeLayout commonContainer;
    private RelativeLayout loginContainer;
    private RelativeLayout unLoginContainer;

    private ImageView goback;// 返回
    private ImageView help;// 帮助
    private ImageView login;// 登录

    private TextView titleContent;// 标题内容
    private TextView userInfo;// 用户信息

    public void init(Activity activity) {
        commonContainer = (RelativeLayout) activity
                .findViewById(R.id.ii_common_container);
        unLoginContainer = (RelativeLayout) activity
                .findViewById(R.id.ii_unlogin_title);
        loginContainer = (RelativeLayout) activity
                .findViewById(R.id.ii_login_title);

        goback = (ImageView) activity.findViewById(R.id.ii_title_goback);
        help = (ImageView) activity.findViewById(R.id.ii_title_help);
        login = (ImageView) activity.findViewById(R.id.ii_title_login);

        titleContent = (TextView) activity.findViewById(R.id.ii_title_content);
        userInfo = (TextView) activity.findViewById(R.id.ii_top_user_info);

        setListener();
    }

    private void setListener() {
        goback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("返回键");

            }
        });
        help.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("help");

            }
        });
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("login");
                MiddleManager middleManager = MiddleManager.getInstance();
                //防止当频繁点击时重复创建对象，所以用字节码
                middleManager.changeUI(SecondUI.class);//changeUI需要修改，不能传递对象，但是明确目标
//				SecondUI secondUI = new SecondUI(MiddleManager.getInstance().getContext());
//                MiddleManager.getInstance().changeUI(SecondUI.class);//changeUI需要修改，不能传递对象，但是明确目标
            }
        });

    }

    private void initTitle() {
        commonContainer.setVisibility(View.GONE);
        loginContainer.setVisibility(View.GONE);
        unLoginContainer.setVisibility(View.GONE);
    }

    /**
     * 显示通用标题
     */
    public void showCommonTitle() {
        initTitle();
        commonContainer.setVisibility(View.VISIBLE);
    }

    /**
     * 显示未登录的标题
     */
    public void showUnLoginTitle() {
        initTitle();
        unLoginContainer.setVisibility(View.VISIBLE);
    }

    /**
     * 显示登陆的标题
     */
    public void showLoginTitle() {
        initTitle();
        loginContainer.setVisibility(View.VISIBLE);

    }

    public void changeTitle(String title) {
        titleContent.setText(title);
    }
}

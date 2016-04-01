package com.hong.rise;

import android.test.AndroidTestCase;
import android.util.Log;

import com.hong.rise.lottery.bean.User;
import com.hong.rise.lottery.engine.impl.UserEngineImpl;
import com.hong.rise.lottery.net.protocal.Message;
import com.hong.rise.utils.NetUtil;

/**
 * Created by Administrator on 2016/3/25.
 */
public class NetTest extends AndroidTestCase {

    private static final String TAG = "NetTest";

    public void testNetType() {
        NetUtil.checkNet(getContext());
    }


    public void testLogin() {
        UserEngineImpl impl = new UserEngineImpl();

        User user = new User();
        user.setUserName("hong");
        user.setPassWord("123456789");

        Message login = impl.login(user);
        Log.i(TAG, login.getBody().getOelement().getErrorcode());
    }
}

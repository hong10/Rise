package com.hong.rise.lottery.engine;

import com.hong.rise.lottery.bean.User;
import com.hong.rise.lottery.net.protocal.Message;

/**
 * 用户相关的业务操作接口
 * Created by hong on 2016/4/4.
 */
public interface UserEngine {

    /**
     * 用户登录
     * @param user
     * @return
     */
    Message login(User user);

    /**
     * 获取用户余额
     * @param user
     * @return
     */
    Message getBalance(User user);
    /**
     * 用户投注
     * @param user
     * @return
     */
    Message bet(User user);

}

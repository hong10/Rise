package com.hong.rise.lottery.engine;

import com.hong.rise.lottery.net.protocal.Message;

/**
 * 公共数据处理
 * Created by hong on 2016/4/25.
 */
public interface CommonInfoEngine {

    /**
     * 获取当前销售期信息
     * @param integer ： 彩种的标示
     * @return
     */
    Message getCurrentIssueInfo(Integer integer);
}

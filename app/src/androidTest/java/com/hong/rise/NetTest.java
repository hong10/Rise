package com.hong.rise;

import android.test.AndroidTestCase;

import com.hong.rise.utils.NetUtil;

/**
 * Created by Administrator on 2016/3/25.
 */
public class NetTest extends AndroidTestCase {

    public void testNetType() {
        NetUtil.checkNet(getContext());
    }
}

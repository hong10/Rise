package com.hong.rise.lottery;

/**
 * Created by hong on 2016/3/13.
 */
public interface ConstantValue {

    String ENCONDING = "utf-8";

    //代理的ID
    String AGENTERID = "889931";

    //信息来源
    String SOURCE = "ivr";

    //body里数据的加密方式
    String COMPRESS = "DES";

    //子代理商的密钥(.so) JNI
    String AGENTER_PASSWORD = "9ab62a694d8bf6ced1fab6acd48d02f8";

    // des加密用密钥
    String DES_PASSWORD = "9b2648fcdfbad80f";

    //String LOTTERY_URI = "http://10.88.0.193:8080/ZCWService/Entrance";
    String LOTTERY_URI = "http://192.168.2.100:8080/ZCWService/Entrance";

    int FRIST_VIEW = 1;
    int SECOND_VIEW = 2;

    /**
     * 购彩大厅
     */
    int VIEW_HALL=10;
    /**
     * 双色球选号界面
     */
    int VIEW_SSQ=15;
    /**
     * 购物车
     */
    int VIEW_SHOPPING=20;
    /**
     * 追期和倍投的设置界面
     */
    int VIEW_PREBET=25;
    /**
     * 双色球标示
     */
    int SSQ = 118;
    /**
     * 服务器返回成功状态码
     */
    String SUCCESS="0";
}

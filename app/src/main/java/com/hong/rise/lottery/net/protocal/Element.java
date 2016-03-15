package com.hong.rise.lottery.net.protocal;

import org.xmlpull.v1.XmlSerializer;

/**
 * Created by Administrator on 2016/3/14.
 */
public abstract class Element {
    /**
     * 序列化：每个请求都要序列化自己
     */
    public abstract  void serializerElement(XmlSerializer serializer);

    /**
     * 获取TransactionType: 每个请求都有自己的唯一标示
     */
    public abstract String getTransactionType();

}

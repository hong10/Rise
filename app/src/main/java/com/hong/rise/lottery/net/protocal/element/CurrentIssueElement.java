package com.hong.rise.lottery.net.protocal.element;

import com.hong.rise.lottery.net.protocal.Element;
import com.hong.rise.lottery.net.protocal.Leaf;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

/**
 * 获取当前销售期的请求
 * Created by Administrator on 2016/3/14.
 */
public class CurrentIssueElement extends Element {

    private Leaf lotteryid = new Leaf("lotteryid");
    private Leaf issue = new Leaf("issue", "1");

    public Leaf getLotteryid() {
        return lotteryid;
    }


    @Override
    public void serializerElement(XmlSerializer serializer) {

        try {
            serializer.startTag(null, "element");
            lotteryid.serializerLeaf(serializer);
            issue.serializerLeaf(serializer);
            serializer.endTag(null, "element");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getTransactionType() {
        return "12002";
    }
}

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
    private Leaf issues = new Leaf("issues", "1");

    public Leaf getLotteryid() {
        return lotteryid;
    }


    @Override
    public void serializerElement(XmlSerializer serializer) {

        try {
            serializer.startTag(null, "element");
            lotteryid.serializerLeaf(serializer);
            issues.serializerLeaf(serializer);
            serializer.endTag(null, "element");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getTransactionType() {
        return "12002";
    }


    /*********************
     * 服务器回复
     ***********************/
    private String issue;
    private String lastTime;

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }
    /********************************************/
}

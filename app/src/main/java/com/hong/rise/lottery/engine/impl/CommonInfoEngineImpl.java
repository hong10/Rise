package com.hong.rise.lottery.engine.impl;

import android.util.Xml;

import com.hong.rise.lottery.ConstantValue;
import com.hong.rise.lottery.engine.BaseEngine;
import com.hong.rise.lottery.engine.CommonInfoEngine;
import com.hong.rise.lottery.net.protocal.Message;
import com.hong.rise.lottery.net.protocal.element.CurrentIssueElement;
import com.hong.rise.utils.DES;

import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;

/**
 * Created by hong on 2016/4/25.
 */
public class CommonInfoEngineImpl extends BaseEngine implements CommonInfoEngine {
    @Override
    public Message getCurrentIssueInfo(Integer lotteryId) {
        //xml
        CurrentIssueElement element = new CurrentIssueElement();
        element.getLotteryid().setTagValue(lotteryId.toString());

        Message message = new Message();
        String xml = message.getXml(element);

        Message result = super.getResult(xml);
        if (result != null) {

            // 第四步：请求结果的数据处理
            // body部分的第二次解析，解析的是明文内容

            XmlPullParser parser = Xml.newPullParser();
            try {

                DES des = new DES();
                String body = "<body>"
                        + des.authcode(result.getBody()
                                .getServiceBodyInsideDESInfo(), "ENCODE",
                        ConstantValue.DES_PASSWORD) + "</body>";

                parser.setInput(new StringReader(body));

                int eventType = parser.getEventType();
                String name;

                CurrentIssueElement resultElement = null;

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            name = parser.getName();
                            if ("errorcode".equals(name)) {
                                result.getBody().getOelement()
                                        .setErrorcode(parser.nextText());
                            }
                            if ("errormsg".equals(name)) {
                                result.getBody().getOelement()
                                        .setErrormsg(parser.nextText());
                            }

                            // 判断是否含有element标签，如果有的话创建resultElement
                            if ("element".equals(name)) {
                                resultElement = new CurrentIssueElement();
                                result.getBody().getElements().add(resultElement);
                            }

                            // 解析特殊的数据
                            if ("issue".equals(name)) {
                                if (resultElement != null) {
                                    resultElement.setIssue(parser.nextText());
                                }
                            }
                            if ("lasttime".equals(name)) {
                                if (resultElement != null) {
                                    resultElement.setLastTime(parser.nextText());
                                }
                            }

                            break;
                    }
                    eventType = parser.next();
                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return null;


    }
}

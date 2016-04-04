package com.hong.rise.lottery.engine.impl;

import android.util.Xml;

import com.hong.rise.lottery.ConstantValue;
import com.hong.rise.lottery.bean.User;
import com.hong.rise.lottery.engine.BaseEngine;
import com.hong.rise.lottery.engine.UserEngine;
import com.hong.rise.lottery.net.protocal.Message;
import com.hong.rise.lottery.net.protocal.element.UserLoginElement;
import com.hong.rise.utils.DES;
import com.hong.rise.utils.HttpClientUtil;

import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.InputStream;
import java.io.StringReader;

/**
 * Created by hong on 2016/3/27.
 */
public class UserEngineImpl extends BaseEngine implements UserEngine{


    public Message login(User user) {
        //第一步：获取到登录用的xml
        //创建登录用Element
        UserLoginElement element = new UserLoginElement();
        //设置用户数据
        element.getActpassword().setTagValue(user.getPassWord());
        Message message = new Message();
        message.getHeader().getUsername().setTagValue(user.getUserName());
        //Message.getXml(element)
        String xml = message.getXml(element);
//        String tmpxml = xml.replace("<?xml version='1.0' encoding='utf-8' ?>", "<?xml version=\"1.0\" encoding=\"utf-8\"?>");

        Message result = getResult(xml);

        if (result != null) {
            XmlPullParser parser = Xml.newPullParser();

            DES des = new DES();
            String body = "<body>" + des.authcode(result.getBody().getServiceBodyInsideDESInfo(), "ENCODE", ConstantValue.DES_PASSWORD) + "</body>";

            try {
                parser.setInput(new StringReader(body));

                int eventType = parser.getEventType();
                String name;

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            name = parser.getName();
                            if ("errorcode".equals(name)) {
                                result.getBody().getOelement().setErrorcode(parser.nextText());
                            }
                            if ("errormsg".equals(name)) {
                                result.getBody().getOelement().setErrormsg(parser.nextText());

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


    public Message login1(User user) {
        //第一步：获取到登录用的xml
        //创建登录用Element
        UserLoginElement element = new UserLoginElement();
        //设置用户数据
        element.getActpassword().setTagValue(user.getPassWord());
        Message message = new Message();
        message.getHeader().getUsername().setTagValue(user.getUserName());
        //Message.getXml(element)
        String xml = message.getXml(element);
//        String tmpxml = xml.replace("<?xml version='1.0' encoding='utf-8' ?>", "<?xml version=\"1.0\" encoding=\"utf-8\"?>");

        //第二步：发送xml到服务器端，等待回复
        //HttpClientUtil.sendXml
        HttpClientUtil util = new HttpClientUtil();
        InputStream is = util.sendXml(ConstantValue.LOTTERY_URI, xml);
        //判断输入流非空
        if (is != null) {
            Message result = new Message();

            //第三步：数据的校验（MD5数据校验）
            //timestamp+digest+body
            XmlPullParser parser = Xml.newPullParser();
            try {
                parser.setInput(is, ConstantValue.ENCONDING);

                int eventType = parser.getEventType();
                String name;

                while (eventType != XmlPullParser.END_DOCUMENT) {

                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            name = parser.getName();
                            if ("timestamp".equals(name)) {
                                result.getHeader().getTimestamp().setTagValue(parser.nextText());
                            }
                            if ("digest".equals(name)) {
                                result.getHeader().getDigest().setTagValue(parser.nextText());
                            }
                            if ("body".equals(name)) {
                                result.getBody().setServiceBodyInsideDESInfo(parser.nextText());
                            }
                            break;
                    }

                    eventType = parser.next();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            //原始数据还原：时间戳（解析）+密码（常量）+body明文（解析+解密DES）
            DES des = new DES();
            String body = "<body>" + des.authcode(result.getBody().getServiceBodyInsideDESInfo(), "ENCODE", ConstantValue.DES_PASSWORD) + "</body>";

            String oriInfo = result.getHeader().getTimestamp().getTagValue() + ConstantValue.AGENTER_PASSWORD + body;

            //利用工具生成手机端MD5
            String md5Hex = DigestUtils.md5Hex(oriInfo);

            //将手机端和服务器的数据进行比较
            if (md5Hex.equals(result.getHeader().getDigest().getTagValue())) {
                //第四步：请求结果的数据处理
                //body部分的第二次解析，解析的是明文内容

                parser = Xml.newPullParser();

                try {
                    parser.setInput(new StringReader(body));

                    int eventType = parser.getEventType();
                    String name;

                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                name = parser.getName();
                                if ("errorcode".equals(name)) {
                                    result.getBody().getOelement().setErrorcode(parser.nextText());
                                }
                                if ("errormsg".equals(name)) {
                                    result.getBody().getOelement().setErrormsg(parser.nextText());

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


        }

        return null;


    }
}

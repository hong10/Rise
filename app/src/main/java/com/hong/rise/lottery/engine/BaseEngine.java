package com.hong.rise.lottery.engine;

import android.util.Xml;

import com.hong.rise.lottery.ConstantValue;
import com.hong.rise.lottery.net.protocal.Message;
import com.hong.rise.utils.DES;
import com.hong.rise.utils.HttpClientUtil;

import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.io.StringReader;

/**
 * Created by hong on 2016/4/4.
 */
public abstract class BaseEngine {

    public Message getResult(String xml) {

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
                return result;

            }

        }

        return null;
    }

}

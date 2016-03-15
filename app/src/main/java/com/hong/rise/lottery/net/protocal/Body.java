package com.hong.rise.lottery.net.protocal;

import android.util.Xml;

import com.hong.rise.lottery.ConstantValue;
import com.hong.rise.utils.DES;

import org.apache.commons.lang3.StringUtils;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/15.
 */
public class Body {

    private List<Element> elements = new ArrayList<Element>();

    /**
     * 由于要获取elemet中TransactionType,所以要暴露一个get方法
     */
    public List<Element> getElements() {
        return elements;
    }

    /**
     * 序列化自己
     */
    public void serializerBody(XmlSerializer serializer) {
        /*
        <body>
            <elements>
                <element>
                    <lotteryid>118</lotteryid>
                       <issues>1</issues>
                </element>
            </elements>
        </body>
        */

        try {
            serializer.startTag(null, "body");
            serializer.startTag(null, "elements");
            for (Element element : elements) {
                element.serializerElement(serializer);
            }
            serializer.endTag(null, "elements");
            serializer.endTag(null, "body");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取body中数据
     */
    public String getWholeBody() {
        StringWriter writer = new StringWriter();
        XmlSerializer tmp = Xml.newSerializer();
        try {
            tmp.setOutput(writer);
            this.serializerBody(tmp);
            tmp.flush();
            return writer.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用des加密数据
     */
    public String getBodyInsideDESInfo() {

        //获取原始数据
        String wholeBody = this.getWholeBody();
        String orgDesInfo = StringUtils.substringBetween(wholeBody, "<body>", "</body>");

        //加密
        DES des = new DES();
        return des.authcode(orgDesInfo, "DECODE", ConstantValue.DES_PASSWORD);

    }
}

package com.hong.rise.lottery.net.protocal;

import android.util.Xml;

import com.hong.rise.lottery.ConstantValue;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by Administrator on 2016/3/15.
 */
public class Message {

    private Header header = new Header();
    private Body body = new Body();

    public Header getHeader() {
        return header;
    }

    public Body getBody() {
        return body;
    }

    /**
     * 序列化自己
     */
    public void serializerMessage(XmlSerializer serializer) {

        try {
            serializer.startTag(null, "message");
            serializer.attribute(null, "version", "1.0");

            header.serializerHeader(serializer, body.getWholeBody());

            serializer.startTag(null, "body");
            serializer.text(body.getBodyInsideDESInfo());
            serializer.endTag(null, "body");

            serializer.endTag(null, "message");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取xml
     *
     * @return
     */
    public String getXml(Element element) {
        if (element == null) {
            throw new IllegalArgumentException("element is null");
        }

        body.getElements().add(element);

        XmlSerializer serializer = Xml.newSerializer();

        StringWriter witer = new StringWriter();
        try {
            serializer.setOutput(witer);
            serializer.startDocument(ConstantValue.ENCONDING, null);
            this.serializerMessage(serializer);
            serializer.endDocument();

            return witer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}

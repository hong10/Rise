package com.hong.rise.lottery.net.protocal;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

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

}

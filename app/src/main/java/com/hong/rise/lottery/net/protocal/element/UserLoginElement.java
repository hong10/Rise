package com.hong.rise.lottery.net.protocal.element;

import com.hong.rise.lottery.net.protocal.Element;
import com.hong.rise.lottery.net.protocal.Leaf;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

/**
 * Created by hong on 2016/3/27.
 */
public class UserLoginElement extends Element {
    private Leaf actpassword = new Leaf("actpassword");

    public Leaf getActpassword() {
        return actpassword;
    }

    @Override
    public void serializerElement(XmlSerializer serializer) {
        try {
            serializer.startTag(null, "element");
            actpassword.serializerLeaf(serializer);
            serializer.endTag(null, "element");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getTransactionType() {
        return "14001";
    }
}

package com.hong.rise.lottery.net.protocal;

import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

/**
 * Created by hong on 2016/3/13.
 */
public class Leaf {
    private String tagName;
    private String tagValue;

    public String getTagValue() {
        return tagValue;
    }

    //获取tagname
    public String getTagName() {
        return tagName;
    }

    //设置tagValue
    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public Leaf() {
    }

    public Leaf(String tagName) {
        this.tagName = tagName;
    }

    public Leaf(String tagName, String tagValue) {

        this.tagName = tagName;
        this.tagValue = tagValue;
    }

    //Serilalizer Leaf
    public void serializerLeaf(XmlSerializer serializer) {

        try {
            serializer.startTag(null, tagName);
            if(tagValue == null){
                this.setTagValue("");
            }
            serializer.text(tagValue);
            serializer.endTag(null, tagName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.hong.rise;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;
import android.util.Xml;

import com.hong.rise.lottery.ConstantValue;
import com.hong.rise.lottery.net.protocal.Body;
import com.hong.rise.lottery.net.protocal.Header;
import com.hong.rise.lottery.net.protocal.Message;
import com.hong.rise.lottery.net.protocal.element.CurrentIssueElement;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    private static final String TAG = "ApplicationTest";

    public ApplicationTest() {
        super(Application.class);
    }

    public void testCreateXml() {
        Log.i(TAG, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        XmlSerializer xmlSerializer = Xml.newSerializer();

        StringWriter writer = new StringWriter();
        try {
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument(ConstantValue.ENCONDING, null);
//            Header header = new Header();
//            header.serializerHeader(xmlSerializer, "hong");
//            CurrentIssueElement element = new CurrentIssueElement();
//            element.getLotteryid().setTagValue("118");
//            Body body = new Body();
//            body.getElements().add(element);
//            body.serializerBody(xmlSerializer);

            Message message = new Message();
            message.serializerMessage(xmlSerializer);
            xmlSerializer.endDocument();

            Log.i(TAG, writer.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

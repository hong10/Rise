package com.hong.rise.lottery.net.protocal;

import android.util.Log;

import com.hong.rise.lottery.ConstantValue;

import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by hong on 2016/3/13.
 */
public class Header {
    private static final String TAG = "Header";
    /*
        <messengerid>20120803102102460191</messengerid>
      <timestamp>20120803102102</timestamp>
      <username>13200000000</username>
      <transactiontype>12002</transactiontype>
      <digest>1b10c6d719c78cdb70c1d357978eff2a</digest>
      <agenterid>1000002</agenterid>
      <compress>DES</compress>
      <source>ivr</source>

         */
    private Leaf messagerid = new Leaf("messengerid");
    private Leaf timestamp = new Leaf("timestamp");
    private Leaf username = new Leaf("username");
    private Leaf transactiontype = new Leaf("transactiontype");
    private Leaf digest = new Leaf("digest");
    private Leaf agenterid = new Leaf("username", ConstantValue.AGENTERID);
    private Leaf compress = new Leaf("compress", ConstantValue.COMPRESS);
    private Leaf source = new Leaf("source", ConstantValue.SOURCE);


    public Leaf getTimestamp() {
        return timestamp;
    }

    public Leaf getDigest() {
        return digest;
    }

    //serializer Header
    public void serializerHeader(XmlSerializer serializer, String body) {

        //设置时间戳
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = format.format(new Date());
        timestamp.setTagValue(time);

        //合成messagerid（当前时间戳+六位随机数）
        Random random = new Random();
        int num = random.nextInt(999999) + 1;//[0,n) [1,1000000)
        DecimalFormat decimalFormat = new DecimalFormat();
        messagerid.setTagValue(time + decimalFormat.format(num).replace(",", ""));
        Log.i(TAG, time + decimalFormat.format(num).replace(",", ""));
        Log.i(TAG, decimalFormat.format(num).replace(",", ""));

        //digest:时间戳+代理商的密码+完整的body（明文）
        String orgInfo = time + ConstantValue.AGENTER_PASSWORD + body;
        String md5Hex = DigestUtils.md5Hex(orgInfo);
        digest.setTagValue(md5Hex);

        try {
            serializer.startTag(null, "header");
            messagerid.serializerLeaf(serializer);
            timestamp.serializerLeaf(serializer);
            username.serializerLeaf(serializer);
            transactiontype.serializerLeaf(serializer);
            digest.serializerLeaf(serializer);
            agenterid.serializerLeaf(serializer);
            compress.serializerLeaf(serializer);
            source.serializerLeaf(serializer);
            serializer.endTag(null, "header");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Leaf getUsername() {
        return username;
    }


    public Leaf getTransactiontype() {
        return transactiontype;
    }

}

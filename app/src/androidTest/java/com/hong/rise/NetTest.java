package com.hong.rise;

import android.test.AndroidTestCase;
import android.util.Log;

import com.hong.rise.lottery.ConstantValue;
import com.hong.rise.lottery.bean.User;
import com.hong.rise.lottery.engine.impl.UserEngineImpl;
import com.hong.rise.lottery.net.protocal.Message;
import com.hong.rise.utils.HttpClientUtil;
import com.hong.rise.utils.NetUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/3/25.
 */
public class NetTest extends AndroidTestCase {

    private static final String TAG = "NetTest";

    public void testNetType() {
        NetUtil.checkNet(getContext());
    }


    public void testSendXml() {
//        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><message version=\"1.0\"><header><messengerid>20160403095145852283</messengerid><timestamp>20160403095145</timestamp><username>13200000000</username><transactiontype>14001</transactiontype><digest>b10ad0ba1719db3e51d259c0428bb6cc</digest><username>889931</username><compress>DES</compress><source>ivr</source></header><body>cgYNf1rUkTlcXIFjWR1NnvH8iusYVv9RnPZwf1jwKdOExvDTIOl4qcfgZpjG4GK5B47oMPSbuNuidYEw6pBoHh42t1JfPWroSqsvKTdI1Ek=</body></message>";
        String xml = "<message version=\\\"1.0\\\"><header><messengerid>20160403095145852283</messengerid><timestamp>20160403095145</timestamp><username>13200000000</username><transactiontype>14001</transactiontype><digest>b10ad0ba1719db3e51d259c0428bb6cc</digest><username>889931</username><compress>DES</compress><source>ivr</source></header><body>cgYNf1rUkTlcXIFjWR1NnvH8iusYVv9RnPZwf1jwKdOExvDTIOl4qcfgZpjG4GK5B47oMPSbuNuidYEw6pBoHh42t1JfPWroSqsvKTdI1Ek=</body></message>";
        HttpClientUtil util = new HttpClientUtil();
        InputStream is = util.sendXml(ConstantValue.LOTTERY_URI, xml);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i;
        try {
            while ((i = is.read()) != -1) {
                baos.write(i);
            }
            String str = baos.toString();
            System.out.println(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testLogin() {
        UserEngineImpl impl = new UserEngineImpl();

        User user = new User();
        user.setUserName("13200000000");
        user.setPassWord("0000000");

        Message login = impl.login(user);
        Log.i(TAG, login.getBody().getOelement().getErrorcode());
    }
}

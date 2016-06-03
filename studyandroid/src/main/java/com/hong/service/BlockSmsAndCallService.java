package com.hong.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 拦截制定关键字的短信和拦截制定电话
 * Created by Administrator on 2016/5/31.
 */
public class BlockSmsAndCallService extends Service {
    private static final String TAG = BlockSmsAndCallService.class.getSimpleName();
    private InnerReceiver innerReceiver;
    private TelephonyManager tm;
    private MyListener listener;

    private class MyListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    if ("110".equals(incomingNumber)) {
                        Log.i(TAG, "block call");
                        endCall();
                    }
                    break;
            }
        }
    }

    private void endCall() {
        //IBinder iBinder = ServiceManager.getService(TELEPHONY_SERVICE);
        try {
            Class clazz = BlockSmsAndCallService.class.getClassLoader().loadClass("android.os.ServiceManager");
            Method method = clazz.getDeclaredMethod("getService", String.class);
            IBinder iBinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
            ITelephony.Stub.asInterface(iBinder).endCall();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    //block sms
    private class InnerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "block sms");
            //检查发件人是否是黑名单号码，设置短信拦截全部拦截。
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            for (Object pdu : pdus) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                //得到短信发件人
                String sender = smsMessage.getOriginatingAddress();
                // 查询数据库时候该发件人被加入黑名单了
//                String result = dao.findMode(sender);
//                if("2".equals(result)||"3".equals(result)){
//                    Log.i(TAG,"拦截短信");
//                    abortBroadcast();
//                }

                //关键字拦截，拦截“fapiao”
                String messageBody = smsMessage.getMessageBody();
                if (messageBody.contains("fapiao")) {
                    Log.i(TAG, "block sms by key word");
                    abortBroadcast();
                }
            }

        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "service start");

        innerReceiver = new InnerReceiver();
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        registerReceiver(innerReceiver, filter);

        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new MyListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "service destroy");
        unregisterReceiver(innerReceiver);
        innerReceiver = null;

        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

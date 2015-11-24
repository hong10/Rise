package com.tencent.cloud.demo.common;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

import com.tencent.upload.Const.FileType;
import com.tencent.upload.Const.ServerEnv;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


/**
 * 向服务器请求更新签名的异步任务，用于演示客户端如何获取签名<br/>
 * 作用: 1.拉取上传使用的签名 2.拉取用于文件资源操作(删除、复制等)的单次有效签名<br/><br/>
 * 
 * 注意：业务修改了BizService.APPID之后，需要自己实现相关的签名拉取逻辑<br/>
 */
public class UpdateSignTask extends AsyncTask<Void,Integer,String>
{  
    private Context mContext;
    private ProgressDialog mDialog;
    
    private String mAppid;
    private String mSecretId;
    private String mBucket;
    private String mFileId;
    private FileType mFileType;

    private boolean mbSingleSign;

    private OnGetSignListener mListener;
    
    public interface OnGetSignListener {
    	public void onSign(String sign);
    }
    
    /**
     * 构造函数
     * @param context
     * @param appid     业务APPID
     * @param secretId  业务在腾讯云注册申请之后分配得到，注意此处仅用于演示，正式业务请勿将secretId写死在客户端，否则可能泄露导致安全问题
     * @param bucket    业务使用的Bucket
     * @param fileId    文件ID； 传入ID为空时，则拉取全局有效的签名；传入ID不为空，则拉取单次有效签名
     * @param listener  任务结果回调
     */
    public UpdateSignTask(Context context, String appid, FileType fileType, String secretId, String bucket, String fileId, OnGetSignListener listener)
    {  
        mContext = context;
        mFileType = fileType;
        mSecretId = secretId;
        mFileId = fileId;
        mBucket = bucket;
        mAppid = appid;
        mBucket = bucket;
        mListener = listener;
        mDialog = new ProgressDialog(context);  
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setMessage("正在更新签名...");  
        mDialog.setCancelable(false);
        mbSingleSign = (!TextUtils.isEmpty(fileId));
    }  
    
    @Override  
    protected void onPreExecute() 
    {
    	mDialog.show();
    }
    
    private String encodeUrl(String url) {
        if(TextUtils.isEmpty(url))
            return url;
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
    
    @Override  
    protected String doInBackground(Void... params) 
    {
        long expired = 0;
        if (!mbSingleSign) {
            expired = System.currentTimeMillis() / 1000 + 3600 * 24 * 30; // 签名有效期一个月
        }

        String devSignHost = "183.61.40.159:5678";
        if (mFileType == FileType.Photo) {
            devSignHost = "web.imagetest.myqcloud.com";
        }

        String cgi = String.format("http://%s/tools/v1/getsign?secret_id=%s&expired=%d&appid=%s&bucket=%s",
                BizService.ENVIRONMENT == ServerEnv.NORMAL ? "web.file.myqcloud.com" : devSignHost,
                mSecretId,
                expired,
                mAppid,
                mBucket
        );

        if (mbSingleSign) {
            String fileid = "/" + mAppid
                    + "/" + mBucket + mFileId;
            if (mFileType == FileType.Photo) {
                fileid = mFileId;
            }

            cgi += "&fileid=" + encodeUrl(fileid);
        }

		try {
			URL url = new URL(cgi);
			Log.i("Demo", "update sign cgi:" + cgi);
	        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	        InputStream in = urlConnection.getInputStream();
            
            byte[] mSocketBuf = new byte[4* 1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int count = 0;
            while ((count = in.read(mSocketBuf, 0, mSocketBuf.length)) > 0) {
                baos.write(mSocketBuf, 0, count);
            }
            
            String config = new String(baos.toByteArray());
            JSONObject jsonData = new JSONObject(config);
            String configContent = jsonData.getString("data");
            JSONObject configJson = new JSONObject(configContent);
            return configJson.getString("sign");
            
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return null;  
    }
    
    @Override  
    protected void onPostExecute(String sign) 
    {  
    	mDialog.dismiss();
    	Log.i("Demo", "update sign response:" + sign);

        if (!TextUtils.isEmpty(sign)) {
            Toast.makeText(mContext, "更新签名成功" + " APPID=" + mAppid, Toast.LENGTH_SHORT).show();

            // mFileId为空说明是长期有效的签名，保存之；否则为单次有效签名，无需保存
            if(!mbSingleSign)
                BizService.getInstance().updateSign(mAppid, mFileType, mBucket, sign);
        } else {
            Toast.makeText(mContext, "更新签名失败" + " APPID=" + mAppid, Toast.LENGTH_SHORT).show();
        }

        if(mListener != null)
            mListener.onSign(sign);
    }
}

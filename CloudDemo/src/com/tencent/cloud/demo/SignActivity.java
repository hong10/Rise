package com.tencent.cloud.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tencent.cloud.demo.common.BizService;

/**
 * 注册签名的页面，在这里可以手动填入APPID、USERID、SIGN的入口以方便调试
 * 
 * @author ianmao
 */
public class SignActivity extends Activity implements OnClickListener
{
	public static final String KEY_APPID = "appid";
    public static final String KEY_FILE_BUCKET = "file_bucket";
    public static final String KEY_PHOTO_BUCKET = "photo_bucket";
    public static final String KEY_VIDEO_BUCKET = "video_bucket";

    private TextView mTextEnv;
	private EditText mEditAppid;
    private EditText mEditFileBucket;
    private EditText mEditPhotoBucket;
    private EditText mEditVideoBucket;

	private Button mBtnCommit;
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        initUI();
    }
	
	private void initUI()
    {
        setContentView(R.layout.activity_sign);

        mTextEnv = (TextView) findViewById(R.id.text_env);
        mEditAppid = (EditText) findViewById(R.id.edit_appid);
        mEditFileBucket = (EditText) findViewById(R.id.edit_file_bucket);
        mEditPhotoBucket = (EditText) findViewById(R.id.edit_photo_bucket);
        mEditVideoBucket = (EditText) findViewById(R.id.edit_video_bucket);
        mBtnCommit = (Button) findViewById(R.id.btn_commit);
        mBtnCommit.setOnClickListener(this);

        mEditAppid.setText(BizService.APPID);
        mEditFileBucket.setText(BizService.FILE_BUCKET);
        mEditPhotoBucket.setText(BizService.PHOTO_BUCKET);
        mEditVideoBucket.setText(BizService.VIDEO_BUCKET);
        mTextEnv.setText(BizService.ENVIRONMENT.getDesc());
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_commit:
			onCommit();
			break;
		default:
			break;
		}
	}
	
	private void onCommit()
	{
		Intent data = new Intent();
        data.putExtra(KEY_APPID, mEditAppid.getText().toString());
        data.putExtra(KEY_FILE_BUCKET, mEditFileBucket.getText().toString());
        data.putExtra(KEY_PHOTO_BUCKET, mEditPhotoBucket.getText().toString());
        data.putExtra(KEY_VIDEO_BUCKET, mEditVideoBucket.getText().toString());
        setResult(RESULT_OK, data);
        finish();
	}
}

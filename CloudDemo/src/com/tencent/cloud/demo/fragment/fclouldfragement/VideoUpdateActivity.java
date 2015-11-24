package com.tencent.cloud.demo.fragment.fclouldfragement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tencent.cloud.demo.R;

/**
 * Created by redickran on 2015/7/9.
 */
public class VideoUpdateActivity extends Activity implements View.OnClickListener{
    public static final String KEY_UPDATE_TYPE = "update_type";
    public static final String KEY_UPDATE_PATH = "update_path";
    public static final String KEY_UPDATE_ATTR = "update_attr";
    public static final String KEY_UPDATE_VIDEO_DESC = "update_video_desc";
    public static final String KEY_UPDATE_VIDEO_CHECK = "update_video_check";
    public static final String KEY_UPDATE_VIDEO_TITLE = "update_video_title";

    private Button mBtnUpdate;
    private TextView mTextViewVideoPath;
    private EditText mEditTextFileAttr;
    private EditText mEditTextVideoTitle;
    private EditText mEditTextVideoDesc;

    private int mUpdateType;
    private String mUpdatePath;
    private String mUpdateFileAttr;
    private String mUpdateVideoDesc;
    private String mUpdateVideoTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_update);
        initialization();
    }

    private void initialization() {
        mBtnUpdate = (Button) findViewById(R.id.btnUpdate);
        mTextViewVideoPath = (TextView) findViewById(R.id.textViewVideoPath);
        mEditTextFileAttr = (EditText) findViewById(R.id.editTextFileAttr);
        mEditTextVideoTitle = (EditText) findViewById(R.id.editTextVideoTitle);
        mEditTextVideoDesc = (EditText) findViewById(R.id.editTextVideoDesc);

        mUpdateType = getIntent().getIntExtra(KEY_UPDATE_TYPE, -1);
        mUpdatePath = getIntent().getStringExtra(KEY_UPDATE_PATH);
        mUpdateFileAttr = getIntent().getStringExtra(KEY_UPDATE_ATTR);
        mUpdateVideoDesc = getIntent().getStringExtra(KEY_UPDATE_VIDEO_DESC);
        mUpdateVideoTitle = getIntent().getStringExtra(KEY_UPDATE_VIDEO_TITLE);

        mBtnUpdate.setOnClickListener(this);
        mTextViewVideoPath.setText(mUpdatePath);
        mEditTextFileAttr.setText(mUpdateFileAttr);
        mEditTextVideoDesc.setText(mUpdateVideoDesc);
        mEditTextVideoTitle.setText(mUpdateVideoTitle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdate:
                onClickBtnUpdate();
                break;
        }
    }

    public void onClickBtnUpdate() {
        Intent intent = new Intent();
        intent.putExtra(KEY_UPDATE_TYPE, mUpdateType);
        intent.putExtra(KEY_UPDATE_PATH, mUpdatePath);
        intent.putExtra(KEY_UPDATE_ATTR, mEditTextFileAttr.getText().toString());
        intent.putExtra(KEY_UPDATE_VIDEO_DESC, mEditTextVideoDesc.getText().toString());
        intent.putExtra(KEY_UPDATE_VIDEO_TITLE, mEditTextVideoTitle.getText().toString());

        VideoUpdateActivity.this.setResult(Activity.RESULT_OK, intent);
        VideoUpdateActivity.this.finish();
    }
}
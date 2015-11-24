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
 * Created by redickran on 2015/5/8.
 */
public class FileUpdateActivity extends Activity implements View.OnClickListener{
    public static final String KEY_UPDATE_TYPE = "update_type";
    public static final String KEY_UPDATE_PATH = "update_path";
    public static final String KEY_UPDATE_ATTR = "update_attr";

    private Button mBtnUpdate;
    private TextView mTextViewUpdPath;
    private EditText mEditTextUpdAttr;

    private int mUpdateType;
    private String mUpdatePath;
    private String mUpdateAttr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_update);
        initialization();
    }

    private void initialization() {
        mBtnUpdate = (Button) findViewById(R.id.btnUpdate);
        mTextViewUpdPath = (TextView) findViewById(R.id.textViewUpdPath);
        mEditTextUpdAttr = (EditText) findViewById(R.id.editTextUpdAttr);
        mUpdateType = getIntent().getIntExtra(KEY_UPDATE_TYPE,-1);
        mUpdatePath = getIntent().getStringExtra(KEY_UPDATE_PATH);
        mUpdateAttr = getIntent().getStringExtra(KEY_UPDATE_ATTR);

        mBtnUpdate.setOnClickListener(this);
        mTextViewUpdPath.setText(mUpdatePath);
        mEditTextUpdAttr.setText(mUpdateAttr);
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
        intent.putExtra(KEY_UPDATE_ATTR, mEditTextUpdAttr.getText().toString());
        FileUpdateActivity.this.setResult(Activity.RESULT_OK, intent);
        FileUpdateActivity.this.finish();
    }
}
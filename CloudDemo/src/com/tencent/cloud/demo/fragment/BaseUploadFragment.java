package com.tencent.cloud.demo.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.cloud.demo.R;
import com.tencent.cloud.demo.common.BizService;
import com.tencent.cloud.demo.common.Utils;
import com.tencent.cloud.demo.fragment.base.BaseFragment;
import com.tencent.upload.task.ITask.TaskState;
import com.tencent.upload.task.UploadTask;

public abstract class BaseUploadFragment extends BaseFragment implements OnClickListener
{
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_LOAD_VIDEO = 2;
    
    public enum FileType {
        Picture, Video, File
    }

    protected UploadTask mUploadTask;
    private final FileType mFileType;
    protected String mFilePath = null;
    protected ImageView mImageView = null;
    protected TextView mTextOutput = null;
    protected TextView mTextUrl = null;
    protected TextView mTextDetail = null;
    
    public BaseUploadFragment(FileType type)
    {
        super();
        mFileType = type;
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View getContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_upload, null);
        initUI(v);
        return v;
    }

    protected void initUI(View v)
    {
        Button btnChoosePic = (Button) v.findViewById(R.id.chooseFile);
        Button btnUpload = (Button) v.findViewById(R.id.upload);
        Button btnPause = (Button) v.findViewById(R.id.pause);
        Button btnDelete = (Button) v.findViewById(R.id.delete);
        Button btnQuery = (Button) v.findViewById(R.id.query);
        Button btnMove = (Button) v.findViewById(R.id.move);
        btnChoosePic.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnQuery.setOnClickListener(this);
        btnMove.setOnClickListener(this);

        mImageView = (ImageView) v.findViewById(R.id.imgView);
        mTextOutput = (TextView) v.findViewById(R.id.output);

        mTextUrl = (TextView) v.findViewById(R.id.url);
        mTextUrl.setOnClickListener(this);
        mTextDetail = (TextView) v.findViewById(R.id.detail);
        
        mTextUrl.setText("");
        mTextDetail.setText("");
        
        if(mUploadTask != null) {
        	mTextOutput.setText("上次任务:" + mUploadTask.getDataSource());
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        case R.id.chooseFile:
            chooseFile(mFileType);
            break;
        case R.id.upload:
            onUploadClicked();
            break;
        case R.id.url:
            toViewFile();
            break;
        case R.id.pause:
            onPauseClicked();
            break;
        case R.id.delete:
            onDeleteClicked();
            break;
        case R.id.query:
            onQueryFileClicked();
            break;
        case R.id.move:
            onMoveFileClicked();
            break;
        default:
            break;
        }
    }

    private void chooseFile(FileType type)
    {
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        int resultCode = RESULT_LOAD_IMAGE;
        if(type == FileType.Video) {
            uri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            resultCode = RESULT_LOAD_VIDEO;
        }
        Intent i = new Intent(Intent.ACTION_PICK, uri);
        startActivityForResult(i, resultCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || data == null)
            return;

        if (requestCode == RESULT_LOAD_IMAGE)
        {
            try
            {
            	Uri selectedImage = data.getData();
                mFilePath = Utils.getPath(getContext(), selectedImage);
                
                Bitmap bmp = Utils.decodeSampledBitmap(mFilePath, 2);
                if(bmp != null)
                    mImageView.setImageBitmap(bmp);
                else
                    mImageView.setImageResource(R.drawable.icon_photo);
            }
            catch (Exception e)
            {
                Log.e("Demo", "choose file error!", e);
            }
            
        } 
        else if(requestCode == RESULT_LOAD_VIDEO) 
        {
        	try 
        	{
        		Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mFilePath = cursor.getString(columnIndex);
                cursor.close();
                mImageView.setImageResource(R.drawable.icon_video);
			} 
        	catch (Exception e) 
			{
        		Log.e("Demo", "choose file error!", e);
			}
        }
    }

    private void clearOutput()
    {
        mTextOutput.setText("上传进度:0%");
        mTextUrl.setText("");
        mTextDetail.setText("");
    }
    
    protected abstract void toViewFile();
    
    private void onUploadClicked()
    {
        if (TextUtils.isEmpty(mFilePath))
        {
            Toast.makeText(getContext(), "请先选择文件", Toast.LENGTH_SHORT).show();
            return;
        }
        
        clearOutput();
        
        if(mUploadTask == null || mUploadTask.getTaskState() == TaskState.SUCCEED || mUploadTask.getTaskState() == TaskState.CANCEL) {
        	// 新建上传任务
            mUploadTask = createUploadTask(mFilePath);
            BizService.getInstance().upload(mUploadTask);
        } else {
        	// 断线续传
        	BizService.getInstance().resume(mUploadTask);
        }
    }
    
    protected abstract UploadTask createUploadTask(String filePath);
    
    private void onPauseClicked() {
        if(mUploadTask == null || mUploadTask.getTaskState() == TaskState.SUCCEED) {
            Toast.makeText(getContext(), "上传任务为空或已经完成.", Toast.LENGTH_SHORT).show();
            return;
        }
        
        BizService.getInstance().pause(mUploadTask);
    }
    
    protected abstract void onDeleteClicked();
    
    protected abstract void onQueryFileClicked();

    protected abstract void onMoveFileClicked();
}

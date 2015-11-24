package com.tencent.cloud.demo.fragment;

import java.util.UUID;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.tencent.cloud.demo.DownloadActivity;
import com.tencent.cloud.demo.common.BizService;
import com.tencent.cloud.demo.common.UpdateSignTask;
import com.tencent.cloud.demo.common.Utils;
import com.tencent.upload.Const;
import com.tencent.upload.task.ITask.TaskState;
import com.tencent.upload.task.IUploadTaskListener;
import com.tencent.upload.task.UploadTask;
import com.tencent.upload.task.data.FileInfo;
import com.tencent.upload.task.impl.FileCopyTask;
import com.tencent.upload.task.impl.FileDeleteTask;
import com.tencent.upload.task.impl.FileStatTask;
import com.tencent.upload.task.impl.PhotoUploadTask;

public class PictureFragment extends BaseUploadFragment
{
    private FileInfo mCurrPhotoInfo = null;

    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    public PictureFragment()
    {
        super(FileType.Picture);
    }

    @Override
    protected UploadTask createUploadTask(String filePath)
    {
    	PhotoUploadTask task = new PhotoUploadTask(filePath, new IUploadTaskListener() {
			
			@Override
			public void onUploadSucceed(final FileInfo result) {
				mMainHandler.post(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        Log.i("xxx", "upload succeed: " + result.url);
                        mCurrPhotoInfo = result;
                        mTextOutput.setText("上传结果:成功!");
                        mTextUrl.setText(Html.fromHtml("<u>" + result.url + "</u>"));

                        mTextDetail.setText("");
                    }
                });
			}
			
			@Override
			public void onUploadStateChange(TaskState state) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onUploadProgress(final long totalSize, final long sendSize) {
				mMainHandler.post(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        long a = (long) ((sendSize * 100) / (totalSize * 1.0f));
                        mTextOutput.setText("上传进度:" + a + "%");
                    }
                });
			}
			
			@Override
			public void onUploadFailed(final int errorCode, final String errorMsg) {
				mMainHandler.post(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        mTextOutput.setText("上传结果:失败! ret:" + errorCode + " msg:" + errorMsg);
                    }
                });
			}
		});
    	task.setBucket(BizService.PHOTO_BUCKET);  // 设置Bucket(可选)
    	task.setFileId("test_fileId_" + UUID.randomUUID()); // 可以为图片自定义FileID(可选)
        task.setAuth(BizService.PHOTO_SIGN);
        return task;
    }
    
    @Override
    protected void onDeleteClicked() {
        if(mCurrPhotoInfo == null) {
            Toast.makeText(getContext(), "还未上传文件.", Toast.LENGTH_SHORT).show();
            return;
        }

        UpdateSignTask updateSignTask = new UpdateSignTask(getContext(), BizService.APPID, Const.FileType.Photo, BizService.PHOTO_SECRETID, BizService.PHOTO_BUCKET, mCurrPhotoInfo.fileId, new UpdateSignTask.OnGetSignListener() {
			
			@Override
			public void onSign(String sign) {
				if(TextUtils.isEmpty(sign)) {
					Toast.makeText(getContext(), "拉取签名失败.", Toast.LENGTH_SHORT).show();
					return;
				}
				
				FileDeleteTask task = new FileDeleteTask(mCurrPhotoInfo.fileId, com.tencent.upload.Const.FileType.Photo, BizService.PHOTO_BUCKET, new FileDeleteTask.IListener() {

					@Override
					public void onSuccess(Void result) {
						mMainHandler.post(new Runnable()
		                {
		                    @Override
		                    public void run()
		                    {
		                        mTextOutput.setText("删除结果:成功!");
		                        mTextDetail.setText("");
		                    }
		                });
					}

					@Override
					public void onFailure(final int ret, final String msg) {
						mMainHandler.post(new Runnable()
		                {
		                    @Override
		                    public void run()
		                    {
		                        mTextOutput.setText("删除结果:失败! ret:" + ret + " msg:" + msg);
		                    }
		                });
					}
		        });

                task.setAuth(sign);
		        BizService.getInstance().sendCommand(task);
			}
		});  
    	updateSignTask.execute();
    }
    
    @Override
    protected void onQueryFileClicked() {
        if(mCurrPhotoInfo == null) {
            Toast.makeText(getContext(), "还未上传文件.", Toast.LENGTH_SHORT).show();
            return;
        }
        
        FileStatTask task = new FileStatTask(mCurrPhotoInfo.fileId, com.tencent.upload.Const.FileType.Photo, BizService.PHOTO_BUCKET, new FileStatTask.IListener() {
            @Override
            public void onSuccess(final FileInfo result) {
                mMainHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mTextUrl.setText(Html.fromHtml("<u>" + result.url + "</u>"));
                        mTextOutput.setText("查询结果:成功!");
                        try {
                        	mTextDetail.setText("MD5:" + result.extendInfo.get("file_md5") + "\nWidth : " + result.extendInfo.get("photo_width") + "\nHeight: "
                                    + result.extendInfo.get("photo_height") + "\n原图大小: " + ((int) (Long.parseLong(result.extendInfo.get("file_size")) / 1024.0)) + " KB"
                                    + "\n图片格式: " + result.extendInfo.get("file_type")
                                    + "\n上传时间: " + Utils.secondToDate(Long.parseLong(result.extendInfo.get("file_upload_time"))));
						} catch (Exception e) {
							
						}
                    }
                });
            }

			@Override
			public void onFailure(final int ret, final String msg) {
				mMainHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mTextOutput.setText("查询结果:失败! ret:" + ret + " msg:" + msg);
                        mTextDetail.setText("");
                    }
                });
			}
        });

        task.setAuth(BizService.PHOTO_SIGN);
        BizService.getInstance().sendCommand(task);
    }

    @Override
    protected void onMoveFileClicked() {
        if (mCurrPhotoInfo == null) {
            Toast.makeText(getContext(), "还未上传文件.", Toast.LENGTH_SHORT).show();
            return;
        }
        
        UpdateSignTask updateSignTask = new UpdateSignTask(getContext(), BizService.APPID, Const.FileType.Photo, BizService.PHOTO_SECRETID, BizService.PHOTO_BUCKET, mCurrPhotoInfo.fileId, new UpdateSignTask.OnGetSignListener() {
			
			@Override
			public void onSign(String sign) {
				if(TextUtils.isEmpty(sign)) {
					Toast.makeText(getContext(), "拉取签名失败.", Toast.LENGTH_SHORT).show();
					return;
				}
				
				FileCopyTask task = new FileCopyTask(com.tencent.upload.Const.FileType.Photo, BizService.PHOTO_BUCKET, mCurrPhotoInfo.fileId, mCurrPhotoInfo.fileId + "_copy" ,new FileCopyTask.IListener() {

					@Override
					public void onSuccess(final FileInfo result) {
						mMainHandler.post(new Runnable()
		                {
		                    @Override
		                    public void run()
		                    {
		                        mTextUrl.setText(Html.fromHtml("<u>" + result.url + "</u>"));
		                        mTextOutput.setText("复制结果:成功!");
		                        mTextDetail.setText(result.fileId);
		                    }
		                });
					}

					@Override
					public void onFailure(final int ret, final String msg) {
						mMainHandler.post(new Runnable()
		                {
		                    @Override
		                    public void run()
		                    {
		                        mTextOutput.setText("复制结果:失败! ret:" + ret + " msg:" + msg);
		                        mTextDetail.setText("");
		                    }
		                });
					}
		        });

                task.setAuth(sign);
		        BizService.getInstance().sendCommand(task);
			}
		});  
    	updateSignTask.execute();
    }

    @Override
    protected void toViewFile()
    {
        if(mCurrPhotoInfo == null || mCurrPhotoInfo.url == null) {
            return;
        }

        String url = mCurrPhotoInfo.url;
        if(url.contains("?"))
        	url = url + "&sign=" + BizService.PHOTO_SIGN;
        else
        	url = url + "?sign=" + BizService.PHOTO_SIGN;
        
        // 跳转PictureDownloadTestActivity，下载图片并展示
        Intent intent = new Intent(getContext(), DownloadActivity.class);
        intent.putExtra(DownloadActivity.KEY_TYPE, DownloadActivity.TYPE_PHOTO);
        intent.putExtra(DownloadActivity.KEY_URL, url);
        startActivity(intent);
    }

    @Override
    protected void onEnvChange(){ }
}

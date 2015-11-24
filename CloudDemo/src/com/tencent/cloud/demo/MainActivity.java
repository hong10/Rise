package com.tencent.cloud.demo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tencent.cloud.demo.common.BizService;
import com.tencent.cloud.demo.common.UpdateSignTask;
import com.tencent.cloud.demo.fragment.fclouldfragement.FClouldFragement;
import com.tencent.cloud.demo.fragment.PictureFragment;
import com.tencent.cloud.demo.fragment.base.BaseFragmentActivity;
import com.tencent.upload.Const.FileType;
import com.tencent.upload.Const.ServerEnv;

public class MainActivity extends BaseFragmentActivity
{
	private static int RESULT_APP_SIGN = 101;

    private static final int MENU_ABOUT = 10000;
	private static final int MENU_REGIST_APPID = 10001;
    private static final int MENU_DEV_ENVIRONMENT = 10002;
    private static final int MENU_NORMAL_ENVIRONMENT = 10003;

    private Handler mMainHandler = null;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = null;
        actionBar = getActionBar();
        actionBar.show();

        mMainHandler = new Handler(getMainLooper());
        mMainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeEnv(ServerEnv.NORMAL);
            }
        }, 1000);
    }
    
    @Override
    public void onDestroy() {
        BizService.getInstance().uploadManagerClose(FileType.File);
        BizService.getInstance().uploadManagerClose(FileType.Photo);
        BizService.getInstance().uploadManagerClose(FileType.Video);
        super.onDestroy();
    }
    
    @Override
    public void onStop() {
        super.onStop();
    }
    
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void initFragmentPages() {
        BizService.getInstance().init(this);
        setTitle("图云" + BizService.APPID + "-"
                + BizService.ENVIRONMENT.getDesc());

        addPage(new PageItem("图片云", new PictureFragment()));
        addPage(new PageItem("文件云", new FClouldFragement(FileType.File)));
        addPage(new PageItem("视频云", new FClouldFragement(FileType.Video)));
    }
    
    @Override  
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_REGIST_APPID, 1, "注册APP");
        menu.add(0, MENU_DEV_ENVIRONMENT, 1, ServerEnv.DEV.getDesc());
        menu.add(0, MENU_NORMAL_ENVIRONMENT, 1, ServerEnv.NORMAL.getDesc());
        menu.add(0, MENU_ABOUT, 1, "关于");
        return true;
    }
    
    @Override  
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == MENU_ABOUT) {
            gotoAboutPage();
        } else if (item.getItemId() == MENU_REGIST_APPID) {
            registApp();
        } else if (item.getItemId() == MENU_DEV_ENVIRONMENT) {
            changeEnv(ServerEnv.DEV);
        } else if (item.getItemId() == MENU_NORMAL_ENVIRONMENT) {
            changeEnv(ServerEnv.NORMAL);
        }

        return super.onContextItemSelected(item);
    }

    private void setAppTitle() {
        setTitle("图云" + BizService.APPID + "-" + BizService.ENVIRONMENT.getDesc());
    }

    private void onChangeEvnFinish() {
        setAppTitle();
        notifyEnvironmentChange();
    }

    private void changeEnv(final ServerEnv env) {
        BizService.getInstance().changeUploadEnv(env);
        updateSign(new UpdateSignTask.OnGetSignListener() {
            @Override
            public void onSign(String sign) {
                onChangeEvnFinish();
                if (TextUtils.isEmpty(sign)) {
                    Log.i(TAG, "change server environment to " + env.getDesc() + " failed");
                } else {
                    Log.i(TAG, "change server environment to:" + env.getDesc() + " success");
                }
            }
        });

        Toast.makeText(this, "切换环境:" + env.getDesc(), Toast.LENGTH_SHORT).show();
    }
    
    /**
     * 更新多次签名
     */
    private void updateSign(UpdateSignTask.OnGetSignListener listener) {

        UpdateSignTask updateSignTask1 = null;
        updateSignTask1 = new UpdateSignTask(this,
                BizService.APPID, FileType.File,
                BizService.FILE_SECRETID, BizService.FILE_BUCKET, null, listener);
        updateSignTask1.execute();

        UpdateSignTask updateSignTask2 = null;
        updateSignTask2 = new UpdateSignTask(this,
                BizService.APPID, FileType.Photo,
                BizService.PHOTO_SECRETID, BizService.PHOTO_BUCKET, null, null);
        updateSignTask2.execute();

        UpdateSignTask updateSignTask3 = null;
        updateSignTask3 = new UpdateSignTask(this,
                BizService.APPID, FileType.Video,
                BizService.VIDEO_SECRETID, BizService.VIDEO_BUCKET, null, null);
        updateSignTask3.execute();
    }
    
    /**
     * 注册APP，提供手动输入APPID、USERID、SIGN的入口以方便调试
     */
    private void registApp() {
        Intent intent = new Intent(this, SignActivity.class);
        startActivityForResult(intent, RESULT_APP_SIGN);
    }
    
    private void gotoAboutPage() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }
    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || data == null)
            return;

        if (requestCode == RESULT_APP_SIGN) {
            String appid = data.getStringExtra(SignActivity.KEY_APPID);
            String fileBucket = data.getStringExtra(SignActivity.KEY_FILE_BUCKET);
            String photoBucket = data.getStringExtra(SignActivity.KEY_PHOTO_BUCKET);
            String videoBucket = data.getStringExtra(SignActivity.KEY_VIDEO_BUCKET);
            if (TextUtils.isEmpty(appid) || TextUtils.isEmpty(fileBucket) || TextUtils.isEmpty(photoBucket)) {
                Toast.makeText(this, "APPID|USERID|FILEBUCKET|PHOTOBUCKET不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            BizService.getInstance().updateSign(appid, FileType.File, fileBucket);
            BizService.getInstance().updateSign(appid, FileType.Photo, photoBucket);
            BizService.getInstance().updateSign(appid, FileType.Video, videoBucket);
            changeEnv(BizService.ENVIRONMENT);
        }
    }
}

package com.tencent.cloud.demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.cloud.demo.common.BizService;
import com.tencent.cloud.demo.common.Utils;
import com.tencent.download.Downloader;
import com.tencent.download.Downloader.DownloadListener;
import com.tencent.download.core.DownloadResult;

import java.io.File;

public class DownloadActivity extends Activity implements View.OnClickListener {
    public static final String KEY_URL = "url";
    public static final String KEY_TYPE = "type";
    public static final String KEY_HTTP_RANGE = "http_range";
    public static final String KEY_CLEAN_DW_CACHE = "clean_dw_cache";
    public static final String KEY_HTTP_KEPPALIVE = "http_keepalive";
    public static final String KEY_HTTP_MAXCONCURRENT = "http_maxconcurrent";

    public static final int TYPE_NONE = 1;
    public static final int TYPE_FILE = 1;
    public static final int TYPE_PHOTO = 2;
    public static final int TYPE_VIDEO = 3;

    private String mURL;
    private int mFileType;
    private int mMaxConcurrent;
    private boolean mEnbaleHttpRange;
    private boolean mEnableKeepAlive;
    private boolean mEnbaleCleanDwCache;

    private ImageView mImageView;
    private TextView mUrlTextView;
    private TextView mTextProgress;
    private ProgressBar mProgressBar;
    private Button mDwRawImageButton;
    private Button mDwThumbImageButton;
    private RelativeLayout mRelativeLayout;

    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    private Downloader mDownloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dwRawImageButton:
                onDownloadRawImage();
                break;

            case R.id.dwThumbImageButton:
                onDwonloadThumbImage();
                break;
        }
    }

    private void initUI() {
        setContentView(R.layout.activity_download);
        mImageView = (ImageView) findViewById(R.id.imgView);
        mUrlTextView = (TextView) findViewById(R.id.urlTextView);
        mTextProgress = (TextView) findViewById(R.id.text_progress);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mDwRawImageButton = (Button) findViewById(R.id.dwRawImageButton);
        mDwThumbImageButton = (Button) findViewById(R.id.dwThumbImageButton);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        mTextProgress.setText("进度:0%");

        mDwRawImageButton.setOnClickListener(this);
        mDwThumbImageButton.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        mURL = intent.getStringExtra(KEY_URL);
        mFileType = intent.getIntExtra(KEY_TYPE, TYPE_NONE);
        mMaxConcurrent = intent.getIntExtra(KEY_HTTP_MAXCONCURRENT, 3);
        mEnbaleHttpRange = intent.getBooleanExtra(KEY_HTTP_RANGE, true);
        mEnableKeepAlive = intent.getBooleanExtra(KEY_HTTP_KEPPALIVE, true);
        mEnbaleCleanDwCache = intent.getBooleanExtra(KEY_CLEAN_DW_CACHE, false);

        initDownloader();

        if (mFileType == TYPE_PHOTO) {
            mProgressBar.setVisibility(View.GONE);
            mTextProgress.setVisibility(View.GONE);
            return;
        }

        if (mFileType == TYPE_VIDEO || mFileType == TYPE_FILE) {
            downloadFile(mURL, mFileType);
            mRelativeLayout.removeView(mUrlTextView);
            mRelativeLayout.removeView(mDwRawImageButton);
            mRelativeLayout.removeView(mDwThumbImageButton);
            return;
        }
    }

    private void initDownloader() {
        mDownloader = new Downloader(this,
                BizService.APPID, "TestDownloader");
        mDownloader.setMaxConcurrent(mMaxConcurrent);
        mDownloader.enableHTTPRange(mEnbaleHttpRange);
        mDownloader.enableKeepAlive(mEnableKeepAlive);
        if (mEnbaleCleanDwCache) {
            mDownloader.cleanCache();
        }
    }

    private String getThumbUrl(String url) {
        return url.replace("original", "test");
    }

    private void downloadImage(String url) {
        if (downloadFile(url, TYPE_PHOTO) > 0) {
            mImageView.setImageBitmap(null);
            mProgressBar.setVisibility(View.VISIBLE);
            mTextProgress.setVisibility(View.VISIBLE);
        }

        int begidx = url.indexOf("&sign=");
        if (begidx <= -1) {
            begidx = url.indexOf("?sign=");
        }
        if (begidx <= -1) {
            begidx = url.length();
        }

        int endidx = url.indexOf("&", begidx + 1);
        if (endidx <= -1) {
            endidx = url.length();
        }

        String url2 = null;
        url2 = url.substring(0, begidx) + url.substring(endidx);
        mUrlTextView.setText(Html.fromHtml("<u>" + url2 + "</u>"));
    }

    private void onDownloadRawImage() {
        downloadImage(mURL);
    }

    private void onDwonloadThumbImage() {
        downloadImage(getThumbUrl(mURL));
    }

    private void onDownloadCompleted(String url, int fileType, String filePath) {
        if (fileType == TYPE_FILE) {
            showFile(filePath);
        } else if (fileType == TYPE_PHOTO) {
            showImage(filePath);
        } else if (fileType == TYPE_VIDEO) {
            showVideo(filePath);
        }
    }

    private int downloadFile(String url, final int fileType) {
        Log.w("DownloadTestActivity", "download url:" + url + " type:" + fileType);
        if (TextUtils.isEmpty(url))
            return -1;

        // 先查找是否有本地缓存文件
        if (mDownloader.hasCache(url)) {
            File file = mDownloader.getCacheFile(url);
            if (file != null && file.exists()) {
                onDownloadCompleted(url, fileType, file.getAbsolutePath());
                Toast.makeText(this, "命中缓存", Toast.LENGTH_SHORT).show();
                return 0;
            }
        }

        // 开始下载
        mDownloader.download(url, new DownloadListener() {
            @Override
            public void onDownloadSucceed(final String url, DownloadResult result) {
                final String path = result.getPath();
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onDownloadCompleted(url, fileType, path);
                    }
                });
            }

            @Override
            public void onDownloadProgress(String url, long totalSize, float progress) {
                final int nProgress = (int) (progress * 100);
                mMainHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        mTextProgress.setText("进度:" + nProgress + "%");
                    }
                });
            }

            @Override
            public void onDownloadFailed(String url, final DownloadResult result) {
                mMainHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        mTextProgress.setText("下载失败！ " + result.getMessage() + " ret:" + result.getErrorCode());
                    }
                });
            }

            @Override
            public void onDownloadCanceled(String url) {
                mMainHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        mTextProgress.setText("取消下载");
                    }
                });
            }
        });
        Toast.makeText(this, "正在下载文件......", Toast.LENGTH_SHORT).show();
        return 1;
    }

    private void showFile(String file_path) {
        mProgressBar.setVisibility(View.GONE);
        mTextProgress.setVisibility(View.GONE);
        Toast.makeText(this, "路径:" + file_path, Toast.LENGTH_LONG).show();
    }

    private void showImage(String file_path) {
        mProgressBar.setVisibility(View.GONE);
        mTextProgress.setVisibility(View.GONE);

        Bitmap bmp = null;
        try {
            bmp = Utils.decodeSampledBitmap(file_path, 2);
            if (bmp != null) {
                mImageView.setImageBitmap(bmp);
            } else {
                mTextProgress.setVisibility(View.VISIBLE);
                mTextProgress.setText("解码失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showVideo(String file_path) {
        mProgressBar.setVisibility(View.GONE);
        mTextProgress.setVisibility(View.GONE);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(file_path)), "video/*");
        startActivity(intent);
    }
}

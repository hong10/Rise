package com.tencent.cloud.demo.fragment.fclouldfragement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.cloud.demo.DownloadActivity;
import com.tencent.cloud.demo.R;
import com.tencent.cloud.demo.common.BizService;
import com.tencent.cloud.demo.common.Utils;
import com.tencent.cloud.demo.fragment.base.BaseFragment;
import com.tencent.upload.Const.FileType;
import com.tencent.upload.task.Dentry;
import com.tencent.upload.task.ITask;
import com.tencent.upload.task.VideoAttr;
import com.tencent.upload.task.VideoInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by redickran on 2015/5/6.
 */
@SuppressWarnings("ValidFragment")
public class FClouldFragement extends BaseFragment implements OnClickListener
{
    private interface MenuOprItemListener {
        public void onClick();
    }

    private class MenuOprItem {
        public String name;
        public MenuOprItemListener listener;
    }

    private FileType mFileType;
    private View mMakeDirDlgView;
    private EditText mEditTextDirName;

    private View mSetOptionDlgView;
    private EditText mEditTextPageSize;
    private CheckBox mCheckBoxLsPatnDir;
    private CheckBox mCheckBoxLsPatnFile;
    private RadioGroup mRadioGroupLsOrder;
    private RelativeLayout mLayoutUploadStatus;

    private CheckBox mCheckBoxHttpRange;
    private CheckBox mCheckBoxCleanDwCache;
    private CheckBox mCheckBoxHttpKeepAlive;
    private EditText mEditTextMaxConcurrent;

    private TextView mTextViewPath;
    private TextView mTextViewClear;
    private TextView mTextViewPause;
    private TextView mTextViewResume;
    private TextView mTextViewCancel;
    private TextView mTextViewBackDir;
    private TextView mTextViewMakeDir;
    private TextView mTextViewMoreOpr;
    private TextView mTextViewProgress;

    private MyBaseAdapter mAdapter;
    private TextView mTextViewEmpty;
    private ListView mListViewDirList;
    private ImageView mImageViewEmpty;

    private String mCurPath;
    private String mRootPath;
    private DentryCache mDentryCache;
    private ProgressDialog mProgressDialog;
    private FClouldDataLayer mFClouldDataLayer;
    private ArrayList<MenuOprItem> mMenuArrayList;
    FClouldDataLayer.IListDirListener mIListDirListener;

    private boolean mIsInitFinish = false;

    private static final int LS_PULL_MODE_NORMAL = 0;
    private static final int LS_PULL_MODE_PREFIX = 1;
    private static final int LS_PULL_MODE_LOCALCACHE = 2;

    private static final int MENU_STAT   = 10001;
    private static final int MENU_DELETE = 10002;
    private static final int MENU_UPDATE = 10003;

    public static final int FILEUPDATE_REQUEST_CODE = 11001;
    public static final int FILESELECT_REQUEST_CODE = 11002;
    public static final int VIDEOUPDATE_REQUEST_CODE = 11003;
    public static final int VIDEOSELECT_REQUEST_CODE = 11004;


    public FClouldFragement(FileType fileType) {
        mFileType = fileType;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialization();
    }

    @Override
    public View getContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater.inflate(R.layout.fragement_fcloud, container, false));
    }

    @Override
    public boolean isInitFinish() {
        return mIsInitFinish;
    }

    @Override
    protected void onEnvChange() {
        resetEnvironment();
    }

    private String getCurPath() {
        if (TextUtils.isEmpty(mCurPath)) {
            return getRootPath();
        }

        int lastPos = mCurPath.length() - 1;
        if (mCurPath.charAt(lastPos) == '/') {
            return mCurPath;
        }

        lastPos = mCurPath.lastIndexOf("/");
        if (lastPos < 0) {
            return getRootPath();
        }

        return mCurPath.substring(0, lastPos + 1);
    }
    private void setCurPath(String path) {
        mCurPath = path;
    }

    private String getRootPath() {
        return "/";
    }

    private String getBucket() {
        if (mFileType == FileType.File) {
            return BizService.FILE_BUCKET;
        }

        if (mFileType == FileType.Video) {
            return BizService.VIDEO_BUCKET;
        }

        return "";
    }

    private String getSign() {
        if (mFileType == FileType.File) {
            return BizService.FILE_SIGN;
        }

        if (mFileType == FileType.Video) {
            return BizService.VIDEO_SIGN;
        }

        return "";
    }

    private void setTextViewPath(String path) {
        String viewPath = "/" + BizService.APPID
                + "/" + getBucket() + path;

        mTextViewPath.setText(viewPath);
    }

    private String getTextViewPath() {
        String viewPath = mTextViewPath.getText().toString();
        String rootPath = "/" + BizService.APPID + "/" + getBucket();
        int pos = viewPath.indexOf(rootPath, 0);
        if (pos != 0) {
            return "/";
        }

        return viewPath.substring(rootPath.length());
    }

    private void resetEnvironment() {
        // 重置环境变量
        mRootPath = getRootPath();
        setCurPath(getRootPath());

        // 清空目录缓存
        mAdapter.clean();
        mDentryCache.cleanCache();
        setTextViewPath(getCurPath());

        // 重新拉取根目录
        doListDirProccess(new Dentry(Dentry.DIR).setPath(mRootPath), LS_PULL_MODE_NORMAL);
    }

    private void initialization() {
        mRootPath = getRootPath();
        setCurPath(getRootPath());

        mDentryCache = new DentryCache(30000);
        mAdapter = new MyBaseAdapter(getContext());
        mFClouldDataLayer = new FClouldDataLayer(
                getContext(), mFileType
        );
        mFClouldDataLayer.loadConfigOption();

        mMenuArrayList = new ArrayList<MenuOprItem>();

        registerOprItem("刷新目录", new MenuOprItemListener() {
            @Override
            public void onClick() {
                OnClickRefresh();
            }
        });

        registerOprItem("目录搜索", new MenuOprItemListener() {
            @Override
            public void onClick() {
                OnClickPrefixSearch();
            }
        });

        if (mFileType == FileType.File) {
            registerOprItem("上传文件", new MenuOprItemListener() {
                @Override
                public void onClick() {
                    OnClickUplaodFile();
                }
            });
        }

        if (mFileType == FileType.Video) {
            registerOprItem("上传视频", new MenuOprItemListener() {
                @Override
                public void onClick() {
                    OnClickUplaodVideo();
                }
            });
        }

        registerOprItem("设置选项", new MenuOprItemListener() {
            @Override
            public void onClick() {
                OnClickSetOption();
            }
        });
    }

    private View initView(View view) {

        mTextViewPath = (TextView) view.findViewById(R.id.textViewPath);
        mTextViewEmpty = (TextView) view.findViewById(R.id.textViewEmpty);
        mImageViewEmpty = (ImageView) view.findViewById(R.id.imageViewEmpty);
        mTextViewClear = (TextView) view.findViewById(R.id.textViewClear);
        mTextViewPause = (TextView) view.findViewById(R.id.textViewPause);
        mTextViewResume = (TextView) view.findViewById(R.id.textViewResume);
        mTextViewCancel = (TextView) view.findViewById(R.id.textViewCancel);
        mTextViewBackDir = (TextView) view.findViewById(R.id.textViewBackDir);
        mTextViewMakeDir = (TextView) view.findViewById(R.id.textViewMakeDir);
        mTextViewMoreOpr = (TextView) view.findViewById(R.id.textViewMoreOpr);
        mListViewDirList = (ListView) view.findViewById(R.id.listViewDirList);
        mTextViewProgress = (TextView) view.findViewById(R.id.textViewProgress);
        mLayoutUploadStatus = (RelativeLayout) view.findViewById(R.id.layoutUploadStatus);

        LayoutInflater factory = LayoutInflater.from(getContext());

        mMakeDirDlgView = factory.inflate(R.layout.view_make_dir_dlg, null);
        mEditTextDirName = (EditText) mMakeDirDlgView.findViewById(R.id.editTextDirName);

        mSetOptionDlgView = factory.inflate(R.layout.view_fcloud_option_dlg, null);
        mEditTextPageSize = (EditText) mSetOptionDlgView.findViewById(R.id.editTextIp);
        mCheckBoxLsPatnDir = (CheckBox) mSetOptionDlgView.findViewById(R.id.checkBoxLsPatnDir);
        mCheckBoxLsPatnFile = (CheckBox) mSetOptionDlgView.findViewById(R.id.checkBoxLsPatnFile);
        mRadioGroupLsOrder = (RadioGroup) mSetOptionDlgView.findViewById(R.id.radioGroupLsOrder);

        mCheckBoxHttpRange = (CheckBox) mSetOptionDlgView.findViewById(R.id.checkBoxHttpRange);
        mCheckBoxCleanDwCache = (CheckBox) mSetOptionDlgView.findViewById(R.id.checkBoxCleanDwCache);
        mCheckBoxHttpKeepAlive = (CheckBox) mSetOptionDlgView.findViewById(R.id.checkBoxHttpKeepAlive);
        mEditTextMaxConcurrent = (EditText) mSetOptionDlgView.findViewById(R.id.editTextMaxConcurrent);

        setTextViewPath(getRootPath());
        mListViewDirList.setAdapter(mAdapter);
        mIListDirListener = getListDirListener();
        mTextViewBackDir.setOnClickListener(this);
        mTextViewMakeDir.setOnClickListener(this);
        mTextViewMoreOpr.setOnClickListener(this);
        mTextViewBackDir.setTextColor(getResources().getColor(R.color.blue));
        mTextViewMakeDir.setTextColor(getResources().getColor(R.color.blue));
        mTextViewMoreOpr.setTextColor(getResources().getColor(R.color.blue));

        mTextViewClear.setOnClickListener(this);
        mTextViewPause.setOnClickListener(this);
        mTextViewResume.setOnClickListener(this);
        mTextViewCancel.setOnClickListener(this);
        mTextViewClear.setTextColor(getResources().getColor(R.color.blue));
        mTextViewPause.setTextColor(getResources().getColor(R.color.blue));
        mTextViewResume.setTextColor(getResources().getColor(R.color.blue));
        mTextViewCancel.setTextColor(getResources().getColor(R.color.blue));

        mListViewDirList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClick(view);
            }
        });

        mListViewDirList.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, MENU_DELETE, 0, "删除");
                menu.add(0, MENU_STAT,   0, "查询");
                menu.add(0, MENU_UPDATE, 0, "更新");
            }
        });

        if (mFClouldDataLayer.getOrder() == false) {
            mRadioGroupLsOrder.check(R.id.radioButtonAsc);
        } else {
            mRadioGroupLsOrder.check(R.id.radioButtonDesc);
        }
        int pageSize = mFClouldDataLayer.getPageSize();
        mEditTextPageSize.setText(Integer.toString(pageSize));

        if (mFClouldDataLayer.getListPattern() == Dentry.ListBoth) {
            mCheckBoxLsPatnDir.setChecked(true);
            mCheckBoxLsPatnFile.setChecked(true);
        }

        if (mFClouldDataLayer.getListPattern() == Dentry.ListDirOnly) {
            mCheckBoxLsPatnDir.setChecked(true);
            mCheckBoxLsPatnFile.setChecked(false);
        }

        if (mFClouldDataLayer.getListPattern() == Dentry.ListFileOnly) {
            mCheckBoxLsPatnDir.setChecked(false);
            mCheckBoxLsPatnFile.setChecked(true);
        }

        mCheckBoxHttpRange.setChecked(mFClouldDataLayer.getEnbaleHttpRange());
        mCheckBoxCleanDwCache.setChecked(mFClouldDataLayer.getEnableCleanDwCache());
        mCheckBoxHttpKeepAlive.setChecked(mFClouldDataLayer.getEnableHttpKeepAlive());
        mEditTextMaxConcurrent.setText(Integer.toString(mFClouldDataLayer.getMaxConcurrent()));

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("努力加载中...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        if (needRefresh()) {
            resetEnvironment();
        }

        mIsInitFinish = true;

        return view;
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (!getUserVisibleHint()) {
            return super.onContextItemSelected(item);
        }

        AdapterContextMenuInfo info = null;
        info = (AdapterContextMenuInfo)item.getMenuInfo();
        if (info == null || info.targetView == null) {
            return true;
        }

        ViewHolder holder = (ViewHolder)info.targetView.getTag();
        if (holder == null|| holder.dentry == null) {
            return true;
        }

        Dentry dentry = holder.dentry;
        switch (item.getItemId()) {
            case MENU_STAT:
                onSelectedStatItem(dentry);
                break;
            case MENU_DELETE:
                onSelectedDeleteItem(dentry);
                break;
            case MENU_UPDATE:
                onSelectedUpdateItem(dentry);
                break;
        }

        return true;
    }

    private void onSelectedStatItem(Dentry dentry) {
        mFClouldDataLayer.asyncStat(dentry, new FClouldDataLayer.IStatListener() {
            @Override
            public void onStatSuccess(String path, Dentry dentry) {
                String msg = "";
                msg += "type: " + dentry.type + "\n";
                msg += "path: " + dentry.path + "\n";
                msg += "name: " + dentry.name + "\n";
                msg += "accessUrl: " + dentry.accessUrl + "\n";
                msg += "attribute: " + dentry.attribute + "\n";

                msg += "fileSize: " + dentry.fileSize + "\n";
                msg += "fileLength: " + dentry.fileLength + "\n";
                msg += "createTime: " + dentry.createTime + "\n";
                msg += "modifyTime: " + dentry.modifyTime + "\n";

                msg += "sha: " + dentry.sha + "\n\n";

                if (dentry.type == Dentry.VIDEO) {
                    VideoInfo videoInfo = dentry.getVideoInfo();
                    if (videoInfo != null) {
                        msg += "videoTitle: " + videoInfo.videoAttr.title + "\n";
                        msg += "videoDesc: " + videoInfo.videoAttr.desc + "\n";
                        msg += "videoStatus: " + videoInfo.videoStatus + "\n";
                        msg += "videoIsCheck: " + videoInfo.videoAttr.isCheck + "\n";
                        msg += "videoTimeLen: " + videoInfo.videoAttr.timeLen + "\n";
                        msg += "transStatus->\n";
                        for (Integer key : videoInfo.transStatus.keySet()) {
                            msg += "            key: " + key + "  -  "
                                    + videoInfo.transStatus.get(key) + "\n";
                        }

                        msg += "playUrlList->\n";
                        for (Integer key : videoInfo.playUrlList.keySet()) {
                            msg += "            key: " + key + "  -  "
                                    + videoInfo.playUrlList.get(key) + "\n";
                        }
                    }
                }

                Intent intent = new Intent(getContext(), StatActivity.class);
                intent.putExtra(StatActivity.KEY_STAT_RESULT, msg);
                startActivity(intent);
            }

            @Override
            public void onStatFailure(String path, int errcode, String errmsg) {
                String msg = "查询失败:" + path;
                msg += "  errcode:" + errcode + " errmsg:" + errmsg;
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onSelectedDeleteItem(Dentry dentry) {
        mFClouldDataLayer.asyncDelete(dentry, new FClouldDataLayer.IDeleteListener() {
            @Override
            public void onDeleteSuccess(String path) {
                mDentryCache.removeCache(path);
                mDentryCache.removeCacheItem(getCurPath(), path);
                doListDirProccess(new Dentry(Dentry.DIR).setPath(getCurPath()), LS_PULL_MODE_LOCALCACHE);
            }

            @Override
            public void onDeleteFailure(String path, int errcode, String errmsg) {
                String msg = "删除失败:" + path;
                msg += "  errcode:" + errcode + " errmsg:" + errmsg;
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onSelectedUpdateItem(Dentry dentry) {
        if (dentry.type == Dentry.VIDEO) {
            VideoInfo videoInfo = dentry.getVideoInfo();
            Intent intent = new Intent(getContext(), VideoUpdateActivity.class);
            intent.putExtra(VideoUpdateActivity.KEY_UPDATE_TYPE, dentry.type);
            intent.putExtra(VideoUpdateActivity.KEY_UPDATE_PATH, dentry.path);
            intent.putExtra(VideoUpdateActivity.KEY_UPDATE_ATTR, dentry.attribute);
            intent.putExtra(VideoUpdateActivity.KEY_UPDATE_VIDEO_DESC, videoInfo.videoAttr.desc);
            intent.putExtra(VideoUpdateActivity.KEY_UPDATE_VIDEO_TITLE, videoInfo.videoAttr.title);
            startActivityForResult(intent, VIDEOUPDATE_REQUEST_CODE);
            return;
        }

        Intent intent = new Intent(getContext(), FileUpdateActivity.class);
        intent.putExtra(FileUpdateActivity.KEY_UPDATE_TYPE, dentry.type);
        intent.putExtra(FileUpdateActivity.KEY_UPDATE_PATH, dentry.path);
        intent.putExtra(FileUpdateActivity.KEY_UPDATE_ATTR, dentry.attribute);
        startActivityForResult(intent, FILEUPDATE_REQUEST_CODE);
        return;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || data == null)
            return;

        switch (requestCode) {
            case FILEUPDATE_REQUEST_CODE:
                onFileUpdateActivityResult(data);
                break;

            case FILESELECT_REQUEST_CODE:
                onFileSelectActivityResult(data);
                break;

            case VIDEOUPDATE_REQUEST_CODE:
                onVideoUpdateActivityResult(data);
                break;

            case VIDEOSELECT_REQUEST_CODE:
                onVideoSelectActivityResult(data);
                break;
        }
    }

    private void doUpdateProccess(Dentry dentry, Object externData) {
        mFClouldDataLayer.asyncUpdate(dentry, externData, new FClouldDataLayer.IUpdateListener() {
            @Override
            public void onUpdateSuccess(String path) {
                String msg  = "更新成功:" + path;
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

                String strCurPath = getCurPath();
                Dentry dentry = new Dentry(Dentry.DIR)
                        .setPath(strCurPath);
                mDentryCache.removeCache(strCurPath);
                doListDirProccess(dentry, LS_PULL_MODE_NORMAL);
            }

            @Override
            public void onUpdateFailure(String path, int errcode, String errmsg) {
                String msg  = "更新失败:" + path;
                msg += "  errcode:" + errcode + " errmsg:" + errmsg;
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onFileUpdateActivityResult(Intent data) {
        int type = data.getIntExtra(FileUpdateActivity.KEY_UPDATE_TYPE, -1);
        String path = data.getStringExtra(FileUpdateActivity.KEY_UPDATE_PATH);
        String attr = data.getStringExtra(FileUpdateActivity.KEY_UPDATE_ATTR);
        doUpdateProccess(new Dentry(type).setPath(path).setAttribute(attr), null);
    }

    private FClouldDataLayer.IListDirListener getListDirListener() {
        return new FClouldDataLayer.IListDirListener() {
            @Override
            public void onListDirFailure(String path, int errcode, String errmsg) {
                mProgressDialog.dismiss();
                String msg  = "拉取目录失败:" + path;
                msg += "  errcode:" + errcode + " errmsg:" + errmsg;
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onListDirSuccess(String path, ArrayList<Dentry> dentrList) {
                setCurPath(path);
                mProgressDialog.dismiss();
                setTextViewPath(path);
                ArrayList<Dentry> cacheDentryList;
                mDentryCache.appendCache(path, dentrList);
                cacheDentryList = mDentryCache.getCache(path);
                if (cacheDentryList.size() > 0) {
                    mTextViewEmpty.setVisibility(View.GONE);
                    mImageViewEmpty.setVisibility(View.GONE);
                } else {
                    mTextViewEmpty.setVisibility(View.VISIBLE);
                    mImageViewEmpty.setVisibility(View.VISIBLE);
                }

                mAdapter.addDatas(cacheDentryList);
            }
        };
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewBackDir:
                onClickBackDir();
                break;

            case R.id.textViewMakeDir:
                onClickMakeDir();
                break;

            case R.id.textViewMoreOpr:
                onClickMoreOpr();
                break;

            case R.id.viewListItem:
                onClickViewListItem(view);
                break;

            case R.id.textViewClear:
                onClickClearTask();
                break;

            case R.id.textViewPause:
                onClickPauseUploadTask();
                break;

            case R.id.textViewResume:
                onClickResumeUploadTask();
                break;

            case R.id.textViewCancel:
                onClickCancelUploadTask();
                break;
        }
    }

    private void onClickBackDir() {
        String path = getTextViewPath();
        if (TextUtils.isEmpty(path)) {
            return;
        }

        String backPath = path;
        if (!path.equals("/")) {
            int endpos = path.length() - 1;
            endpos  = path.lastIndexOf("/", endpos - 1);
            backPath = path.substring(0, endpos + 1);
        }

        if (backPath.indexOf(mRootPath) < 0) {
            backPath = mRootPath;
        }

        doListDirProccess(new Dentry(Dentry.DIR).setPath(backPath), LS_PULL_MODE_LOCALCACHE);
    }

    private void doListDirProccess(Dentry dentry, int mode)
    {
        if (dentry == null
                || dentry.type == Dentry.FILE
                || dentry.type == Dentry.VIDEO
                || TextUtils.isEmpty(dentry.path)) {
            return;
        }

        String path = dentry.path;
        ArrayList<Dentry> dentryList = null;
        dentryList = mDentryCache.getCache(path);
        if (mode == LS_PULL_MODE_LOCALCACHE && dentryList != null) {
            mIListDirListener.onListDirSuccess(path, dentryList);
            return;
        }

        if (mode == LS_PULL_MODE_PREFIX) {
            mProgressDialog.show();
            if(!mFClouldDataLayer.asyncListDir(dentry, true, getListDirListener())) {
                mProgressDialog.dismiss();
            }
            return;
        }

        // 默认拉取方式
        mProgressDialog.show();
        if (!mFClouldDataLayer.asyncListDir(dentry, false, getListDirListener())) {
            mProgressDialog.dismiss();
        }
    }


    private void onClickViewDir(Dentry dentry) {
        doListDirProccess(dentry, LS_PULL_MODE_NORMAL);
    }

    private String genSignUrl(String url) {
        int endIdx = url.indexOf("#");
        if (endIdx >= 0) {
            url = url.substring(0, endIdx);
        }

        String query = "&";
        endIdx = url.indexOf("?");
        if (endIdx < 0) {
            query = "?";
            endIdx = url.length();
        } else if (endIdx + 1 == url.length()) {
            query = "";
        }

        query += "SIGN=" + getSign();
        query = url.substring(endIdx) + query;

        return url.substring(0, endIdx) + query;
    }

    private void onClickViewFile(Dentry dentry) {
    	if(dentry == null || TextUtils.isEmpty(dentry.accessUrl)) {
            return;
        }

        // 如果fileLength和fileSize不相等则表示上传失败
        if (dentry.fileLength != dentry.fileSize) {
            String msg = "文件上传失败，请删除文件索引：" + dentry.name;
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            return;
        }

        downloadUrl(dentry.accessUrl);
    }

    private void onClickViewVideo(Dentry dentry) {
        if(dentry == null || TextUtils.isEmpty(dentry.accessUrl)) {
            return;
        }

        // 如果fileLength和fileSize不相等则表示上传失败
        if (dentry.fileLength != dentry.fileSize) {
            String msg = "文件上传失败，请删除文件索引：" + dentry.name;
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            return;
        }

        VideoInfo videInfo = dentry.getVideoInfo();
        if (videInfo == null) {
            return;
        }

        downloadUrl(videInfo.playUrlList.get(VideoInfo.F10));
    }

    private void downloadUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        if (!TextUtils.isEmpty(getSign())) {
            url = genSignUrl(url);
        }

        // 跳转PictureDownloadTestActivity，下载文件到本地磁盘目录
        Intent intent = new Intent(getContext(), DownloadActivity.class);
        intent.putExtra(DownloadActivity.KEY_URL, url);
        if (mFileType == FileType.File) {
            intent.putExtra(DownloadActivity.KEY_TYPE, DownloadActivity.TYPE_FILE);
        }

        if (mFileType == FileType.Video) {
            intent.putExtra(DownloadActivity.KEY_TYPE, DownloadActivity.TYPE_VIDEO);
        }

        intent.putExtra(DownloadActivity.KEY_HTTP_RANGE, mFClouldDataLayer.getEnbaleHttpRange());
        intent.putExtra(DownloadActivity.KEY_CLEAN_DW_CACHE, mFClouldDataLayer.getEnableCleanDwCache());
        intent.putExtra(DownloadActivity.KEY_HTTP_KEPPALIVE, mFClouldDataLayer.getEnableHttpKeepAlive());
        intent.putExtra(DownloadActivity.KEY_HTTP_MAXCONCURRENT, mFClouldDataLayer.getMaxConcurrent());

        startActivity(intent);
    }

    private void onClickViewMore(Dentry dentry) {
        doListDirProccess(dentry, LS_PULL_MODE_NORMAL);
    }

    private void onClickViewListItem(View view) {
        ViewHolder holder = (ViewHolder)view.getTag();
        if (holder == null) {
            return;
        }

        Dentry denry = holder.dentry;
        if (denry == null) {
            return;
        }

        if (denry.type == Dentry.DIR) {
            onClickViewDir(denry);
        } else if (denry.type == Dentry.FILE) {
            onClickViewFile(denry);
        }  else if (denry.type == Dentry.VIDEO) {
            onClickViewVideo(denry);
        } else {
            onClickViewMore(denry);
        }
    }

    private void onClickMakeDir() {
        AlertDialog.Builder makeDirDlg = null;
        makeDirDlg = new AlertDialog.Builder(getContext());
        makeDirDlg.setTitle("请输入文件夹名称");
        makeDirDlg.setView(getView(mMakeDirDlgView));
        makeDirDlg.setPositiveButton("创建", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doMakeDirProccess(mEditTextDirName.getText().toString());
            }
        });
        makeDirDlg.setNegativeButton("取消", null);

        makeDirDlg.show();
    }

    private void doMakeDirProccess(String dirName)
    {
        if (TextUtils.isEmpty(dirName)) {
            Toast.makeText(getContext(), "目录不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        int lastpos = dirName.length() - 1;
        if (dirName.charAt(lastpos) != '/') {
            dirName += "/";
        }

        String path = getCurPath() + dirName;
        if (dirName.charAt(0) == '/') {
            path = dirName;
        }

        Dentry dentry = new Dentry(Dentry.DIR).setPath(path).setAttribute("");
        mFClouldDataLayer.asyncMkDir(dentry, new FClouldDataLayer.IMkDirListener() {
            @Override
            public void onMkDirSuccess(String path, String accessUrl) {
                mDentryCache.removeCache(getCurPath());
                Dentry dentry = new Dentry(Dentry.DIR).setPath(path);
                mFClouldDataLayer.asyncListDir(dentry, false, getListDirListener());
            }

            @Override
            public void onMkDirFailure(String path, int errcode, String errmsg) {
                String msg = "创建目录失败:" + path;
                msg += "  errcode:" + errcode + " errmsg:" + errmsg;
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    String[] getOprArrayList() {
        int i = 0;
        int size = mMenuArrayList.size();
        String[] strOprList = new String[size];
        for (MenuOprItem opr : mMenuArrayList) {
            strOprList[i++] = opr.name;
        }

        return strOprList;
    }

    private void doMoreOprProcess(int which) {
        int menusize = mMenuArrayList.size();
        if (which < 0 || which >= menusize) {
            return;
        }

        MenuOprItem opr = mMenuArrayList.get(which);
        if (opr == null || opr.listener == null) {
            return;
        }

        opr.listener.onClick();
    }

    void registerOprItem(String name, MenuOprItemListener listener) {
        MenuOprItem oprItem = null;
        for (MenuOprItem opr : mMenuArrayList) {
            if (opr.name.equals(name)) {
                oprItem = opr;
                break;
            }
        }

        if (oprItem != null) {
            oprItem.name = name;
            oprItem.listener = listener;
            return;
        }

        oprItem = new MenuOprItem();
        oprItem.name = name;
        oprItem.listener = listener;
        mMenuArrayList.add(oprItem);
    }

    private void onClickMoreOpr() {
        AlertDialog.Builder moreOprDlg = null;
        moreOprDlg = new AlertDialog.Builder(getContext());
        moreOprDlg.setTitle("更多操作");
        moreOprDlg.setItems(getOprArrayList(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doMoreOprProcess(which);
            }
        });

        moreOprDlg.show();
    }

    private void OnClickRefresh() {
        String path = getCurPath();
        if (TextUtils.isEmpty(path)) {
            path = getRootPath();
        }

        if (TextUtils.isEmpty(path)) {
            return;
        }

        mDentryCache.removeCache(path);
        doListDirProccess(new Dentry(Dentry.DIR).setPath(path), LS_PULL_MODE_NORMAL);
    }

    private void OnClickPrefixSearch() {
        final EditText view = new EditText(getContext());
        PopMessageBox("请输入前缀", "查找", "取消", view,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doPrefixSearchProccess(getCurPath(), view.getText().toString());
                    }
                },
                null
        );
    }

    private void doPrefixSearchProccess(String path, String prefix) {
        if (TextUtils.isEmpty(path)
                || TextUtils.isEmpty(prefix)) {
            Toast.makeText(getContext(), "前缀不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        mDentryCache.removeCache(path);
        String startPath = path + prefix;
        doListDirProccess(new Dentry(Dentry.DIR).setPath(startPath), LS_PULL_MODE_PREFIX);
    }

    private void onClickClearTask() {
        if (mFClouldDataLayer.isUploading()) {
            mFClouldDataLayer.clear();
        }
    }

    private void onClickPauseUploadTask() {
        if (mFClouldDataLayer.isUploading()) {
            mFClouldDataLayer.pauseUploadTask();
        }
    }

    private void onClickResumeUploadTask() {
        if (mFClouldDataLayer.isUploading()) {
            mFClouldDataLayer.resumeUploadTask();
        }
    }

    private void onClickCancelUploadTask() {
        if (mFClouldDataLayer.isUploading()) {
            mFClouldDataLayer.cancelUploadTask();
        }
    }

    /**
     * 上传文件到当前目录
     * step1: 选择文件
     * step2: 上传文件
     */
    private void OnClickUplaodFile() {
        if (mFClouldDataLayer.isUploading()) {
            Toast.makeText(getActivity(), "正在上传，请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),
                    FILESELECT_REQUEST_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getActivity(), "请安装文件管理器", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 上传视频到当前目录
     * step1: 选择文件
     * step2: 上传文件
     */
    private void OnClickUplaodVideo() {
        if (mFClouldDataLayer.isUploading()) {
            Toast.makeText(getActivity(), "正在上传，请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            );
            startActivityForResult(intent, VIDEOSELECT_REQUEST_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getActivity(), "请安装文件管理器", Toast.LENGTH_SHORT).show();
        }
    }

    private void onFileSelectActivityResult(Intent data) {
        try {
            Uri uri = data.getData();
            String path = Utils.getPath(getContext(), uri);
            if(!TextUtils.isEmpty(path)) {
                doUploadFile(new File(path));
            } else {
                Toast.makeText(getContext(), "选择文件路径为空", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onVideoUpdateActivityResult(Intent data) {
        int type = data.getIntExtra(VideoUpdateActivity.KEY_UPDATE_TYPE, -1);
        String path = data.getStringExtra(VideoUpdateActivity.KEY_UPDATE_PATH);
        String attr = data.getStringExtra(VideoUpdateActivity.KEY_UPDATE_ATTR);
        String desc = data.getStringExtra(VideoUpdateActivity.KEY_UPDATE_VIDEO_DESC);
        String title = data.getStringExtra(VideoUpdateActivity.KEY_UPDATE_VIDEO_TITLE);

        VideoAttr videoAttr = new VideoAttr();
        videoAttr.desc = desc;
        videoAttr.title = title;
        doUpdateProccess(new Dentry(type).setPath(path).setAttribute(attr), videoAttr);
    }

    private void onVideoSelectActivityResult(Intent data) {
        try {
            Uri uri = data.getData();
            String path = Utils.getPath(getContext(), uri);
            if(!TextUtils.isEmpty(path)) {
                doUploadVideo(new File(path));
            } else {
                Toast.makeText(getContext(), "选择视频路径为空", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 文件上传
    private void doUploadFile(File file) {
        if (file == null || !file.exists()) {
            Toast.makeText(getContext(), "文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }

        final String srcFilePath = file.getAbsolutePath();
        final String destFilePath = getCurPath() + file.getName();
        doUploadProccess(srcFilePath, new Dentry(Dentry.FILE).setPath(destFilePath), null);
    }

    // 文件上传
    private void doUploadVideo(File file) {
        if (file == null || !file.exists()) {
            Toast.makeText(getContext(), "视频不存在", Toast.LENGTH_SHORT).show();
            return;
        }

        VideoAttr videoAttr = new VideoAttr();
        final String srcFilePath = file.getAbsolutePath();
        final String destFilePath = getCurPath() + file.getName();

        videoAttr.isCheck = false;
        videoAttr.title = file.getName();
        videoAttr.desc = "cos-video-desc-" + file.getName();
        doUploadProccess(srcFilePath, new Dentry(Dentry.VIDEO).setPath(destFilePath), videoAttr);
    }

    private void doUploadProccess(final String srcFilePath, Dentry destDentry, Object externData) {
        mFClouldDataLayer.asyncUploadFile(srcFilePath, destDentry, externData,
                new FClouldDataLayer.IUploadListener() {
                    private boolean visibility = false;

                    @Override
                    public void onUploadSucceed(Dentry result) {
                        String path = getCurPath();
                        if (TextUtils.isEmpty(path)) {
                            path = getRootPath();
                        }

                        mDentryCache.removeCache(path);
                        mLayoutUploadStatus.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "上传成功", Toast.LENGTH_SHORT).show();
                        doListDirProccess(new Dentry(Dentry.DIR).setPath(path), LS_PULL_MODE_NORMAL);
                    }

                    @Override
                    public void onUploadStateChange(ITask.TaskState state) {
                        if (visibility == false) {
                            visibility = true;
                            mLayoutUploadStatus.setVisibility(View.VISIBLE);
                        }

                        mTextViewProgress.setText("任务状态:" + state.getDesc());

                        if (state == ITask.TaskState.CANCEL) {
                            mLayoutUploadStatus.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onUploadFailed(int errorCode, String errorMsg) {
                        String msg = "上传失败:" + srcFilePath;
                        msg += "  errcode:" + errorCode + " errmsg:" + errorMsg;
                        mLayoutUploadStatus.setVisibility(View.GONE);
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onUploadProgress(long totalSize, long sendSize) {
                        long a = (long) ((sendSize * 100) / (totalSize * 1.0f));
                        mTextViewProgress.setText(
                                "上传进度:" + a + "%"
                        );
                    }
                }
        );
    }

    private void OnClickSetOption() {
        AlertDialog.Builder makeDirDlg = null;
        makeDirDlg = new AlertDialog.Builder(getContext());
        makeDirDlg.setTitle("配置选项");
        makeDirDlg.setView(getView(mSetOptionDlgView));
        makeDirDlg.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doSetDirListOption();
                doSetDownloadOption();
                doSaveFCloudConfigOption();
            }
        });
        makeDirDlg.setNegativeButton("取消", null);

        makeDirDlg.show();
    }

    private void doSetDirListOption() {
        int pageSize = 5;
        int listPattern = 0;
        int orderCheckid = 0;
        boolean order = false;
        String strPageSize = "";

        if (mCheckBoxLsPatnDir.isChecked()
                && mCheckBoxLsPatnFile.isChecked()) {
            listPattern = Dentry.ListBoth;
        } else if (mCheckBoxLsPatnDir.isChecked()) {
            listPattern = Dentry.ListDirOnly;
        } else if (mCheckBoxLsPatnFile.isChecked()) {
            listPattern = Dentry.ListFileOnly;
        } else {
            // 如果用户没有选中则默认选择文件和目录
            mCheckBoxLsPatnDir.setChecked(true);
            mCheckBoxLsPatnFile.setChecked(true);
            listPattern = Dentry.ListBoth;
        }

        strPageSize = mEditTextPageSize.getText().toString();
        if (!TextUtils.isEmpty(strPageSize)) {
            pageSize = Integer.parseInt(strPageSize);
        }
        orderCheckid = mRadioGroupLsOrder.getCheckedRadioButtonId();
        if (orderCheckid == R.id.radioButtonDesc) {
            order = true;
        }

        boolean bNeedCleanCahce = false;
        if (mFClouldDataLayer.getOrder() != order) {
            bNeedCleanCahce = true;
        }

        if (mFClouldDataLayer.getPageSize() != pageSize) {
            bNeedCleanCahce = true;
        }

        if (mFClouldDataLayer.getListPattern() != listPattern) {
            bNeedCleanCahce = true;
        }

        mFClouldDataLayer.setOrder(order);
        mFClouldDataLayer.setPageSize(pageSize);
        mFClouldDataLayer.setListPattern(listPattern);

        if (bNeedCleanCahce) {
            Dentry dentry = null;
            mDentryCache.cleanCache();
            String curPath = getCurPath();
            dentry = new Dentry(Dentry.DIR).setPath(curPath);
            doListDirProccess(dentry, LS_PULL_MODE_NORMAL);
        }
    }

    private void doSetDownloadOption(){
        String maxConcurrent = mEditTextMaxConcurrent.getText().toString();
        if (TextUtils.isEmpty(maxConcurrent)) {
            maxConcurrent = "3";
            mEditTextMaxConcurrent.setText(maxConcurrent);
        }

        mFClouldDataLayer.setMaxConcurrent(Integer.parseInt(maxConcurrent));
        mFClouldDataLayer.setEnbaleHttpRange(mCheckBoxHttpRange.isChecked());
        mFClouldDataLayer.setEnableCleanDwCache(mCheckBoxCleanDwCache.isChecked());
        mFClouldDataLayer.setEnableHttpKeepAlive(mCheckBoxHttpKeepAlive.isChecked());
    }

    private void doSaveFCloudConfigOption() {
        mFClouldDataLayer.saveConfigOption();
    }

    public class ViewHolder {
        ImageView icon;
        TextView  name;
        TextView  memu;
        Dentry dentry;
    }

    public class MyBaseAdapter extends BaseAdapter {

        private Context context;
        private LayoutInflater mInflater;

        private List<Dentry> values = new ArrayList<Dentry>();

        public MyBaseAdapter(Context context) {
            this.context = context;
        }

        public void clean()
        {
            values.clear();
            notifyDataSetChanged();
        }

        public void addDatas(ArrayList<Dentry> datas)
        {
            if(datas == null)
                return;

            values.clear();
            values.addAll(datas);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return (values == null) ? 0 : values.size();
        }

        @Override
        public Object getItem(int position) {
            return values.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private LayoutInflater getInflater() {
            if(mInflater == null)
                mInflater = LayoutInflater.from(context);
            return mInflater;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = getInflater().inflate(R.layout.view_dentry_list_item, null);

                viewHolder = new ViewHolder();
                viewHolder.icon = (ImageView) convertView
                        .findViewById(R.id.icon);
                viewHolder.name = (TextView) convertView
                        .findViewById(R.id.name);
                viewHolder.memu = (TextView) convertView
                        .findViewById(R.id.memu);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            initUI(position, viewHolder);

            return convertView;
        }

        private void initUI(int position, ViewHolder viewHolder)
        {
            if(viewHolder == null)
                return;

            Dentry dirEntry = (Dentry) getItem(position);
            if (dirEntry == null){
                return;
            }

            viewHolder.dentry = dirEntry;
            viewHolder.name.setText(dirEntry.name);

            if (dirEntry.type == Dentry.DIR) {
                viewHolder.memu.setVisibility(View.VISIBLE);
                viewHolder.icon.setImageResource(R.drawable.icon_dir);
            } else if (dirEntry.type == Dentry.FILE){
                viewHolder.memu.setVisibility(View.INVISIBLE);
                if (dirEntry.fileLength == dirEntry.fileSize) {
                    viewHolder.icon.setImageResource(R.drawable.icon_file);
                } else {
                    viewHolder.icon.setImageResource(R.drawable.icon_invalid);
                }
            } else if (dirEntry.type == Dentry.VIDEO){
                viewHolder.memu.setVisibility(View.INVISIBLE);
                if (dirEntry.fileLength == dirEntry.fileSize) {
                    viewHolder.icon.setImageResource(R.drawable.icon_video);
                } else {
                    viewHolder.icon.setImageResource(R.drawable.icon_invalid);
                }
            } else if (dirEntry.type == Dentry.BUCKET) {
                viewHolder.memu.setVisibility(View.VISIBLE);
                viewHolder.icon.setImageResource(R.drawable.icon_dir);
            }else {
                viewHolder.memu.setVisibility(View.INVISIBLE);
                viewHolder.icon.setImageResource(R.drawable.icon_more);
            }
        }
    }
}
package com.tencent.cloud.demo.fragment.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment
{
    private boolean mRefresh;
    private View mView = null;
    private Context mContext = null;

    protected View getView(View view) {
        if (view == null) {
            return null;
        }

        ViewGroup parent = null;
        parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        return view;
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{ 
		super.onCreateView(inflater, container, savedInstanceState);
		if(mView == null) {
            mView = getContentView(inflater, container, savedInstanceState);
        }

		return getView(mView);
	}

    protected abstract void onEnvChange();

	protected abstract View getContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    
    @Override
    public void onAttach(Activity activity)
    {
        mContext = activity;
        super.onAttach(activity);
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }
    
    public Context getContext()
    {
        return mContext;
    }

    protected void PopMessageBox(String title, String confirm, String cancel, View view,
                               DialogInterface.OnClickListener confirmListener,
                               DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder makeDirDlg = null;
        makeDirDlg = new AlertDialog.Builder(getContext());
        makeDirDlg.setTitle(title);
        makeDirDlg.setView(this.getView(view));
        makeDirDlg.setPositiveButton(confirm, confirmListener);
        makeDirDlg.setNegativeButton(cancel, cancelListener);
        makeDirDlg.show();
    }

    public boolean needRefresh() {
        return mRefresh;
    }

    public void setRefresh(boolean bRefresh) {
        mRefresh = bRefresh;
    }

    public boolean isInitFinish() {
        return true;
    }
}

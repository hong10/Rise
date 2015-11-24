package com.tencent.cloud.demo.fragment.fclouldfragement;

import android.os.SystemClock;
import android.text.TextUtils;

import com.tencent.upload.task.Dentry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by redickran on 2015/5/7.
 */
public class DentryCache {
    class CacheNode {
        long begTime = 0;
        String path = "";
        ArrayList<Dentry> dentryList = null;
    }

    public DentryCache(long nodeTimeout){
        if (nodeTimeout <= 0) {
            nodeTimeout = 0;
        }
        mNodeTimeoutMs = nodeTimeout;
        mDentryCache = new HashMap<String, CacheNode>();
    }

    public void cleanCache(){
        synchronized (mDentryCache) {
            mDentryCache.clear();
        }
    }

    private long getTimeMs() {
        return SystemClock.currentThreadTimeMillis();
    }

    public void putCache(String path, ArrayList<Dentry> dentryList){
        appendCache(path, dentryList);
    }

    private void _putCache(String path, ArrayList<Dentry> dentryList){
        CacheNode node = new CacheNode();
        node.path = path;
        node.begTime = getTimeMs();
        node.dentryList = dentryList;
        mDentryCache.put(path, node);
    }

    public void appendCache(String path, ArrayList<Dentry> dentryList){
        if (TextUtils.isEmpty(path)
                || dentryList == null) {
            return;
        }

        CacheNode node = null;
        synchronized (mDentryCache) {
            node = (CacheNode)mDentryCache.get(path);
            if (node == null) {
                _putCache(path, dentryList);
                return;
            }

            _appendCache(node.dentryList, dentryList);
        }
    }

    private boolean find4Update(ArrayList<Dentry> nodeList, Dentry dentry) {
        if (nodeList == null 
			|| dentry == null) {
            return false;
        }
        
        for (Dentry d : nodeList) {
            if (dentry.equals(d)) {
                d.copy(dentry);
                return true;
            }
        }

        return false;
    }

    private void _appendCache(ArrayList<Dentry> nodeList, ArrayList<Dentry> appendList)
    {
        if (nodeList == null
                || appendList == null
                || nodeList == appendList) {
            return;
        }

        ArrayList<Dentry> delList = null;
        delList = new ArrayList<Dentry>();
        for (Dentry dentry : nodeList) {
            if (dentry == null) {
                delList.add(dentry);
                continue;
            }

            if (dentry.type == Dentry.MORE) {
                delList.add(dentry);
                continue;
            }
        }

        for (Dentry dentry : delList) {
            nodeList.remove(dentry);
        }

        delList.clear();
        for (Dentry dentry : appendList) {
            if (dentry == null) {
                delList.add(dentry);
                continue;
            }

            if (find4Update(nodeList, dentry)) {
                delList.add(dentry);
                continue;
            }
        }

        for (Dentry dentry : delList) {
            appendList.remove(dentry);
        }

        if (appendList.size() > 0) {
            nodeList.addAll(appendList);
        }
    }

    public ArrayList<Dentry> getCache(String path){
        if (TextUtils.isEmpty(path)) {
            return null;
        }

        CacheNode node = null;
        long curTime = getTimeMs();
        synchronized(mDentryCache) {
            node = mDentryCache.get(path);
            if (node == null) {
                return null;
            }

            if (mNodeTimeoutMs <= 0) {
                return node.dentryList;
            }

            long interval = curTime - node.begTime;
            if (interval <= mNodeTimeoutMs) {
                return node.dentryList;
            }

            mDentryCache.remove(path);
            return null;
        }
    }

    private void _removeCache(String path) {
        ArrayList<String> delPathList;
        delPathList = new ArrayList<String>();
        delPathList.add(path);
        
        for (String key : mDentryCache.keySet()) {
            if (key.indexOf(path) == 0) {
                delPathList.add(key);
            }
        }

        for (String delPath : delPathList) {
            mDentryCache.remove(delPath);
        }
    }
    public void removeCache(String path){
        if (TextUtils.isEmpty(path)) {
            return;
        }

        synchronized(mDentryCache) {
            _removeCache(path);
        }
    }

    public void removeCacheItem(String path, String subPath){
        if (TextUtils.isEmpty(path)
                || TextUtils.isEmpty(subPath)) {
            return;
        }

        synchronized(mDentryCache) {
            _removeCache(subPath);
            CacheNode node = mDentryCache.get(path);
            if (node == null || node.dentryList == null) {
                return;
            }

            ArrayList<Dentry> dentryList = node.dentryList;
            ArrayList<Dentry> deleteList = new ArrayList<Dentry>();
            for (Dentry dentry : dentryList) {
                if (dentry.path.equals(subPath)) {
                    deleteList.add(dentry);
                }
            }

            for (Dentry dentry : deleteList) {
                dentryList.remove(dentry);
            }
        }
    }

    private long mNodeTimeoutMs = 30000;
    private Map<String, CacheNode> mDentryCache = null;
}
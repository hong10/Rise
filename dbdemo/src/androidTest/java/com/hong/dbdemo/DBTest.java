package com.hong.dbdemo;

import android.test.AndroidTestCase;
import android.util.Log;

import com.hong.database.dao.domain.Book;
import com.hong.database.dao.domain.News;
import com.hong.database.dao.impl.BookDaoImpl;
import com.hong.database.dao.impl.NewsDaoImpl;

import java.util.List;

/**
 * Created by hong on 2016/5/1.
 */
public class DBTest extends AndroidTestCase {

    private static final String TAG = "DBTest";
    public void testRuntimeObject() {
        NewsDaoImpl daoImpl = new NewsDaoImpl(getContext());
        daoImpl.getRuntimeClass();
    }

    public void insert() {
        NewsDaoImpl daoImpl = new NewsDaoImpl(getContext());
        News news = new News();
        news.setTitle("测试标题一");
        news.setSummary("测试摘要一");

        daoImpl.insert(news);
    }

    public void update()
    {
        NewsDaoImpl daoImpl = new NewsDaoImpl(getContext());
        News news = new News();
        news.setId(1);
        news.setTitle("测试标题一");
        news.setSummary("测试摘要_修改");

        daoImpl.update(news);
    }
    public void findAll()
    {
        NewsDaoImpl daoImpl = new NewsDaoImpl(getContext());
        List<News> findAll = daoImpl.findAll();
        Log.i(TAG, "size:" + findAll.size());
    }
    public void delete()
    {
        NewsDaoImpl daoImpl = new NewsDaoImpl(getContext());
        daoImpl.delete(1);
        List<News> findAll = daoImpl.findAll();
        Log.i(TAG, "size:"+findAll.size());
    }

    public void bookInsert()
    {
        BookDaoImpl daoImpl=new BookDaoImpl(getContext());
        daoImpl.insert(new Book());
    }
}

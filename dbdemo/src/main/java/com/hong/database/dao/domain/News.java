package com.hong.database.dao.domain;

import com.hong.database.dao.DBHelper;
import com.hong.database.dao.annotation.Column;
import com.hong.database.dao.annotation.ID;
import com.hong.database.dao.annotation.TableName;

/**
 * Created by hong on 2016/5/1.
 */

@TableName(DBHelper.TABLE_NEWS_NAME)
public class News {

    @Column(DBHelper.TABLE_ID)
    @ID(autoincrement = true)
    private long id;

    @Column(DBHelper.TABLE_NEWS_TITLE)
    private String title;

    @Column(DBHelper.TABLE_NEWS_SUMMARY)
    private String summary;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}

package com.hong.database.dao.domain;

import com.hong.database.dao.DBHelper;
import com.hong.database.dao.annotation.Column;
import com.hong.database.dao.annotation.ID;
import com.hong.database.dao.annotation.TableName;

/**
 *
 * Created by hong on 2016/5/1.
 */
@TableName("book")
public class Book {

    @Column(DBHelper.TABLE_ID)
    @ID(autoincrement = true)
    private int id;

    @Column("name")
    private String name;
}

package com.hong.database.dao.impl;

import android.content.Context;

import com.hong.database.dao.BookDao;
import com.hong.database.dao.base.DAOSupport;
import com.hong.database.dao.domain.Book;

public class BookDaoImpl extends DAOSupport<Book> implements BookDao {

	public BookDaoImpl(Context context) {
		super(context);
	}

	@Override
	public void aaa() {
		//CRUD之外
		
	}

	

}

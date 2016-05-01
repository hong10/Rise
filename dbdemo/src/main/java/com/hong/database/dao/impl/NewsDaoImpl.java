package com.hong.database.dao.impl;

import android.content.Context;

import com.hong.database.dao.NewsDao;
import com.hong.database.dao.base.DAOSupport;
import com.hong.database.dao.domain.News;

public class NewsDaoImpl extends DAOSupport<News> implements NewsDao {
//	private Context context;
//
//	private DBHelper helper;
//	private SQLiteDatabase db;

	public NewsDaoImpl(Context context) {
//		this.context = context;
//		helper = new DBHelper(context);
//		db = helper.getWritableDatabase();
		super(context);
	}

//	@Override
//	public int delete(long id) {
//		//问题一：表名获取
//		return db.delete(DBHelper.TABLE_NEWS_NAME, DBHelper.TABLE_ID + "=?", new String[] { id + "" });
//	}
//
//	@Override
//	public List<News> findAll() {
//		//问题一：表名获取
//		//问题二：实体对象的创建
//		//问题三：给实体中相应属性设置值
//		List<News> result = new ArrayList<News>();//List<M> result = new ArrayList<M>();
//		Cursor query = db.query(DBHelper.TABLE_NEWS_NAME, null, null, null, null, null, null);
//
//		if (query != null) {
//			while (query.moveToNext()) {
//				News item = new News();// M m=new M();
//
//				int columnIndex = query.getColumnIndex(DBHelper.TABLE_NEWS_TITLE);
//				String title = query.getString(columnIndex);
//				item.setTitle(title);
//				
//				//此次省略N行代码
//				
//
//				result.add(item);
//			}
//		}
//
//		return result;
//	}
//
//	@Override
//	public long insert(News news) {
//		// 问题一：将实体中封装的信息设置到数据中
//		// 问题二：表名获取
//		
//		ContentValues values = new ContentValues();
//
//		values.put(DBHelper.TABLE_NEWS_TITLE, news.getTitle());
//		values.put(DBHelper.TABLE_NEWS_SUMMARY, news.getSummary());
//
//		return db.insert(DBHelper.TABLE_NEWS_NAME, null, values);
//	}
//
//	@Override
//	public int update(News news) {
//		// 问题一：将实体中封装的信息设置到数据中
//		// 问题二：表名获取
//		// 问题三：获取实体中对应的主键信息
//		
//		ContentValues values = new ContentValues();
//
//		values.put(DBHelper.TABLE_NEWS_TITLE, news.getTitle());
//		values.put(DBHelper.TABLE_NEWS_SUMMARY, news.getSummary());
//
//		return db.update(DBHelper.TABLE_NEWS_NAME, values, DBHelper.TABLE_ID + "=?", new String[] { news.getId() + "" });
//	}

}

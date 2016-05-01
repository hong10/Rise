package com.hong.database.dao.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hong.database.dao.DBHelper;
import com.hong.database.dao.annotation.Column;
import com.hong.database.dao.annotation.ID;
import com.hong.database.dao.annotation.TableName;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public abstract class DAOSupport<M> implements DAO<M> {
    // 问题一：表名获取
    // 问题二：将实体中封装的信息设置到数据中
    // 问题三：将数据库中信息设置到实体当中
    // 问题四：获取实体中对应的主键信息
    // 问题五：实体对象的创建

    private static final String TAG = "DAOSupport";

    protected Context context;

    protected DBHelper helper;
    protected SQLiteDatabase db;

    public DAOSupport(Context context) {
        this.context = context;
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    @Override
    public int delete(Serializable id) {
        return db.delete(getTableName(), DBHelper.TABLE_ID + "=?", new String[]{id + ""});
    }

    @Override
    public List<M> findAll() {
        List<M> result = new ArrayList<M>();
        Cursor query = db.query(getTableName(), null, null, null, null, null, null);
        if (query != null) {
            while (query.moveToNext()) {
                M m = getInstatnce();
                fillField(query, m);
                result.add(m);
            }
            query.close();
        }
        return result;
    }

    public List<M> findByCondition(String selection, String[] selectionArgs, String orderBy, String limit) {
        return findByCondition(null, selection, selectionArgs, null, null, orderBy, limit);
    }

    public List<M> findByCondition(String[] columns, String selection, String[] selectionArgs, String groupBy, String having,
                                   String orderBy, String limit) {
        List<M> result = new ArrayList<M>();
        Cursor query = db.query(getTableName(), columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        if (query != null) {
            while (query.moveToNext()) {
                M m = getInstatnce();
                fillField(query, m);
                result.add(m);
            }
            query.close();
        }
        return result;
    }

    @Override
    public long insert(M m) {
        ContentValues values = new ContentValues();
        fillTable(m, values);
        return db.insert(getTableName(), null, values);
    }

    @Override
    public int update(M m) {
        ContentValues values = new ContentValues();
        fillTable(m, values);

        return db.update(getTableName(), values, DBHelper.TABLE_ID + "=?", new String[]{getId(m)});
    }

    /**
     * 问题一：表名获取
     *
     * @return
     */
    private String getTableName() {
        M m = getInstatnce();
        TableName annotation = m.getClass().getAnnotation(TableName.class);
        if (annotation != null) {
            return annotation.value();
        }
        return "";
    }

    /**
     * 问题二：将实体中封装的信息设置到数据中
     *
     * @param m
     * @param values
     */
    private void fillTable(M m, ContentValues values) {
        // ContentValues values = new ContentValues();
        //
        // values.put(DBHelper.TABLE_NEWS_TITLE, news.getTitle());

        Field[] fields = m.getClass().getDeclaredFields();
        for (Field item : fields) {
            item.setAccessible(true);
            Column annotation = item.getAnnotation(Column.class);
            if (annotation != null) {
                String key = annotation.value();

                String value;
                try {
                    value = item.get(m).toString();
                    ID id = item.getAnnotation(ID.class);
                    // 在添加信息时需要判断当前的item是不是主键
                    if (id != null) {
                        // 如果是：主键是自增长的吗？
                        if (id.autoincrement()) {
                            // 只有主键是自增长的时候不会设置值
                        } else {
                            values.put(key, value);
                        }
                    } else {
                        values.put(key, value);
                    }

                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    /**
     * 问题三：将数据库中信息设置到实体当中
     */
    private void fillField(Cursor query, M m) {
        // News item = new News();// M m=new M();
        //
        // int columnIndex = query.getColumnIndex(DBHelper.TABLE_NEWS_TITLE);
        // String title = query.getString(columnIndex);
        // item.setTitle(title);

        // 将实体中的属性与数据库中列向关联

        Field[] fields = m.getClass().getDeclaredFields();
        for (Field item : fields) {
            item.setAccessible(true);

            Column annotation = item.getAnnotation(Column.class);
            String value = annotation.value();

            int columnIndex = query.getColumnIndex(value);
            String info = query.getString(columnIndex);
            try {
                item.set(m, info);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 问题四：获取实体中对应的主键信息
     *
     * @param m
     * @return
     */
    private String getId(M m) {
        Field[] fields = m.getClass().getDeclaredFields();
        for (Field item : fields) {
            item.setAccessible(true);
            ID id = item.getAnnotation(ID.class);
            if (id != null) {
                try {
                    return item.get(m).toString();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 问题五：实体对象的创建
     *
     * @return
     */
    private M getInstatnce() {
        // 添加一条新闻
        // NewsDaoImpl创建对象impl
        // impl.insert();//添加

        // M 什么时候确定
        // ①获取到NewsDaoImpl实例
        Class clazz = getClass();
        // ②获取父类——支持泛型的父类
        Type genericSuperclass = clazz.getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;

        // ③支持泛型的父类获取到泛型中参数信息
        Class m = (Class) parameterizedType.getActualTypeArguments()[0];
        try {
            return (M) m.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getRuntimeClass() {
        Class clazz = getClass();
        Log.i(TAG, clazz.getName());
    }

}

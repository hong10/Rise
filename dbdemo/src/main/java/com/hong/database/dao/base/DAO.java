package com.hong.database.dao.base;

import java.io.Serializable;
import java.util.List;

/**
 * 实体的通用操作接口
 * Created by hong on 2016/5/1.
 *
 * @param <M>
 */
public interface DAO<M> {

    /**
     * 添加
     *
     * @param m
     * @return
     */
    public long insert(M m);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public int delete(Serializable id);// long String int


    /**
     * 修改
     *
     * @param m
     * @return
     */
    public int update(M m);

    /**
     * 查询
     *
     * @return
     */
    public List<M> findAll();
}

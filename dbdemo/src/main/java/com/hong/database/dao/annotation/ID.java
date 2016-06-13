package com.hong.database.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标定实体主键
 * Created by hong on 2016/5/1.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ID {
    /**
     * 明确主键是否是自增长
     *
     * @return
     */
    boolean autoincrement();

}

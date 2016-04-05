package com.hong.rise.utils;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by hong on 2016/4/5.
 */
public class BeanFactory {

    /**
     * 依据配置文件加载实例类
     */

    private static Properties properties;

    static {
        properties = new Properties();
        //配置文件bean.properties必须在src的目录下
        try {
            properties.load(BeanFactory.class.getClassLoader().getResourceAsStream("assets/bean.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载需要的实现类
     */
    public static<T> T getImpl(Class clazz) {
        String key = clazz.getSimpleName();
        String className = properties.getProperty(key);
        try {
            return (T)Class.forName(className).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

}

package com.zxl.zxlframework.xmlfactory.context;


/**
 * @Author: zxl
 * @Time: 2020/06/23 10:34:24
 * @system: ASUS
 **/
public interface ApplicationContext {
    /**
     * 获取全局对象
     * @param className 全局对象名
     * @param <T>   泛型
     * @return  返回值
     */
    <T> T getAttribute(String className);
    /**
     * 设置全局对象
     * @param name  全局对象名
     * @param o 全局对象名
     */
    void setAttribute(String name,Object o);
}

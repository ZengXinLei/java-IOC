package com.zxl.zxlframework.xmlFactory.beans.factory;

/**
 * @Author: zxl
 * @Time: 2020/06/23 10:36:54
 * @system: ASUS
 **/
public interface BeanFactory {

    <T> T getBean(String className);
}

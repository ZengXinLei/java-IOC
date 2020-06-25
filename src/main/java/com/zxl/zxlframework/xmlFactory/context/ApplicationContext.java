package com.zxl.zxlframework.xmlFactory.context;


/**
 * @Author: zxl
 * @Time: 2020/06/23 10:34:24
 * @system: ASUS
 **/
public interface ApplicationContext  {
    <T> T getBean(String className);
}

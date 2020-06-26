package com.zxl.zxlframework.xmlfactory.context.http;

import com.zxl.zxlframework.xmlfactory.context.ApplicationContext;

import java.util.Map;

/**
 * @Author: zxl
 * @Time: 2020/06/26 13:45:30
 * @system: ASUS
 **/
public abstract class AbstractGlobalContext implements ApplicationContext {

    private Map<String,Object> variable=null;


    public AbstractGlobalContext(Map<String,Object> map){
        variable=map;
    }


    @Override
    public <T> T getAttribute(String className) {

        return (T) variable.get(className);
    }

    @Override
    public void setAttribute(String name, Object o) {
        variable.put(name,o);
    }
}

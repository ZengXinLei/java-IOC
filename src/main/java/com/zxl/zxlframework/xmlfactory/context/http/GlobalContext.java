package com.zxl.zxlframework.xmlfactory.context.http;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zxl
 * @Time: 2020/06/26 13:49:54
 * @system: ASUS
 **/
public class GlobalContext extends AbstractGlobalContext {

    private static final Map<String,Object> VARIABLE=new HashMap<>();

    public GlobalContext(){
        super(VARIABLE);

    }

}

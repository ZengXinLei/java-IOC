package com.zxl.zxlframework.converter;

import com.zxl.zxlframework.converter.factory.ConverterFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zxl
 * @Time: 2020/06/24 09:12:51
 * @system: ASUS
 **/
public abstract class AbstractConverter{


    private Object value = null;
    //转换工厂的包路径
    final static String CONVERTERFACTORYPACKAGE ="com.zxl.zxlframework.converter.factory.";

    //所有转换工厂
    //如果要扩展，OtherConverterFactory工厂应该放在最后
    final static String[] CONVERTERFACTORYS ={
      "BaseConverterFactory",
      "OtherConverterFactory"
    };
    public AbstractConverter(Class<?> s, Object t) {
        //如果属性和目标属性类型一样
        if (s.getName().equals(t.getClass().getName())) {
            value = t;
            return;
        }
        //遍历所有转换工厂
        for (String cFactory :
                CONVERTERFACTORYS) {
            if (value == null){
                try {
                    //获取转换工厂对象
                    Object converterFactory = Class.forName(CONVERTERFACTORYPACKAGE + cFactory).getDeclaredConstructor().newInstance();
                    Class[] cArgs=new Class[2];
                    cArgs[0]=Class.class;
                    cArgs[1]=Object.class;
                    //获取转换工厂对象的build方法
                    Method build = converterFactory.getClass().getDeclaredMethod("build", cArgs);
                    //转换完的值
                    value=build.invoke(converterFactory,s,t);

                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    public <K> K getBean() {
        return (K) value;
    }
}

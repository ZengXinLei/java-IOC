package com.zxl.zxlframework.converter;

import com.zxl.zxlframework.converter.factory.ConverterFactory;

import java.lang.reflect.InvocationTargetException;

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
        String name = t.getClass().getName();
//        if(t.getClass().getName().equals("Ljava.lang.String")){
//            t=(String)t;
//        }
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
                    ConverterFactory converterFactory = (ConverterFactory) Class.forName(CONVERTERFACTORYPACKAGE + cFactory).getDeclaredConstructor().newInstance();
                    value=converterFactory.build(s,t);
                    break;

                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    public   <K> K getBean() {
        return (K) value;
    }
}

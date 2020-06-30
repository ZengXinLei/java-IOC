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

        if(t==null){
            isNull(s);
            return;
        }
        if (s.getName().equals(t.getClass().getName())) {
            value = t;
            return;
        }
        //遍历所有转换工厂
        for (String cFactory : CONVERTERFACTORYS) {
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
    public void isNull(Class<?> s){

        String[] baseNum={
                "java.lang.Int",
                "java.lang.Float",
                "java.lang.Long",
                "java.lang.Double",
                "java.lang.Short",
                "int",
                "float",
                "long",
                "double",
                "short",
        };

        if(String.class==s){
            value="";
        }
        else  if(Boolean.class==s||boolean.class==s){
            value=null;
        }
        for(String type:baseNum){
            if(type.equals(s)){
                value=0;
                return;
            }
        }
        value=null;
    }

    public   <K> K getBean() {
        return (K) value;
    }
}

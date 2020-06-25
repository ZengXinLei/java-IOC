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
public abstract class AbstractConverter implements ConverterFactory {

    private final Map<String,String> BASECLASS=new HashMap<>();
    private Object value=null;
    public AbstractConverter(Class<?> s,Object t) {
        if(s.getName().equals(t.getClass().getName())){
            value=t;
            return;
        }
        init();
        try {
            baseType(s,t);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }



        if (value==null){
            try {
                otherType(s,t);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }

        }
        if(value==null){
            value=t;
        }
    }

    private void init() {
        BASECLASS.put("int","Int");
        BASECLASS.put("long","Long");
        BASECLASS.put("double","Double");
        BASECLASS.put("float","Float");
        BASECLASS.put("short","Short");
        BASECLASS.put("byte","Byte");
        BASECLASS.put("boolean","Boolean");
    }

    /**
     * 转换成基本类型
     * @param s
     * @param o 目标
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void baseType(Class<?> s, Object o) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String typeName=s.getTypeName();
        if(typeName.startsWith("java.lang")){
            String type=typeName.split("\\.")[2];
            if (BASECLASS.containsValue(type)){
                Method method=Class.forName(typeName).getDeclaredMethod("parse"+type, String.class);
                value=method.invoke(null,o);

            }
            else if (type.equals("Integer")){
                Method method=Class.forName(typeName).getDeclaredMethod("parseInt", String.class);
                value=method.invoke(null,o);
            }
            return;


        }
        if(BASECLASS.containsKey(typeName)){
            Method method=Class.forName("java.lang."+BASECLASS.get(typeName)).getDeclaredMethod("parse"+BASECLASS.get(typeName), String.class);
            value=method.invoke(null,o);

        }
    }

    private void otherType(Class<?> s,Object o) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String name = s.getClass().getName();
        Object sClass = s.getDeclaredConstructor().newInstance();
        Field[] sField = s.getDeclaredFields();
        for (Field f :
                sField) {
            Field oField = null;
            try {
                oField = o.getClass().getDeclaredField(f.getName());
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            f.setAccessible(true);
            f.set(sClass,oField.get(o));
        }
        value=sClass;

    }
    @Override
    public <K> K getBean() {
        return (K) value;
    }
}

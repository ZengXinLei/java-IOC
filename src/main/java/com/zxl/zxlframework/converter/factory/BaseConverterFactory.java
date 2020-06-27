package com.zxl.zxlframework.converter.factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zxl
 * @Time: 2020/06/25 17:03:20
 * @system: ASUS
 **/
public class BaseConverterFactory implements ConverterFactory {
    private final Map<String, String> BASECLASS = new HashMap<>();
    private void init(){
        BASECLASS.put("int", "Int");
        BASECLASS.put("long", "Long");
        BASECLASS.put("double", "Double");
        BASECLASS.put("float", "Float");
        BASECLASS.put("short", "Short");
        BASECLASS.put("byte", "Byte");
        BASECLASS.put("boolean", "Boolean");
    }
    @Override
    public Object build(Class<?> s, Object o) {
        init();
        Object value=null;
        String typeName = s.getTypeName();
        //如果是基本类
        if (typeName.startsWith("java.lang")) {
            String type = typeName.split("\\.")[2];
            if (BASECLASS.containsValue(type)) {
                Method method = null;
                try {
                    method = Class.forName(typeName).getDeclaredMethod("parse" + type, String.class);
                    value = method.invoke(null, o);
                } catch (NoSuchMethodException | ClassNotFoundException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }


            } else if (type.equals("Integer")) {
                Method method = null;
                try {
                    method = Class.forName(typeName).getDeclaredMethod("parseInt", String.class);
                    value = method.invoke(null, o);
                } catch (NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
            return value;


        }

         // 如果是基本类型
        if (BASECLASS.containsKey(typeName)) {


            try {
                Method method = Class.forName("java.lang." + (BASECLASS.get(typeName).equals("Int") ? "Integer" : typeName)).getDeclaredMethod("parse" + BASECLASS.get(typeName), String.class);
                value = method.invoke(null, o);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return value;
        }
        return null;
    }
}

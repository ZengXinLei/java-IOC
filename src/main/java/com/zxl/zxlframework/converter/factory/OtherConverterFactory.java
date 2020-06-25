package com.zxl.zxlframework.converter.factory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * @Author: zxl
 * @Time: 2020/06/25 17:05:21
 * @system: ASUS
 **/
public class OtherConverterFactory  implements ConverterFactory{
    @Override
    public Object build(Class<?> s, Object o) {
        Object sClass = null;
        try {
            sClass = s.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
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
            try {
                f.set(sClass, oField.get(o));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return sClass;
    }
}

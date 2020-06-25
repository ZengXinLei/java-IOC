package com.zxl.zxlframework.annotationFactory.field;

import com.zxl.util.Util;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @Author: zxl
 * @Time: 2020/06/20 22:16:06
 * @system: ASUS
 **/
public class InerFactory implements FieldFactory {
    @Override
    public <T> T build(Field field,Object o) {

        ArrayList<Class<?>> list = Util.getClasses();
        field.setAccessible(true);
        try {
            field.set(o,list);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}

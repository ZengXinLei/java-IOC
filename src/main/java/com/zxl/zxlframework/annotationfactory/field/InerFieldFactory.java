package com.zxl.zxlframework.annotationfactory.field;

import com.zxl.util.Util;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @Author: zxl
 * @Time: 2020/06/20 22:16:06
 * @system: ASUS
 **/
public class InerFieldFactory implements FieldFactory {
    /**
     *
     * @param field 动态代理对象父类的属性
     * @param o 动态代理对象
     * @param <T>   null
     * @return
     */
    @Override
    public <T> T build(Field field,Object o) {

        /*
        Util.getClasses()该方法返回该工作目录下所有的类的Class的list
         */
        ArrayList<Class<?>> list = Util.getClasses();
        //设置该属性权限
        field.setAccessible(true);
        try {
            //把该属性更改为list
            field.set(o,list);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}

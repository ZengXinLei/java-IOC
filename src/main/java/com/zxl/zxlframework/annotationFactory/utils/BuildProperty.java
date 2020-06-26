package com.zxl.zxlframework.annotationFactory.utils;

import com.zxl.zxlframework.annotation.field.Bean;
import com.zxl.zxlframework.converter.Converter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @Author: zxl
 * @Time: 2020/06/26 09:47:31
 * @system: ASUS
 **/
public abstract class BuildProperty extends ClassesUtil {
    /**
     * @param name  name属性
     * @param value value属性
     * @param o
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws NoSuchFieldException
     * @throws ClassNotFoundException
     */
    public void buildValue(String name, String value, Object o) {

        //name内容首字母大写
        char[] chars = name.toCharArray();
        if (chars[0] >= 97 && chars[0] <= 122){
            chars[0] -= 32;
        }

        //获取setter方法
        String methodName = "set" + String.valueOf(chars);
        //获取动态代理对象父类的所有方法
        /*
        这里如果用通过名字获取方法的话好像会出现问题，现在暂时这么写
         */
        Method[] methods = o.getClass().getSuperclass().getDeclaredMethods();

        //遍历方法
        for (Method m :
                methods) {
            //如果方法名匹配
            if (m.getName().equals(methodName)) {

                //进行类型转换
                Object v = null;
                try {
                    v = dealType(name, value, o);
                    //执行属性的set方法
                    m.invoke(o, v);
                } catch (NoSuchFieldException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void buildRef(String name, String ref, Object o) {

        //获取所有类
        ArrayList<Class<?>> classes = getClasses();
//        遍历所有类，找到带有bean注解的属性并且name相同
        for (Class<?> c :
                classes) {
            Field[] fields = c.getDeclaredFields();
            for (Field f :
                    fields) {
                if (f.isAnnotationPresent(Bean.class)) {
                    Bean b = f.getDeclaredAnnotation(Bean.class);
                    if(b.name().equals(name)){

                    }

                }
            }

        }
    }

    /**
     * @param name  name属性
     * @param value value属性
     * @param o
     * @return
     * @throws NoSuchFieldException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     */
    private Object dealType(String name, String value, Object o) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException {
        //获取动态代理对象的属性
        Field field = o.getClass().getSuperclass().getDeclaredField(name);
        //返回类型转换工厂生成的对象
        return new Converter(field.getType(), value).getBean();
    }

}

package com.zxl.zxlframework.annotationfactory;

import com.zxl.zxlframework.annotationfactory.field.FieldFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * @Author: zxl
 * @Time: 2020/06/20 22:50:57
 * @system: ASUS
 **/
public class BuildFiledFactory {
    //属性注解的包路径，这里写死
    final static String FIELDANNOTATION ="com.zxl.zxlframework.annotation.field.";

    //属性注解工厂包路径，也写死
    final static String FIELDANNOTATIONFACTORY ="com.zxl.zxlframework.annotationFactory.field.";
    /*
    自定义注解名称写在该数组里
    当有新的自定义属性在注解时
    在该数组里添加注解名称
    然后添加自定义注解的工厂来处理该注解即可，最大程度的接近开闭原则
     */
    final static String[] FIELDANNOTATIONS ={
      "Iner"
    };

    /**
     * 返回属性的所有注解的对应的工厂list
     * @param f 对象的属性
     * @return
     */
    public static ArrayList<FieldFactory> build(Field f){
//        创建一个属性注解工厂的list
        ArrayList<FieldFactory> fieldFactories=new ArrayList<>();

        //遍历所有属性注解名称
        for (String annotation:
                FIELDANNOTATIONS) {
            try {
//                如果该属性上有该注解
                if(f.isAnnotationPresent((Class<? extends Annotation>) Class.forName(FIELDANNOTATION +annotation))){
//                    获取该注解对应的工厂
                    FieldFactory fieldFactory= (FieldFactory) Class.forName(FIELDANNOTATIONFACTORY +annotation+"FieldFactory").getDeclaredConstructor().newInstance();
//                    添加到list里
                    fieldFactories.add(fieldFactory);
                  }
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }

        }
//        返回List
        return fieldFactories;
    }
}

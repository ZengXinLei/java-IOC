package com.zxl.zxlframework.annotationFactory;

import com.zxl.zxlframework.annotationFactory.field.FieldFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * @Author: zxl
 * @Time: 2020/06/20 22:50:57
 * @system: ASUS
 **/
public class CreateFiledFactory {
    final static String ANNOTATION ="com.zxl.zxlframework.annotation.field.";
    final static String ANNOTATIONFACTORY="com.zxl.zxlframework.annotationFactory.field.";
    final static String[] ANNOTATIONS={
      "Iner","Bean"
    };
    public static ArrayList<FieldFactory> build(Field f){
        ArrayList<FieldFactory> fieldFactories=new ArrayList<>();


        for (String annotation:
             ANNOTATIONS) {
            try {
                if(f.isAnnotationPresent((Class<? extends Annotation>) Class.forName(ANNOTATION +annotation))){
                    FieldFactory fieldFactory= (FieldFactory) Class.forName(ANNOTATIONFACTORY +annotation+"Factory").getDeclaredConstructor().newInstance();
                    fieldFactories.add(fieldFactory);
                  }
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }

        }
        return fieldFactories;
    }
}

package com.zxl.app;


import com.zxl.zxlframework.annotationFactory.AnnotationBeanFactory;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App
{

    public static void main(String[] args) {
        A a=new AnnotationBeanFactory(A.class).build();
        List<Class> list = a.list;
    }
}

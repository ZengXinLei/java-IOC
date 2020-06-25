package com.zxl.app;


import com.zxl.zxlframework.annotationFactory.AnnotationBeanFactory;
import com.zxl.zxlframework.xmlFactory.context.ApplicationContext;
import com.zxl.zxlframework.xmlFactory.context.support.XMLReader;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App
{

    public static void main(String[] args) {
        ApplicationContext applicationContext =new XMLReader("classpath:example.xml");
        B b = applicationContext.getBean("b");
        System.out.println(b.toString());
    }
}

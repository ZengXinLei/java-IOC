package com.zxl.app;


import com.zxl.zxlframework.annotationFactory.AnnotationBeanFactory;
import com.zxl.zxlframework.xmlFactory.context.support.XMLReader;

/**
 * Hello world!
 *
 */
public class App
{

    public static void main(String[] args) {
        B b=new XMLReader("classpath:example.xml").getBean("b");
    }
}

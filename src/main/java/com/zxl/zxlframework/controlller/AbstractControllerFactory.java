package com.zxl.zxlframework.controlller;

import com.zxl.zxlframework.annotationFactory.BuildTypeAnnotation;
import com.zxl.zxlframework.converter.Converter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Author: zxl
 * @Time: 2020/06/26 16:23:32
 * @system: ASUS
 **/
public abstract class AbstractControllerFactory implements Controller {

    private static final String PACKAGE="com.zxl.zxlframework.annotationFactory.param.";


    public AbstractControllerFactory(HttpServletRequest req, HttpServletResponse resp) {
            doService(req,resp);
    }

    @Override
    public void doService(HttpServletRequest req, HttpServletResponse resp) {
        Map<Object, Method> map=new BuildTypeAnnotation(req.getRequestURI()).getBean();
        Object o=null;
        Method method=null;
        for(Map.Entry<Object, Method> entry:map.entrySet()){
            o=entry.getKey();
            method=entry.getValue();
        }
//        try {
//            method.invoke(o,"你好",2);
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//        }




        buildX(o,method,req,resp);
    }

    /**
     * 处理x-www-form-urlencoded数据
     */
    public <T> void buildX(Object o,Method method,HttpServletRequest req, HttpServletResponse resp){


        int a=new Converter(int.class,"22").getBean();
        Annotation[][] annotations = method.getParameterAnnotations();

        //获取方法的属性注解map
        Map<String, String[]> parameterMap = req.getParameterMap();
        //创建一个方法参数数组
        Object[] parameters= new Object[annotations.length];
        //获取方法的参数类型数组
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < annotations.length; i++) {
            String s = annotations[i][0].toString();
            if(annotations[i].length==0){

            }
            else {
                String[] strings = s.split("\\.");
                String paramAnnotation = strings[strings.length - 1].split("\\(")[0];
                try {
                    Object o1 = Class.forName(PACKAGE + paramAnnotation+"ParamFactory").getDeclaredConstructor().newInstance();
                    Method build=o1.getClass().getDeclaredMethods()[0];
                    build.invoke(o1,parameterTypes[i],parameters,parameterMap,annotations[i][0],i);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            method.invoke(o, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }


}

package com.zxl.zxlframework.controlller;

import com.zxl.zxlframework.annotationFactory.BuildTypeAnnotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * @Author: zxl
 * @Time: 2020/06/26 16:23:32
 * @system: ASUS
 **/
public abstract class AbstractControllerFactory implements Controller {

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
        Parameter[] parameters = method.getParameters();
        System.out.println(parameters.length);

    }


}

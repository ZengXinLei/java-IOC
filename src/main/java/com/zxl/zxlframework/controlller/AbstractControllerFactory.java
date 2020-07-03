package com.zxl.zxlframework.controlller;

import com.alibaba.fastjson.JSONObject;
import com.zxl.zxlframework.annotation.method.ResponseBody;
import com.zxl.zxlframework.annotationfactory.BuildTypeAnnotation;
import com.zxl.zxlframework.annotationfactory.param.ParamFactory;
import com.zxl.zxlframework.converter.Converter;
import com.zxl.zxlframework.xmlfactory.context.ApplicationContext;
import com.zxl.zxlframework.xmlfactory.context.http.GlobalContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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

    private ApplicationContext applicationContext=new GlobalContext();

    public AbstractControllerFactory(HttpServletRequest req, HttpServletResponse resp) {
            doService(req,resp);
    }

    @Override
    public void doService(HttpServletRequest req, HttpServletResponse resp) {

        Map<Object, Method> map=new BuildTypeAnnotation(req.getRequestURI(),req.getMethod()).getBean();
        if(map==null){
            try {
                resp.sendError(404);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        Object o=null;
        Method method=null;
        for(Map.Entry<Object, Method> entry:map.entrySet()){
            o=entry.getKey();
            method=entry.getValue();
        }


        assert method != null;
        Object body = buildX(o, method, req, resp);
        //如果有@ResponseBody注解
        if(method.isAnnotationPresent(ResponseBody.class)){

            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json; charset=utf-8");
            PrintWriter out = null;

            try {
                out = resp.getWriter();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String s="";
            if(body!=null) {
                s = JSONObject.toJSON(body).toString();
            }
            assert out != null;

            out.write(s);



        }
        else{
            if(body instanceof String){
                if(!((String) body).startsWith("/")){
                    body="/"+body;
                }
                try {
                    req.getRequestDispatcher(body.toString()).forward(req, resp);
                } catch (ServletException | IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    resp.sendError(404);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 处理x-www-form-urlencoded数据
     */
    public <T> T buildX(Object o,Method method,HttpServletRequest req, HttpServletResponse resp){

        Annotation[][] annotations = method.getParameterAnnotations();


        //创建一个方法参数数组
        Object[] parameters= new Object[annotations.length];
        //获取方法的参数类型数组
        Class<?>[] parameterTypes = method.getParameterTypes();
        //遍历函数所有参数
        for (int i = 0; i < annotations.length; i++) {

            parameters[i]=new Converter(parameterTypes[i],null).getBean();
            //如果没有注解
            if(annotations[i].length==0){

                if(HttpServletRequest.class==parameterTypes[i]){
                    parameters[i]=req;
                }
                if(HttpServletResponse.class==parameterTypes[i]){
                    parameters[i]=resp;
                }
                if(parameterTypes[i]==ApplicationContext.class){
                    parameters[i]=applicationContext;
                }
            }
            //如果有注解
            else {
                String s = annotations[i][0].toString();
                String[] strings = s.split("\\.");
                String paramAnnotation = strings[strings.length - 1].split("\\(")[0];
                try {
                    //获取参数注解工厂
                    ParamFactory o1 = (ParamFactory) Class.forName(PACKAGE + paramAnnotation+"ParamFactory").getDeclaredConstructor().newInstance();
//                    Method build=o1.getClass().getDeclaredMethods()[0];
                    //执行工厂build方法
                    o1.build(parameterTypes[i],parameters,req,annotations[i][0],i);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        try {

            //执行用户自定义的函数
            method.setAccessible(true);
            return (T) method.invoke(o, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


}

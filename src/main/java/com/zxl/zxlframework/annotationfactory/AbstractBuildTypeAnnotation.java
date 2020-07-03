package com.zxl.zxlframework.annotationfactory;

import com.zxl.zxlframework.annotation.classz.Controller;
import com.zxl.zxlframework.annotation.method.RequestMapping;
import com.zxl.zxlframework.annotationfactory.utils.ClassesUtil;
import com.zxl.zxlframework.annotationfactory.utils.UrlPattern;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zxl
 * @Time: 2020/06/26 17:26:41
 * @system: ASUS
 **/
public abstract class AbstractBuildTypeAnnotation {

    private String baseUrl = "/";
    private String mMethod= "GET";
    public AbstractBuildTypeAnnotation() {

    }

    public AbstractBuildTypeAnnotation(String baseUrl,String method) {
        this.baseUrl = baseUrl;

        this.mMethod=method;
    }

    public <T> Map<T,Method> getBean() {
        //获取所有的类
        ArrayList<Class<?>> classes = new ClassesUtil().getClasses();

        ArrayList<Class<?>> controllers = new ArrayList<>();

        //获取所有有Controller注解并且开头和用户url匹配的
        for (Class<?> c : classes) {
            if (c.isAnnotationPresent(Controller.class)) {
                Controller controller = c.getDeclaredAnnotation(Controller.class);
                String value = controller.value();
                if (baseUrl.startsWith(value)) {
                    controllers.add(c);
                }
            }
        }
        for (Class<?> c :
                controllers) {
            Controller controller = c.getDeclaredAnnotation(Controller.class);

            Method[] methods = c.getDeclaredMethods();
            for (Method m:methods){
                if(m.isAnnotationPresent(RequestMapping.class)){
                    RequestMapping requestMapping = m.getDeclaredAnnotation(RequestMapping.class);

                    UrlPattern urlPattern=new UrlPattern(requestMapping,baseUrl,controller,mMethod);
                    if(urlPattern.isFlag()){
                        Map<T, Method> map = new HashMap<>();
                        map.put(new  AnnotationBeanFactory(c).build(),m);
                        return map;
                    }
                }
            }

        }
        return null;
    }
}

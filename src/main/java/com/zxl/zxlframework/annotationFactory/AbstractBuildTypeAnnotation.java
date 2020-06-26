package com.zxl.zxlframework.annotationFactory;

import com.zxl.zxlframework.annotation.classz.Controller;
import com.zxl.zxlframework.annotation.method.RequestMapping;
import com.zxl.zxlframework.annotationFactory.utils.ClassesUtil;

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
    private Enum mMethod= com.zxl.zxlframework.annotation.fieldenum.Method.GET;
    public AbstractBuildTypeAnnotation() {

    }

    public AbstractBuildTypeAnnotation(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public <T> Map<T,Method> getBean() {
        ArrayList<Class<?>> classes = new ClassesUtil().getClasses();
        ArrayList<Class<?>> controllers = new ArrayList<>();
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
            String value = controller.value();
            String backUrl=baseUrl.substring(value.length());
            Method[] methods = c.getDeclaredMethods();
            for (Method m:methods){
                if(m.isAnnotationPresent(RequestMapping.class)){
                    RequestMapping requestMapping = m.getDeclaredAnnotation(RequestMapping.class);
                    com.zxl.zxlframework.annotation.fieldenum.Method method = requestMapping.method();
                    String value1 = requestMapping.value();
                    if(value1.equals(backUrl)&&method==mMethod){
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

package com.zxl.zxlframework.annotationFactory;

import com.zxl.zxlframework.annotationFactory.field.FieldFactory;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @Author: zxl
 * @Time: 2020/06/24 15:16:11
 * @system: ASUS
 **/
public abstract class AbstractAnnotationBeanFactory implements BeanFactory {
    private Class<?> aClass;
    public AbstractAnnotationBeanFactory(Class<?> clazz){
        this.aClass=clazz;
    }



    @Override
    public <T> T build(Object... initargs) {
        Enhancer enhancer=new Enhancer();
        enhancer.setSuperclass(aClass);
        enhancer.setCallback((MethodInterceptor) (o, method, objects, methodProxy) -> methodProxy.invokeSuper(o,objects));
        Object o =enhancer.create();


        fieldBuild(o);
        return (T) o;
    }

    private void fieldBuild(Object o){
        Field[] fields = o.getClass().getSuperclass().getDeclaredFields();

        for (Field f : fields) {

            ArrayList<FieldFactory> fieldFactories =CreateFiledFactory.build(f);


            for (FieldFactory fieldFactory:
                 fieldFactories) {
                fieldFactory.build(f,o);
            }
        }
    }

    private void methodBuild(Object o){
        Method[] methods = o.getClass().getDeclaredMethods();
    }
}

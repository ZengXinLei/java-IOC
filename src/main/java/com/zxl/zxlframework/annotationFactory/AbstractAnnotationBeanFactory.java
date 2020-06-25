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

/**
 * 注解工厂抽象类，实现注解工厂接口
 */
public abstract class AbstractAnnotationBeanFactory implements AnnotationFactory {
    private Class<?> aClass;
    public AbstractAnnotationBeanFactory(Class<?> clazz){
        this.aClass=clazz;
    }


    /**
     * 创建一个动态代理对象，并对属性注解和方法注解进行处理
     * @param initargs
     * @param <T>
     * @return
     */
    @Override
    public <T> T build(Object... initargs) {
        //CGLIB动态代理
        Enhancer enhancer=new Enhancer();//实例化动态代理
        enhancer.setSuperclass(aClass);//设置动态的代理对象继承的类
        /**
         * 设置回调拦截器
         */
        enhancer.setCallback((MethodInterceptor) (o, method, objects, methodProxy) -> {
            /**
             * 在这里调用methodBuild(Object o)方法来处理方法注解，目前没定义方法注解
             */
            return methodProxy.invokeSuper(o,objects);
        });

        Object o =enhancer.create();//获取动态代理对象

        //属性注解处理
        fieldBuild(o);
        return (T) o;
    }

    /**
     * 处理属性注解
     * @param o 动态代理对象
     */
    private void fieldBuild(Object o){
        //获取动态代理对象父类，即用户想要创建对象的所有属性
        Field[] fields = o.getClass().getSuperclass().getDeclaredFields();
        //遍历动态代理对象父类的所有属性
        for (Field f : fields) {
            /*
            获取该对象的所有对应注解的工厂对象列表
             */
            ArrayList<FieldFactory> fieldFactories = BuildFiledFactory.build(f);

            /*
            遍历该列表，调用build方法
             */
            for (FieldFactory fieldFactory:
                 fieldFactories) {
                fieldFactory.build(f,o);
            }
        }
    }

    /**
     * 处理方法注解
     * @param o 动态代理对象
     */
    private void methodBuild(Object o){
//        Method[] methods = o.getClass().getDeclaredMethods();
    }
}

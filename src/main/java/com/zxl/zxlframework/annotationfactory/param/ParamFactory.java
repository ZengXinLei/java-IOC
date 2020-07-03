package com.zxl.zxlframework.annotationfactory.param;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;

/**
 * @Author: zxl
 * @Time: 2020/06/27 22:10:02
 * @system: ASUS
 **/
public interface ParamFactory {
    /**
     * 改变方法参数的指定下标的值
     * @param clazz 需要转换的参数类型
     * @param parameters    方法的参数列表
     * @param req req
     * @param annotation     参数注解
     * @param index 下标
     */
    void build(Class<?> clazz, Object[] parameters, HttpServletRequest req, Annotation annotation, int index);
}

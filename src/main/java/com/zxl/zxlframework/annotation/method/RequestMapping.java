package com.zxl.zxlframework.annotation.method;

import com.zxl.zxlframework.annotation.fieldenum.Method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: zxl
 * @Time: 2020/06/26 16:07:41
 * @system: ASUS
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {

    String value();

    Method method() default Method.GET;
}

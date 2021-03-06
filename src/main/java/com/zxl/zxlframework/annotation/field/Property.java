package com.zxl.zxlframework.annotation.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: zxl
 * @Time: 2020/06/24 14:31:32
 * @system: ASUS
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {

    String name() default "";

    String value() default "";

    String ref() default "";
}

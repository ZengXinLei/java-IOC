package com.zxl.zxlframework.annotationFactory.field;

import java.lang.reflect.Field;

/**
 * @Author: zxl
 * @Time: 2020/06/20 22:15:20
 * @system: ASUS
 **/
public interface FieldFactory {
    <T> T build(Field field,Object o);
}

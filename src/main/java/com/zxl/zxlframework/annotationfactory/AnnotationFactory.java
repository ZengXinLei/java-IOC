package com.zxl.zxlframework.annotationfactory;

/**
 * @Author: zxl
 * @Time: 2020/06/24 15:17:25
 * @system: ASUS
 **/
public interface AnnotationFactory {
    <T> T build(Object... initargs);
}

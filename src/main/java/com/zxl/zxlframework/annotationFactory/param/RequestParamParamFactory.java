package com.zxl.zxlframework.annotationFactory.param;

import com.zxl.zxlframework.annotation.field.RequestParam;
import com.zxl.zxlframework.converter.Converter;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @Author: zxl
 * @Time: 2020/06/27 22:10:27
 * @system: ASUS
 **/
public class RequestParamParamFactory implements ParamFactory {

    @Override
    public void build(Class<?> clazz, Object[] parameters, Map<String, String[]> parameterMap, Annotation annotation, int index) {
        RequestParam requestParam=(RequestParam)annotation;
        if(parameterMap.get(requestParam.value())==null){
            return;
        }
        parameters[index]=new Converter(clazz,parameterMap.get(requestParam.value())[0]).getBean();
    }
}

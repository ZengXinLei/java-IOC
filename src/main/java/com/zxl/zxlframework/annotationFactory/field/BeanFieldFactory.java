package com.zxl.zxlframework.annotationFactory.field;

import com.zxl.zxlframework.annotation.field.Bean;
import com.zxl.zxlframework.annotation.field.Property;
import com.zxl.zxlframework.annotationFactory.utils.BuildProperty;

import java.lang.reflect.Field;

/**
 * @Author: zxl
 * @Time: 2020/06/26 09:08:09
 * @system: ASUS
 **/
public class BeanFieldFactory extends BuildProperty implements FieldFactory{


    @Override
    public <T> T build(Field field, Object o) {
        Bean bean = field.getDeclaredAnnotation(Bean.class);
        String name = bean.name();
        Property[] properties = bean.value();
        for (Property p :
                properties) {
            String pName = p.name();
            String pValue = p.value();
            String pRef=p.ref();
            if(!"".equals(pValue)){
                buildValue(name,pValue,o);
            }else {

            }
        }
        return (T) o;
    }

}

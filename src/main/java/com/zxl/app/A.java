package com.zxl.app;

import com.zxl.zxlframework.annotation.field.Bean;
import com.zxl.zxlframework.annotation.field.Iner;
import com.zxl.zxlframework.annotation.field.Property;

import java.util.List;

/**
 * @Author: zxl
 * @Time: 2020/06/20 22:44:55
 * @system: ASUS
 **/
public class A {
    @Iner
    List<Class> list;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    String a;

}

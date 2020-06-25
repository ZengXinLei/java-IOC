package com.zxl.app;
import com.zxl.zxlframework.annotation.field.Iner;

import java.util.List;

/**
 * @Author: zxl
 * @Time: 2020/06/20 22:44:55
 * @system: ASUS
 **/
public class A {
    @Iner
    private List<Class> list;

    public List<Class> getList() {
        return list;
    }

    public void setList(List<Class> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "A{" +
                "list=" + list +
                '}';
    }
}

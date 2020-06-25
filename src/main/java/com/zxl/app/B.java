package com.zxl.app;

/**
 * @Author: zxl
 * @Time: 2020/06/23 10:10:25
 * @system: ASUS
 **/
public class B {

    private String b1;
    private int b2;
    private A a;

    @Override
    public String toString() {
        return "B{" +
                "b1='" + b1 + '\'' +
                ", b2=" + b2 +
                ", a=" + a +
                '}';
    }

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }


    public String getB1() {
        return b1;
    }

    public void setB1(String b1) {
        this.b1 = b1;
    }

    public int getB2() {
        return b2;
    }

    public void setB2(int b2) {
        this.b2 = b2;
    }
}

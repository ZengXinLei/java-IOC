package com.servlet;

import com.zxl.zxlframework.annotation.classz.Controller;
import com.zxl.zxlframework.annotation.field.RequestParam;
import com.zxl.zxlframework.annotation.fieldenum.Method;
import com.zxl.zxlframework.annotation.method.RequestMapping;
import com.zxl.zxlframework.annotation.method.ResponseBody;
import com.zxl.zxlframework.xmlfactory.context.ApplicationContext;

/**
 * @Author: zxl
 * @Time: 2020/06/27 00:17:56
 * @system: ASUS
 **/
@Controller("/test")
public class Test {
    @ResponseBody
    @RequestMapping(value = "/test",method = Method.POST)
    public Student test(
            @RequestParam("age") int age,
            String s,
            ApplicationContext applicationContext
    ){
        Student student=applicationContext.getAttribute("student");
        student.setAge(age);
        return student;
    }

    @RequestMapping("login")
    public String login(){
        return "login.jsp";
    }
}

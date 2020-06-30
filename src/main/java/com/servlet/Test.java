package com.servlet;

import com.zxl.zxlframework.annotation.classz.Controller;
import com.zxl.zxlframework.annotation.field.RequestBody;
import com.zxl.zxlframework.annotation.field.RequestParam;
import com.zxl.zxlframework.annotation.fieldenum.Method;
import com.zxl.zxlframework.annotation.method.RequestMapping;
import com.zxl.zxlframework.annotation.method.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @Author: zxl
 * @Time: 2020/06/27 00:17:56
 * @system: ASUS
 **/
@Controller("/")
public class Test {
//    @ResponseBody
//    @RequestMapping(value = "/test",method = Method.POST)
//    public Student test(
//            @RequestParam("age") int age,
//            String s,
//            ApplicationContext applicationContext
//    ){
//        Student student=applicationContext.getAttribute("student");
//        student.setAge(age);
//        return student;
//    }

    @RequestMapping("login")
    public String login(
            @RequestParam("age") String string
    ){
        System.out.println(string);
        return "login.jsp";
    }
    @ResponseBody

    @RequestMapping(value = "/index",method = Method.POST)
    public String index(HttpServletRequest request, @RequestBody Map map) throws IOException {
//        System.out.println(student.getAge());
        return "OK";
    }
}

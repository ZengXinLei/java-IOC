package com.servlet;

import com.zxl.zxlframework.annotation.classz.Controller;
import com.zxl.zxlframework.annotation.field.RequestParam;
import com.zxl.zxlframework.annotation.fieldenum.Method;
import com.zxl.zxlframework.annotation.method.RequestMapping;

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

    @RequestMapping("/l**")
    String test(){
        return "login.jsp";
    }
    @RequestMapping(value = "/rest",method = Method.POST)
    String rest(@RequestParam("name") String name){
        System.out.println(name);
        return "成功";
    }
//    @RequestMapping("login")
//    String login(
//            @RequestParam("age") String string
//    ){
//        System.out.println(string);
//        return "login.jsp";
//    }
//    @ResponseBody
//
//    @RequestMapping(value = "/index",method = Method.POST)
//    public String index(HttpServletRequest request, @RequestBody Map map) throws IOException {
//        return "OK";
//    }
}

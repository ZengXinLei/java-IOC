package com.servlet;

import com.zxl.zxlframework.annotation.classz.Controller;
import com.zxl.zxlframework.annotation.field.RequestParam;
import com.zxl.zxlframework.annotation.method.RequestMapping;
import com.zxl.zxlframework.annotation.method.ResponseBody;

/**
 * @Author: zxl
 * @Time: 2020/06/27 00:17:56
 * @system: ASUS
 **/
@Controller
public class Test {
    @ResponseBody
    @RequestMapping("/test")
    public Student test(
            @RequestParam("test") int test,
            String s
    ){
        System.out.println(test);
        return null;
    }
}

package com.servlet;

import com.zxl.zxlframework.annotation.classz.Controller;
import com.zxl.zxlframework.annotation.field.RequestParam;
import com.zxl.zxlframework.annotation.method.RequestMapping;

/**
 * @Author: zxl
 * @Time: 2020/06/27 00:17:56
 * @system: ASUS
 **/
@Controller
public class Test {
    @RequestMapping("/test")
    public String test(
            @RequestParam("test") int test
    ){
        System.out.println(test);
        return "成功！！！！";
    }
}

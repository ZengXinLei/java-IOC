package com.zxl.zxlframework.controlller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: zxl
 * @Time: 2020/06/26 16:23:39
 * @system: ASUS
 **/
public class ControllerFactory extends AbstractControllerFactory {

    public ControllerFactory(HttpServletRequest req, HttpServletResponse resp){
        super(req,resp);
    }
}

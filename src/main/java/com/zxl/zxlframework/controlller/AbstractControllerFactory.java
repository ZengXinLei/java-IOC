package com.zxl.zxlframework.controlller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: zxl
 * @Time: 2020/06/26 16:23:32
 * @system: ASUS
 **/
public abstract class AbstractControllerFactory implements Controller {

    public AbstractControllerFactory(HttpServletRequest req, HttpServletResponse resp) {
        String method=req.getMethod();
        if("GET".equals(method)){
            doGet(req, resp);
        }else if("POST".equals(method))
        {
            doPost(req,resp);
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {

    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {

    }
}

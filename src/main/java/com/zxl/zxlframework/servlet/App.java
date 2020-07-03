package com.zxl.zxlframework.servlet;

import com.zxl.zxlframework.controlller.ControllerFactory;
import com.zxl.zxlframework.xmlfactory.context.support.XmlReader;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: zxl
 * @Time: 2020/06/26 10:56:18
 * @system: ASUS
 **/
public class App extends HttpServlet {
    public App() {

    }
    @Override
    public void init() {
        //加载配置文件
        new XmlReader("classpath:application.xml");
    }
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        new ControllerFactory(req, resp);
    }
}

package com.servlet;

import com.zxl.app.A;
import com.zxl.app.B;
import com.zxl.zxlframework.xmlfactory.context.ApplicationContext;
import com.zxl.zxlframework.xmlfactory.context.http.GlobalContext;
import com.zxl.zxlframework.xmlfactory.context.support.XmlReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: zxl
 * @Time: 2020/06/26 10:56:18
 * @system: ASUS
 **/
public class App extends HttpServlet {
    public App() {

    }



    @Override
    public void init() throws ServletException {
        ApplicationContext applicationContext =new XmlReader("classpath:application.xml");



    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ApplicationContext applicationContext =new GlobalContext();
        B b= applicationContext.getAttribute("b");
        A a=applicationContext.getAttribute("a");
        System.out.println(a.toString());
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }



    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }


}

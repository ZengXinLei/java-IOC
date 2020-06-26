package com.servlet;

import com.zxl.app.B;
import com.zxl.zxlframework.xmlFactory.context.support.XMLReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @Author: zxl
 * @Time: 2020/06/26 10:56:18
 * @system: ASUS
 **/
public class App extends HttpServlet {
    public App() {
//        B b =new XMLReader("classpath:example.xml").getBean("b");
//
//        System.out.println(b.toString());
    }



    @Override
    public void init() throws ServletException {
        String realPath = getServletContext().getRealPath("/");
        File[] files = new File(realPath + "../classes").listFiles();
        System.out.println();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

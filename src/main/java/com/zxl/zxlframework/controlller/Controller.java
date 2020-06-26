package com.zxl.zxlframework.controlller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: zxl
 * @Time: 2020/06/26 16:23:10
 * @system: ASUS
 **/
public interface Controller {
    /**
     * 处理get请求
     */
    void doGet(HttpServletRequest req, HttpServletResponse resp);

    /**
     * 处理post请求
     */
    void doPost(HttpServletRequest req, HttpServletResponse resp);
}

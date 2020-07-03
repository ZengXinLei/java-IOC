package com.zxl.zxlframework.annotationfactory.utils;

import com.zxl.zxlframework.annotation.classz.Controller;
import com.zxl.zxlframework.annotation.method.RequestMapping;

/**
 * @Author: zxl
 * @Time: 2020/07/03 21:30:45
 * @system: ASUS
 **/
public class UrlPattern {


    private String reqUrl;

    //controller注解上的url
    private String controllerUrl;

    //    用户的url

    private String url;

    //去掉Controller注解上的url的url

    private String backUrl;

    //requestMapping注解

    private RequestMapping requestMapping;

    //用户的请求方法

    private String type;

    public boolean isFlag() {
        return flag;
    }

    private boolean flag = false;

    public UrlPattern(RequestMapping requestMapping, String url, Controller controller, String type) {
        this.requestMapping = requestMapping;
        this.url = url;
        this.backUrl = url.substring(controller.value().length());
        this.controllerUrl = controller.value();
        this.type=type;
        //如果不是以/开头
        if (!this.backUrl.startsWith("/")) {
            this.backUrl = "/" + this.backUrl;
        }


        this.reqUrl = requestMapping.value();
        if (!reqUrl.startsWith("/")) {
            reqUrl = "/" + reqUrl;
        }
        basePattern();
        regPattern();
        
    }


    private void basePattern() {
        if (this.reqUrl.equals("/".equals(this.controllerUrl) ? this.url : backUrl) && requestMapping.method().name().equals(this.type)) {
            this.flag=true;
        }
    }
    private void regPattern(){
        String pattern="^"+requestMapping.value().replaceAll("\\*\\*","(.*)")+"$";
//        System.out.println(pattern);
        if(backUrl.matches(pattern)){
            flag=true;
        }
    }
}

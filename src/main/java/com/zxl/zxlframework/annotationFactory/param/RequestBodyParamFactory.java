package com.zxl.zxlframework.annotationFactory.param;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;

/**
 * @Author: zxl
 * @Time: 2020/06/30 10:22:56
 * @system: ASUS
 **/
public class RequestBodyParamFactory implements ParamFactory {
    @Override
    public void build(Class<?> clazz, Object[] parameters, HttpServletRequest req, Annotation annotation, int index) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(req.getInputStream(),"utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line = null;
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                if (!((line = br.readLine()) != null)) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            sb.append(line);
        }

        parameters[index]=JSONObject.parseObject(sb.toString(),clazz);
        int a=6;
    }
}

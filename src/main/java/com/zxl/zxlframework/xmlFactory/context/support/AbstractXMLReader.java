package com.zxl.zxlframework.xmlFactory.context.support;

import com.zxl.zxlframework.annotationFactory.AnnotationBeanFactory;
import com.zxl.zxlframework.converter.Converter;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import com.zxl.zxlframework.xmlFactory.context.ConfigurableApplicationContext;
import org.xml.sax.SAXException;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;

/**
 * @Author: zxl
 * @Time: 2020/06/23 11:18:55
 * @system: ASUS
 **/
public abstract class AbstractXMLReader implements ConfigurableApplicationContext {


    private static Element root = null;
    private static boolean flag = true;

    public AbstractXMLReader(String config) {
        String filiname = config;
        if (config.startsWith("classpath:")) {
            String path[] = config.split(":");
            String file = path[1];
            String p = ClassLoader.getSystemResource("").getPath();
            filiname = p + "/" + file;
        }
        flag = validateXMLByXSD(filiname);
        try {
            root = new SAXReader().read(new File(filiname)).getRootElement();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    @Override
    public <T> T getBean(String name) {
        if (!flag) {
            return null;
        }
        List<Element> elements = root.elements();
        for (Element e :
                elements) {

            if (e.attributeValue("name").equals(name)) {

                return buildBean(e);

            }

        }
        return null;
    }

    private <T> T buildBean(Element e) {

        Class<?> aClass = null;
        String classname = e.attributeValue("class");
        try {
            aClass = Class.forName(classname);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        Object object = new AnnotationBeanFactory(aClass).build();


        List<Element> elements = e.elements();
        for (Element e1 : elements) {
            String key = e1.attributeValue("name");
            String value = e1.attributeValue("value");
            String ref = e1.attributeValue("ref");

            try {
                buildValue(key, value, ref, object);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException | NoSuchFieldException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }

        }
        return (T) object;
    }

    public void buildValue(String name, String value, String ref, Object o) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, NoSuchFieldException, ClassNotFoundException {

        //首字母大写
        char[] chars = name.toCharArray();
        if (chars[0] >= 97 && chars[0] <= 122)
            chars[0] -= 32;
        //获取setter方法
        String methodName = "set" + String.valueOf(chars);
        Method[] methods = o.getClass().getSuperclass().getDeclaredMethods();

        for (Method m :
                methods) {
            if (m.getName().equals(methodName)) {

                if (value != null){
                    Object v = dealType(name, value, o);

                    m.invoke(o, v);
                }
                else
                {
                    Field field=o.getClass().getSuperclass().getDeclaredField(name);
                    field.setAccessible(true);
                    field.set(o,getBean(ref));
                }
            }

        }
    }

    public Object dealType(String name,String value,Object o) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException {

        Field field=o.getClass().getSuperclass().getDeclaredField(name);
        return new  Converter(field.getType(),value).getBean();
    }
    public Object dealObject(String name,String ref,Object o) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException {

        Field field=o.getClass().getSuperclass().getDeclaredField(name);
        return new  Converter(field.getType(),getBean(ref)).getBean();
    }




    public boolean validateXMLByXSD(String xmlFileName) {
        String xsdFileName = "https://cdn.jsdelivr.net/gh/ZengXinLei/blog@master/xsd/zxl-1.0.xsd";
//        String xsdFileName=ClassLoader.getSystemResource("").getPath()+"/xsd/zxl-1.0.xsd";
        boolean flag = true;

        try {

            URL schemaFile = new URL(xsdFileName);
            HttpsURLConnection schemaConn = (HttpsURLConnection) schemaFile.openConnection();
            InputStream is = schemaConn.getInputStream();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new StreamSource(is));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlFileName)));
        } catch (IOException | SAXException e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    public <T> T value(Object o) {
        return (T) o;
    }

}

package com.zxl.zxlframework.xmlfactory.context.support;

import com.zxl.zxlframework.annotationfactory.AnnotationBeanFactory;
import com.zxl.zxlframework.converter.Converter;
import com.zxl.zxlframework.xmlfactory.context.ApplicationContext;
import com.zxl.zxlframework.xmlfactory.context.http.GlobalContext;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
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
public abstract class AbstractXmlReader implements ApplicationContext {


    /**
     * //<zxl>元素
     */

    private static Element root = null;
    /**
     * xml文件是否合法
     */

    private static boolean flag = true;

    private ApplicationContext applicationContext;
    public AbstractXmlReader(String config) {
        String filiname = config;
        //如果路径开头为classpath:，则进行以下处理
        if (config.startsWith("classpath:")) {
            String[] path = config.split(":");
            //获取文件名
            String file = path[1];
            //获取当前工作目录路径

            String p = this.getClass().getClassLoader().getResource("/").getPath();
            //进行拼接
            filiname = p + "/" + file;
        }
        if(!new File(filiname).exists()){
            return;
        }
        //获取配置文件的合法性
        flag = validateXMLByXSD(filiname);
        try {
            //获取配置文件的<zxl>标签
            root = new SAXReader().read(new File(filiname)).getRootElement();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //初始化GlobalContext
        applicationContext =new GlobalContext();
        initAttribute();
    }

    /**
     * 根据xml文件里的bean标签的name属性获取动态代理对象，并进行依赖注入
     * @param name  xml文件里bean的name属性
     * @param <T>   动态代理对象
     * @return
     */
    @Override
    public <T> T getAttribute(String name) {

        return applicationContext.getAttribute(name);
    }

    @Override
    public void setAttribute(String name, Object o) {
        applicationContext.setAttribute(name,o);

    }

    /**
     * 初始化全局map
     */

    public void initAttribute(){
        //如果配置文件不合法
        if (!flag) {
            return;
        }
        //获取所有<bean>标签
        List<Element> elements = root.elements();
        //遍历所有bean
        for (Element e : elements) {
            //存入map
            String name = e.attributeValue("name");
            Object o = buildBean(e);
            applicationContext.setAttribute(name,o);

        }

    }
    /**
     * 进行依赖注入
     * @param e 指定的bean元素
     * @param <T>   动态代理对象
     * @return
     */
    private <T> T buildBean(Element e) {

        Class<?> aClass = null;
        //获取bean标签的class属性
        String classname = e.attributeValue("class");
        try {
            //根据class属性获取类
            aClass = Class.forName(classname);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        //获取这个类的动态代理对象
        //该对象已经对注解进行过处理
        Object object = new AnnotationBeanFactory(aClass).build();


        //获取bean标签下的所有property标签
        List<Element> elements = e.elements();
        for (Element e1 : elements) {
            //获取name属性
            String key = e1.attributeValue("name");
            //获取value属性
            String value = e1.attributeValue("value");
            //获取ref属性
            String ref = e1.attributeValue("ref");


            //用这三个属性对动态代理对象进行处理
            buildProperty(key, value, ref, object);


        }
        return (T) object;
    }

    /**
     *
     * @param name name属性
     * @param value value属性
     * @param ref   ref属性
     * @param o
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws NoSuchFieldException
     * @throws ClassNotFoundException
     */
    public void buildProperty(String name, String value, String ref, Object o){

        //name内容首字母大写
        char[] chars = name.toCharArray();
        if (chars[0] >= 97 && chars[0] <= 122){
            chars[0] -= 32;
        }

        //获取setter方法
        String methodName = "set" + String.valueOf(chars);
        //获取动态代理对象父类的所有方法
        /*
        这里如果用通过名字获取方法的话好像会出现问题，现在暂时这么写
         */
        Method[] methods = o.getClass().getSuperclass().getDeclaredMethods();

        //遍历方法
        for (Method m :
                methods) {
            //如果方法名匹配
            if (m.getName().equals(methodName)) {
                //如果value属性不为空
                //即传入的是属性
                if (value != null){
                    //进行类型转换
                    Object v = null;
                    try {
                        v = dealType(name, value, o);
                        m.invoke(o, v);
                    } catch (NoSuchFieldException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    //执行属性的set方法

                }
                //如果传入的是对象
                else
                {
                    //直接更改对象的field属性
                    //这里如果通过set方法注入的话好像也会出错
                    Field field= null;
                    try {
                        field = o.getClass().getSuperclass().getDeclaredField(name);
                        field.setAccessible(true);
                        field.set(o, InerBean(ref));
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }
            }

        }
    }

    /**
     *
     * @param name name属性
     * @param value value属性
     * @param o
     * @return
     * @throws NoSuchFieldException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     */
    private Object dealType(String name,String value,Object o) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException {
        //获取动态代理对象的属性
        Field field=o.getClass().getSuperclass().getDeclaredField(name);
        //返回类型转换工厂生成的对象
        return new  Converter(field.getType(),value).getBean();
    }


    /**
     * 检测xml文件是否合法
     * @param xmlFileName
     * @return
     */
    private boolean validateXMLByXSD(String xmlFileName) {
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

    /**
     * 根据xml文件里的bean标签的name属性获取动态代理对象，并进行依赖注入
     * @param name  xml文件里bean的name属性
     * @param <T>   动态代理对象
     * @return
     */
    private  <T> T InerBean(String name) {
        //如果配置文件不合法
        if (!flag) {
            return null;
        }
        //获取所有<bean>标签
        List<Element> elements = root.elements();

        //遍历所有bean
        for (Element e :
                elements) {
            //如果bean的name属性和传入进来的name相同
            if (e.attributeValue("name").equals(name)) {
                //返回注入后的动态代理对象
                return buildBean(e);

            }

        }
        return null;
    }

}

## **java IOC**

> java 实现依赖注入动态代理对象

- [**java IOC**](#--java-ioc--)
- [简介](#--)
- [注解依赖注入](#------)
  * [注解使用方法](#------)
  * [注解注入的实现](#-------)
    + [自定义注解`@Iner`](#-------iner-)
    + [`AnnotationBeanFactory`类](#-annotationbeanfactory--)
    + [`AnnotationBeanFactory`继承的抽象类`AbstractAnnotationBeanFactory`](#-annotationbeanfactory--------abstractannotationbeanfactory-)
    + [`AbstractAnnotationBeanFactory`类实现的接口`AnnotationFactory`](#-abstractannotationbeanfactory--------annotationfactory-)
    + [BuildFiledFactory类](#buildfiledfactory-)
    + [InerFactory类](#inerfactory-)
- [xml配置文件依赖注入](#xml--------)
  * [xml配置文件注入使用方法](#xml----------)
  * [xml配置文件注入的实现](#xml---------)
    + [配置文件](#----)
    + [XMLReader类](#xmlreader-)
    + [`AbstractXMLReader`类](#-abstractxmlreader--)
    + [`ApplicationContext`接口](#-applicationcontext---)
- [类型转换器Converter](#-----converter)
    + [`Converter`类](#-converter--)
    + [`AbstractConverter`类](#-abstractconverter--)

## 简介

分为两种注入模式，注解注入动态代理对象和xml配置文件注入动态代理对象

项目仍然在维护中，有问题请交lssue。

存在一些设计上的缺陷

qq:1599484837

wx:17872338819



## 注解依赖注入

### 注解使用方法

实体类:

```java
public class A {
    /*
    `@Iner`的作用就是注入一个当前工作目录所有Class的list
    */
    @Iner
    List<Class> list;
}
```



主类:

```java
public class App
{

    public static void main(String[] args) {
        A a=new AnnotationBeanFactory(A.class).build();
        List<Class> list = a.list;
    }
}
```

### 注解注入的实现

#### 自定义注解`@Iner`

```java
@Target(ElementType.FIELD)//属性注解
@Retention(RetentionPolicy.RUNTIME)
public @interface Iner {
}
```

#### `AnnotationBeanFactory`类

```java
public class AnnotationBeanFactory extends AbstractAnnotationBeanFactory {


    public AnnotationBeanFactory(Class<?> clazz){
        super(clazz);
    }


}
```

该类继承该类的抽象类`AbstractAnnotationBeanFactory`，构造器接收一个`Class` 类型的参数，一般为要创建的对象的类型，然后调用父类的构造器。

#### `AnnotationBeanFactory`继承的抽象类`AbstractAnnotationBeanFactory`



```java
/**
 * 注解工厂抽象类，实现注解工厂接口
 */
public abstract class AbstractAnnotationBeanFactory implements AnnotationFactory {
    private Class<?> aClass;
    public AbstractAnnotationBeanFactory(Class<?> clazz){
        this.aClass=clazz;
    }


    /**
     * 创建一个动态代理对象，并对属性注解和方法注解进行处理
     * @param initargs
     * @param <T>
     * @return
     */
    @Override
    public <T> T build(Object... initargs) {
        //CGLIB动态代理
        Enhancer enhancer=new Enhancer();//实例化动态代理
        enhancer.setSuperclass(aClass);//设置动态的代理对象继承的类
        /**
         * 设置回调拦截器
         */
        enhancer.setCallback((MethodInterceptor) (o, method, objects, methodProxy) -> {
            /**
             * 在这里调用methodBuild(Object o)方法来处理方法注解，目前没定义方法注解
             */
            return methodProxy.invokeSuper(o,objects);
        });

        Object o =enhancer.create();//获取动态代理对象

        //属性注解处理
        fieldBuild(o);
        return (T) o;
    }

    /**
     * 处理属性注解
     * @param o 动态代理对象
     */
    private void fieldBuild(Object o){
        //获取动态代理对象父类，即用户想要创建对象的所有属性
        Field[] fields = o.getClass().getSuperclass().getDeclaredFields();
        //遍历动态代理对象父类的所有属性
        for (Field f : fields) {
            /*
            获取该对象的所有对应注解的工厂对象列表
             */
            ArrayList<FieldFactory> fieldFactories = BuildFiledFactory.build(f);

            /*
            遍历该列表，调用build方法
             */
            for (FieldFactory fieldFactory:
                 fieldFactories) {
                fieldFactory.build(f,o);
            }
        }
    }

    /**
     * 处理方法注解
     * @param o 动态代理对象
     */
    private void methodBuild(Object o){
//        Method[] methods = o.getClass().getDeclaredMethods();
    }
}

```

该抽象类首先实现了注解工厂类的`build`方法，改方法生成了用户对象的动态代理类，并对该类的属性注解和方法注解进行处理，由于目前没有定义方法注解，所以`methodBuild`方法是空的。

#### `AbstractAnnotationBeanFactory`类实现的接口`AnnotationFactory`

```java
public interface AnnotationFactory {
    <T> T build(Object... initargs);
}
```



#### BuildFiledFactory类

在`AnnotationBeanFactory`类的`fieldBuild`方法中，对代理对象的每个属性都调用了

`BuildFiledFactory`类的静态方法`build`,该方法接受一个Field类型的参数，为对象的一个属性，返回该属性上所有注解对应的工厂类

该类代码如下:

```java
public class BuildFiledFactory {
    //属性注解的包路径，这里写死
    final static String FIELDANNOTATION ="com.zxl.zxlframework.annotation.field.";

    //属性注解工厂包路径，也写死
    final static String FIELDANNOTATIONFACTORY ="com.zxl.zxlframework.annotationFactory.field.";
    /*
    自定义注解名称写在该数组里
    当有新的自定义属性在注解时
    在该数组里添加注解名称
    然后添加自定义注解的工厂来处理该注解即可，最大程度的接近开闭原则
     */
    final static String[] FIELDANNOTATIONS ={
      "Iner"
    };

    /**
     * 返回属性的所有注解的对应的工厂list
     * @param f 对象的属性
     * @return
     */
    public static ArrayList<FieldFactory> build(Field f){
//        创建一个属性注解工厂的list
        ArrayList<FieldFactory> fieldFactories=new ArrayList<>();

        //遍历所有属性注解名称
        for (String annotation:
                FIELDANNOTATIONS) {
            try {
//                如果该属性上有该注解
                if(f.isAnnotationPresent((Class<? extends Annotation>) Class.forName(FIELDANNOTATION +annotation))){
//                    获取该注解对应的工厂
                    FieldFactory fieldFactory= (FieldFactory) Class.forName(FIELDANNOTATIONFACTORY +annotation+"Factory").getDeclaredConstructor().newInstance();
//                    添加到list里
                    fieldFactories.add(fieldFactory);
                  }
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }

        }
//        返回List
        return fieldFactories;
    }
}

```

`FieldFactory`接口

```java
public interface FieldFactory {
    <T> T build(Field field,Object o);
}
```

所有的注解都应该创建一个该注解的工厂来实现该接口，由于现在且这些工厂应该都在`com.zxl.zxlframework.annotationFactory.field`包下，而属性注解都应该在`com.zxl.zxlframework.annotation.field`包下

#### InerFactory类

`@iner`注解的工厂类，该类实现了`FieldFactory`接口

```java
public class InerFactory implements FieldFactory {
    /**
     * 
     * @param field 动态代理对象
     * @param o 动态代理对象的父类的属性
     * @param <T>   null
     * @return
     */
    @Override
    public <T> T build(Field field,Object o) {

        /*
        Util.getClasses()该方法返回该工作目录下所有的类的Class的list
         */
        ArrayList<Class<?>> list = Util.getClasses();
        //设置该属性权限
        field.setAccessible(true);
        try {
            //把该属性更改为list
            field.set(o,list);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}

```

## xml配置文件依赖注入

### xml配置文件注入使用方法

实体类`A`

```java
public class A {
    @Iner
    private List<Class> list;

    public List<Class> getList() {
        return list;
    }

    public void setList(List<Class> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "A{" +
                "list=" + list +
                '}';
    }
}
```

实体类`B`

```java
public class B {

    private String b1;
    private int b2;
    private A a;

    @Override
    public String toString() {
        return "B{" +
                "b1='" + b1 + '\'' +
                ", b2=" + b2 +
                ", a=" + a +
                '}';
    }

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }


    public String getB1() {
        return b1;
    }

    public void setB1(String b1) {
        this.b1 = b1;
    }

    public int getB2() {
        return b2;
    }

    public void setB2(int b2) {
        this.b2 = b2;
    }
}

```

example.xml配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<zxl  xmlns="http://www.miluyiguan.club"
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xsi:schemaLocation="http://www.miluyiguan.club
     https://cdn.jsdelivr.net/gh/ZengXinLei/blog@master/xsd/zxl-1.0.xsd">


    <bean name="b" class="com.zxl.app.B">
        <property name="b1" value="注入的字符串"/>
        <property name="b2" value="888"/>
        <property name="a" ref="a"/>

    </bean>

    <bean name="a" class="com.zxl.app.A">
    </bean>
</zxl>
```





主类`App`

```java
public class App
{

    public static void main(String[] args) {
        ApplicationContext applicationContext =new XMLReader("classpath:example.xml");
        B b = applicationContext.getBean("b");
        System.out.println(b.toString());
    }
}
```

输出

```java
B{b1='注入的字符串', b2=888, a=A{list=[class com.zxl.app.A, class com.zxl.app.App, class com.zxl.app.B, class com.zxl.util.BeanUtil$1, class com.zxl.util.BeanUtil$FieldType, class com.zxl.util.BeanUtil, class com.zxl.util.Util, interface com.zxl.zxlframework.annotation.field.Bean, interface com.zxl.zxlframework.annotation.field.Iner, interface com.zxl.zxlframework.annotation.field.Property, class com.zxl.zxlframework.annotationFactory.AbstractAnnotationBeanFactory, class com.zxl.zxlframework.annotationFactory.AnnotationBeanFactory, interface com.zxl.zxlframework.annotationFactory.AnnotationFactory, class com.zxl.zxlframework.annotationFactory.BuildFiledFactory, interface com.zxl.zxlframework.annotationFactory.field.FieldFactory, class com.zxl.zxlframework.annotationFactory.field.InerFactory, class com.zxl.zxlframework.converter.AbstractConverter, class com.zxl.zxlframework.converter.Converter, interface com.zxl.zxlframework.converter.factory.ConverterFactory, interface com.zxl.zxlframework.xmlFactory.beans.factory.BeanFactory, interface com.zxl.zxlframework.xmlFactory.beans.factory.ListableBeanFactory, interface com.zxl.zxlframework.xmlFactory.context.ApplicationContext, interface com.zxl.zxlframework.xmlFactory.context.ConfigurableApplicationContext, class com.zxl.zxlframework.xmlFactory.context.support.AbstractXMLReader, class com.zxl.zxlframework.xmlFactory.context.support.XMLReader]}}
```



### xml配置文件注入的实现

先获取`AnnotationBeanFactory`类生成的处理过注解的动态代理对象，然后读取配置文件，按文件内容通过对象的`setter`方法进行注入

#### 配置文件

xml文件需要使用约束头

```xml
<zxl  xmlns="http://www.miluyiguan.club"
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xsi:schemaLocation="http://www.miluyiguan.club
     https://cdn.jsdelivr.net/gh/ZengXinLei/blog@master/xsd/zxl-1.0.xsd">
```

zxl-1.0.xsd文件内容如下

```xml
<?xml version="1.0" encoding="UTF-8"?>
<zxl:schema xmlns:zxl="http://www.w3.org/2001/XMLSchema"
            targetNamespace="http://www.miluyiguan.club"
            xmlns="http://www.miluyiguan.club"
            elementFormDefault="qualified"
           >
<!--    <zxl:import namespace="http://www.w3.org/XML/1998/namespace"/>-->
    <zxl:element name="zxl" >
        <zxl:complexType>
            <zxl:sequence>
                <zxl:element ref="bean"  minOccurs="0" maxOccurs="unbounded"/>
            </zxl:sequence>
        </zxl:complexType>
    </zxl:element>


    <zxl:element name="bean">

        <zxl:complexType>
            <zxl:sequence>
                <zxl:element ref="property" minOccurs="0" maxOccurs="unbounded"/>
            </zxl:sequence>
            <zxl:attributeGroup ref="bean_attribute"/>

        </zxl:complexType>
    </zxl:element>
    <zxl:element name="property">
        <zxl:complexType>
            <zxl:attributeGroup ref="property_attribute"/>
        </zxl:complexType>
    </zxl:element>



    <zxl:attributeGroup name="bean_attribute">
        <zxl:attribute name="class" type="zxl:string">
            <zxl:annotation>
                <zxl:documentation source="java:java.lang.Class"><![CDATA[
                The fully qualified name of the bean's class, except if it serves only
                as a parent definition for child bean definitions.
                            ]]></zxl:documentation>
            </zxl:annotation>
        </zxl:attribute>
        <zxl:attribute name="name" type="zxl:string"/>

    </zxl:attributeGroup>
    <zxl:attributeGroup name="property_attribute">
        <zxl:attribute name="name" type="zxl:string" use="required"/>
        <zxl:attribute name="value" type="zxl:string"/>
        <zxl:attribute name="ref" type="zxl:string"/>
    </zxl:attributeGroup>




</zxl:schema>
```

规定内容大致如下

\<bean>

| 属性  | 必须/可选 | 数量 | 备注 |
| :---: | :-------: | :--: | :--: |
| name  |   必须    |  1   | 名称 |
| class |   必须    |  1   | 类名 |

\<property>

| 属性  | 必须/可选 | 数量 | 备注                |
| ----- | --------- | ---- | ------------------- |
| name  | 必须      | 1    | 属性名称            |
| value | 可选      | 1    | 属性的值            |
| ref   | 可选      | 1    | 配置文件中的\<bean> |



#### XMLReader类

继承了`AbstractXMLReader`类,构造器接受一个参数，为配置文件的地址。调用了父类的构造器，把路径传给父类

#### `AbstractXMLReader`类

该类实现了`ApplicationContext`接口，通过读取配置文件来进行依赖注入

```java
public abstract class AbstractXMLReader implements ApplicationContext {


    //<zxl>元素
    private static Element root = null;
    //xml文件是否合法
    private static boolean flag = true;

    public AbstractXMLReader(String config) {
        String filiname = config;
        //如果路径开头为classpath:，则进行以下处理
        if (config.startsWith("classpath:")) {
            String path[] = config.split(":");
            //获取文件名
            String file = path[1];
            //获取当前工作目录路径
            String p = ClassLoader.getSystemResource("").getPath();
            //进行拼接
            filiname = p + "/" + file;
        }
        //获取配置文件的合法性
        flag = validateXMLByXSD(filiname);
        try {
            //获取配置文件的<zxl>标签
            root = new SAXReader().read(new File(filiname)).getRootElement();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据xml文件里的bean标签的name属性获取动态代理对象，并进行依赖注入
     * @param name  xml文件里bean的name属性
     * @param <T>   动态代理对象
     * @return
     */
    @Override
    public <T> T getBean(String name) {
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

            try {
                //用这三个属性对动态代理对象进行处理
                buildProperty(key, value, ref, object);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException | NoSuchFieldException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }

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
    private void buildProperty(String name, String value, String ref, Object o) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, NoSuchFieldException, ClassNotFoundException {

        //name内容首字母大写
        char[] chars = name.toCharArray();
        if (chars[0] >= 97 && chars[0] <= 122)
            chars[0] -= 32;
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
                    Object v = dealType(name, value, o);
                    //执行属性的set方法
                    m.invoke(o, v);
                }
                //如果传入的是对象
                else
                {
                    //直接更改对象的field属性
                    //这里如果通过set方法注入的话好像也会出错
                    Field field=o.getClass().getSuperclass().getDeclaredField(name);
                    field.setAccessible(true);
                    field.set(o,getBean(ref));
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


}
```

#### `ApplicationContext`接口

```java
public interface ApplicationContext  {
    <T> T getBean(String className);
}

```

## 类型转换器Converter

目前只有将字符串转为其它基础类的功能



#### `Converter`类

继承了`AbstractConverter`类



```java
public class Converter extends AbstractConverter {

    public Converter(Class<?> s,Object t) {
        super(s, t);
    }

}
```

构造器有两个类型，`s`为待转换的类的类型，`t`为目标类

#### `AbstractConverter`类

```java
public abstract class AbstractConverter{


    private Object value = null;
    //转换工厂的包路径
    final static String CONVERTERFACTORYPACKAGE ="com.zxl.zxlframework.converter.factory.";

    //所有转换工厂
    //如果要扩展，OtherConverterFactory工厂应该放在最后
    final static String[] CONVERTERFACTORYS ={
      "BaseConverterFactory",
      "OtherConverterFactory"
    };
    public AbstractConverter(Class<?> s, Object t) {
        //如果属性和目标属性类型一样
        if (s.getName().equals(t.getClass().getName())) {
            value = t;
            return;
        }
        //遍历所有转换工厂
        for (String cFactory :
                CONVERTERFACTORYS) {
            if (value == null){
                try {
                    //获取转换工厂对象
                    Object converterFactory = Class.forName(CONVERTERFACTORYPACKAGE + cFactory).getDeclaredConstructor().newInstance();
                    Class[] cArgs=new Class[2];
                    cArgs[0]=Class.class;
                    cArgs[1]=Object.class;
                    //获取转换工厂对象的build方法
                    Method build = converterFactory.getClass().getDeclaredMethod("build", cArgs);
                    //转换完的值
                    value=build.invoke(converterFactory,s,t);

                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    public <K> K getBean() {
        return (K) value;
    }
}

```
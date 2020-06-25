## **java IOC**

> java 实现依赖注入动态代理对象

## 简介

分为两种注入模式，注解注入动态代理对象和xml配置文件注入动态代理对象

目录结构:

![tree](F:\all\github\Java-IOC\img\tree.png)

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


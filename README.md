# 使用方式

## 导入方式

### 使用maven

pom.xml

```xml
<dependencies>

    <!-- https://mvnrepository.com/artifact/javax.servlet/servlet-api -->
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>servlet-api</artifactId>
      <version>3.0-alpha-1</version>
      <scope>provided</scope>
    </dependency>

    <dependency>
      <groupId>cglib</groupId>
      <artifactId>cglib</artifactId>
      <version>3.2.12</version>
    </dependency>
    <!-- https://mvnrepository.com/artifact/dom4j/dom4j  -->
    <dependency>
      <groupId>dom4j</groupId>
      <artifactId>dom4j</artifactId>
      <version>1.6.1</version>
    </dependency>


    <!-- https://mvnrepository.com/artifact/com.alibaba/fastjson -->
    <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>fastjson</artifactId>
      <version>1.2.71</version>
    </dependency>

    <dependency>
      <groupId>ZengXinLei</groupId>
      <artifactId>java-IOC</artifactId>
      <version>1.0</version>
    </dependency>
  </dependencies>
```

### 使用jar包

下载该项目下lib文件夹下所有jar包并导入

## 配置

该框架基于servlet进行开发，所以只需配置一个web.xml文件即可

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">

    <servlet>
        <servlet-name>zxl</servlet-name>
        <servlet-class>com.zxl.zxlframework.servlet.App</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>zxl</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```

## 页面转发

新建一个类，这里取名为App,添加@Controller注解告诉框架这是一个控制类，在方法上面添加@RequestMapping注解，则会根据注解里的value属性执行对应的方法，并返回视图，代码如下

```java
@Controller
public class App {
    @RequestMapping("login")
    String login(){
        return "/login.jsp";
    }
}
```

在web根目录下新建login.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>登录</h1>
</body>
</html>

```

访问[localhost/login](localhost/login)即可跳转到login.jsp视图

@Controller注解有一个参数value,默认为‘/’

@RequestMapping注解有两个参数，value为方法的请求链接，method为方法的请求方式

## 模糊匹配

新建一个App类

```java
@Controller
public class App {
    @RequestMapping("l**")
    String login(){
        return "/login.jsp";
    }
}
```

注意，这里改了@RequestMapping注解里的value参数，这样，任何以l开头的链接都会在这个方法进行处理

## @RequestParam注解

更改login.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    
</head>
<body>
<h1><a href="/rest?name=zxl">跳转</a></h1>
</body>
</html>
```

App类

```java
@Controller
public class App {
    @RequestMapping("login")
    String login(){
        return "/login.jsp";
    }
    @RequestMapping(value = "/rest",method = Method.POST)
    String rest(HttpServletRequest req, @RequestParam("name") String name){
        System.out.println(name);
        req.setAttribute("name",name);
        return "/app.jsp";
    }
}
```

app.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>${requestScope.get("name")}</h1>
</body>
</html>
```

当点击跳转链接后，会请求到rest方法，进行参数自动注入，然后把参数写入request作用域，返回app.jsp的视图

## @RequestBody注解

定义一个实体类 Student

```java
public class Student {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private String name;
    private int age;
}
```

更改login.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js"></script>
</head>
<body>
</body>
<script>
    $.ajax({
        url:"/rest",
        data:JSON.stringify({
            "name":"stu",
            "age":"21"
        }),
        type:"POST"
    })
</script>
</html>
```

更改App类

```java
@Controller
public class App {
    @RequestMapping("login")
    String login(){
        return "/login.jsp";
    }
    @RequestMapping(value = "/rest",method = Method.POST)
    String rest(HttpServletRequest req, @RequestBody Student student){
        req.setAttribute("name",student.getName());
        return "/app.jsp";
    }
}
```

app.jsp不变，发送ajax请求的时候，会自动对student进行注入，打开浏览器的调试模式，查看/rest请求的返回内容，即可看到app的视图

## @ResponseBody注解

该注解作用在方法上，声明该方法会返回文本内容，而不是视图

App类

```java
@Controller
public class App {
    @RequestMapping("login")
    String login(){
        return "/login.jsp";
    }
    @ResponseBody
    @RequestMapping(value = "/rest",method = Method.POST)
    Student rest(@RequestBody Student student){
        return student;
    }
}
```

login.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js"></script>
</head>
<body>
</body>
<script>
    $.ajax({
        url:"/rest",
        data:JSON.stringify({
            "name":"stu",
            "age":"21"
        }),
        // contentType: "application/json;charset=utf-8",
        type:"POST",
        success:function (result) {
            console.log(result)
        }
    })
</script>
</html>
```

然后，就会在控制台打印出这个student的对象信息

## application.xml配置

这是一个可选配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<zxl  xmlns="http://www.miluyiguan.club"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://www.miluyiguan.club
     https://cdn.jsdelivr.net/gh/ZengXinLei/blog@master/xsd/zxl-1.0.xsd">



</zxl>
```

新建Student类

```java
public class Student {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private String name;
    private int age;
}

```

新建Teacher类

```java
public class Teacher {
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    private Student student;
}

```

在application.xml里面这样配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<zxl  xmlns="http://www.miluyiguan.club"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://www.miluyiguan.club
     https://cdn.jsdelivr.net/gh/ZengXinLei/blog@master/xsd/zxl-1.0.xsd">

    <bean name="studdent" class="org.example.Student">
        <property name="name" value="学生"/>
        <property name="age" value="21"/>
    </bean>

    <bean name="teacher" class="org.example.Teacher">
        <property name="student" ref="student"/>
    </bean>
</zxl>
```

其中，ref指向另一个对象

然后在控制器里面使用

```java
@Controller
public class App {
    @ResponseBody
    @RequestMapping(value = "/rest",method = Method.POST)
    Teacher rest(ApplicationContext applicationContext){
        return applicationContext.getAttribute("teacher");
    }
}
```

访问该接口，则会返回teacher对象
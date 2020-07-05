<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2020/6/26
  Time: 11:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js"></script>
</head>
<body>
<h1>注册</h1>
</body>
<script>
    $.ajax({
        url:"/rest",
        data:{
            "name":"stu",
            "age":"21"
        },
        // contentType: "application/json;charset=utf-8",
        type:"POST",
        success:function (result) {
            console.log(result)
        }
    })
</script>
</html>

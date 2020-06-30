
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js"></script>
</head>
<body>
<h2>Hello World!</h2>
</body>
<script>
    let stu={
        "name":"曾鑫磊",
        "age":"21"
    }
    $.ajax({
        url:"/index",
        data:JSON.stringify({
            "name":"stu",
            "age":"21"
        }),
        contentType: "application/json;charset=utf-8",
        type:"POST",
        success:function (result) {
            console.log(result)
        }
    })
</script>
</html>

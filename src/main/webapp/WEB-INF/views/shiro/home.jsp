<%--
  Created by IntelliJ IDEA.
  User: 刘忠伟
  Date: 2017/1/19
  Time: 11:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Document</title>
</head>
<body>
    <h1>欢迎<shiro:principal property="userName"/>登录成功</h1>
    <%--使用shiro标签，获取登录认证中的SimpleAuthenticationInfo方法传的三个参数，中的第一个参数。实质就是获取session--%>

</body>
</html>

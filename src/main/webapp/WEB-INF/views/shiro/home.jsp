
<%--
  Created by IntelliJ IDEA.
  User: 刘忠伟
  Date: 2017/1/12
  Time: 20:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>AdminLTE 2 | Blank Page</title>
    <%@include file="../include/css.jsp" %>
    <%--<link rel="stylesheet" href="/static/adminLTE2/plugins/datatables/jquery.dataTables.min.css">--%>

</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">


    <%@include file="../include/header.jsp" %>
    <%--jsp节点向某个页面传值。sys_accounts代表本页面。同属于一个节点下的以sys_开头 --%>
    <%@include file="../include/sidebar.jsp"%>
    <!-- =============================================== -->
        <div class="content-wrapper">

            <!-- Main content -->
            <section class="content">
    <h1>欢迎<shiro:principal property="userName"/>登录成功</h1>
    <%--使用shiro标签，获取登录认证中的SimpleAuthenticationInfo方法传的三个参数，中的第一个参数。实质就是获取session--%>
    </section>
            </div>


</div>
<!-- ./wrapper -->

<!-- js -->
<%@include file="../include/js.jsp" %>


</body>
</html>

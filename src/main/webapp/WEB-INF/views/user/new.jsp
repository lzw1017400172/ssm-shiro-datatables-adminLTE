<%--
  Created by IntelliJ IDEA.
  User: 刘忠伟
  Date: 2017/1/12
  Time: 19:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Document</title>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css">
    <%@include file="../include/css.jsp"%>
</head>
<body  class="hold-transition skin-blue sidebar-mini">

<%@include file="../include/header.jsp"%>
<jsp:include page="../include/sidebar.jsp">
    <jsp:param name="menu" value="sys_accounts"/>
</jsp:include>
<div class="content-wrapper">

    <!-- Main content -->
    <section class="content ">

        <!-- Default box -->
        <div class="box box-solid box-primary">
            <div class="box-header with-border">
                <h3 class="box-title">新增用户</h3>
                <div class="box-tools pull-right">
                    <a href="/user/new" class="btn"><i class="fa fa-plus"></i></a>
                </div>
            </div>
            <div class="container">
                <div class="row">
                    <div class="col-md-5">
                        <form method="post" class="form-group"><%--不写提交地址，默认地址栏--%>
                            <div class="form-group">

                            </div>
                            <div class="form-group">
                                <label>帐号</label>
                                <input type="text" class="form-control" name="userName" >
                            </div>

                            <div class="form-group">
                                <label>密码(默认密码000000)</label>
                                <input type="password" value="000000" class="form-control" name="passWord">
                            </div>
                            <div class="form-group">
                                <label>手机</label>
                                <input type="text"  class="form-control" name="mobile">
                            </div>

                            <div class="form-group">
                                <label>角色</label>
                                <div>
                                    <c:forEach items="${roleList}" var="role">
                                        <label class="checkbox-inline"><%--多选框，有共同的name值，提交过去，获取到的值是个数组--%>
                                            <input type="checkbox" name="roleIds" value="${role.id}"> ${role.viewName}
                                        </label>
                                    </c:forEach>
                                </div>
                            </div>
                            <div class="form-group">
                                <button class="btn btn-block btn-primary">保存</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- /.content -->
</div>
<!-- /.content-wrapper -->
<%@include file="../include/js.jsp"%>
</body>
</html>

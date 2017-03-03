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
    <%@include file="../../include/css.jsp"%>
</head>
<body  class="hold-transition skin-blue sidebar-mini">

<%@include file="../../include/header.jsp"%>
<jsp:include page="../../include/sidebar.jsp">
    <jsp:param name="menu" value="sys_device"/>
</jsp:include>
<div class="content-wrapper">

    <!-- Main content -->
    <section class="content ">

        <!-- Default box -->
        <div class="box box-solid box-primary">
            <div class="box-header with-border">
                <h3 class="box-title">新增设备</h3>
            </div>
            <div class="container">
                <div class="row">
                    <div class="col-md-5">
                        <form method="post" class="form-group"><%--不写提交地址，默认地址栏--%>
                            <div class="form-group">

                            </div>
                            <div class="form-group">
                                <label>设备名称</label>
                                <input type="text" class="form-control" name="name" >
                            </div>
                            <div class="form-group">
                                <label>单位</label>
                                <input type="text" class="form-control" name="unit" >
                            </div>
                            <div class="form-group">
                                <label>总数量</label>
                                <input type="text" class="form-control" name="totalNum" >
                            </div>
                            <div class="form-group">
                                <label>租赁单价(元/天)</label>
                                <input type="text" class="form-control" name="price" >
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
<%@include file="../../include/js.jsp"%>
</body>
</html>

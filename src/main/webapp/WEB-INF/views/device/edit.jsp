<%--
  Created by IntelliJ IDEA.
  User: 刘忠伟
  Date: 2017/1/12
  Time: 19:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Document</title>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css"><%--就这静态资源，需要设置静态资源不经过中央控制器，才不会404--%>
    <%@include file="../include/css.jsp"%>
</head>
<body  class="hold-transition skin-blue sidebar-mini">

<%@include file="../include/header.jsp"%>
<jsp:include page="../include/sidebar.jsp">
    <jsp:param name="menu" value="sys_device"/>
</jsp:include>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">

    <!-- Main content -->
    <section class="content">

        <!-- Default box -->
        <div class="box box-solid box-primary">
            <div class="box-header with-border">
                <h3 class="box-title">设备修改</h3>
                <div class="box-tools pull-right">
                    <a href="/device/new" class="btn"><i class="fa fa-plus"></i></a>
                </div>
            </div>

        <!-- ./wrapper -->
        <div class="container ">
            <div class="row">
                <div class="col-md-5">
                    <form method="post"><%--不写提交地址，默认地址栏。默认是地址栏。地址栏是动态url传的有id的值，就不需要在传值确定修改的对象--%>
                        <div class="form-group">
                        <input type="hidden" value="${device.id}" name="id"><%--隐藏域的id会默认封装给user--%>
                            </div>
                        <div class="form-group">
                            <label>设备名称</label>
                            <input type="text" class="form-control" name="name" value="${requestScope.device.name}">
                        </div>

                        <div class="form-group">
                            <label>单位</label>
                            <input type="text" class="form-control" name="unit" value="${requestScope.device.unit}" >
                        </div>
                        <div class="form-group">
                            <label>总数量</label>
                            <input type="text" class="form-control" name="totalNum" value="${requestScope.device.totalNum}">
                        </div>
                        <div class="form-group">
                            <label>租赁单价(元/天)</label>
                            <input type="text" class="form-control" name="price" value="${requestScope.device.price}">
                        </div>

                        <div class="form-group">
                            <button class="btn btn-block btn-primary">修改</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        </div>

        <!-- /.box -->

    </section>
    <!-- /.content -->
</div>
<!-- /.content-wrapper -->




<%@include file="../include/js.jsp"%>
</body>
</html>

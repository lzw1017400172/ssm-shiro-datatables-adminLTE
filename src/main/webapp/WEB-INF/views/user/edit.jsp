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
    <jsp:param name="menu" value="sys_accounts"/>
</jsp:include>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">

    <!-- Main content -->
    <section class="content">

        <!-- Default box -->
        <div class="box box-solid box-primary">
            <div class="box-header with-border">
                <h3 class="box-title">用户</h3>
                <div class="box-tools pull-right">
                    <a href="/user/new" class="btn"><i class="fa fa-plus"></i></a>
                </div>
            </div>

        <!-- ./wrapper -->
        <div class="container ">
            <div class="row">
                <div class="col-md-5">
                    <form method="post"><%--不写提交地址，默认地址栏。默认是地址栏。地址栏是动态url传的有id的值，就不需要在传值确定修改的对象--%>
                        <div class="form-group">
                        <input type="hidden" value="${user.id}" name="id"><%--隐藏域的id会默认封装给user--%>
                            </div>
                        <div class="form-group">
                            <label>帐号</label>
                            <input type="text" class="form-control" name="userName" value="${requestScope.user.userName}">
                        </div>

                        <div class="form-group">
                            <label>密码(如果密码不修改请留空)</label><%--因为密码加盐，不能加密过后密码传上去，留空就表示不更改密码--%>
                            <input type="password"  class="form-control" name="passWord">
                        </div>

                        <div class="form-group">
                            <label>角色</label>
                            <div>
                                <c:forEach items="${roleList}" var="role">
                                    <c:set var="flag" value="false" scope="page"/><%--作用于本页，命名一个变量--%>
                                    <%--获取复选框提交的值。只要被选中，提交才会传数据，使用相同的name，可以获取到数组。checked默认选中--%>
                                    <%--需要判断哪个被默认选中--%>
                                    <c:forEach items="${user.roleList}" var="item">
                                        <c:if test="${role.id == item.id}">
                                        <%--只要相等就被选中。应该被选中，只会出现一次相等，--%>
                                            <label class="checkbox-inline">
                                                <input type="checkbox" value="${role.id}" name="roleids" checked>${role.viewName}
                                            </label>
                                            <%--flag变成true，说明已经输出了本复选框，是选中状态，不用再输出这个了--%>
                                            <c:set var="flag" value="true"/>
                                        </c:if>
                                    </c:forEach>

                                    <c:if test="${not flag}"><%--取反,当flag还是false时，说明这个复选框在对象里面没有对应。就是未被选中。这样设计保证了，复选框只会出现一次--%>
                                        <label class="checkbox-inline">
                                            <input type="checkbox" value="${role.id}" name="roleids" >${role.viewName}
                                        </label>

                                    </c:if>

                                </c:forEach>
                            </div>
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

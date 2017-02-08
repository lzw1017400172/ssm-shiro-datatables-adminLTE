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
    <%@include file="../include/css.jsp"%>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">



    <%@include file="../include/header.jsp"%>
    <%--jsp节点向某个页面传值。sys_accounts代表本页面。同属于一个节点下的以sys_开头 --%>
    <jsp:include page="../include/sidebar.jsp">
        <jsp:param name="menu" value="sys_accounts"/>
    </jsp:include>
    <!-- =============================================== -->

    <!-- Left side column. contains the sidebar -->
    <%--<%@include file="../include/sidebar.jsp"%>--%>

    <!-- =============================================== -->

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">

        <!-- Main content -->
        <section class="content">

            <div class ="box box-solid box-primary">
                <div class="box-header  with-border">
                    <h3 class ="box-title"><i class="fa fa-search"></i>搜索</h3>
                </div>
                <div class="box-body">
                    <div class ="box-tools pull-left">
                        <form class="form-inline">
                            <input type="text" class="form-control" value="${requestScope.q_name}" name="q_name" placeholder="姓名">
                            <select class="form-control" name="q_role"><%--需要有name值才能提交结果--%>
                                <option value="">-- 角色 --</option>
                                <c:forEach items="${requestScope.roleList}" var="role">
                                    <option value="${role.id}" ${role.id == requestScope.q_role?"selected":""} >${role.viewName}</option><%--需要显示所有角色，所以传值要穿所有角色。角色id作为值，选中那一个select框 的值就是哪一个--%>
                                </c:forEach>
                            </select>
                            <button class="btn btn-default"><i class="fa fa-search"></i></button>
                        </form>
                    </div>

                </div>

            </div>

            <!-- Default box -->
            <div class="box box-solid box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">用户列表</h3>
                    <div class="box-tools pull-right">
                        <a href="/user/new" class="btn"><i class="fa fa-plus"></i></a>
                    </div>
                </div>
                <div class="container">

                    <div class="row">
                        <div class="col-md-11">
                            <div class="form-group"></div>
                            <div class="form-group">
                            <c:if test="${not empty requestScope.message}">
                                <div class="alert alert-success ">
                                        ${message}
                                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                                </div>
                            </c:if>
                            <div class="form-group"></div>
                            <table class="table" >
                                <thead>
                                <tr>
                                    <th>姓名</th>
                                    <th>角色</th>
                                    <th>#</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${page.items}" var="user">
                                    <tr>
                                        <td>${user.userName}</td>
                                            <%--当修改或者删除的时候需要传参数id，修改的对象。设计这种形式的url，动态url传值--%>
                                        <td><%--角色，是使用EL表达式获取值，默认调用get方法，所以就封装一个get。用来返回所有角色的字符串--%>
                                            ${user.viewNames}
                                        </td>
                                        <td>
                                            <a href="/user/${user.id}/edit">修改</a>

                                            <a href="/user/${user.id}/del">删除</a>
                                        </td>

                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <!-- /.box -->
               <div class="box-footer">
                    <ul style="margin:5px 0px" id="pagination" class="pagination pull-right"></ul>
               </div>


            </div>

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->




</div>
<!-- ./wrapper -->

<!-- js -->
<%@include file="../include/js.jsp"%>

<script src="/static/js/jquery.twbsPagination.min.js"></script>
<script>
    $(function () {
//分页插件的使用
        $("#pagination").twbsPagination({
            totalPages:${page.totalPage},
            visiblePages:${page.pageSize},
            href:"/user?p={{number}}&q_name=${requestScope.q_name}&q_role=${requestScope.q_role}",/*传值关键字，也要传过去。没有按关键字查获取null，动态sql就返回所有*/
            first:"首页",
            prev:"上一页",
            next:"下一页",
            last:"末页"
        });
    });



</script>
</body>
</html>

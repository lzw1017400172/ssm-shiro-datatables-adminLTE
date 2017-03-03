<%--
  Created by IntelliJ IDEA.
  User: 刘忠伟
  Date: 2017/1/19
  Time: 11:36
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>系统登录</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.6 -->
    <link rel="stylesheet" href="/static/adminLTE2/bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="/static/css/font-awesome.min.css">
    <!-- Ionicons -->
    <!-- Theme style -->
    <link rel="stylesheet" href="/static/adminLTE2/dist/css/AdminLTE.min.css">
    <!-- iCheck -->
    <link rel="stylesheet" href="/static/adminLTE2/plugins/iCheck/square/blue.css">

    <link rel="stylesheet" href="/static/css/style.css">


</head>
<body class="hold-transition login-page" style="background-image: url(/static/img/bg.jpg);">
<div class="login-box">
    <div class="login-logo">
        <a href="../../index2.html"><h2>功成企业管理系统登录</h2></a>
    </div>
    <!-- /.login-logo -->
    <div class="login-box-body" style="background-color: #ffe;">
        <p class="login-box-msg">请输入帐号密码</p>

        <form action="/" method="post" id="login_form"><%--默认get提交--%>
            <div class="form-group has-feedback">
                <input type="email" class="form-control" placeholder="帐号/邮箱/手机号码" id="userName" name="userName">
                <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
            </div>
            <div class="form-group has-feedback">
                <input type="password" class="form-control" placeholder="密码" id="passWord" name="passWord">
                <span class="glyphicon glyphicon-lock form-control-feedback"></span>
            </div>
            <div class="row">
                <div class="col-xs-8">
                    <div class="checkbox icheck">
                        <label style="margin-left:20px"><input type="checkbox" id="checkbox">记住我</label>
                    </div>
                </div>
                <!-- /.col -->
                <div class="col-xs-4">
                    <button type="button" id="login_btn" class="btn btn-primary btn-block btn-flat">登录</button>
                </div>
                <!-- /.col -->
            </div>
        </form>


        <!-- /.social-auth-links -->

        <a href="#">忘记密码</a><br>

    </div>
    <!-- /.login-box-body -->
    <c:if test="${not empty requestScope.message}">
        <div class="alert alert-error ">
                ${message}
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
        </div>
    </c:if>

</div>
<!-- /.login-box -->

<!-- jQuery 2.2.0 -->
<script src="/static/plugins/jQuery/jQuery-2.2.0.min.js"></script>
<script src="/static/jquery.cookie.js"></script>
<!-- Bootstrap 3.3.6 -->
<script src="/static/bootstrap/js/bootstrap.min.js"></script>
<!-- SlimScroll -->
<script src="/static/plugins/slimScroll/jquery.slimscroll.min.js"></script>
<!-- AdminLTE App -->
<script src="/static/dist/js/app.min.js"></script>
<%--base64.js--%>
<script src="/static/Base64.js"></script>
<script>
    $(function () {

        var b = new Base64();

        /*一进来就获取帐号或者密码*/
        $("#userName").val(b.decode($.cookie("userName1")));
        $("#passWord").val(b.decode($.cookie("passWord1")));



        $("#login_btn").click(function () {
            if($("#checkbox")[0].checked){

                //创建cookie
                //这两个只在客户端获取，只用作记住密码
                $.cookie("userName1",b.encode($("#userName").val()),{expires:7,path:"/"});
                $.cookie("passWord1",b.encode($("#passWord").val()),{expires:7,path:"/"});


                //但是只有点击记住密码，以后才能实现自动登录。或者写在if外面，点击登录按钮以后就自动登录
                // 这两个用作在服务端获取，用于自动登录。并且安全退出后，删除这个cookie，但是客户端是记住的上面的cookie，所以仍然可以记住密码
                //把值进行base64加密，比较严谨，并且可以解决中文乱码
                $.cookie("userName2",b.encode($("#userName").val()),{expires:7,path:"/"});
                $.cookie("passWord2",b.encode($("#passWord").val()),{expires:7,path:"/"});
            }
            $("#login_form").submit();
        });

        /*怎么在客户端记住密码。两种方法。
         * 在客户端使用jquery，当复选框选中时创建cookie,当每次进来此页面时获取cookie
         * */
        /*判断复选框是否被选中，必须使用原生js。jquery和原生js区别是，jquery是数组，选一个就是原生的*/
        /*每次更换帐号，就重置键的值，下次获取就获取最新的*/



    });
</script>
</body>
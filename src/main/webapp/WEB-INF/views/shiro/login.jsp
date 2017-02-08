<%--
  Created by IntelliJ IDEA.
  User: 刘忠伟
  Date: 2017/1/19
  Time: 11:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Document</title>
    <link href="http://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container">
        <div class="row">


            <div class="col-md-4">
                <h1>登录页面</h1>
                <%--登录失败，重定向到这里，。有mvc的传值redirectAttbuilt传的值。先经过过滤器传到url，在请求转发到这里--%>
                <c:if test="${not empty requestScope.message}">
                    <div class="alert alert-success ">
                            ${message}
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                    </div>
                </c:if>

                <form method="post"><%--不写action就是"/"这个匿名可访问--%><%--这里使用shiro权限框架之后，有一个问题。曲线作用后，来到登录页面。表单提交的地址如果随便写肯定会被shiro权限限制。然后又登录，死循环。
                解决方法：1直接cation写为配置spring时loginUrl拦截后跳转url。肯定不会被限制。匿名访问即可，2或者配置一下权限。可以匿名访问--%>
                    <div class="form-group">
                        <label>帐号</label>
                        <input class="form-control" type="text" name="userName">
                    </div>
                    <div class="form-group">
                        <label>密码</label>
                        <input class="form-control" type="password" name="passWord">
                    </div>

                    <button class="btn btn-default">登录</button>
                </form>
            </div>
        </div>
    </div>
</body>
</html>

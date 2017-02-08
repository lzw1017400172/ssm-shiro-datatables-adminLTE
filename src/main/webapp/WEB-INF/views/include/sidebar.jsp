<%--
  Created by IntelliJ IDEA.
  User: 刘忠伟
  Date: 2017/1/12
  Time: 21:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!-- Left side column. contains the sidebar -->
<aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">

        <!-- sidebar menu: : style can be found in sidebar.less -->
        <ul class="sidebar-menu">
            <shiro:hasRole name="role_admin"><%--hasRole节点。会触发权限认证方法。查询当前登录用户的所有角色。封装到对象返回这里。如果里面有name值就表示可以显示--%>
            <li class="header">设置模块</li>
            <li class="treeview ${fn:startsWith(param.menu,'sys_') ?'active':''}"  >
                <a href="#"><%--只要全是sys_开头都是系统管理，所以都要高亮。使用fn--%>
                    <i class="fa fa-cogs"></i> <span >系统管理</span> <i class="fa fa-angle-left pull-right"></i>
                </a>
                <ul class="treeview-menu">
                    <%--当list传值到jsp时。就说明正处于list，可以获取到值。不在就获取不到--%>
                    <li class="${param.menu == 'sys_accounts' ? 'active' : ''}" ><a href="/user" ><i class="fa fa-circle-o"></i> 用户管理 </a></li>
                    <li class="${param.menu == 'sys_device' ? 'active' : ''}"><a href="/device"><i class="fa fa-circle-o"></i> 设备管理</a></li>
                </ul>
            </li>
            </shiro:hasRole>
            <shiro:hasRole name="role_sales"><%--触发权限认证。查询当前用户的角色，并且返回。需要写权限认证--%>
            <li class="header">业务模块</li>
            <li class="treeview"  >
                <a href="#"><%--只要全是sys_开头都是系统管理，所以都要高亮。使用fn--%>
                    <i class="fa fa-cogs"></i> <span >财务报表</span>
                </a>
            </li>
            </shiro:hasRole>
            <li class="header">用户模块</li>
            <li class="treeview"  >
                <a href="/logout"><%--只要全是sys_开头都是系统管理，所以都要高亮。使用fn--%>
                    <i class="fa fa-fw fa-sign-out"></i> <span >安全退出</span>
                </a>
            </li>
        </ul>
    </section>
    <!-- /.sidebar -->
</aside>

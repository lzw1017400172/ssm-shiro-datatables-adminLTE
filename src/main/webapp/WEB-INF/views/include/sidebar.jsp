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
                    <li class="${param.menu == 'sys_device' ? 'active' : ''}"><a href="/setting/device"><i class="fa fa-circle-o"></i> 设备管理</a></li>
                </ul>
            </li>
            </shiro:hasRole>
            <shiro:hasRole name="role_sales"><%--触发权限认证。查询当前用户的角色，并且返回。需要写权限认证--%>
            <li class="header">业务模块</li>
                <li class="treeview ${fn:startsWith(param.menu,'business_')?'active':''}">
                    <a href="#">
                        <i class="fa fa-edit"></i> <span>业务</span>
                        <i class="fa fa-angle-left pull-right"></i>
                    </a>
                    <ul class="treeview-menu">
                        <li class="${param.menu == 'business_device_rent'?'active':''}"><a href="/business/device/rent"><i class="fa fa-circle-o"></i> 设备租赁</a>

                        </li>



                        <li class="${fn:startsWith(param.menu,'business_outSourcing_') ?'active':''}">
                            <a href="#"><i class="fa fa-circle-o"></i> 劳务外包 <i class="fa fa-angle-left pull-right"></i></a>
                            <ul class="treeview-menu">
                                <li class="${param.menu == 'business_outSourcing_list' ? 'active':''}"><a href="/business/outSourcing"><i class="fa fa-circle-o"></i> 业务流水</a></li>
                                <li class="${param.menu == 'business_outSourcing_new' ? 'active':''}">
                                    <a href="/business/outSourcing/new"><i class="fa fa-plus"></i> 新增流水 <i class="fa fa-angle-left pull-right"></i></a>

                                </li>
                            </ul>
                        </li>

                    </ul>
                </li>
            </shiro:hasRole>
            <shiro:hasRole name="role_fin"><%--shiro触发权限认证--%>
                <li class="header">财务模块</li>
                <li class="treeview ${fn:startsWith(param.menu,"finance_")?'active':''}">
                    <a href="#">
                        <i class="fa fa-pie-chart"></i>
                        <span>财务报表</span>
                        <i class="fa fa-angle-left pull-right"></i>
                    </a>
                    <ul class="treeview-menu">
                        <li class="${param.menu== 'finance_list' ? 'active':''}"><a href="/finance/list"><i class="fa fa-circle-o"></i> 财务报表全部</a></li>
                        <li class="${param.menu=='finance_dayReport'?'active':''}"><a href="/finance/darReport"><i class="fa fa-circle-o"></i> 日报</a></li>
                        <li class="${param.menu=='finance_monthReport'?'active':''}"><a href="/finance/monthReport"><i class="fa fa-circle-o"></i> 月报</a></li>
                        <li class="${param.menu=='finance_yearReport'?'active':''}"><a href="/finance/yearReport"><i class="fa fa-circle-o"></i> 年报</a></li>
                    </ul>
                </li>
            </shiro:hasRole>
            <shiro:hasRole name="role_dev">
                <li class="header">开发模块</li>
                <li class="treeview ${param.menu == "disk_list" ? "active":""}">
                    <a href="/disk/list">
                        <i class="fa fa-folder"></i>
                        <span>公司网盘</span>
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

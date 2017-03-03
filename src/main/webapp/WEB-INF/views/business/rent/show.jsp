<%--
  Created by IntelliJ IDEA.
  User: 刘忠伟
  Date: 2017/2/18
  Time: 12:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>设备租赁合同</title>
    <%@include file="../../include/css.jsp"%>

</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper" id="app">
    <%@include file="../../include/header.jsp"%>
    <!--使用jsp节点，向sidebar传值，作用标亮。jsp只在页面之间传值。这里这里同属于设备租凭，和其相同即可-->
    <jsp:include page="../../include/sidebar.jsp">
        <jsp:param name="menu" value="business_device_rent"/>
    </jsp:include>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">租赁合同详情</h3>

                    <div class="box-tools pull-right hidden-print">
                        <button v-on:click="print" class="btn btn-default btn-sm"><i class="fa fa-print"></i></button>
                    </div>
                </div>
                <div class="box-body">
                    <div class="row">
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>公司名称:&nbsp&nbsp&nbsp</label>
                                ${requestScope.map.deviceRent.companyName}
                            </div>
                            <div class="form-group">
                                <label>联系电话:&nbsp&nbsp&nbsp</label>
                                ${requestScope.map.deviceRent.tel}
                            </div>
                            <div class="form-group">
                                <label>租赁日期:&nbsp&nbsp&nbsp</label><%--此日期必须和服务器时间一致，不能和客户端一致--%>
                                ${requestScope.map.deviceRent.rentDate}
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>法人代表:&nbsp&nbsp&nbsp</label>
                              ${requestScope.map.deviceRent.linkMan}
                            </div>
                            <div class="form-group">
                                <label>地&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp址:&nbsp&nbsp&nbsp</label>
                                ${requestScope.map.deviceRent.address}
                            </div>
                            <div class="form-group">
                                <label>归还日期:&nbsp&nbsp&nbsp</label>
                               ${requestScope.map.deviceRent.backDate}
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>身份证号:&nbsp&nbsp&nbsp</label>
                               ${requestScope.map.deviceRent.cardNum}
                            </div>
                            <div class="form-group">
                                <label>传&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp真:&nbsp&nbsp&nbsp</label>
                               ${requestScope.map.deviceRent.fax}
                            </div>
                            <div class="form-group">
                                <label>总&nbsp&nbsp天&nbsp&nbsp数:&nbsp&nbsp&nbsp</label>
                                ${requestScope.map.deviceRent.totalDay}
                            </div>
                        </div>
                    </div>
                </div>
                <!-- /.box-body -->
            </div>
            <!-- /.box -->

            <div class="box">
                <div class="box-header">
                    <h3 class="box-title">设备列表</h3>

                </div>
                <div class="box-body">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>设备名称</th>
                            <th>单位</th>
                            <th>租赁单价</th>
                            <th>数量</th>
                            <th>总价</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${empty requestScope.map.deviceRentDetails}">
                            <tr>
                                <td colspan="6" style="align-content: center">暂无数据</td>
                            </tr>
                        </c:if>
                        <c:forEach items="${requestScope.map.deviceRentDetails}" var="deviceRentDetail">
                            <tr>
                                <td>${deviceRentDetail.deviceName}</td>
                                <td>${deviceRentDetail.deviceUnit}</td>
                                <td>${deviceRentDetail.devicePrice}</td>
                                <td>${deviceRentDetail.deviceNum}</td>
                                <td>${deviceRentDetail.totalPrice}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="box-footer" style="text-align: right">
                    总租赁费 ${requestScope.map.deviceRent.totalPrice} 元 预付款 ${requestScope.map.deviceRent.preCost} 元 尾款 ${requestScope.map.deviceRent.lastCost} 元
                </div>
            </div>

            <div class="box hidden-print">
                <div class="box-header">
                    <h3 class="box-title">合同扫描件</h3>
                    <div class="box-tools pull-right">
                    <a href="/business/device/rent/docs/zip/${requestScope.map.deviceRent.id}" class="btn btn-sm btn-default">
                        <i class="fa fa-download"></i> 打包下载
                    </a>
                </div>
                </div>
                <div class="box-body">

                    <ul id="fileName">
                        <c:forEach items="${requestScope.map.deviceRentDocses}" var="deviceRentDocs">
                            <li><a href="/business/device/rent/docs/${deviceRentDocs.id}">${deviceRentDocs.sourceName}</a></li>
                        </c:forEach>

                    </ul>
                </div>
            </div>

            <div class="box visible-print-block">
                <div class="box-header">
                    <h3 class="box-title" >出入证</h3>

                </div>
                <div class="box-body">
                    <br/>
                    <br/>
                    <br/>
                    此处盖章：
                    <br/>
                    <br/>
                    <br/>
                </div>
            </div>
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->



</div>

<%@include file="../../include/js.jsp"%>
<script src="/static/adminLTE2/plugins/vue.js"></script>
<script>
    $(function () {

        var app = new Vue({
            el:"#app",
            data:{
            },//数据
            methods:{
                print:function () {
                    window.print();
                }
            },//函数事件，需要触发
            computed:{}//计算属性，使用return直接可获取到值

        });



    });
</script>
</body>
</html>
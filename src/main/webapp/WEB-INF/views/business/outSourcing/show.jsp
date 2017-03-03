<%--
  Created by IntelliJ IDEA.
  User: 刘忠伟
  Date: 2017/2/19
  Time: 15:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Created by IntelliJ IDEA.
  User: 刘忠伟
  Date: 2017/2/18
  Time: 16:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>劳务外包新增</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <%@include file="../../include/css.jsp"%>

    <link rel="stylesheet" href="/static/plugins/datepicker/datepicker3.css">
    <!-- 文件上传 -->
    <link rel="stylesheet" href="/static/adminLTE2/plugins/uploader/webuploader.css">
    <link rel="stylesheet" href="/static/css/style.css">
    <link rel="stylesheet" href="/static/adminLTE2/plugins/select2/select2.min.css"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper" id="app">

    <%@include file="../../include/header.jsp"%>
    <jsp:include page="../../include/sidebar.jsp">
        <jsp:param name="menu" value="business_outSourcing_new"/>
    </jsp:include>

    <!-- =============================================== -->

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">

        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box box-primary box-solid">
                <div class="box-header with-border">
                    <h3 class="box-title">新增劳务外包流水</h3>

                    <div class="box-tools pull-right">
                        <a href="javascript:;" class="btn btn-default btn-sm" id="print"><i class="fa fa-print"></i></a>
                    </div>
                </div>
                <div class="box-body">
                    <div class="box">

                        <div class="box-body">
                            <form role="form" >
                                <div class="box-body" class="form-group">

                                    <!--公司 -->
                                    <div class="row">
                                        <div class="col-lg-3">
                                            <div class="" >
                                                <label >租赁公司：&nbsp</label>
                                                ${requestScope.map.craftOutSourcing.companyName}

                                            </div>
                                        </div>

                                        <div class="col-lg-3">
                                            <div >
                                                <label >地 &nbsp;&nbsp址：&nbsp</label>
                                                ${requestScope.map.craftOutSourcing.companyAddress}
                                            </div>
                                        </div>


                                        <div class="col-lg-3">
                                            <div class="form-group">
                                                <label>公司电话：&nbsp</label>
                                                ${requestScope.map.craftOutSourcing.companyPhone}
                                            </div>
                                        </div>



                                    </div>

                                    <%--法人--%>
                                    <div class="row">
                                        <div class="col-lg-3">
                                            <div class="" >
                                                <label >法人代表：&nbsp</label>
                                                ${requestScope.map.craftOutSourcing.linkMan}
                                            </div>
                                        </div>
                                        <div class="col-lg-3">
                                            <div class="form-group">
                                                <label >电 &nbsp;&nbsp话：&nbsp</label>
                                                ${requestScope.map.craftOutSourcing.linkManPhone}
                                            </div>
                                        </div>

                                        <div class="col-lg-3">
                                            <div>
                                                <label >身份证号：&nbsp</label>
                                                ${requestScope.map.craftOutSourcing.idNum}
                                            </div>
                                        </div>
                                    </div>

                                    <!--金额 -->
                                    <div class="row">
                                        <div class="col-lg-3">
                                            <div class="" >
                                                <label >佣金金额：&nbsp</label>
                                                ${requestScope.map.craftOutSourcing.totalCost}
                                            </div>
                                        </div>
                                        <div class="col-lg-3">
                                            <div class="form-group">
                                                <label >预付款：&nbsp</label>
                                                ${requestScope.map.craftOutSourcing.preCost}
                                            </div>
                                        </div>
                                        <div class="col-lg-3">
                                            <div >
                                                <label >尾&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;款：&nbsp</label>
                                                ${requestScope.map.craftOutSourcing.lastCost}
                                            </div>
                                        </div>
                                    </div>

                                    <div> <br/></div>
                                    <%--工种--%>
                                    <div class="box">
                                        <div class="box-header">
                                            <h3 class="box-title">&nbsp工种列表</h3>

                                        </div>
                                        <div class="box-body">
                                            <table class="table">
                                                <thead>
                                                <tr>
                                                    <th>工种</th>
                                                    <th>工种单位佣金</th>
                                                    <th>工种数量</th>
                                                    <th>小计</th>
                                                    <th>#</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:if test=" ${empty requestScope.map.detailList}">
                                                    <tr>
                                                        <td colspan="5" style="align-content: center">暂无数据</td>
                                                    </tr>
                                                </c:if>
                                                <c:forEach items="${requestScope.map.detailList}" var="detail">
                                                    <tr>
                                                        <td>${detail.craftName}</td>
                                                        <td>${detail.craftPrice}</td>
                                                        <td>${detail.craftNum}</td>
                                                        <td>${detail.subTotal}</td>

                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div> <br/></div>



                                    <div class="box">
                                        <div class="box-header">
                                            <h4 class="box-title"><i class="fa fa-user"></i> &nbsp合同列表:</h4>
                                            <div class="box-tools pull-right">
                                                <a href="/business/outSourcing/docs/zipDownload/${requestScope.map.craftOutSourcing.id}" class="btn btn-sm btn-default"><i class="fa fa-download">打包下载</i></a>
                                            </div>
                                        </div>


                                        <div class="box-body">

                                            <ul id="fileName">
                                                <c:forEach items="${requestScope.map.docsList}" var="docs">
                                                    <li><a href="/business/outSourcing/download/${docs.id}">${docs.sourceName}</a></li>
                                                </c:forEach>
                                            </ul>
                                        </div>


                                    </div>




                                </div>
                            </form>
                        </div>
                        <!-- /.box-body -->
                        <!-- /.content-wrapper -->



                    </div>
                    <!-- /.box -->
                </div>
                <!-- /.box-body -->

            </div>
            <!-- /.box -->

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

</div>
<!-- ./wrapper -->

<%@include file="../../include/js.jsp"%>

<%--提示框--%>
<script src="/static/adminLTE2/plugins/layer/layer.js"></script>

<script>

    $(function () {
        $("#print").click(function () {
            window.print();
        });


    });

</script>
</body>
</html>



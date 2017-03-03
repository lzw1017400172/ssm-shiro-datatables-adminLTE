<%--
  Created by IntelliJ IDEA.
  User: 刘忠伟
  Date: 2017/2/23
  Time: 16:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>财务报表-年报</title>
    <%@include file="../include/css.jsp"%>
    <link rel="stylesheet" href="/static/adminLTE2/plugins/datatables/dataTables.bootstrap.css">
    <link rel="stylesheet" href="/static/adminLTE2/plugins/datepicker/datepicker3.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <%@include file="../include/header.jsp"%>
    <jsp:include page="../include/sidebar.jsp">
        <jsp:param name="menu" value="finance_yearReport"/>
    </jsp:include>
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">

            <div class="box box-primary box-solid">
                <div class="box-body with-border">
                    <div class="row">
                        <div class="col-md-2">
                            <input type="text" id="datepicker" class="form-control">
                        </div>
                    </div>
                </div>
            </div>
            <!-- Default box -->
            <div class="box box-primary box-solid">
                <div class="box-header with-border">
                    <h3 class="box-title">财务年报列表</h3>
                    <div class="box-tools pull-right">
                        <a href="javascript:;"  class="btn btn-default" id="export"><i class="fa fa-file-excel-o"></i> 导出Excel</a>
                    </div>
                </div>
                <div class="box-body">
                    <table  id="example" class="table table-striped table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>月份</th>
                            <th>当月支出</th>
                            <th>当月收入</th>
                            <th>备注</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <!-- /.box-body -->
            </div>
            <!-- /.box -->

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

</div>

<%@include file="../include/js.jsp"%>
<script src="/static/adminLTE2/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="/static/adminLTE2/plugins/datatables/dataTables.bootstrap.min.js"></script>
<script src="/static/adminLTE2/plugins/datepicker/bootstrap-datepicker.js"></script>
<script src="/static/adminLTE2/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>
<script src="/static/adminLTE2/plugins/layer/layer.js"></script>
<%--js时间--%>
<script src="/static/adminLTE2/plugins/moment.js"></script>
<script>
    $(function () {
        $("#datepicker").val(moment().format("YYYY"));

        $("#datepicker").datepicker({
            language: "zh-CN",  //选择语言，需要导入对应的语言包js
            autoclose: true,//选中之后自动隐藏日期选择框
            startView: 2,
            minViewMode: 2,
            maxViewMode: 2,
            format: "yyyy"

        }).on('changeDate',function(ev) {
            //选择之后触发的事件，再发送一次ajax请求。又获取一下日期发送过去
            dataTable.draw();
        });

        var dataTable = $("#example").DataTable({

            "lengthMenu":[3,5,10,15,20],
            "serverSide":true,//全部操作在服务端进行
            "ajax":{
                url:"/finance/yearReport/data.json",
                type:"get",
                data:function (dataSouce) {//发送给服务端的值
                    dataSouce.date=$("#datepicker").val()   //当发送请求时，就获取一下日期。日期是可以选择变化的。点击下一页会发送ajax，搜索按钮我们也要设置发送一次，每次发送都获取一次日期
                }
            },
            "ordering":false,//不排序
            "searching":false,
            "columns":[//客户端接受值
                {"data":"date"},
                {"data":"income"},
                {"data":"expend"},
                {"data":function () {
                    return "无";
                }},
                {"data":function (row) {

                    return "<a href='/finance/monthReport?date="+row.date+"' class='affirm'>详情</a>";/*点击详情，需要传参过去，就把日期年月作为参数*/

                }},

            ],

            "language" : {
                "sProcessing" : "处理中...",
                "sLengthMenu" : "显示 _MENU_ 项结果",
                "sZeroRecords" : "没有匹配结果",
                "sInfo" : "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
                "sInfoEmpty" : "显示第 0 至 0 项结果，共 0 项",
                "sInfoFiltered" : "(由 _MAX_ 项结果过滤)",
                "sInfoPostFix" : "",
                "sSearch" : "搜索:",
                "sUrl" : "",
                "sEmptyTable" : "表中数据为空",
                "sLoadingRecords" : "载入中...",
                "sInfoThousands" : ",",
                "oPaginate" : {
                    "sFirst" : "首页",
                    "sPrevious" : "上页",
                    "sNext" : "下页",
                    "sLast" : "末页"
                },
                "oAria" : {
                    "sSortAscending" : ": 以升序排列此列",
                    "sSortDescending" : ": 以降序排列此列"
                }
            }
        });


        /*点击export时导出excel，就是下载*/
        $("#export").click(function () {
            window.location.href="/finance/yearReport/"+$("#datepicker").val()+"/excel";

        });


    });

</script>
</body>
</html>

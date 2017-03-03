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
    <title>财务报表-月报</title>
    <%@include file="../include/css.jsp"%>
    <link rel="stylesheet" href="/static/adminLTE2/plugins/datatables/dataTables.bootstrap.css">
    <link rel="stylesheet" href="/static/plugins/datepicker/datepicker3.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <%@include file="../include/header.jsp"%>
    <jsp:include page="../include/sidebar.jsp">
        <jsp:param name="menu" value="finance_monthReport"/>
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
                    <h3 class="box-title">财务月报列表</h3>
                    <div class="box-tools pull-right">
                        <a href="javascript:;"  class="btn btn-default" id="export"><i class="fa fa-file-excel-o"></i> 导出Excel</a>
                    </div>
                </div>
                <div class="box-body">
                    <table  id="example" class="table table-striped table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>日期</th>
                            <th>当日收入</th>
                            <th>当日支出</th>
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
<script src="/static/plugins/datepicker/bootstrap-datepicker.js"></script>
<script src="/static/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>
<script src="/static/adminLTE2/plugins/layer/layer.js"></script>
<%--前端时间的插件--%>
<script src="/static/adminLTE2/plugins/moment.js"></script>
<script>
    $(function () {

        //和日报相同，点击年报的详情时，会跳转到这里，所以第一个值不一定是当前时间。需要判断。date为空即获取当前，不为空就是点击的年报表详情
        if(${not empty requestScope.date}){
            $("#datepicker").val("${date}");//进来就要对时间进行设置，。根据设计，跳转后搜索参数没值
        } else {
            $("#datepicker").val(moment().format("YYYY-MM"));//进来就要对时间进行设置，。根据设计，跳转后搜索参数没值
        }

        $("#datepicker").datepicker({
            language: "zh-CN",  //选择语言，需要导入对应的语言包js
            autoclose: true,//选中之后自动隐藏日期选择框
            startView: 0,
            format: "yyyy-mm",
            minViewMode: 1,
            maxViewMode: 0
        }).on('changeDate',function(ev) {
        //选择之后触发的事件，再发送一次ajax请求。又获取一下日期发送过去
            datatable.draw();
    });

        //写datatables，让其发送ajax
        var datatable = $("#example").DataTable({
            "lengthMenu":[3,5,10,15,20],
            "serverSide":true,//全部操作在服务端进行
           "ajax":{
                url:"/finance/monthReport/data.json",
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

                        return "<a href='/finance/darReport?date="+row.date+"' class='affirm'>详情</a>";/*点击详情，需要传参过去，就把日期年月作为参数*/

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

        //确认按钮是后来生成的必须使用事件委托
        /*$(document).delegate(".affirm","click",function () {
            var id = $(this).attr("rel");
            layer.confirm('你确定要将此流水状态修改为\"已确认\"吗?', function(index){
                //do something
                $.get("/finance/finish/dataTable.json",{"id":id}).done(function (result) {
                    if(result.status == "success"){
                        layer.msg("已经更改确定");
                        datatable.ajax.reload(false,null);
                    } else {
                        layer.msg(result.message);
                    }
                }).error(function () {
                    layer.msg("服务器繁忙");
                });


                layer.close(index);
            });
        });*/

        /*导出excel，当月所有数据。以.xlsx后缀的excel*/
        $("#export").click(function () {
            window.location.href = "/finance/monthReport/"+ $("#datepicker").val() +"/data.xlsx";
            /*即文件下载*/
        });

    });

</script>
</body>
</html>

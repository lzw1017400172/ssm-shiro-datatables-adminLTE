<%--
  Created by IntelliJ IDEA.
  User: 刘忠伟
  Date: 2017/2/18
  Time: 15:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>劳务外包流水查询</title>
    <!-- Tell tde browser to be responsive to screen widtd -->
    <meta content="widtd=device-widtd, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <%@include file="../../include/css.jsp"%>
    <link rel="stylesheet" href="/static/css/style.css">
    <link rel="stylesheet" href="/static/adminLTE2/plugins/datepicker/datepicker3.css">
    <link rel="stylesheet" href="/static/adminLTE2/plugins/datatables/dataTables.bootstrap.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <%@include file="../../include/header.jsp"%>
    <jsp:include page="../../include/sidebar.jsp">
        <jsp:param name="menu" value="business_outSourcing_list"/>
    </jsp:include>

    <!-- =============================================== -->

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">

        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box box-primary box-solid">
                <div class="box-header with-border">
                    <h3 class="box-title">劳务外包流水</h3>

                    <div class="box-tools pull-right">
                        <a href="/business/outSourcing/new" class="btn btn-primary"><i class="fa fa-plus"></i></a>
                    </div>
                </div>
                <div class="box-body">
                    <div class="box">
                        <div id="filtrate-box" class="screen-condition scd01"><!-- 筛选开始 -->
                            <form action="" class="form-inline" method="post" id="form"><%--默认方式get，不写action默认提交到地址栏--%>
                                <div class="form-group form-marginR">
                                    <label >流水号:</label>
                                    <input type="text" class="form-control form-angle input-sm" id="serialNumber" name="serialNumber" placeholder="">
                                </div>
                                <div class="form-group form-marginR">
                                    <label >用人单位:</label>
                                    <input type="text" class="form-control form-angle input-sm" id="companyName" name="companyName" placeholder="">
                                </div>
                                <div class="form-group form-marginR">
                                    <label>状态:</label>
                                    <!-- <div class="input-group"> -->
                                    <select class="form-control form-angle input-sm" id="status" name="status">
                                        <option value="">请选择</option>
                                        <option value="完成">完成</option>
                                        <option value="未完成">未完成</option>
                                    </select>
                                    <input type="hidden" name="workFlowType" id="workFlowType">
                                    <!-- </div> -->
                                </div>
                                <div class="form-group form-marginR">
                                    <label >起止时间:</label>
                                    <div class="input-group">
                                        <div class="input-group-addon form-angle input-sm"><i class="fa fa-calendar"></i></div>
                                        <input type="text" class="form-control form-angle form_datetime input-sm" name="startDate" id="startDate" >
                                    </div> -
                                    <div class="input-group">
                                        <div class="input-group-addon form-angle input-sm"><i class="fa fa-calendar"></i></div>
                                        <input type="text" class="form-control form-angle form_datetime input-sm" name="stopDate" id="stopDate" >
                                    </div>
                                </div>
                                <button type="button" class="btn btn-default btn-sm" id="btn">查询</button>
                            </form>
                        </div><!-- 筛选结束 -->
                        <div class="box-body">
                            <table class="table table-striped table-bordered" cellspacing="0" width="100%" id="myTable">
                                <thead>
                                <tr>
                                    <th>id</th>
                                    <th>流水号</th>
                                    <th>需求公司</th>
                                    <th>公司地址</th>
                                    <th>公司电话</th>
                                    <th>法人代表</th>
                                    <th>电话号码</th>
                                    <th>身份证号</th>
                                    <th>创建时间</th>
                                    <th>状态</th>
                                    <th>总佣金</th>
                                    <th>合同</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        </tr>
                                </tbody>
                            </table>
                        </div>

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
<script src="/static/adminLTE2/plugins/datepicker/bootstrap-datepicker.js"></script>
<script src="/static/adminLTE2/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>

<%--弹框--%>
<script src="/static/adminLTE2/plugins/layer/layer.js"></script>
<%--datetables--%>
<script src="/static/adminLTE2/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="/static/adminLTE2/plugins/datatables/dataTables.bootstrap.min.js"></script>
<script>
    $(function(){
        //开始日期
        $("#startDate").datepicker({
            language: "zh-CN",  //选择语言，需要导入对应的语言包js
            autoclose: true,//选中之后自动隐藏日期选择框
            startView: 0,
            format: "yyyy-mm-dd",
        });
        //结束日期
        $("#stopDate").datepicker({
            language: "zh-CN",  //选择语言，需要导入对应的语言包js
            autoclose: true,//选中之后自动隐藏日期选择框
            startView: 0,
            format: "yyyy-mm-dd",
        });

        //使用datetables插件可以完成表格。datatables是在跳转到此页面后就发送ajax，然后接受值才显示

        var dataTables = $('#myTable').DataTable({
            "lengthMenu":[3,5,10,15,20],
            "serverSide":true,
            "ajax":{
                url:"/business/outSourcing/data.json",
                data:function (dataSouce) {
                    dataSouce.serialNumber=$("#serialNumber").val(),
                    dataSouce.companyName=$("#companyName").val(),
                    dataSouce.status=$("#status").val(),
                    dataSouce.startDate=$("#startDate").val(),
                    dataSouce.stopDate=$("#stopDate").val()
                }
            },
            "searching":false,
            "columns":[
                {"data":"id"},
                {"data":"serialNumber"},
                {"data":"companyName"},
                {"data":"companyAddress"},
                {"data":"companyPhone"},
                {"data":"linkMan"},
                {"data":"linkManPhone"},
                {"data":"idNum"},
                {"data":"createTime"},
                {"data":"status"},
                {"data":"totalCost"},
                {"data":function (row) {
                    return  "<a href='/business/outSourcing/docs/zipDownload/"+ row.id +"'>下载</a>";
                }},
                {"data":function (row) {
                    if(row.status == "完成"){/*点击详情应该跳转页面，*/
                        return "<a href='/business/outSourcing/"+  row.serialNumber +"'>详情</a>";
                    } else{
                        return "<a href='/business/outSourcing/" + row.serialNumber +  "'>详情</a>   <a href='javascript:;' rel='"+  row.id +"' class='checkBtn'>完成</a>";
                    }                                                              /*点击完成，就变成完成状态，不是跳转。发送ajax完成状态修改。并且使用datatable刷新表格。或者修改html*/
                }},
            ],
            "columnDefs":[
                {targets:[0],visible:false},
                {targets:[1,2,3,4,5,6,7,8,9,10,11,12],orderable:false}
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

        /*点击    完成，更新数据库为完成，客户端也更新，只更新datatanble，或者改变html*/
        /*完成    按钮是后来生成的，所以无法绑定事件。需要事件委托，*/
        $(document).delegate(".checkBtn","click",function () {
            //根据rel属性获取需要修改的合同id，this
            var id = $(this).attr("rel");
            layer.confirm("确定要将该合同修改为\"完成\"状态吗？", function(index) {
                //do something
                $.post("/business/outSourcing/status/finish",{"id":id}).done(function (result) {
                    if(result.status = "success"){
                        //修改成功，刷新一次datatables表。或者改变html，
                        dataTables.ajax.reload();

                    } else {
                        layer.msg();
                    }
                }).error(function () {
                    layer.msg("服务器繁忙，请稍候再试！");
                });
                layer.close(index);
            });
        });

        /*搜索点击发送一次ajax请求*/
        $("#btn").click(function () {

            dataTables.draw();

        });
    });
</script>
</body>
</html>

<%--
  Created by IntelliJ IDEA.
  User: 刘忠伟
  Date: 2017/2/23
  Time: 11:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>财务报表</title>
    <%@include file="../include/css.jsp"%>
    <link rel="stylesheet" href="/static/adminLTE2/plugins/datatables/dataTables.bootstrap.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <%@include file="../include/header.jsp"%>
    <jsp:include page="../include/sidebar.jsp">
        <jsp:param name="menu" value="finance_list"/>
    </jsp:include>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box box-primary box-solid">
                <div class="box-header with-border">
                    <h3 class="box-title">财务报表列表</h3>

                </div>
                <div class="box-body">
                    <table  id="example" class="table table-striped table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>流水号</th>
                            <th>类型</th>
                            <th>金钱</th>
                            <th>业务模块</th>
                            <th>业务流水</th>
                            <th>创建时间</th>
                            <th>创建人</th>
                            <th>确认时间</th>
                            <th>确认人</th>
                            <th>状态</th>
                            <th>备注</th>
                            <th>#</th>
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
<script src="/static/adminLTE2/plugins/layer/layer.js"></script>
<script>
    $(function () {

        var dataTable = $('#example').DataTable({

            /* "lengthChange": false,*///把选择每页显示数量的select取消掉
            "lengthMenu":[3,5,10,15,20],
            "serverSide":true,//所有数据都在服务端处理
            "ajax":{
                "url":"/finance/list/datatables.json",
                "type":"post"
                //没有搜索的话就不需要传自定义的值
            },
            "ordering": false,
            "searching": false,
            "columns":[
                {"data":"serialNumber"},
                {"data":"type"},
                {"data":"money"},
                {"data":"module"},
                {"data":"moduleSerialNumber"},
                {"data":"createDate"},
                {"data":"createUser"},
                {"data":"confirmUser"},
                {"data":"confirmDate"},
                {"data":"state"},
                {"data":"remark"},
                {"data":function (row) {
                    if(row.state == "未确认"){
                        return "<a href='javascript:;' class='finish' rel="+ row.id +">确认</a>";
                    } else {
                        return null;
                    }
                }}

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

        /*点击完成，发送ajax请求，更改数据库为完成。并且更改页面*/
        /*后来生成的超链接，需要事件委托*/
        $(document).delegate(".finish","click",function () {
            var id = $(this).attr("rel");
            layer.confirm('确定要把此流水变更为\"已确定\"吗?', function(index){
                //do something

                $.get("/finance/finish/dataTable.json",{"id":id}).done(function (result) {
                    if(result.status == "success"){
                        layer.msg("变更成功");
                        //刷新datatable,只加载本页不跳到第一页
                        //刷新一次本页面，数据库已经改变，新的数据。相当于有一次发送的是ajax。没有刷新页面。又把值更新到表
                        dataTable.ajax.reload(false,null);
                    } else {
                        layer.msg(result.message);
                    }
                }).error(function () {
                    layer.msg("服务器繁忙，请稍候再试！");
                });

                layer.close(index);
            });
        });

    });

</script>
</body>
</html>


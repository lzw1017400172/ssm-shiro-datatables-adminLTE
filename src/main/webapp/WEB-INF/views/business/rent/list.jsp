<%--
  Created by IntelliJ IDEA.
  User: 刘忠伟
  Date: 2017/2/16
  Time: 16:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>设备租赁</title>
    <%@include file="../../include/css.jsp"%>
    <link rel="stylesheet" href="/static/adminLTE2/plugins/datatables/dataTables.bootstrap.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <%@include file="../../include/header.jsp"%>
    <jsp:include page="../../include/sidebar.jsp">
        <jsp:param name="menu" value="business_device_rent"/>
    </jsp:include>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box box-primary box-solid">
                <div class="box-header with-border">
                    <h3 class="box-title">租赁合同列表</h3>

                    <div class="box-tools pull-right">
                        <a href="/business/device/rent/new" class="btn btn-primary"><i class="fa fa-plus"></i></a>
                    </div>
                </div>
                <div class="box-body">
                    <table  id="example" class="table table-striped table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>流水号</th>
                            <th>租赁公司</th>
                            <th>电话</th>
                            <th>租赁日期</th>
                            <th>归还日期</th>
                            <th>状态</th>
                            <th>租金</th>

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

<%@include file="../../include/js.jsp"%>
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
                "url":"/business/device/rent/datatables.json",
                "type":"get"
                //没有搜索的话就不需要传自定义的值
            },
            "ordering": false,
            "searching": false,
            "columns":[
                {"data":"serialNumber"},
                {"data":"companyName"},
                {"data":"tel"},
                {"data":"rentDate"},
                {"data":"backDate"},
                {"data":"status"},
                {"data":"totalPrice"},

                {"data":function (row) {//row为每行数据的对象
                    if(row.status == "未完成"){
                        return "<a href=\"/business/device/rent/"+ row.serialNumber +"\">详情</a>   <a rel=\""+ row.id +"\" href=\"javascript:;\" class=\"checkBtn\">完成</a>";
                    } else {/*点击详情是跳转到详情页面，点击完成发送ajax请求，然后更新页面数据*/
                        return "<a href=\"/business/device/rent/"+ row.serialNumber +"\">详情</a>";/*添加隐藏属性，为合同的id号，用于对合同的修改*/
                    }
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

        /*点击完成，发送ajax请求，更改数据库为完成。并且更改页面*/
        /*事件 的绑定是在加载页面时，完成实在后来生成，需要事件委托*/
        $(document).delegate(".checkBtn","click",function () {
            var id = $(this).attr("rel");
            layer.confirm('确定要将合同修改为已完成吗?', function(index){
                //do something
                $.post("/business/device/rent/finish",{"id":id}).done(function (result) {
                    if(result.status == "success"){

                        layer.msg(result.data);
                        //修改成功后，刷新一次表格，相当于又发ajax请求
                        dataTable.ajax.reload();
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


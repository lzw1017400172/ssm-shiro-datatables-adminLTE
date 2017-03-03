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
    <title>财务报表-日报</title>
    <%@include file="../include/css.jsp"%>
    <link rel="stylesheet" href="/static/adminLTE2/plugins/datatables/dataTables.bootstrap.css">
    <link rel="stylesheet" href="/static/plugins/datepicker/datepicker3.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <%@include file="../include/header.jsp"%>
    <jsp:include page="../include/sidebar.jsp">
        <jsp:param name="menu" value="finance_dayReport"/>
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
                    <h3 class="box-title">财务日报列表</h3>
                    <div class="box-tools pull-right">
                        <a href="javascript:;"  class="btn btn-default" id="export"><i class="fa fa-file-excel-o"></i> 导出Excel</a>
                    </div>
                </div>
                <div class="box-body">
                    <table  id="example" class="table table-striped table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>流水号</th>
                            <th>创建时间</th>
                            <th>类型</th>
                            <th>金钱</th>
                            <th>业务模块</th>
                            <th>业务流水</th>
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
<script src="/static/plugins/datepicker/bootstrap-datepicker.js"></script>
<script src="/static/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>
<%--关于时间--%>
<script src="/static/adminLTE2/plugins/moment.js"></script>
<script>
    $(function () {
        /*因为月报详情会来到日报，所以可能会传参数date，判断有就是详情根据这个date发送ajax，没有进来就要获取当前时间*/
        if(${not empty requestScope.date}){
            $("#datepicker").val("${requestScope.date}");
        } else {
            $("#datepicker").val(moment().format("YYYY-MM-DD"));
        }

        $("#datepicker").datepicker({
            language: "zh-CN",  //选择语言，需要导入对应的语言包js
            autoclose: true,//选中之后自动隐藏日期选择框
            startView: 0,
            format: "yyyy-mm-dd"
        }).on('changeDate',function(ev) {
                //选择之后触发的事件，再发送一次ajax请求。
            dataTable.draw();
        });

        //使用datatables，发送ajax
        var dataTable = $("#example").DataTable({

            "lengthMenu":[3,5,10,15,20],
            "serverSide":true,//所有操作都在服务端进行
            "ajax":{
                url:"/finance/dayReport/data.json",
                type:"get",
                data:function (dataSouce) {//发送给服务端的值
                    dataSouce.date=$("#datepicker").val();
                    //每次向服务端发送ajax都要把搜索的值传过去。每次都获取一次。当日期选择后自动让datatables发送一次请求，又在这里获取一下值。最新值
                }
            },
            "ordering":false,//不排序
             "searching":false,
             "columns":[//客户端接受值
             {"data":"serialNumber"},
             {"data":"createDate"},
             {"data":"type"},
             {"data":"money"},
             {"data":"module"},
             {"data":"moduleSerialNumber"},
             {"data":"state"},
             {"data":"remark"},
             {"data":function (row) {
             if(row.state == "未确认"){
             return "<a href='javascript:;' class='affirm' rel='"+ row.id +"'>确认</a>";
             } else {
             return null;
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

        //后来衍生出来的确认按钮。无法绑定事件
        //事件委托
        $(document).delegate(".affirm","click",function () {
            var id = $(this).attr("rel");
            layer.confirm('你确定要讲把此流水变更为\"已确定\"吗?', function(index){
                //do something

                $.get("/finance/finish/dataTable.json",{"id":id}).done(function (result) {
                    if(result.status == "success"){
                        layer.msg("已经更改确定");
                        dataTable.ajax.reload(false,null);
                    } else {
                        layer.msg(result.message);
                    }
                }).error(function () {
                    layer.msg("服务器繁忙");
                });

                layer.close(index);
            });

        });

        //导出excel就是文件下载。下载的文件格式为.csv后缀。使用js是因为直接能够获取日期值
        $("#export").click(function () {
            window.location.href = "/finance/dayReport/"+ $("#datepicker").val() +"/data.xlsx";
        });
    });
</script>
</body>
</html>
<%--
  Created by IntelliJ IDEA.
  User: 刘忠伟
  Date: 2017/1/12
  Time: 20:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>AdminLTE 2 | Blank Page</title>
    <%@include file="../include/css.jsp" %>
    <%--<link rel="stylesheet" href="/static/adminLTE2/plugins/datatables/jquery.dataTables.min.css">--%>
    <link rel="stylesheet" href="/static/adminLTE2/plugins/datatables/dataTables.bootstrap.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">


    <%@include file="../include/header.jsp" %>
    <%--jsp节点向某个页面传值。sys_accounts代表本页面。同属于一个节点下的以sys_开头 --%>
    <jsp:include page="../include/sidebar.jsp">
        <jsp:param name="menu" value="sys_device"/>
    </jsp:include>
    <!-- =============================================== -->

    <!-- Left side column. contains the sidebar -->
    <%--<%@include file="../include/sidebar.jsp"%>--%>

    <!-- =============================================== -->

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">

        <!-- Main content -->
        <section class="content">

            <div class="box box-solid box-primary">
                <div class="box-header  with-border">
                    <h3 class="box-title"><i class="fa fa-search"></i>搜索</h3>
                </div>
                <div class="box-body">
                    <div class="box-tools pull-left">
                        <form class="form-inline">
                            <input type="text" class="form-control" value="${requestScope.q_name}" name="q_name"
                                   placeholder="设备名称" id="device_name">

                            <button type="button" class="btn btn-default" id="btn"><i class="fa fa-search"></i></button>
                        </form>
                    </div>

                </div>

            </div>

            <!-- Default box -->
            <div class="box box-solid box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">设备列表</h3>
                    <div class="box-tools pull-right">
                        <a href="/device/new" class="btn"><i class="fa fa-plus"></i></a>
                    </div>
                </div>
                <div class="container">

                    <div class="row">
                        <div class="col-md-11">
                            <div class="form-group"></div>
                            <div class="form-group">
                                <c:if test="${not empty requestScope.message}"><%--这个就是从重定向传过来的值，存在就显示--%>
                                    <div class="alert alert-success ">
                                            ${message}
                                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×
                                        </button>
                                    </div>
                                </c:if>
                                <div class="form-group"></div>
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th>id</th>
                                        <th>设备名称</th>
                                        <th>单位</th>
                                        <th>总数量</th>
                                        <th>单价</th>
                                        <th>
                                            #
                                        </th>
                                    </tr>
                                    </thead>
                                    <tbody>

                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- /.box -->


            </div>

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->


</div>
<!-- ./wrapper -->

<!-- js -->
<%@include file="../include/js.jsp" %>
<script src="/static/adminLTE2/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="/static/adminLTE2/plugins/datatables/dataTables.bootstrap.min.js"></script>
<script>

    $(function () {
        var datatable = $(".table").DataTable({
            "lengthMenu":[5,10,15,20],    //分页每页显示的数量
            "serverSide":true,      //表示所有的数据都在服务端处理
            /*"ajax":"/device/load",              //发送ajax异步请求，默认并且是以get的方式*/
            //因为要自定义搜索，每次发送请求都要传过去素偶所得内容。所以要设置ajax请求
            "ajax":{
                url:"/device/load",
                type:"get",
                data:function(dataSouce){//dataSouce表示每次ajax请求发送过去的所有值的对象。需要另添新值，
                    dataSouce.deviceName=$("#device_name").val();//添加值，多传过去的值
                }
            },

            "searching":false,//禁止使用自带搜索。只是不显示，仍然发送关于搜索数据。

            //配置返回json中的data属性的key对应的列。指定每列的对应，然后循环
            "columns":[
                {"data":"id","name":"id"},//第一列
                {"data":"name"},//第二列
                {"data":"unit"},
                {"data":"totalNum","name":"totalNum"},//不仅设置结果及对应映射。而且设置每一列的name属性，一般设置为列名。排序时候先获取第几列，在获取name值即可获取列名。获取列名
                {"data":"price","name":"price"},
                {"data":function (row) {//对象row代表每行数据的对象。
                    return "<a href='javascript:;' rel='"+ row.id +"' class='delLink'>删除</a>  <a href='/device/"+ row.id +"/edit' class='edit'>修改</a>";//可以返回html
                }}

            ],
            "columnDefs":[
                {targets:[0],visible:false},//最少有一个排序，就是用id列，并且显示
                {targets:[1,2,5],orderable:false}//其他列全部不排序
            ],
            "language":{//定义中文
              "search":"请输入设备名称：",
            }
        });
       /* $(".delLink").click(function () {
            alert("删除");
        });*/        //这里不能直接绑定事件。因为delLink元素，是js衍生出来的。在元素和事件绑定的时候还没衍生出来。所以无法绑定。就点击没用。
        //解决方案：事件委托
        $(document).delegate(".delLink","click",function () {
           if(confirm("确定要删除吗?")){
               //还需要知道删除对象的id。在创建元素的时候应该给一个id。使用$(this)获取
                var id = $(this).attr("rel");
                $.ajax({
                    type:"get",
                    url:"/device/"+ id +"/del", //仍然可以使用动态url传值
                    success:function (json) {
                        if(json == "success"){
                            alert("删除成功！");
                            //并且需要刷新数据，只需要刷新这个插件表格这部分，不需要刷新全部整个页面
                            //刷新整个页面
                            //location.href = "/device";
                            //history.go(0);

                            //datatables插件中只刷新这个表格.重新加载表数据
                            datatable.ajax.reload();

                        }else{
                            alert("删除失败！");
                        }
                    },
                    error:function () {
                        alert("服务器异常！");
                    }
                });


           }
        });


        //自定义搜索
        $("#btn").click(function () {
            //也就是当点击这个按钮时，需要datatables发送一次请求，并且把搜索的内容传过去。
            //所以这里要产生一次请求。就是上面的ajax请求
            datatable.draw();

        });

    });





</script>
</body>
</html>

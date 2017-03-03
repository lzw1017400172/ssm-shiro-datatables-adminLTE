
<%--
  Created by IntelliJ IDEA.
  User: 刘忠伟
  Date: 2017/2/21
  Time: 12:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>网盘系统</title>
    <%@include file="../include/css.jsp"%>
    <link rel="stylesheet" href="/static/adminLTE2/plugins/datatables/dataTables.bootstrap.css">
    <link rel="stylesheet" href="/static/adminLTE2/plugins/uploader/webuploader.css">
    <style>

    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <%@include file="../include/header.jsp"%>
    <jsp:include page="../include/sidebar.jsp">
        <jsp:param name="menu" value="disk_list"/>
    </jsp:include>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box box-primary box-solid">
                <div class="box-header with-border">
                    <h3 class="box-title">网盘系统</h3>

                </div>
                <div class="box-body">

                        <div id="pick" type="button" class="btn btn-primary">文 件 上 传</div><%--点击文件上传，需要知道这个文件存到当前文件夹里面，需要当前文件夹id--%>
                        <button type="button" id="newFolder" href="javascript:;" class="btn btn-primary">新建文件夹</button>
                        <%--点击应该弹出创建文件夹的名字--%>
                    <%--新建文件夹，需要知道新建文件夹的位置，由跳转传过来的当前文件夹的id当作创建新文件夹的fid--%>
                    <br/><br/>
                    <table  id="example" class="table table-striped table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th></th>
                            <th>名称</th>
                            <th>大小</th>
                            <th>创建时间</th>
                            <th>创建人</th>
                            <th>#</th>
                        </tr>
                        </thead>
                        <tbody>
                            <%--传过来的文件夹里的内容可能没有为空，这显示暂无数据--%>
                            <c:if test="${ empty requestScope.diskList}">
                                <tr>
                                    <td colspan="6">暂无资源</td>
                                </tr>
                            </c:if>
                            <c:forEach items="${requestScope.diskList}" var="disk">
                                <tr>
                                    <c:choose>
                                        <c:when test="${disk.type == 'file'}">
                                            <td><i class="fa fa-file-o"></i></td>
                                        </c:when>
                                        <c:otherwise>
                                            <td><i class="fa fa-folder-o"></i></td>
                                        </c:otherwise>
                                    </c:choose>
                                    <td><%--如果这里是个文件夹，则点击进去展示下一级。如果是文件，点击进去应该是下载。两者url不同。需要判断--%>
                                        <c:choose>
                                            <c:when test="${disk.type == 'file'}">
                                                <%--是文件的时候，需要下载，也是超链接--%>
                                                <a href="/disk/download/${disk.id}">${disk.sourceName}</a>
                                            </c:when>
                                            <c:otherwise>
                                                <%--这里表示是文件夹，要进入下一级。根据把此文件夹id当作fid的值去查，就查到下一级。数据库这样设计的，属于某文件夹的资源fid都为此文件夹id--%>
                                                <%--使用?传值，第一次进根不传值。或者动态url，/disk/list/0即可--%>
                                                <a href="/disk/list?fid=${disk.id}">${disk.sourceName}</a>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${disk.size}</td>
                                    <td>${disk.createTime}</td>
                                    <td>${disk.createUser}</td>
                                    <td><a href="javascript:;" class="del" rel="${disk.id}">删除</a></td>
                                </tr><%--因为是删除操作，当点击时发送ajax，根据id删除--%>

                            </c:forEach>
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
<script src="/static/adminLTE2/plugins/uploader/webuploader.min.js"></script>
<script src="/static/adminLTE2/plugins/layer/layer.js"></script>
<script>
    $(function () {

        var uploder = WebUploader.create({
            swf : "/static/adminLTE2/plugins/uploader/Uploader.swf",
            server: "/disk/upload",
            pick: '#pick',
            auto : true,
            fileVal:'file',
            formData:{"fid":${requestScope.id}}
        });

        uploder.on("uploadSuccess",function(file,resp){
            if(resp.status == 'success') {
                layer.msg("文件上传成功！");
                window.history.go(0);
            } else {
                layer.msg(resp.message);
            }
        });
        uploder.on("uploadError",function(){
            layer.msg("服务器异常");
        });

        $("#newFolder").click(function () {

            layer.prompt({title: '请输入新文件夹名字，并确认'}, function(newFolder,index){
                layer.close(index);

                /*发送ajax请求添加新的文件夹，然后刷新页面即可*/
                $.get("/disk/new/folder",{"fid":${requestScope.id},"newFolderName":newFolder})
                        .done(function (result) {
                            if(result.status == "success"){
                                layer.msg("文件夹"+newFolder+"创建成功");
                                window.history.go(0);
                            } else {
                                layer.msg(result.message);
                            }

                }).error(function () {
                    layer.msg("服务器繁忙，请稍候再试！");
                });
            });


        });


        /*点击删除指定资源，根据id*/
        $(".del").click(function () {
            var id = $(this).attr("rel");
            layer.confirm('确定要删除吗?', function(index){
                //do something
                /*发送ajax删除完毕后，刷新本页面*/
                $.post("/disk/remove",{"id":id}).done(function (result) {
                    if(result.status =="success"){
                        layer.msg("删除成功！");
                        window.history.go(0);
                    } else {
                        layer.msg(result.message);
                    }

                }).error(function () {
                    layer.msg("服务器繁忙，请稍候再试");
                });

                layer.close(index);
            });

        });
    });

</script>
</body>
</html>

<%--
  Created by IntelliJ IDEA.
  User: 刘忠伟
  Date: 2017/2/16
  Time: 19:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>新增设备租赁合同</title>
    <%@include file="../../include/css.jsp"%>
    <link rel="stylesheet" href="/static/adminLTE2/plugins/uploader/webuploader.css">
    <link rel="stylesheet" href="/static/adminLTE2/plugins/select2/select2.min.css">
    <link rel="stylesheet" href="/static/adminLTE2/plugins/datepicker/datepicker3.css">
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
                        <h3 class="box-title">新增租赁合同</h3>

                        <div class="box-tools pull-right">
                            <a href="/business/device/rent" class="btn btn-default btn-sm"><i class="fa fa-reply"></i></a>
                        </div>
                    </div>
                    <div class="box-body">
                        <div class="row">
                            <div class="col-md-4">
                                <div class="form-group">
                                    <label>公司名称</label>
                                    <input type="text" class="form-control"  id="companyName" tabindex="1">
                                </div>
                                <div class="form-group">
                                    <label>联系电话</label>
                                    <input type="text" class="form-control" id="tel" tabindex="4">
                                </div>
                                <div class="form-group">
                                    <label>租赁日期</label><%--此日期必须和服务器时间一致，不能和客户端一致--%>
                                    <input type="text" class="form-control" id="rentDate" readonly>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="form-group">
                                    <label>法人代表</label>
                                    <input type="text" class="form-control" id="linkMan" tabindex="2">
                                </div>
                                <div class="form-group">
                                    <label>地址</label>
                                    <input type="text" class="form-control" id="address" tabindex="5">
                                </div>
                                <div class="form-group">
                                    <label>归还日期</label>
                                    <input type="text" class="form-control" id="backDate" tabindex="7">
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="form-group">
                                    <label>身份证号</label>
                                    <input type="text" class="form-control" id="cardNum" tabindex="3">
                                </div>
                                <div class="form-group">
                                    <label>传真</label>
                                    <input type="text" class="form-control" id="fax" tabindex="6">
                                </div>
                                <div class="form-group">
                                    <label>总天数</label>
                                    <input type="text" class="form-control" id="totalDay" readonly>
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
                        <div class="box-tools pull-right">
                            <button class="btn btn-default btn-sm" data-toggle="modal" data-target="#myModal"><i class="fa fa-plus"></i></button>
                        </div>
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
                                <th>#</th>
                            </tr>
                            </thead>
                            <tbody>
                                <tr v-if="deviceArray.length == 0">
                                    <td colspan="6" style="align-content: center">暂无数据</td>
                                </tr>
                                <tr v-for="device in deviceArray">
                                    <td>{{device.deviceName}}</td>
                                    <td>{{device.unit}}</td>
                                    <td>{{device.rentPrice}}</td>
                                    <td>{{device.rentNum}}</td>
                                    <td>{{device.totalPrice}}</td>
                                    <td><a href="javascript:;" v-on:click="remove(device)"><i class="fa fa-trash text-danger"></i></a></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="box-footer" style="text-align: right">
                        总租赁费 {{totalCost}} 元 预付款 {{preCost}} 元 尾款 {{lastCost}} 元
                    </div>
                </div>

                <div class="box">
                    <div class="box-header">
                        <h3 class="box-title">合同扫描件</h3>
                    </div>
                    <div class="box-body">
                        <div id="picker">选择文件</div>
                        注意：上传合同扫描件要求清晰可见 合同必须公司法人签字盖章
                        <ul id="fileName"></ul>
                        <button v-on:click="saveRent" class="btn btn-primary pull-right">保存合同</button>
                    </div>
                </div>

            </section>
            <!-- /.content -->
        </div>
        <!-- /.content-wrapper -->

        <div class="modal fade" id="myModal">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title">选择设备</h4>
                    </div>
                    <div class="modal-body">
                        <form action="">
                            <div class="form-group">
                                <input type="hidden" id="deviceName">
                                <label>设备名称</label>
                                <select id="deviceId" style="width: 300px;" class="form-control js-example-basic-single" >
                                    <option value="">请选择设备</option>
                                    <c:forEach items="${requestScope.deviceList}" var="device">
                                        <option value="${device.id}">${device.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>库存数量</label>
                                <input type="text" class="form-control" readonly id="currNum" >
                            </div>
                            <div class="form-group">
                                <label>单位</label>
                                <input type="text" class="form-control" id="unit" readonly>
                            </div>
                            <div class="form-group">
                                <label>租赁单价</label>
                                <input type="text" class="form-control" id="rentPrice" readonly>
                            </div>
                            <div class="form-group">
                                <label>租赁数量</label>
                                <input type="text" class="form-control" id="rentNum">
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                        <button type="button" class="btn btn-primary" v-on:click="addDevice">加入列表</button><!--使用vue把此按钮绑定click事件-->
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->
        
    </div>

    <%@include file="../../include/js.jsp"%>
    <script src="/static/adminLTE2/plugins/uploader/webuploader.min.js"></script>

    <script src="/static/adminLTE2/plugins/select2/select2.min.js"></script>
    <script src="/static/adminLTE2/plugins/moment.js"></script>
    <script src="/static/adminLTE2/plugins/datepicker/bootstrap-datepicker.js"></script>
    <script src="/static/adminLTE2/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>
    <script src="/static/adminLTE2/plugins/layer/layer.js"></script>
    <script src="/static/adminLTE2/plugins/vue.js"></script>
<script>

    /*jsp页面分为三个表单部分，全部数据都要提交，如果三个表单同时提交，不能保证事务。所以以向服务端发送json字符串的形式发送所有数据*/
    /*分别获取三个部分的数据，包装成一个大json，发送服务端*/

    /*表单的数据*/

    /*设备列表的数据在deviceArray里面存的device对象*/

    /*合同上传的数据，使用fileArray[]数组，把所有合同都传上去，保存的是合同在电脑的文件名，保存到数据库，用于查找合同*/
    var fileArray = [];



    $(function () {

        $("#deviceId").select2();
        /*select的选中事件,选中不为null时才执行，此事件*/

        $("#deviceId").change(function () {/*ajax传值，相当于url的?传值*/
                var deviceId = $(this).val();
                if(deviceId) {
                    $.get("/business/device/rent/device.json", {"deviceId":deviceId}).done(function (data) {

                        /*$("#currNum").val(data.currNum);
                        $("#unit").val(data.unit);
                        $("#rentPrice").val(data.price);*/
                        if (data.status == "success") {

                            $("#currNum").val(data.data.currNum);

                            $("#unit").val(data.data.unit);
                            $("#rentPrice").val(data.data.price);
                            /*顺便把deviceName赋值，下面需要*/
                            $("#deviceName").val(data.data.name);
                        } else {
                            layer.msg(data.message);
                        }
                    }).error(function () {
                        layer.msg("服务器异常，请稍候再试！");
                    })

                }
            });

        /*webupload文件上传*/
        var uploader = WebUploader.create({
            swf : "js/uploader/Uploader.swf",
            server: "/business/file/upload",
            pick: '#picker',
            auto : true,
            fileVal:'file'
        });
        /*-通过监听事件来完成操作
         文件上传成功，显示原始文件名。*/
        uploader.on('uploadSuccess', function(file,resp) {/*resp是json自动转换后的对象*/
            layer.msg("上传成功");
            /*原始名字显示到html，新名字和旧名字要提交到数据库，所以保存到fileArray数组*/
            var html = "<li>"+ resp.data.sourceFileName +"</li>";
            $("#fileName").append(html);
            //新名字和旧名子都保存到数组，创建一个对象
            var name = {
                sourceFileName:resp.data.sourceFileName,
                newFileName:resp.data.newFileName
            }
            fileArray.push(name);
        });

        // 文件上传失败，现实上传出错。
        uploader.on('uploadError', function(file) {
            layer.msg("服务器繁忙，请稍后再试！");
        });


        <!--因为获取客户端时间不准确，所以需要ajax获取服务端时间-->
        /*客户端时间的话使用moment*/
        /*$("#rentDate").val(moment().format("YYYY-MM-DD"));*/
        $.get("/business/device/rent/time").done(function (data) {
            $("#rentDate").val(moment(data).format("YYYY-MM-DD"));
        }).error(function () {
           layer.msg("服务器异常，请稍候再试");
        })

        $("#backDate").datepicker({
            language: "zh-CN",  //选择语言，需要导入对应的语言包js
            format: "yyyy-mm-dd",
            autoclose: true,//选中之后自动隐藏日期选择框

            clearBtn:false,//清除按钮
            todayBtn:false,//今日按钮
            startDate:moment().add(1,"days").format("YYYY-MM-DD")
        }).on("changeDate",function (ev) {
            var nowDate = moment().format("YYYY-MM-DD");
           var backDate =  moment(ev.format("yyyy-mm-dd"));
            $("#totalDay").val(backDate.diff(nowDate,"days"));
        });
       /* $("#totalDay").val();*/



    });


    /*使用vue.js插件去完成，第二部分，添加的设备列表。注意：vue.js是不需要jquery的支持的*/
    var app = new Vue({
        el:"#app",      //表示交给vue管理的节点，尽量大
        data:{//所有数据
            deviceArray:[]     //数组，用来装所有的device，并且于dom绑定，只要数组改变dom改变//数据，当dom与数据绑定，dom会随着数据变化而时刻变化
        },
        methods:{//所有事件
            addDevice:function () {
                //此事件需要把穿进来device对象，添加到数组deviceArray。一种情况，就是如果添加的设备是在数组之中已经存在（使用遍历，），需要把设备进行累加，而不是显示
                var flag = false;
                if(this.$data.deviceArray) {//数组不等于null才去遍历

                   for (var i = 0; i < this.$data.deviceArray.length; i++) {
                       if (this.$data.deviceArray[i].id == $("#deviceId").val()) {
                            flag = true;

                           //累加之后进行判断是否超过库存
                           if(parseInt(this.$data.deviceArray[i].rentNum) + parseInt($("#rentNum").val())<=parseInt(this.$data.deviceArray[i].currNum)){
                           //存在了，进行累加。并且对库存数量进行改变
                           this.$data.deviceArray[i].rentNum = parseInt(this.$data.deviceArray[i].rentNum) + parseInt($("#rentNum").val());
                           this.$data.deviceArray[i].totalPrice = this.$data.deviceArray[i].rentNum * this.$data.deviceArray[i].rentPrice;

                           } else if(!parseInt($("#rentNum").val())) {
                               layer.msg("请输入数量！");
                           } else {
                               layer.msg("库存不足");
                           }

                           break;
                       }
                   }
               }
               if(!flag) {
                   var id = $("#deviceId").val();
                   var deviceName = $("#deviceName").val();
                   var currNum = $("#currNum").val();
                   var unit = $("#unit").val();
                   var rentPrice = $("#rentPrice").val();
                   var rentNum = $("#rentNum").val();
                   var totalPrice = parseFloat(rentPrice) * parseInt(rentNum);
                   var device = {
                       id: id,
                       deviceName: deviceName,
                       currNum:currNum,
                       unit: unit,
                       rentPrice: rentPrice,
                       rentNum: rentNum,
                       totalPrice: totalPrice
                   }
                   if(parseInt($("#rentNum").val())<=parseInt($("#currNum").val())) {
                       this.$data.deviceArray.push(device);
                   } else if(!parseInt($("#rentNum").val())){
                       layer.msg("请输入数量！");
                   } else {
                       layer.msg("库存不足！");
                   }


               }
            },
            remove:function(device){
                layer.confirm("确定要删除吗",function(index){
                    app.$data.deviceArray.splice(app.$data.deviceArray.indexOf(device),1);
                    layer.close(index);
                });

            },
            saveRent:function () {
                //这个事件，将三个部分的数据都发送到服务端，以前都是接受json，现在发送值为json字符串，需要使用GSON.pres(对象)
                var json = {
                    companyName:$("#companyName").val(),
                    tel:$("#tel").val(),
                    rentDate:$("#rentDate").val(),
                    linkMan:$("#linkMan").val(),
                    address:$("#address").val(),
                    backDate:$("#backDate").val(),
                    cardNum:$("#cardNum").val(),
                    fax:$("#fax").val(),
                    totalDay:$("#totalDay").val(),
                    deviceArray:app.$data.deviceArray,
                    fileArray:fileArray     //fileArray数组是全局的
                }

                $.ajax({
                    url:"/business/device/rent/new",
                    type:"post",
                    data:JSON.stringify(json),         //把json转成对象使用JSON.paese(json),把对象转成json，JSON.stringify(对象)
                    contentType: "application/json;charset=UTF-8",
                    success:function (json) {
                        if(json.status == "success"){
                            layer.confirm('保存成功', {
                                btn: ['继续添加','打印合同'] //按钮
                            }, function(){
                                history.go(0);
                            }, function(){
                                //打印合同跳转到，此流水号对应额合同展示页面
                                location.href="/business/device/rent/"+json.data;
                            });
                        } else {
                            layer.msg(json.message);
                        }
                    },
                    error:function () {
                        layer.msg("服务器繁忙，请稍后再试");
                    }
                    
                });
                
                
                
                
                
            }
        },
        computed: {//计算属性
            totalCost:function () {
                //总钱数，遍历数组所有的，相加
                var totalCost = 0;
                for(var i= 0;i<this.$data.deviceArray.length;i++){
                    totalCost = totalCost + parseInt(this.$data.deviceArray[i].totalPrice);
                }
                return totalCost;
            },
            preCost :function () {
                return this.totalCost*0.3;        //当前对象this是app（vue）
            },
            lastCost :function () {
                return this.totalCost - this.preCost;
            }
        }
    });



</script>
</body>
</html>

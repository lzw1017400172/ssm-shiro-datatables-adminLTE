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
                        <a href="/business/outSourcing" class="btn btn-default btn-sm"><i class="fa fa-reply"></i></a>
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
                                                <input type="text"  class="" id="companyName" placeholder="">

                                            </div>
                                        </div>

                                        <div class="col-lg-3">
                                            <div >
                                                <label >地 &nbsp;&nbsp址：&nbsp</label>
                                                <input type="text" class="" name="representative" placeholder="" id="address">
                                            </div>
                                        </div>


                                        <div class="col-lg-3">
                                            <div class="form-group">
                                                <label>公司电话：&nbsp</label>
                                                <input type="text" class="" name="telephone" placeholder="" id="companyPhone">
                                            </div>
                                        </div>



                                    </div>

                                    <%--法人--%>
                                    <div class="row">
                                        <div class="col-lg-3">
                                            <div class="" >
                                                <label >法人代表：&nbsp</label>
                                                <input type="text"  class="" id="linkMan" placeholder="">

                                            </div>
                                        </div>
                                        <div class="col-lg-3">
                                            <div class="form-group">
                                                <label >电 &nbsp;&nbsp话：&nbsp</label>
                                                <input type="text" class="" name="linkManPhone" placeholder="" id="linkManPhone">
                                            </div>
                                        </div>

                                        <div class="col-lg-3">
                                            <div>
                                                <label for="idNum">身份证号：&nbsp</label>
                                                <input type="text" class="" id="idNum" placeholder="">
                                            </div>
                                        </div>
                                    </div>

                                    <!--金额 -->
                                    <div class="row">
                                        <div class="col-lg-3">
                                            <div class="" >
                                                <label >佣金金额：&nbsp</label>
                                                <input type="text" disabled="disabled" class="" id="totalCost" placeholder="" v-model="totalCost" >

                                            </div>
                                        </div>
                                        <div class="col-lg-3">
                                            <div class="form-group">
                                                <label >预付款：&nbsp</label>
                                                <input type="text" class="" disabled="disabled" name="firstMoney" id="preCost" placeholder="" v-model="preCost" >
                                            </div>
                                        </div>
                                        <div class="col-lg-3">
                                            <div >
                                                <label >尾&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;款：&nbsp</label>
                                                <input type="text" class="" disabled="disabled" name="lastMoney" id="lastCost" placeholder="" v-model="lastCost">
                                            </div>
                                        </div>
                                    </div>

                                    <div> <br/></div>
                                    <%--工种--%>
                                    <div class="box">
                                        <div class="box-header">
                                            <h3 class="box-title">&nbsp工种列表</h3>
                                            <div class="box-tools pull-right">
                                                <a class="btn btn-default btn-sm" data-toggle="modal" data-target="#myModal"><i class="fa fa-plus"></i></a>
                                            </div>
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
                                                <tr v-if="carftArray.length == 0">
                                                    <td colspan="5" style="align-content: center">暂无数据</td>
                                                </tr>
                                                <tr v-for="carft in carftArray"> <%--迭代crarftArray集合，于数据绑定--%>
                                                    <td>{{carft.name}}</td>
                                                    <td>{{carft.price}}</td>
                                                    <td>{{carft.outSourcingNum}}</td>
                                                    <td>{{carft.subtotal}}</td>
                                                    <td><a href="javascript:;" v-on:click="remove(carft)"> <i class="fa fa-trash-o"></i></a></td>
                                                    <%--超链接执行javascript，使用vue绑定事件，点击就删除数组指定的元素,需要知道删除哪个，传参数--%>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div> <br/></div>



                                    <div class="box" style="padding-left: 20px">
                                        <div class="box-header">
                                            <span class="title"><i class="fa fa-user"></i> 合同上传</span>
                                        </div>
                                        <form action="" class="form-horizontal">
                                            <hr>
                                            <p style="padding-left: 20px">注意事项</p>
                                            <ul>
                                                <li>上传合同扫描件要求清晰可见</li>
                                                <li>合同必须公司法人签字盖章</li>
                                            </ul>
                                            <div class="form-actions">
                                                <div id="picker">上传合同</div>

                                            </div>
                                            <div>
                                                <ul id="fileName"></ul>
                                            </div>
                                        </form>

                                    </div>


                                    <div class="row">

                                        <div class="col-lg-3">

                                        </div>
                                        <div class="col-lg-3">
                                            <div class="box-footer">
                                                <button type="button" class="btn btn-primary" v-on:click="saveContract">提交</button><%--提交按钮绑定vue事件，点击就提交全部数据，是以json形式能保证事务，需要把对象或者数组转化成json--%>
                                                &nbsp;&nbsp;&nbsp;&nbsp;
                                                <button type="button" class="btn btn-primary" v-on:click="reset">重置</button><%--重置按钮也绑定事件，即刷新页面可以重置，所有数据清零--%>
                                            </div>
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

    <div class="modal fade" id="myModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">选择工种</h4>
                </div>
                <div class="modal-body">
                    <form action="">
                        <div class="form-group">
                            <input type="hidden" id="carftName">
                            <label>工种名称</label>
                            <select id="craftId" style="width: 300px;" class="form-control js-example-basic-single" >
                                <option value="">请选择工种</option>
                                <c:forEach items="${requestScope.craftList}" var="craft">
                                    <option value="${craft.id}">${craft.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>库存数量</label>
                            <input type="text" class="form-control" id="currNum" readonly>
                        </div>
                        <div class="form-group">
                            <label>工种单位佣金</label>
                            <input type="text" class="form-control" id="price" readonly>
                        </div>
                        <div class="form-group">
                            <label>工种数量</label>
                            <input type="text" class="form-control" id="outSourcingNum">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-primary" v-on:click="addCarft">加入列表</button><!--使用vue把此按钮绑定click事件-->
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<!-- ./wrapper -->

<%@include file="../../include/js.jsp"%>
<!-- datepicker -->
<script src="/static/plugins/datepicker/bootstrap-datepicker.js"></script>
<script src="/static/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>
<script src="/static/adminLTE2/plugins/uploader/webuploader.min.js"></script>
<%--select下拉框--%>
<script src="/static/adminLTE2/plugins/select2/select2.min.js"></script>
<%--提示框--%>
<script src="/static/adminLTE2/plugins/layer/layer.js"></script>
<%--vue.js数据与dom绑定--%>
<script src="/static/adminLTE2/plugins/vue.js"></script>
<script>
    <!-- $(function() {		$( "#datepicker").datepicker();	});
    -->

    /*因为向服务端发送数据，需要把文件上传的新老名字发送，属于第三部分，创建一个对象数组，装文件名*/
    var fileArray = [];
    $(function () {

        $(".js-example-basic-single").select2();

        /*select框的change事件*/
        $("#craftId").change(function () {
            var carftId = $("#craftId").val();
            /*我们虽然把carft数据都穿了过来，但是不能用js使页面显示对应值。
            根据carftId发送ajax请求，再查一次数据库才能显示对应值*/
            if(carftId) {
                $.get("/business/outSourcing/carft.json", {"carftId": carftId}).done(function (json) {//ajax传值相当于?跟参数
                    if (json.status == "success") {
                        $("#currNum").val(json.data.currNum);
                        $("#price").val(json.data.price);
                        /*因为在下面需要工种的名称，表单没有，所以需要隐藏域来引用工种名称。在这里赋值*/
                        $("#carftName").val(json.data.name);
                        //这个name值，会随着选择的select而改变。会一直赋值
                    } else {
                        layer.msg(json.message);
                    }
                }).error(function () {
                    layer.msg("服务器异常，请稍后再试！");
                })


            }
        });


        $("#datepicker").datepicker({
            language: "zh-CN",
            autoclose: true,//选中之后自动隐藏日期选择框
            //clearBtn: true,//清除按钮
            //todayBtn: true,//今日按钮
            format: "yyyy-mm-dd"//日期格式，详见 http://bootstrap-datepicker.readthedocs.org/en/release/options.html#format
        });
        //文件上传
        var uploader = WebUploader.create({
            swf : "js/uploader/Uploader.swf",
            server: "/business/file/upload",    /*可以和设备租赁的文件上传使用一个，都是保存到一个文件夹（可能文件夹不一样），都是随机数+后缀重命名，都是返回新名字和源名字*/
            pick: '#picker',
            auto : true,
            fileVal:'file',
            /*accept: {
             title: 'Images',
             extensions: 'gif,jpg,jpeg,bmp,png',
             mimeTypes: 'image/!*'
             }*/
        });

        /*upload通过监听事件，三个事件，开始时用于做进度条，上传成功时，上传失败时，无论成功或者失败时*/
        /*这里不需要做进度条*/
        uploader.on( 'uploadSuccess', function( file,data ) {//第二个参数是上传成功返回的json
            //1把老名字显示到html
            var html = "<li>"+ data.data.sourceFileName +"</li>";
            $("#fileName").append(html);
            //2把新老名字全部发送到服务端,即添加到fileArray数组
            var file = {
                sourceFileName:data.data.sourceFileName,
                newFileName:data.data.newFileName
            }
            fileArray.push(file);

        });

        uploader.on( 'uploadError', function( file ) {
            layer.msg("文件上传失败，请稍后再试！");
        });

    });


    /*现在需要达到的效果是，点击 加入列表 ，实现添加一条html，也就是todoList的效果
     * 可以使用vue,js来达到todoList的效果，即数组于循环绑定，html会随着数组而改变*/
    var app = new Vue({
        el:"#app",
        data:{//数据
            carftArray:[],//对象数据
        },
        methods:{//事件
            addCarft:function () {
                //添加工种事件，因为数组carftArray于dom绑定，只需要向carftArray添加数据即可
                var carft = {
                    id:$("#craftId").val(),
                    name:$("#carftName").val(),
                    price:$("#price").val(),
                    outSourcingNum:parseInt($("#outSourcingNum").val()),
                    subtotal:parseFloat($("#price").val())*parseInt($("#outSourcingNum").val())
                }
                //当加入数组之前，判断一下是否是数组里面已经有的，有的话就原有元素累加，不添加新的元素
                var flag = false;
                for(var i = 0;i<app.carftArray.length;i++){
                    if(app.carftArray[i].name == carft.name){
                        //数组中存在。就累加，不新增元素。并且需要判断累加结果不能大于库存
                        if(parseInt(app.carftArray[i].outSourcingNum) + parseInt(carft.outSourcingNum)<=parseInt($("#currNum").val())) {
                            app.carftArray[i].outSourcingNum = parseInt(app.carftArray[i].outSourcingNum) + parseInt(carft.outSourcingNum);
                            app.carftArray[i].subtotal = app.carftArray[i].subtotal + carft.subtotal;
                        } else if(!parseInt($("#outSourcingNum").val())) {
                            layer.msg("请输入数量！");
                        } else {
                            layer.msg("库存不足！");
                        }
                        flag=true;
                        break;
                    }
                }
                if(!flag){
                    //并且判断是否超过库存
                    if(parseInt($("#outSourcingNum").val())<=parseInt($("#currNum").val())) {
                        app.carftArray.push(carft);
                    } else if(!parseInt($("#rentNum").val())) {
                        layer.msg("请输入数量！");
                    }else{
                        layer.msg("库存不足！");
                    }
                }
            },
            remove:function (carft) {//直接传过来的就是数组中的元素，使用splice删除
                app.carftArray.splice(app.carftArray.indexOf(carft),1);/*splice从指定索引开始，删除几个*/
            },
            saveContract:function () {
                //页面分为三个部分的数据，所以把三个部分的数据全部封装成一个对象，再转json发送到服务端。保证事务
                var json = {
                    companyName:$("#companyName").val(),
                    address:$("#address").val(),
                    companyPhone:$("#companyPhone").val(),
                    linkMan:$("#linkMan").val(),
                    linkManPhone:$("#linkManPhone").val(),
                    idNum:$("#idNum").val(),
                    /*这三个值仍然可以不传过去，在服务端计算更加严谨*/
                    /*totalCost:$("#totalCost").val(),
                    preCost:$("#preCpost").val(),
                    lastCost:$("#lastCost").val(),*/
                    /*还有两个对象数组，装的是剩下两部分*/
                    carftArray:app.carftArray,
                    fileArray:fileArray
                }

                //发送ajax把上述对象传过去
                $.ajax({
                    url:"/business/outSourcing/new",
                    type:"post",
                    data:JSON.stringify(json),//把对象转化成json字符串发送
                    contentType: "application/json;charset=UTF-8",
                    success:function (result) {
                        if(result.status == "success"){
                            //result.data就是流水号
                            layer.confirm('新增劳务外包流水成功', {
                                btn: ['继续添加','打印合同'] //按钮
                            }, function(){
                                window.history.go(0);
                            }, function(){
                                //打印合同，跳转到文件显示页面
                                window.location.href = "/business/outSourcing/"+result.data;
                            });

                        } else {
                            layer.msg(result.message);
                        }

                    },
                    error:function () {
                        layer.msg("服务器繁忙，请稍候再试！");
                    }
                });
            },
            reset:function () {
                window.history.go(0);//重置即刷新页面
            }
        },
        computed: {//计算属性
            totalCost:function () {//总佣金金额，对数组进行迭代
                var total = 0;
                for (var i = 0; i < this.$data.carftArray.length; i++) {
                    total = total + parseInt(this.carftArray[i].subtotal);
                }
                return total;
            },
            preCost:function () {
                return this.totalCost*0.2;
            },
            lastCost:function () {
                return this.totalCost - this.preCost;
            }
        }


    });

</script>
</body>
</html>


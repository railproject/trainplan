<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>华云Paas系统</title>
    <link href="${ctx}/assets/css/custom-bootstrap.css" rel="stylesheet">
    <!--font-awesome-->
    <link  type="text/css" rel="stylesheet" href="${ctx}/assets/css/font-awesome.min.css"/>
    <!-- Custom styles for this template -->
    <link href="${ctx}/assets/css/style.css" rel="stylesheet">
    <script src="${ctx}/assets/js/html5.js"></script>
    <script src="${ctx}/assets/js/jquery.js"></script>
    <script src="${ctx}/assets/js/bootstrap.min.js"></script>
    <script type="text/javascript" defer="defer">

        //被嵌入的Iframe根据不同的屏幕高度自适应
        $(document).ready(function () {
            var header = $(".header");
            var headerH = header.height();
            var body = $("body");
            var bodyW = body.width();
            var bodyH = body.height();
            var Win = $(document);
            var WW = $(document).width();
            var WH = $(document).height();
            var TargetBox = $("#contentLayerFrame");
            var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
            var Content_frame = function() {
                var WH = $(window).height();
                if (isChrome) {
                    TargetBox.css({ "height": (WH - headerH - 40)-5 + "px"});
                    TargetBox.css({ "min-height": (WH - headerH - 40)-5 + "px"});

                }else{
                    TargetBox.css({ "height": (WH - headerH - 40) - 5 + "px"});
                    TargetBox.css({ "min-height": (WH - headerH - 40) - 5 + "px"});
                }
            };
            Content_frame();
            $(window).resize(function () {
                Content_frame();
            });
        });
        //被嵌入的Iframe根据不同的屏幕高度自适应

        $(document).ready(function(){
            $(".navHref").click(function(){
                var src = $(this).attr("data-href");
                $("#contentLayerFrame").attr("src",src);
                location.hash=src;
            });
            if(location.hash!==''){
                var src = location.hash.substring(1, location.hash.length);
                $("#contentLayerFrame").attr("src",src);
            }
        });
    </script>
</head>
<body>
<div class="header">
    <div class="row">
        <div class="pull-left logo_name"><img src="${ctx}/assets/img/login-logo.png" height="50px"> </div>
        <div class="col-md-4 col-sm-4 col-xs-4 pull-right">
            <div class="btn-group pull-right" style="margin-top:15px;">
                <button type="button" class="btn btn-danger dropdown-toggle" data-toggle="dropdown">
                    <i class="fa fa-user"></i>
                    <shiro:principal/>
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" role="menu">
                    <li><a href="#"><i class="fa fa-cog"></i>修改密码</a></li>
                    <li class="divider"></li>
                    <li><a href="${ctx}/logout"><i class="fa fa-sign-out"></i> 退 出</a></li>
                </ul>
            </div>
            <!--btn-group-->
        </div>
        <!--col-md-6-->
    </div>
    <!--row-->
</div>
<!--header-->
<div class="sidebar">
    <!--nav-->
    <nav class="Navigation">
        <ul>
            <li><a data-href="Console.html" class="menu_one active navHref"> <i class="fa fa-tachometer"></i>主控台</a></li>
            <li><a data-href="severManagement.html" class="menu_one navHref"><i class="fa fa-align-center"></i>我的服务</a></li>
            <li><a class="menu_one" style="cursor:default;"><i class="fa fa-signal"></i>我的应用<i class="fa fa-caret-down pull-right"></i> </a>
                <ul>
                    <li><a class=" navHref" data-href="myEnvironment.html"><i class="fa fa-random"></i>我的环境</a></li>
                    <li><a class=" navHref" data-href="applicationManagement.html"><i class="fa fa-folder-open"></i>应用管理</a></li>
                </ul>
            </li>
            <li><a data-href="myResource.html" class="menu_one navHref"> <i class="fa fa-tasks"></i>我的资源</a></li>
            <li><a class="menu_one" style="cursor:default;"><i class="fa fa-bar-chart-o"></i>我的监控<i class="fa fa-caret-down pull-right"></i> </a>
                <ul>
                    <li><a data-href="resourceMonitoring.html" class=" navHref"><i class="fa fa-external-link"></i>资源监控</a></li>
                    <li><a data-href="appMonitoring.html" class=" navHref"><i class="fa fa-desktop"></i>应用监控</a></li>
                    <li><a data-href="alarmMonitoring.html" class=" navHref"><i class="fa fa-bell-o"></i>告警管理</a></li>
                </ul>
            </li>
            <li><a class="menu_one" style="cursor:default;"><i class="fa fa-user" style="margin-right:11px;"></i>用户中心<i class="fa fa-caret-down pull-right"></i> </a>
                <ul>
                    <li><a data-href="agencyMatter.html" class=" navHref"><i class="fa fa-random"></i>待办事项</a></li>
                    <li><a data-href="userManagement.html" class=" navHref"><i class="fa fa-user-md"></i>用户管理</a></li>
                </ul>
            </li>
        </ul>
    </nav>
</div>
<!--sidebar-->
<div class="content">
    <!--content-menu-->
    <!--Iframe嵌入页面-->
    <div class="iframebox" style="width:100%; height:auto;" id="ContentBox">
        <iframe id="contentLayerFrame" src="Console.html" frameborder=0 name="contentFrame" style="width:100%; height:auto; overflow-x:hidden;" > </iframe>
    </div>
    <!--嵌入页面end-->
</div>
<!--content-->
</body>
</html>

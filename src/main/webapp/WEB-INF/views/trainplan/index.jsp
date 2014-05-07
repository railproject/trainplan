<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Cache-Control" content="no-store" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <title>华云Paas系统</title>
    <!-- Bootstrap core CSS -->
    <link type="text/css" href="assets/css/custom-bootstrap.css" rel="stylesheet">
    <!--font-awesome-->
    <link type="text/css" rel="stylesheet" href="assets/css/font-awesome.min.css"/>
    <!-- Custom styles for this template -->
    <link href="assets/css/style.css" rel="stylesheet">
    <script type="text/javascript" src="assets/js/jquery.js"></script>
    <script type="text/javascript" src="assets/js/html5.js"></script>
    <script type="text/javascript" src="assets/js/bootstrap.min.js"></script>
    <script type="text/javascript" defer="defer">
        //被嵌入的Iframe根据不同的屏幕高度自适应
        $(window).load(function () {
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
            var Content_frame = function () {
                var WH = $(window).height();
                if (isChrome) {
                    TargetBox.css({ "height": (WH - headerH) - 60 + "px"});
                    TargetBox.css({ "min-height": (WH - headerH) - 60 + "px"});

                } else {
                    TargetBox.css({ "height": (WH - headerH) - 65 + "px"});
                    TargetBox.css({ "min-height": (WH - headerH) - 65 + "px"});
                }
            };
            Content_frame();
            $(window).resize(function () {
                Content_frame();
            });
        });
    </script>
</head>
<body>
<div class="header">
    <div class="row">
        <div class="col-md-4 col-sm-4 col-xs-4 logo_name"><img src="assets/img/logo02.png" height="40px"></div>
        <div class="col-md-4 col-sm-4 col-xs-4 pull-right">
            <div class="btn-group pull-right" style="padding:9px 0px; border-left:1px solid #444;">
                <button type="button" class="btn btn-link dropdown-toggle" data-toggle="dropdown">
                    <i class="fa fa-user" style="margin-right:10px;"></i>Admin
                    <span class="caret"></span></button>
                <ul class="dropdown-menu" role="menu">
                    <li><a href="#"><i class="fa fa-cog"></i>修改密码</a></li>
                    <li class="divider"></li>
                    <li><a href="login.jsp"><i class="fa fa-sign-out"></i> 退 出</a></li>
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
            <li><a class="menu_one active margin-right-5" href="Console.jsp" target="contentFrame"> <i
                    class="fa fa-tachometer"></i>主控台</a></li>
            <li><a href="resourceManagement.html" target="contentFrame" class="menu_one"> <i class="fa fa-tasks"></i>资源管理</a>
            </li>
            <li><a href="applicationManagement.html" target="contentFrame" class="menu_one"><i class="fa fa-signal"></i>应用管理
            </a></li>
            <li><a href="severManagement.html" target="contentFrame" class="menu_one"><i class="fa fa-align-center"></i>服务管理</a>
            </li>
            <li><a target="contentFrame" class="menu_one" style="cursor:default;"><i class="fa fa-retweet"></i>工单管理<i
                    class="fa fa-caret-down pull-right"></i> </a>
                <ul>
                    <li><a href="workOrderManage.html" target="contentFrame"><i class="fa fa-list-ol"></i>工单列表</a></li>
                    <li><a href="agencyMatter.html" target="contentFrame"><i class="fa fa-random"></i>代办事项</a></li>
                </ul>
            </li>
            <li><a target="contentFrame" class="menu_one" style="cursor:default;"><i class="fa fa-indent"></i>计量管理<i
                    class="fa fa-caret-down pull-right"></i> </a>
                <ul>
                    <li><a href="resourceStatistics.html" target="contentFrame"><i class="fa fa-external-link"></i>资源统计</a>
                    </li>
                    <li><a href="serverStatistics.html" target="contentFrame"><i class="fa fa-folder-open"></i>服务统计</a>
                    </li>
                    <li><a href="appStatistics.html" target="contentFrame"><i class="fa fa-desktop"></i>应用统计</a></li>
                    <li><a href="userStatistics.html" target="contentFrame"><i class="fa fa-user"></i>租户统计</a></li>
                </ul>
            </li>
            <li><a target="contentFrame" class="menu_one" style="cursor:default;"><i
                    class="fa fa-bar-chart-o"></i>监控管理<i class="fa fa-caret-down pull-right"></i> </a>
                <ul>
                    <li><a href="monitoringConfiguration.html" target="contentFrame"><i class="fa fa-eye"></i>监控配置</a>
                    </li>
                    <li><a href="monitoringPlatform.html" target="contentFrame"><i class="fa fa-list-alt"></i>平台监控</a>
                    </li>
                    <li><a href="resourceMonitoring.html" target="contentFrame"><i class="fa fa-external-link"></i>资源监控</a>
                    </li>
                    <li><a href="serviceMonitoring.html" target="contentFrame"><i class="fa fa-coffee"></i>服务监控</a></li>
                    <li><a href="appMonitoring.html" target="contentFrame"><i class="fa fa-desktop"></i>应用监控</a></li>
                    <li><a href="alarmMonitoring.html" target="contentFrame"><i class="fa fa-bell-o"></i>告警管理</a></li>
                    <li><a href="logsMonitoring.html" target="contentFrame"><i class="fa fa-file-text-o"></i>日志管理</a>
                    </li>
                </ul>
            </li>
            <li><a target="contentFrame" class="menu_one" style="cursor:default;"><i class="fa fa-cogs"
                                                                                     style="margin-right:11px;"></i>系统管理<i
                    class="fa fa-caret-down pull-right"></i> </a>
                <ul>
                    <li><a href="adminManage.html" target="contentFrame"><i class="fa fa-user"></i>管员管理</a></li>
                    <li><a href="userManagement.html" target="contentFrame"><i class="fa fa-user-md"></i>用户管理</a></li>
                    <li><a href="roleManagement.html" target="contentFrame"><i class="fa fa-sun-o"></i>角色管理</a></li>
                </ul>
            </li>
        </ul>
    </nav>
</div>
<!--sidebar-->
<div class="contentIframe">
    <!--content-menu-->
    <!--Iframe嵌入页面-->
    <div class="iframebox" style="width:100%; height:auto;" id="ContentBox">
        <iframe id="contentLayerFrame" src="content" frameborder=0 name="contentFrame"
                style="width:100%; height:auto; overflow-x:hidden;"></iframe>
    </div>
    <!--嵌入页面end-->
</div>
<!--content-->
</body>
</html>

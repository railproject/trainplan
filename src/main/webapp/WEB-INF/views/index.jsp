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
    <title>铁路实施计划平台</title>
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

            $("#kanban").get(0).click();
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
            <li>
                <a href="http://10.1.186.116:8090/dashboard/kanban/kanban.html" id="kanban" target="contentFrame" class="menu_one" style="cursor:default;"><i class="fa fa-list-ol"></i>计划看板 </a>
            </li>
            <li>
                <a target="contentFrame" class="menu_one"><i class="fa fa-bar-chart-o"></i>计划分析<i class="fa fa-caret-down pull-right"></i></a>
                <ul>
                    <li><a href="http://10.1.186.116:8090/dashboard/kanban/railline_sf.html" target="contentFrame"><i class="fa fa-level-down"></i>局别始发汇总</a></li>
                    <li><a href="http://10.1.186.116:8090/dashboard/kanban/railline_jr.html" target="contentFrame"><i class="fa fa-level-up"></i>局别接入汇总</a></li>
                    <li><a href="${ctx}/default/transfer/planReviewLines" target="contentFrame"><i class="fa fa-search"></i>日计划查询</a></li>
                </ul>
            </li>
            <li><a target="contentFrame" class="menu_one"><i class="fa fa-road"></i>客运列车<i class="fa fa-caret-down pull-right"></i> </a>
                <ul>
                    <li><a href="${ctx}/default/transfer/planReviewAll" target="contentFrame"><i class="fa fa-list-alt"></i>开行计划汇总</a></li>
                    <li><a href="${ctx}/default/transfer/planReview" target="contentFrame"><i class="fa fa-eye"></i>核查编制图定开行</a></li>
                    <li><a href="${ctx}/default/transfer/planRunlineBatch" target="contentFrame"><i class="fa fa-external-link"></i>批量上图</a></li>
                    <li><a href="${ctx}/default/transfer/planDesign" target="contentFrame"><i class="fa fa-sign-out"></i>新图初始化</a></li>
                    
                    
                    <li><a href="${ctx}/cross" target="contentFrame"><i class="fa fa-pencil"></i>对数表维护</a></li>
                    <li><a href="${ctx}/cross/unit" target="contentFrame"><i class="fa fa-retweet"></i>基本交路单元维护</a></li>
                    <li><a href="${ctx}/audit" target="contentFrame"><i class="fa fa-eye"></i>日计划审核</a></li>
                    <li><a href="${ctx}/jbtcx" target="contentFrame"><i class="fa fa-search"></i>基本图查询</a></li>
                </ul>
            </li>
            <li><a href="${ctx}/default/transfer/planConstruction" class="menu_one" target="contentFrame"><i class="fa fa-truck"></i>货运列车 </a></li>
            <li><a href="http://10.1.191.135:7003/sgdd" target="_blank" class="menu_one"><i class="fa fa-gavel"></i>施工维修 </a></li>
            <li class="pull-right"><a href="http://10.1.191.99/jszl/htmlFrame/jszlIndex.htm" target="_blank" class="menu_one"><i class="fa fa-file"></i>技术资料 </a></li>
        </ul>

        <!--      <a  href="form.html" class="menu_one"  target="contentFrame">FORM</a>
          <a  href="error.html" class="menu_one"  target="contentFrame">ERROR </a>-->

    </nav>
</div>
<!--sidebar-->
<div class="content">
    <!--content-menu-->
    <!--Iframe嵌入页面-->
    <div class="iframebox" style="width:100%; height:auto;" id="ContentBox">
        <iframe id="contentLayerFrame" src="" frameborder=0 name="contentFrame" style="width:100%; height:auto; overflow-x:hidden;" > </iframe>
    </div>
    <!--嵌入页面end-->
</div>
<!--content-->
</body>
</html>

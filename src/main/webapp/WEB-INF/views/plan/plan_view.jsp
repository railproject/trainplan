<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>新图初始化</title>
<!-- Bootstrap core CSS -->
<link href="../../assets/css/custom-bootstrap.css" rel="stylesheet">
<!--font-awesome-->
<link href="../../assets/css/datepicker.css" rel="stylesheet">
<link  type="text/css" rel="stylesheet" href="../../assets/css/font-awesome.min.css"/>
<!-- Custom styles for this template -->
<link href="../../assets/css/style.css" rel="stylesheet">


<!--<script src="../../assets/js/exporting.js"></script>-->
</head>
<body class="Iframe_body">
<!--以上为必须要的-->

<ol class="breadcrumb">
  <span><i class="fa fa-anchor"></i>当前位置：</span>
  <li><a href="#">新图初始化</a></li>
</ol>
<p class="col-md-12 col-sm-12 col-xs-12  text-left" style="border-bottom:1px dashed #ccc;padding-top:25px;padding-bottom:5px;margin-bottom:5px;color:#1838A5;">
	<b>
		方案名：<label id="plan_view_label_schemeText"></label>&nbsp;&nbsp;&nbsp;
		启用日期：<label id="plan_view_label_startDate"></label></b>&nbsp;&nbsp;&nbsp;
		当前进度：<label id="plan_view_label_planCurrentStatus"></label>&nbsp;&nbsp;&nbsp;
		<font color="red"><b>错误数：<label id="plan_view_label_planErrorRecord"></label>条</b></font>
</p>



<!--分栏框开始-->
<div class="row">
	<div class="panel panel-default">
      <div class="panel-heading">
        <h3 class="panel-title" style="float: left;"> <i class="fa fa-tachometer"></i>开行计划概况</h3>
        <p class="p_right">刷新时间：<label id="plan_view_label_refreshtime1"></label></p>
      </div>
      <!--panle-heading-->
      <div class="panel-body">
        <div class="row">
          <div class="table-responsive" >
            <div id="hightChart_lcmx" style="height:283px; margin: 0 auto;"></div>
          </div>
        </div>
        <!--myTabContent--> 
        <!--panel-body--> 
      </div>
      <!--panel panel-default--> 
    </div>
</div>


<!--分栏框开始-->
<div class="row">
  <div style="margin-right:-410px; float:left; width:100%;">
    <!--分栏框开始-->
    <div class="panel panel-default"  style="margin-right:410px;">
      <div class="panel-heading">
        <h3 class="panel-title" style="float: left;"> <i class="fa  fa-external-link-square"></i>计划详情</h3>
        <p class="p_right">刷新时间：<label id="plan_view_label_refreshtime2"></label></p>
      </div>
      
      
      <!--panle-heading-->
      <div class="panel-body">
        <div class="table-responsive" >
          <table class="table table-bordered table-striped table-hover" id="plan_view_table_1">
            <thead>
              <tr>
                <th width="30px"><div style="text-align:center;">序号</div></th>
                <th style="width:130px"><div style="text-align:center;">日期</div></th>
                <th><div style="text-align:center;">交路总数</div></th>
                <th><div style="text-align:center;">列车总数</div></th>
                <th><div style="text-align:center;">状态描述信息</div></th>
                <th><div style="text-align:center;">状态</div></th>
              </tr>
            </thead>
            <tbody id="plan_view_table_tbody">
            </tbody>
          </table>
        </div>
      </div>
      <!--panel-body--> 
    </div>
    <!--分栏框结束--> 
    <!--分栏框开始-->
    <!--分栏框结束--> 
  </div>
  
  
  <div class="pull-right" style="width:400px;"> 
    <!--分栏框开始-->
    <div id="plan_view_div_palnDayDetail" class="panel panel-default">
      <div class="panel-heading">
        <h3 class="panel-title" style="float: left;"> <i class="fa fa fa-folder-open"></i>车次详情&nbsp;&nbsp;日期:<label id="plan_view_div_palnDayDetail_title"></label></h3>
      </div>
      <!--panle-heading-->
      <div class="panel-body" style="padding:10px;">
      	<div class="row" style="padding-bottom:10px;"> 
	      <!--col-md-2--> 
	      <div class="col-md-17 col-sm-17  col-xs-17 pull-left">
	        <div class="input-group">
	          <input id="plan_view_div_palnDayDetail_trainNum" type="text" class="form-control" placeholder="输入车次查询">
	          <div class="input-group-btn">
	            <button id="plan_view_div_palnDayDetail_btnQuery" type="button" class="btn  btn-primary dropdown-toggle" data-toggle="dropdown">查询</button>
	          </div>
	          <!-- /btn-group --> 
	        </div>
	        <!-- /input-group --> 
	      </div>
	    </div>
      	
      	<div class="table-responsive" >
          <table class="table table-bordered table-striped table-hover"  id="plan_view_div_palnDayDetail_trainDetail">
            <thead>
              <tr>
                <th><div style="text-align:center;">车次</div></th>
                <th><div style="text-align:center;">始发站</div></th>
                <th><div style="text-align:center;">终到站</div></th>
              </tr>
            </thead>
            <tbody>
            </tbody>
          </table>
        </div>
        <!-- 
        <div class="pull-right" style="margin-top:10px;"> <a href="#" type="button" class="">更多&nbsp;&nbsp;<i class="fa fa-angle-double-right" style=" margin-right:0px;"></i></a> </div>
         -->
      </div>
      <!--panel-body--> 
    </div>
    <!--分栏框结束--> 
  </div>
  <!--分栏框结束--> 
</div>


<script src="../../assets/js/jquery.js"></script>
<script src="../../assets/js/html5.js"></script>
<script src="../../assets/js/bootstrap.min.js"></script>
<script src="../../assets/js/respond.min.js"></script>
<script src="../../assets/lib/knockout/knockout.js"></script>
<script src="../../assets/js/jquery.dataTables.js"></script>
<script src="../../assets/lib/sockjs/sockjs.js"></script>
<script src="../../assets/lib/stomp/lib/stomp.min.js"></script>
<script src="../../assets/js/highcharts.js"></script>
<script src="../../assets/js/jquery.gritter.min.js"></script>
<script src="../../js/common.js"></script>
<script src="../../js/plan/plan_view.js"></script>
</body>
</html>
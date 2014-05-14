<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<% 
String basePath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>新图初始化</title>
<!-- Bootstrap core CSS -->
<link href="<%=basePath %>/assets/oldAssets/css/custom-bootstrap.css" rel="stylesheet">
<!--font-awesome-->
<link  type="text/css" rel="stylesheet" href="<%=basePath %>/assets/oldAssets/css/font-awesome.min.css"/>
<link  type="text/css" rel="stylesheet" href="<%=basePath %>/assets/oldAssets/css/datepicker.css">
<!-- Custom styles for this template -->
<link href="<%=basePath %>/assets/oldAssets/css/style.css" rel="stylesheet">
<link href="<%=basePath %>/assets/oldAssets/easyui/themes/default/easyui.css" rel="stylesheet">
</head>
<body class="Iframe_body">
<input id="basePath_hidden" type="hidden" value="<%=basePath %>">

<!--以上为必须要的-->
<ol class="breadcrumb">
  <span><i class="fa fa-anchor"></i>当前位置：</span>
  <li><a href="plan_design.html">新图初始化</a></li>
</ol>
<!--分栏框开始-->
<div class="panel panel-default">
  <div class="panel-heading">
    <h3 class="panel-title" style="float: left; "> <i class="fa fa-wrench"></i>参数</h3>
  </div>
  <!--panle-heading-->
  
  <div class="panel-body">
    <div class="bs-example bs-example-tabs">
      <!--一一-->
      <div id="myTabContent" class="tab-content"> 
        <!--一一-->
        <div class="tab-pane fade active in" id="home">
          <div class="panel-body row">
            <form class="bs-example form-horizontal" style="margin:0;"  action="<%=basePath%>/default/transfer/plan/planView" method="POST"  >
              <!--1开始-->
              <div class="form-group" style="margin:0;"> 
                <!--1.1开始-->
                <p class="col-md-12 col-sm-12 col-xs-12  text-left" style="border-bottom:1px dashed #ccc;padding-bottom:8px;margin-bottom:25px;color:#1838A5;">参数设置：</p>
                <!--1.1结束--> 
                <!--1.2开始-->
                  <div class="form-group">
                    <label class="col-md-4 col-sm-4 col-xs-4 control-label text-right" >方案：</label>
                    	<div class="col-md-4 col-sm-4 col-xs-4">
                    	<input id="input_plan_design_scheme_text" name="schemeText" type="hidden"/>
                    	<select id="input_plan_design_scheme" class="form-control" name="schemeVal" onchange="planDesignSchemeChange()">
				        </select>
				        </div>
                  </div>
                  <div class="form-group">
                    <label class="col-md-4 col-sm-4 col-xs-4 control-label text-right">启用日期：</label>
                    <div class="col-md-4 col-sm-4 col-xs-4">
                      <label>
                      	<input type="text" class="form-control" style="width:120px;" placeholder="" id="input_plan_design_startDate"  name="startDate">
			          </label>
			        </div>
                  </div>
                  <div class="form-group" style="margin-bottom:0;">
                    <label class="col-md-4 col-sm-4 col-xs-4 control-label text-right" >天数：</label>
                    <div class="col-md-4 col-sm-4 col-xs-4">
                      <input id="input_plan_design_days" type="text" name="days" value="40" class="form-control easyui-validatebox" required="true" validtype="positive_integer" missingMessage="必填项" placeholder="">
                    </div>
                    <div class="col-md-2 col-sm-2 col-xs-2 " style="padding:0px; margin-top:7px;">单位（天）</div>
                  </div>
                <!--1.2结束--> 
              </div>
              <!--1结束--> 
              <!--1开始-->
               <div class=" form-group text-center"> 
              	<button type="submit" class="btn btn-primary" style="cursor:pointer;">&nbsp;提交&nbsp; </button>
               </div>
            </form>
          </div>
             
          <!--panel-footer--> 
        </div>
        <!--一一--> 
        <!--一一-->
        <!--一一--> 
      </div>
    </div>
  </div>
  <!--panel-body-->
  <!--panel-footer--> 
</div>

<!--分栏框结束--> 

<script src="<%=basePath %>/assets/oldAssets/js/jquery.js"></script> 
<script src="<%=basePath %>/assets/oldAssets/js/html5.js"></script> 
<script src="<%=basePath %>/assets/oldAssets/js/bootstrap.min.js"></script> 
<script src="<%=basePath %>/assets/oldAssets/js/respond.min.js"></script> 
<script src="<%=basePath %>/assets/oldAssets/js/jquery.dataTables.js"></script>
<script src="<%=basePath %>/assets/oldAssets/easyui/jquery.easyui.min.js"></script>
<script src="<%=basePath %>/assets/oldAssets/js/highcharts.js"></script>
<script src="<%=basePath %>/assets/oldAssets/js/datepicker.js"></script>
<script src="<%=basePath %>/assets/oldAssets/js/jquery.gritter.min.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/util/util.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/common.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/plan/plan_design.js"></script>
</body>
</html>

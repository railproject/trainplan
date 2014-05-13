<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<% 
String basePath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>批量上图</title>
<!-- Bootstrap core CSS -->
<link href="<%=basePath %>/assets/oldAssets/css/custom-bootstrap.css" rel="stylesheet">
<!--font-awesome-->
<link href="<%=basePath %>/assets/oldAssets/css/datepicker.css" rel="stylesheet">
<link  type="text/css" rel="stylesheet" href="<%=basePath %>/assets/oldAssets/css/font-awesome.min.css"/>
<link  type="text/css" rel="stylesheet" href="<%=basePath %>/assets/oldAssets/css/datepicker.css">
<!-- Custom styles for this template -->
<link href="<%=basePath %>/assets/oldAssets/css/style.css" rel="stylesheet">
<script src="<%=basePath %>/assets/oldAssets/js/jquery.js"></script>
<script src="<%=basePath %>/assets/oldAssets/js/html5.js"></script>
<script src="<%=basePath %>/assets/oldAssets/js/bootstrap.min.js"></script>
<script src="<%=basePath %>/assets/oldAssets/js/respond.min.js"></script>
<script src="<%=basePath %>/assets/oldAssets/js/jquery.dataTables.js"></script>


</head>
<body class="Iframe_body">
<input id="basePath_hidden" type="hidden" value="<%=basePath %>">
<!--以上为必须要的-->

<ol class="breadcrumb">
  <span><i class="fa fa-anchor"></i>当前位置：</span>
  <li><a href="#">批量上图</a></li>
</ol>


<!--分栏框开始-->
<div class="row">
      <!--panle-heading-->
      	<div class="row" style="padding-top:5px;">
            <form class="form-horizontal" role="form">
              <div class="form-group" style="float:left;margin-left:10px;margin-bottom:2;">
                <label for="exampleInputEmail2" class="control-label pull-left"> 路局:&nbsp;</label>
                <div class="pull-left">
                  <select id="plan_runline_batch_select_lj" onchange="refreshPlanRunlineBatchTableLjtjxx()" class="form-control"></select>
                </div>
              </div>
              <div class="form-group" style="float:left;margin-left:20px;margin-bottom:2;">
                <label for="exampleInputEmail2" class="control-label pull-left">&nbsp;&nbsp;&nbsp;起始日期:&nbsp;</label>
                <div class="pull-left">
                  <input type="text" class="form-control" style="width:110px;" placeholder="" id="plan_runline_batch_selectdate">
                </div>
              </div>
              <div class="form-group" style="float:left;margin-left:30px;margin-bottom:2;">
                <label for="exampleInputEmail2" class="control-label pull-left"> 天数:&nbsp;</label>
                <div class="pull-left">
                	<input id="plan_runline_batch_input_days" type="text" value="20" class="form-control" style="width:50px" placeholder="">
                </div>
              </div>
              <div class="form-group" style="float:left;margin-left:30px;margin-bottom:2;">
                <div class="pull-left">
                	<input type="text" class="form-control" style="width:180px;" placeholder="输入车次查询" id="plan_runline_input_trainNbr">
               </div>
              </div>
              
              <a type="button" href="#" class="btn btn-success" style="float:left;margin-left:20px;margin-bottom:0;" id="plan_runline_batch_btnQuery_ljtjxx">查询</a>
              <a type="button" href="#" class="btn btn-success" style="float:left;margin-left:3px;margin-bottom:0;" id="plan_runline_batch_btn_save">保存</a>
              <a type="button" href="#" class="btn btn-success" style="float:left;margin-left:3px;margin-bottom:0;" id="plan_runline_batch_btn_createRunLine">生成运行线</a>
              
            </form>
          </div>
	      <table class="table table-bordered table-striped table-hover">
	        <thead id="plan_runline_batch_table_ljtjxx_head">
	        </thead>
	        <tbody id="plan_runline_batch_table_ljtjxx_body">
	        
	        </tbody>
	      </table>
		    <div class="pull-left" style="line-height:34px;"><label id="page_footer_ul_desc"></label></div>
        	<ul id="page_footer_ul" class="pagination pull-right" style="margin:0px;"></ul>
      <!--panel panel-default--> 
</div>



<script src="<%=basePath %>/assets/oldAssets/js/jquery.gritter.min.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/util/pagination.js" type="text/javascript"></script>
<script src="<%=basePath %>/assets/oldAssets/js/datepicker.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/common.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/plan/plan_runline_batch.js"></script>
</body>
</html>
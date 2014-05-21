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
<title>核查编制图定开行</title>
<!-- Bootstrap core CSS -->
<link href="<%=basePath %>/assets/css/custom-bootstrap.css" rel="stylesheet">
<!--font-awesome-->
<link href="<%=basePath %>/assets/css/datepicker.css" rel="stylesheet">
<link  type="text/css" rel="stylesheet" href="<%=basePath %>/assets/css/font-awesome.min.css"/>
<link  type="text/css" rel="stylesheet" href="<%=basePath %>/assets/css/datepicker.css">
<!-- Custom styles for this template -->
<link href="<%=basePath %>/assets/css/style.css" rel="stylesheet">
<script src="<%=basePath %>/assets/js/jquery.js"></script>
<script src="<%=basePath %>/assets/js/html5.js"></script>
<script src="<%=basePath %>/assets/js/bootstrap.min.js"></script>
<script src="<%=basePath %>/assets/js/respond.min.js"></script>
<script src="<%=basePath %>/assets/js/jquery.dataTables.js"></script>
<script src="<%=basePath %>/assets/js/jquery.gritter.min.js"></script>
<script src="<%=basePath %>/assets/js/datepicker.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/common.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/knockout.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/plan/plan_runline_check.js"></script>

</head>
<body class="Iframe_body">
<input id="basePath_hidden" type="hidden" value="<%=basePath %>">
<!--以上为必须要的-->

<ol class="breadcrumb">
  <span><i class="fa fa-anchor"></i>当前位置：</span>
  <li><a href="#">运行线查询</a></li>
</ol> 
<div class="pull-left" style="width:65%;"> 
    <!--分栏框开始-->
    <div id="plan_view_div_palnDayDetail" class="panel panel-default">
       <div class="row" style="margin:15px 0 10px 0;"> 
		      <form class="form-horizontal" role="form">
		      <button class="btn btn-primary" type="button" id="plan_construction_btnQuery" data-bind="click: loadTrainsForPage">查询</button> 
		        <div class="pull-left">
		        		<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left:10px">
										方案:&nbsp;</label> 
						<div class="pull-left">
							<select style="width:200px" id="input_cross_chart_id"
								class="form-control" data-bind="options:searchModle().charts, value: searchModle().chart, optionsText: 'name', optionsCaption:''">
							</select>
						</div>  
				  <label for="exampleInputEmail3" class="control-label pull-left" style="margin-left:15px"> 
										始发局:&nbsp;</label> 
						<div class="pull-left">
							<select style="width: 50px" id="input_cross_chart_id"
								class="form-control" data-bind="options:searchModle().startBureaus(), value: searchModle().startBureau,  optionsValue: 'code', optionsText: 'shortName', optionsCaption: ''">
							</select>
						</div>   
				     <label for="exampleInputEmail3" class="control-label pull-left" style="margin-left:15px">
										终到局:&nbsp;</label> 
						<div class="pull-left">
							<select style="width:50px" id="input_cross_chart_id"
								class="form-control" data-bind="options:searchModle().endBureaus(), value: searchModle().endBureau, optionsValue: 'code', optionsText: 'shortName', optionsCaption: ''">
							</select> 
						</div>    
		          <div class="form-group" style="float:left;margin-left:20px;margin-bottom:0;">
		          	<label for="exampleInputEmail3" class="control-label pull-left"> 车次：&nbsp;</label>
		            <div class="pull-left">
                    	<input type="text" class="form-control" style="width:180px;" data-bind="value: searchModle().trainNbr" placeholder="输入车次查询" id="plan_construction_input_trainNbr">
				    </div> 
		          </div>
		        </div> 
					
					 &nbsp;&nbsp;&nbsp;<a type="button" class="btn btn-success" data-toggle="modal" data-target="#" id="plan_construction_createRunLine">生成运行线</a>
				 
		        <!--col-md-3 col-sm-4 col-xs-4-->
		      </form> 
		    </div> 
		     <div class="table-responsive" >
		          <table class="table table-bordered table-striped table-hover" id="plan_review_table_trainInfo"> 
				        <thead>
				        <tr>
				          <th style="width:25px" ></th>
		                  <th style="width:100px">车次</th>
		                  <th style="width:25px">始发局</th>
		                  <th style="width:150px">始发站</th>
		                  <th style="width:130px">始发时间</th>
		                  <th style="width:150px">终到站</th>
		                  <th style="width:130px">终到时间</th>  
		                 </tr>
				        </thead> 
				        <tbody data-bind="foreach: trains">
				             <tr data-bind="click: $parent.showTrainTimes">
				               <!-- data-bind="event:{change: $parent.checkboxSelect}, checked: selected" -->
							    <td align="center"><input type="checkbox" value="1" ></td>
								<td data-bind=" text: name"></td>
								<td data-bind="text: startBureau"></td>
								<td data-bind="text: startStn"></td>
								<td data-bind="text: sourceTime"></td>
								<td data-bind="text: endStn"></td>
								<td data-bind="text: targetTime"></td>
							 </tr> 
				        </tbody>
				      </table>
		        </div> 
		  </div> 
      </div> 
  <div class="pull-right" style="width:34%;"> 
    <!--分栏框开始-->
    <div id="plan_view_div_palnDayDetail" class="panel panel-default">
      <div class="panel-heading">
        <h3 class="panel-title" style="float: left;"> <i class="fa fa fa-folder-open"></i>生成运行线详情&nbsp;&nbsp;<label id="plan_view_div_palnDayDetail_title"></label></h3>
      </div>
      <!--panle-heading-->
      <div class="panel-body" style="padding:10px;">
      	<div class="table-responsive" > 
          <table class="table table-bordered table-striped table-hover" id="plan_review_table_trainLine">
		        <thead>
		        <tr >
		          <th style="width:25px">序号</th>
                  <th>站名</th>
                  <th style="width:40px">路局</th>
                  <th style="width:135px">到达时间</th>
                  <th style="width:135px">出发时间</th>
                  <th style="width:50px">停留时间</th>
                  <th style="width:20px">股道</th> 
                 </tr>
		        </thead>
		        <tbody data-bind="foreach: trainLines">
		           <tr>  
					<td data-bind=" text: $index"></td>
					<td data-bind="text: stnName"></td>
					<td data-bind="text: bureauShortName"></td>
					<td data-bind="text: sourceTime"></td>
					<td data-bind="text: targetTime"></td>
					<td data-bind="text: stepStr"></td>
					<td data-bind="text: trackName"></td>
		        	</tr>
		        </tbody>
		      </table>
        </div>  
        </div>  
      </div>
      <!--panel-body--> 
    </div>
    <!--分栏框结束--> 
  </div>
  <!--分栏框结束--> 
</div> 
</body>
</html>
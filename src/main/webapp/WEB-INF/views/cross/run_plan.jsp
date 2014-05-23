<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<% 
String basePath = request.getContextPath();
System.out.println(basePath);
%>
<!DOCTYPE HTML>
<html lang="en">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>核查编制图定开行</title>
<!-- Bootstrap core CSS -->
<link href="<%=basePath %>/assets/css/cross/custom-bootstrap.css" rel="stylesheet">
<!--font-awesome-->
<link href="<%=basePath %>/assets/css/datepicker.css" rel="stylesheet">
<link href="<%=basePath %>/assets/easyui/themes/default/easyui.css"
	rel="stylesheet">
<link href="<%=basePath %>/assets/easyui/themes/icon.css" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="<%=basePath %>/assets/css/font-awesome.min.css" />
 
<!-- Custom styles for this template --> 
<link href="<%=basePath %>/assets/css/cross/cross.css" rel="stylesheet">  
<link href="<%=basePath %>/assets/css/style.css" rel="stylesheet">

<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/html5.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/respond.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>

<script type="text/javascript" src="<%=basePath %>/assets/js/knockout.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.freezeheader.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/ajaxfileupload.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/cross/run_plan.js"></script>  
<script type="text/javascript" src="<%=basePath %>/assets/js/datepicker.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.gritter.min.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/common.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/knockout.pagemodle.js"></script> 
<script type="text/javascript">
var basePath = "<%=basePath %>";
</script>
<!--#include virtual="assets/js/trainplan/knockout.pagefooter.tpl"-->
 <style type="text/css">
.pagination > li > a, .pagination > li > span {
	position: relative;
	float: left; 
	line-height: 1.428571429;
	text-decoration: none;
	background-color: #ffffff;
	border: 1px solid #dddddd;
	margin-left: -1px;
}
 </style>

 
</head>
<body class="Iframe_body"  >
	
	<ol class="breadcrumb">
		<span><i class="fa fa-anchor"></i>当前位置:</span>
		<li><a href="#">车底交路维护</a></li>
	</ol> 
   	<div id="plan_view_div_palnDayDetail" class="panel panel-default">
	   <div class="row" style="margin: 10px 10px 10px 10px;"> 
	
	<!--分栏框开始-->
		<div class="pull-left" style="width: 27%;height:100%">
			<!--分栏框开始-->  
			    <div class="row" style="margin: 5px 10px 10px 10px;">
				    <section class="panel panel-default">
				        <div class="panel-heading"><i class="fa fa-table"></i>车底交路列表</div>
				        	<div class="panel-body">
								<form class="form-horizontal" role="form"> 
									<div class="row" style="margin-top: 5px;">
									        <div class="row"  style="margin-top: 5px;">
											    <div class="form-group" style="float: left; margin-left: 0px; margin-top: 0px;width: 100%">  
														<label for="exampleInputEmail3" class="control-label pull-left">
																		方案:&nbsp;</label> 
														<div class="pull-left">
															<select style="width: 273px" id="input_cross_chart_id"
																class="form-control" data-bind="options:searchModle().charts, value: searchModle().chart, optionsText: 'name', optionsCaption: ''">
															</select>
														</div>  
												</div> 
											</div>
											<div class="row"  style="margin-top: 5px;">
											    <label for="exampleInputEmail3" class="control-label pull-left">
													车次:&nbsp;</label>
												<div class="pull-left">
													<input type="text" class="form-control" style="width: 100px;"
														placeholder="车次" id="input_cross_filter_trainNbr" data-bind=" value: searchModle().trainNbr">
												</div>  
												<div class="pull-left" style="margin-left: 16px;">
													<a type="button" class="btn btn-success" data-toggle="modal"
														data-target="#" id="btn_cross_search"  data-bind="click: loadCrosses">查询</a>
												</div> 
											</div> 
									</div>
									<div class="row" >
										   <div class="pull-left" style="width: 60%;">
												<div class="table-responsive"> 
													<table class="table table-bordered table-striped table-hover" 
														id="cross_table_crossInfo">
														<thead>
															<tr style="height: 25px"> 
																<th style="width: 54%" align="center">车底交路名</th> 
															</tr>
														</thead>
														<tbody data-bind="foreach: crossRows.rows">
															<tr data-bind=" visible: visiableRow, style:{color: $parent.currentCross().crossId == crossId ? 'blue':''}" >
																<td data-bind=" text: $parent.crossRows.currentIndex()+$index()+1 , click: $parent.showTrains"></td>
															</tr> 
														</tbody>  					 
													</table>
												</div>
											</div>
											<div class="pull-right" style="width: 40%;">  
												<div class="table-responsive"> 
													<table class="table table-bordered table-striped table-hover"
														id="cross_trainInfo">
														<thead>
															<tr> 
																<th style="width: 100%">车次</th> 
															</tr>
														</thead> 
														<tbody data-bind="foreach: trains" >
															<tr  data-bind="click: $parent.setCurrentTrain, style:{color: $parent.currentTrain() != null && $parent.currentTrain().trainNbr == trainNbr ? 'blue':''}">
																<td style="width: 60px" data-bind="text: trainNbr, attr:{title: trainNbr}"></td>
															</tr>
														</tbody>
													</table> 
											</div>  
										</div> 
								  </div> 
							 </form>
						 </div>
				   </section>
				</div>   
		</div>
		 <div class="pull-right" style="width: 72%;"> 
		<!-- Nav tabs -->
			<ul class="nav nav-tabs" >
			  <li class="active"><a href="#home" data-toggle="tab">车底交路</a></li>
			  <li><a href="#profile" data-toggle="tab">交路基本信息</a></li> 
			</ul> 
			<!-- Tab panes -->
			<div class="tab-content" >
			  <div class="tab-pane active" id="home"> 
				
					
		  	</div>  
		  <div class="tab-pane" id="profile">
		      <div class="row" style="margin: 10px 10px 10px 10px;">
				   <section class="panel panel-default">
				        <div class="panel-heading"><i class="fa fa-table"></i>交路信息</div>
				          <div class="panel-body">
							<div class="row" >
								<form class="form-horizontal" role="form" data-bind="with: currentCross">  
								<div class="pull-left" id="left_div">
									<div class="row" style="margin: 0px 0 5px 0;"> 
											<label for="exampleInputEmail3"
												class="control-label pull-left"> 车底交路名:&nbsp;</label>
											<div class="pull-left" style="margin-left: 26px;">
												<input type="text" class="form-control" style="width: 470px;" data-bind="value: crossName"
													id="plan_construction_input_trainNbr">
											</div> 
											<label for="exampleInputEmail5" class="control-label pull-left" style="margin-left:40px">
												线路线型:</label> 
											<div class="pull-left">
												<input type="radio" class="pull-left" class="form-control" 
													style="width: 20px; margin-left: 5px; margin-top: 5px"
													class="form-control" data-bind="checked: highlineFlag" value="0">
											</div>
											<label for="exampleInputEmail5" class="control-label pull-left">
												普线</label> 
											<div class="pull-left">
												<input type="radio" class="pull-left" class="form-control" 
													style="width: 20px; margin-left: 5px; margin-top: 5px"
													class="form-control" value="1" data-bind="checked: highlineFlag">
											</div>
											<label for="exampleInputEmail5" class="control-label pull-left">
												高线</label> 
											<div class="pull-left">
												<input type="radio" class="pull-left" class="form-control" 
													style="width: 20px; margin-left: 5px; margin-top: 5px"
													class="form-control" value="2" data-bind="checked: highlineFlag" >
											</div>
											<label for="exampleInputEmail5" class="control-label pull-left">
												混合</label> 
									</div>
									<div class="row" style="margin: 5px 0 0px 0;">
									    <label for="exampleInputEmail3"
												class="control-label pull-left"> 备用套跑交路名:&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 470px;" data-bind="value: crossSpareName">
										</div> 
										<label for="exampleInputEmail5" style="margin-left: 40px;" class="control-label pull-left">
											开行状态:</label>
												
										<div class="pull-left">
											<input type="radio" class="pull-left" class="form-control"
												value="1" data-bind="checked: spareFlag"
												style="width: 20px; margin-left: 5px; margin-top: 5px"
												class="form-control">
										</div>
										<label for="exampleInputEmail5" class="control-label pull-left">
											开行</label>
	
										<div class="pull-left">
											<input type="radio" class="pull-left" class="form-control"
												value="2" data-bind="checked: spareFlag"
												style="width: 20px; margin-left: 5px; margin-top: 5px"
												class="form-control">
										</div>
										<label for="exampleInputEmail5" class="control-label pull-left">
											备用</label>
										<div class="pull-left">
											<input type="radio" class="pull-left" class="form-control"
												value="0" data-bind="checked: spareFlag"
												style="width: 20px; margin-left: 5px; margin-top: 5px"
												class="form-control">
										</div>
										<label for="exampleInputEmail5" class="control-label pull-left">
											停运</label> 
									</div>
									<div class="row" style="margin: 5px 0 0px 0;"> 
										<label for="exampleInputEmail3"
											class="control-label pull-left"> 组数:&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 40px;" data-bind="value: groupTotalNbr">
										</div> 
										<label for="exampleInputEmail2" style="margin-left: 23px;" class="control-label pull-left">对数:&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 40px;" data-bind="value: pairNbr">
										</div> 
										
										
											
										<div class="pull-left">
											<input type="checkBox" class="pull-left" class="form-control"
												value="1" data-bind="checked: cutOld"
												style="width: 20px; margin-left: 23px; margin-top: 5px"
												class="form-control">
										</div>
										<label for="exampleInputEmail5" class="control-label pull-left">
											截断原交路</label>
										
									</div>
									<div class="row" style="margin: 5px 0 0px 0;">
										<label for="exampleInputEmail5" class="control-label pull-left">
											普线开行规律:</label>
										<div class="pull-left">
											<input type="radio" class="pull-left" class="form-control"
												value="1" data-bind="checked: commonlineRule"
												style="width: 20px; margin-left: 5px; margin-top: 5px"
												class="form-control">
										</div>
										<label for="exampleInputEmail5" class="control-label pull-left">
											每日</label>
										<div class="pull-left">
											<input type="radio" class="pull-left" class="form-control"
												value="2" data-bind="checked: commonlineRule"
												style="width: 20px; margin-left: 5px; margin-top: 5px"
												class="form-control">
										</div>
										<label for="exampleInputEmail5" class="control-label pull-left">
											隔日</label>
										
										<label for="exampleInputEmail5" style="margin-left: 30px;" class="control-label pull-left">
											高线开行规律:</label>	
										<div class="pull-left">
											<input type="radio" class="pull-left" class="form-control"
												value="1" data-bind="checked: highlineRule"
												style="width: 20px; margin-left: 5px; margin-top: 5px"
												class="form-control">
										</div>
										<label for="exampleInputEmail5" class="control-label pull-left">
											日常</label>
										<div class="pull-left">
											<input type="radio" class="pull-left" class="form-control"
												value="2" data-bind="checked: highlineRule"
												style="width: 20px; margin-left: 5px; margin-top: 5px"
												class="form-control">
										</div>
										<label for="exampleInputEmail5" class="control-label pull-left">
											周末</label> 
										<div class="pull-left">
											<input type="radio" class="pull-left" class="form-control"
												value="3" data-bind="checked: highlineRule"
												style="width: 20px; margin-left: 5px; margin-top: 5px"
												class="form-control">
										</div>
										<label for="exampleInputEmail5" class="control-label pull-left">
											高峰</label>  
										<label for="exampleInputEmail5" style="margin-left: 30px;" class="control-label pull-left">
											指定星期:&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 71px;"
												placeholder=""   data-bind="value: appointWeek">
										</div>
										
										<label for="exampleInputEmail5"  style="margin-left: 10px;" class="control-label pull-left">
											指定日期:&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 140px;"
												placeholder=""  data-bind="value: appointDay">
										</div>
										 
										 
									</div>
									
									<div class="row" style="margin: 5px 0 0px 0;">
										<label class="control-label pull-left"> 车辆担当局:&nbsp;</label>
											<div class="pull-left">
												<select style="width: 50px" class="form-control" data-bind="options: $parent.gloabBureaus, value: tokenVehBureau, optionsText: 'shortName', optionsValue:'code' , optionsCaption: ''"></select>
												<!-- <input type="text" class="form-control" style="width: 30px;"  data-bind="value: tokenVehBureau"> -->
											</div>
										<label  class="control-label pull-left" style=" margin-left: 20px;"> 车辆段/动车段:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" style="width: 100px;" data-bind="value: tokenVehDept">
											</div>
										<label  class="control-label pull-left" style=" margin-left: 20px;" > 动车所:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" style="width: 100px;" data-bind="value: tokenVehDepot">
											</div>
										<label for="exampleInputEmail3"
												class="control-label pull-left" style=" margin-left: 30px;" > 客运担当局:&nbsp;</label>
										<div class="pull-left">
											<!-- <input type="text" class="form-control" style="width: 30px;" data-bind="value: tokenPsgDept"> -->
											
											<select style="width: 50px" class="form-control" data-bind="options: $parent.gloabBureaus, value: tokenPsgBureau, optionsText: 'shortName', optionsValue:'code', optionsCaption: ''"></select>
										</div>
										<label for="exampleInputEmail3"
											class="control-label pull-left" style=" margin-left: 20px;" > 客运段:&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 100px;" data-bind="value: tokenPsgDept">
										</div>
									</div>
									<div class="row" style="margin: 5px 0 0px 0;"> 
										<label for="exampleInputEmail3"
											class="control-label pull-left" > 运行区段:&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 200px;" data-bind="value: crossSection">
										</div>
										<label for="exampleInputEmail3" style=" margin-left: 20px;" 
												class="control-label pull-left"> 机车类型:&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 50px;" data-bind="value: locoType">
										</div>
										<label for="exampleInputEmail3"
												class="control-label pull-left" style=" margin-left: 13px;"  > 动车组车型:&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 80px;" data-bind="value: crhType">
										</div>
										<div class="pull-left">
											<input type="checkbox" class="pull-left" class="form-control"
												value="1" data-bind="checked: elecSupply"
												style="width: 20px; margin-left: 25px; margin-top: 5px"
												class="form-control">
										</div>
										<label for="exampleInputEmail5" class="control-label pull-left">
											供电</label>
										<div class="pull-left">
											<input type="checkbox" class="pull-left" class="form-control"
												value="1" data-bind="checked: dejCollect"
												style="width: 20px; margin-left: 5px; margin-top: 5px"
												class="form-control">
										</div>
										<label for="exampleInputEmail5" class="control-label pull-left">
											集便</label>
										<div class="pull-left">
											<input type="checkbox" class="pull-left" class="form-control"
												value="1" data-bind="checked: airCondition"
												style="width: 20px; margin-left: 5px; margin-top: 5px"
												class="form-control">
										</div>
										<label for="exampleInputEmail5" class="control-label pull-left">
											空调</label> 
									
									<!-- <div class="pull-left">
										<input type="checkBox" class="pull-left" class="form-control"
											name="exampleInputEmail5"
											style="width: 20px; margin-left: 10px; margin-top: 5px"
											class="form-control">
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left">
										是否切断久交路图</label> <label for="exampleInputEmail2"
										class="control-label pull-left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;调整:&nbsp;</label>
									<div class="pull-left">
										<input type="text" class="form-control" style="width: 50px;"
											placeholder="" id="plan_construction_selectdate">
									</div> -->
								</div>
								<div class="row" style="margin: 5px 0 0px 0;">
								    <label for="exampleInputEmail3"
											class="control-label pull-left" > 经由线:&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 214px;" data-bind="value: throughline">
										</div> 
									<label for="exampleInputEmail3"   style=" margin-left: 19px;" 
												class="control-label pull-left"> 备注:&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 400px;" data-bind="value: note">
										</div> 
										  <a type="button" style="margin-left: 15px"
											class="btn btn-success" data-toggle="modal" data-target="#"
											id="cross_train_save" data-bind="click: $parent.saveCrossInfo"> 保存</a>
									</div>  
								</div>
								<!--col-md-3 col-sm-4 col-xs-4-->
							</form>
						    </div>
						  </div>
					   </section>
					</div>
		  	 </div>
		  </div> 
		</div>
     </div>
   </div>
</body>  
 <script type="text/html" id="tablefooter-short-template"> 
  <table style="width:100%;height:20px;">
    <tr style="width:100%;height:20px;">
     <td style="width:60%;height:20px;">
  		<span class="pull-left">共<span data-bind="html: totalCount()"></span>条  当前<span data-bind="html: totalCount() > 0 ? (currentIndex() + 1) : '0'"></span>到<span data-bind="html: endIndex()"></span>条   共<span data-bind="text: pageCount()"></span>页</span> 								 
  	 </td>
     <td style="width:40%;height:20px;padding:0px;pading-bottom:-14">   
		<span data-bind="attr:{class:currentPage() == 0 ? 'disabed': ''}"><a style="cursor:pointer;background-color: #ffffff;border: 1px solid #dddddd;margin-right:-5px;padding:0px 5px;" data-bind="text:'<<', click: currentPage() == 0 ? null: loadPre"></a>
	    <input type="text"  style="padding-left:8px;margin-bottom:0px;padding-bottom:0;width:30px;height: 19px;background-color: #ffffff;border: 1px solid #dddddd;" data-bind="value: parseInt(currentPage())+1, event:{keyup: pageNbrChange}"/>
		<a style="cursor:pointer;background-color: #ffffff;border: 1px solid #dddddd;margin-left:-5px;padding:0px 5px;" data-bind="text:'>>', click: (currentPage() == pageCount()-1 || totalCount() == 0) ? null: loadNext"  style="padding:0px 5px;"></a>
       </ul> 
	 
     </td >
  </tr>
</table> 
</script> 
</html>

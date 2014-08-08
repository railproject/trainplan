<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="org.apache.shiro.SecurityUtils,org.railway.com.trainplan.service.ShiroRealm,java.util.List" %>
<% 
/* ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();

boolean isZgsUser = false;	//当前用户是否为总公司用户
if (user!=null && user.getBureau()==null) {
	isZgsUser = true;
} */
/* String currentUserBureau = "";
List<String> permissionList = "";
String userRolesString = "";
for(String p : permissionList){
	userRolesString += userRolesString.equals("") ? p : "," + p;
}  */
String basePath = request.getContextPath();
%>
<!DOCTYPE HTML>
<html lang="en">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>高铁交路计划审核</title>
<!-- Bootstrap core CSS -->

<!--font-awesome-->
<link href="<%=basePath %>/assets/css/datepicker.css" rel="stylesheet">
<link href="<%=basePath %>/assets/easyui/themes/default/easyui.css"
	rel="stylesheet">
<link href="<%=basePath %>/assets/easyui/themes/icon.css"
	rel="stylesheet">
<link href="<%=basePath %>/assets/css/cross/custom-bootstrap.css" rel="stylesheet">

<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>/assets/css/cross/custom-bootstrap.css">
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>/assets/css/font-awesome.min.css"/>
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>/assets/css/datepicker.css">
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>/assets/css/style.css"> 
	
<link href="<%=basePath %>/assets/easyui/themes/icon.css" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="<%=basePath %>/assets/css/font-awesome.min.css" />
 
<!-- Custom styles for this template --> 
<link href="<%=basePath %>/assets/css/cross/cross.css" rel="stylesheet">  
<link href="<%=basePath %>/assets/css/style.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>/assets/css/rightmenu.css">



   

<script type="text/javascript">
var basePath = "<%=basePath %>";
var all_role = "";
var _isZgsUser = true;//当前用户是否为总公司用户
var currentUserBureau = "";
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
.ckbox.disabled{
	cursor: not-allowed;
	pointer-events: none;
	opacity: 0.65;
	filter: alpha(opacity=65);
	-webkit-box-shadow: none;
	box-shadow: none;
}

 </style>

 
</head>
<body id="body_highline" class="Iframe_body"  >
	
	<ol class="breadcrumb">
		<span><i class="fa fa-anchor"></i>当前位置:</span>
		<li><a href="#">发布计划 -> 交路计划 -> 高铁交路计划审核</a></li>
	</ol>  
	 <div id="div_searchForm" class="row" style="margin:15px 0 10px 0;"> 
	    <form class="form-horizontal" role="form">
	   	<div class="row" style="margin:15px 0 10px 0;">
		      <div class="pull-left"> 
		        <div class="pull-left"> 
					 <label for="exampleInputEmail3" class="control-label pull-left" style="margin-left: 15px;">
														日期:&nbsp;</label> 
					 <input type="text" class="form-control" style="width:75px;" placeholder="" id="runplan_input_startDate"  name="startDate" data-bind="value: searchModle().planStartDate" />
				 </div>
				 <div class="pull-left" style="margin-left:10px"> 
					 <label for="exampleInputEmail5" style="vertical-align: bottom;">铁路线:&nbsp;</label> 
					 <select class="form-control" style="width: 100px;display:inline-block;" id="input_cross_filter_showFlag"
							 data-bind="options: searchModle().throughLines, value: searchModle().searchThroughLine, optionsText: 'name', optionsValue: 'code', optionsCaption: ''">
					 </select>  
				  </div>
				   <div class="pull-left" style="margin-left:10px"> 
					 <label for="exampleInputEmail5" style="vertical-align: bottom;">担当局:&nbsp;</label> 
					 <select class="form-control" style="width: 56px;display:inline-block;" id="input_cross_filter_showFlag"
							 data-bind="options: searchModle().bureaus, value: searchModle().bureau, optionsText: 'shortName', optionsValue: 'code', optionsCaption: ''">
					 </select> 
				  </div>
				   <div class="pull-left" style="margin-left:10px"> 
					 <label for="exampleInputEmail5" style="vertical-align: bottom;">动车所:&nbsp;</label> 
					 <select class="form-control" style="width: 130px;display:inline-block;" id="input_cross_filter_showFlag"
							 data-bind="options: searchModle().tokenVehDepots, value: searchModle().searchTokenVehDepot, optionsText: 'name', optionsValue: 'name' , optionsCaption: ''">
					 </select> 
				  </div>
				  <div class="pull-left"> 
					 <label for="exampleInputEmail3" class="control-label pull-left" style="margin-left: 15px;">
														车次:&nbsp;</label> 
					 <input type="text" class="form-control" style="width:75px;" placeholder="" />
				 </div>
				  <a type="button"  style="margin-left:5px" class="btn btn-success" data-toggle="modal" data-target="#"  data-bind="click: loadCrosses">查询</a>
			 </div>
		 </div>
		 
		 <div class="row" style="margin:5px 0 0px 5;"> 
	        <div class="pull-left">   
			  <a type="button"  style="margin-left:15px" class="btn btn-success" data-bind="click: createHighLineCrosses">加载</a>
			  <a type="button"  style="margin-left:5px" class="btn btn-success" data-bind="click: deleteAllHighLineCrosses">清空</a>
			  <a type="button"  style="margin-left:5px" class="btn btn-success" data-bind="click: showActiveHighLineCrossDlg">调整</a>
			  <a type="button"  style="margin-left:5px" class="btn btn-success" data-bind="click: submitHighLineWithRole">提交</a>
			  <a type="button"  style="margin-left:5px" class="btn btn-success" data-bind="click: createCmdInfo">生成命令</a>
			</div>
			<div class="pull-right" style="margin-right:10px;">
				<input type="checkbox" class="pull-left" class="form-control" data-bind="checked: isShowCrossDetailInfo, event:{change: showRunPlans}"> 
				<label for="exampleInputEmail5" class="control-label pull-left">显示交路详情</label>  
		 	</div>
	    </div>
	    </form> 
	  </div>  
	    <div class="row" style="margin: 10px 10px 10px 10px;">   
		    <!--分栏框开始-->
		    <div id="div_hightline_planDayDetail" style="height:300px" class="panel panel-default">
				     <div class="row" style="margin:15px 10px 10px 10px;overflow-y:auto"> 
					     <div class="table-responsive" >
					          <table class="table table-bordered table-striped table-hover" style="margin-left:5px; margin-right:5px; width:98%">
									<thead>
										<tr> 
											<!-- <th style="width: 33px" align="center"><input type="checkbox" style="margin-top:0" value="1" data-bind="checked: crossAllcheckBox, event:{change: selectCrosses}"></th> -->
											<th style="width: 25px" align="center">序号</th> 
											<th style="width: 100px" align="center">铁路线</th>
											<th style="width: 70px" align="center">首车日期</th>  
											<th style="width: 180px" align="center">  
											    <label for="exampleInputEmail5" style="font-weight: bold;vertical-align: bottom;">交路</label> 
												<select class="form-control" style="width: 56px;display:inline-block;" id="input_cross_filter_showFlag"
													 data-bind="options: [{'code': 2, 'text': '全称'},{'code': 1, 'text': '简称'}], value: searchModle().shortNameFlag, optionsText: 'text', optionsValue: 'code'">
												</select>
											</th>
											<th style="width: 100px" align="center">车型</th>
											<th style="width: 100px" align="center">车底1</th>
											<th style="width: 100px" align="center">车底2</th>
											<th style="width: 100px" align="center">出库所/始发站</th>  
											<th style="width: 100px" align="center">入库所/终到站</th>
											<th style="width: 30px" align="center">热备</th>
											<th style="width: 55px" align="center">担当局</th>
											<th style="width: 100px" align="center">动车所</th>
											<th style="width: 100px" align="center">管辖动车台</th>
											<th style="width: 100px" align="center">来源</th> 
										</tr>
									</thead>
									<tbody data-bind="foreach: highLineCrossRows">
										<tr data-bind=" visible: visiableRow, style:{color: $parent.currentCross().highLineCrossId == highLineCrossId ? 'blue':''}" >
											<!-- <td align="center"><input type="checkbox" value="1" data-bind="event:{change: $parent.selectCross}, checked: selected"></td> -->
									        <td data-bind="text: ($index() + 1), click: $parent.showTrains"></td> 
									        <td data-bind="text: throughline, click: $parent.showTrains"></td> 
									        <td data-bind="text: crossStartDate, click: $parent.showTrains"></td> 
										    <td data-bind="text: $parent.searchModle().shortNameFlag() == 1 ? shortName : crossName, attr:{title: crossName}, click: $parent.showTrains" ></td>
										    <td data-bind="text: crhType, click: $parent.showTrains"></td>
										    <td data-bind="text: vehicle1, click: $parent.showTrains"></td>
										    <td data-bind="text: vehicle2, click: $parent.showTrains"></td>       
										    <td data-bind="text: startStn, click: $parent.showTrains"></td>
										    <td data-bind="text: endStn, click: $parent.showTrains"></td> 
										    <td data-bind="text: (spareFlag() == 2 ? '是' : '否'), click: $parent.showTrains"></td>
										    <td data-bind="text: tokenVehBureauShowValue, click: $parent.showTrains"></td>
										    <td data-bind="text: tokenPsgDept, click: $parent.showTrains"></td>
										    <td data-bind="text: postName, click: $parent.showTrains"></td>
										    <td data-bind="text: note, click: $parent.showTrains"></td>
										</tr>
									</tbody>
								</table>
								<div data-bind="template: { name: 'tablefooter-short-template'}" style="margin-bottom: 5px"></div>
					        </div> 
				       </div>
				  </div>  
	    </div>
	   <div id="div_crossDetailInfo" class="row" style="margin: 10px 10px 10px 10px;">  
	        <!--分栏框开始-->
		    <div class="pull-left" style="width: 39.7%;height:100%">
			<!--分栏框开始-->   
			         <div class="panel panel-default"> 
				       <div class="panel-body"> 
							<table class="table table-bordered table-striped table-hover" style="margin-left:2px; margin-right:5px;">
								<thead>
									<tr style="height: 26px">
										<th class="text-center" style="vertical-align: middle;width:20px" rowspan="2">序号</th>
										<th class="text-center" style="vertical-align: middle;width:100px" rowspan="2">车次</th>
										<th class="text-center" style="vertical-align: middle;width:20px" colspan="3">始发</th>
										<th class="text-center" style="vertical-align: middle;width:20px" colspan="3">终到</th>
									</tr>
									<tr style="height: 26px"> 
										<th style="width: 30px" align="center">局</th> 
										<th style="width: 120px" align="center">站名</th> 
										<th style="width: 70px" align="center">时间</th>
										<th style="width: 30px" align="center">局</th> 
										<th style="width: 120px" align="center">站名</th> 
										<th style="width: 70px" align="center">时间</th>
									</tr>
								</thead>
								<tbody data-bind="foreach: trains" >
									<tr  data-bind="click: $parent.showTrainTimes, style:{color: $parent.currentTrain() != null && $parent.currentTrain().trainNbr == trainNbr ? 'blue':''}">
										<td style="width: 36px" data-bind="text: ($index() + 1)"></td>
										<td style="width: 100px" data-bind="text: trainNbr, attr:{title: trainNbr}"></td>
										
										<td style="width: 30px" data-bind="text: startStnBureau, attr:{title: startStnBureau}"></td>
										<td style="width: 120px" data-bind="text: startStn, attr:{title: startStn}"></td>
										<td style="width: 70px" data-bind="text: startTime, attr:{title: startTime}"></td>
										
										
										<td style="width: 30px" data-bind="text: endStnBureau, attr:{title: endStnBureau}"></td>
										<td style="width: 120px" data-bind="text: endStn, attr:{title: endStn}"></td>
										<td style="width: 70px" data-bind="text: endTime, attr:{title: endTime}"></td>
									</tr>
								</tbody>
							</table>   
					 </div>  
					 </div>
					</div>
			 
		        <div class="pull-right" style="width: 59.7%;">
		        <div class="panel panel-default"> 
					 <div class="panel-body" >
					      	<div class="row" style="margin:5px 0 10px 0;">
						      <form class="form-inline" role="form">
					              <div class="row" style="margin:5px 0 10px 50px;">
						         	  <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_refresh"><i class="fa fa-refresh"></i>刷新</button>
						              <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_x_magnification"><i class="fa fa-search-plus"></i>X+</button>
						              <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_x_shrink"><i class="fa fa-search-minus"></i>X-</button>
						              <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_y_magnification"><i class="fa fa-search-plus"></i>Y+</button>
						              <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_y_shrink"><i class="fa fa-search-minus"></i>Y-</button>
						                                                 比例：｛X:<label id="canvas_event_label_xscale">1</label>倍；Y:<label id="canvas_event_label_yscale">1</label>倍｝
									  <span><input type="checkbox" id="canvas_checkbox_stationType_jt" name="canvas_checkbox_stationType" checked="checked" style="margin-left:10px">简图</span>
						         	  <input type="checkbox" id="canvas_checkbox_trainTime" style="margin-left:10px;margin-top:2px"  value=""/>时刻
						         	   &nbsp;&nbsp;车底：<select id="canvas_select_groupSerialNbr"></select>
						                       
						         </div>
					          </form>
						    </div> 
					        <div id="canvas_parent_div" class="table-responsive" style="width:100%;height:200px;overflow-x:auto; overflow-y:auto;">
					        	<canvas id="canvas_event_getvalue"></canvas>
					        </div> 
					      </div> 
				</div> 
			</div>
		</div>  
	  <!--详情时刻表--> 
	 <div id="run_plan_train_times" class="easyui-dialog" title="时刻表"
		data-options="iconCls:'icon-save'"
		style="width: 608px; height: 500px; padding: 10px;"> 
			      <!--panle-heading-->
			      <div class="panel-body" style="padding:10px;margin-right:10px;">
				       <ul class="nav nav-tabs" >
						  <li class="active"><a style="padding:3px 10px;" href="#simpleTimes" data-toggle="tab">简点</a></li> 
						  <li><a style="padding:3px 10px;" href="#allTimes" data-toggle="tab">详点</a></li> 
						  <li style="float:right" ><span style="font: -webkit-small-control;" data-bind="html: currentTrainInfoMessage()"></span></li>
						</ul> 
						<!-- Tab panes -->
						<div class="tab-content" >
						  <div class="tab-pane active" id="simpleTimes" > 
					      	<div class="table-responsive" > 
					            <table class="table table-bordered table-striped table-hover" id="plan_runline_table_trainLine">
							        <thead> 
							         <tr>
							          <th style="width:7.5%">序号</th>
					                  <th style="width:19%">站名</th>
					                  <th style="width:7%">路局</th>
					                  <th style="width:14.5%">到达</th>
					                  <th style="width:14.5%">出发</th>
					                  <th style="width:14%">停时</th>
					                  <th style="width:10%">天数</th> 
					                  <th style="width:15%" colspan="2">股道</th>  
					                 </tr>
							        </thead>
							        <tbody style="padding:0">
										 <tr style="padding:0">
										   <td colspan="9" style="padding:0">
												 <div id="simpleTimes_table" style="height: 400px; overflow-y:auto;"> 
													<table class="table table-bordered table-striped table-hover" >
														 <tbody data-bind="foreach: simpleTimes">
												           <tr data-bind="visible: stationFlag != 'BTZ'">  
															<td style="width:7.5%" align="center" data-bind=" text: $index() + 1"></td>
															<td style="width:19%" data-bind="text: stnName, attr:{title: stnName}"></td>
															<td style="width:7.5%" align="center" data-bind="text: bureauShortName"></td>
															<td style="width:14.3%" align="center" data-bind="text: sourceTime"></td>
															<td style="width:14.3%" align="center" data-bind="text: targetTime"></td>
															<td style="width:14%" align="center" data-bind="text: stepStr"></td>
															<td style="width:10%" align="center" data-bind="text: runDays"></td>
															<td style="width:10%" align="center" data-bind="text: trackName"></td>
												        	</tr>
												        </tbody>
													</table> 
											 	</div>
											</td>
										</tr>
									</tbody> 
						        </table>
			        		</div>   
			        	</div>
			        	<div class="tab-pane" id="allTimes" > 
					      	<div class="table-responsive" > 
					            <table class="table table-bordered table-striped table-hover" id="plan_runline_table_trainLine">
							        <thead> 
							         <tr>
							          <th style="width:7.5%">序号</th>
					                  <th style="width:19%">站名</th>
					                  <th style="width:7%">路局</th>
					                  <th style="width:14.5%">到达</th>
					                  <th style="width:14.5%">出发</th>
					                  <th style="width:14%">停时</th>
					                  <th style="width:10%">天数</th> 
					                  <th style="width:15%" colspan="2">股道</th>  
					                 </tr> 
							        </thead>
							        <tbody style="padding:0">
										 <tr style="padding:0">
										   <td colspan="9" style="padding:0">
												 <div id="allTimes_table" style="height: 400px; overflow-y:auto;"> 
													<table class="table table-bordered table-striped table-hover" > 
														 <tbody data-bind="foreach: times">
												           <tr>  
															<td style="width:7.5%" align="center" data-bind=" text: $index() + 1"></td>
															<td style="width:19%" data-bind="text: stnName, attr:{title: stnName}"></td>
															<td style="width:7.5%" align="center" data-bind="text: bureauShortName"></td>
															<td style="width:14.3%" align="center" data-bind="text: sourceTime"></td>
															<td style="width:14.3%" align="center" data-bind="text: targetTime"></td>
															<td style="width:14%" align="center" data-bind="text: stepStr"></td>
															<td style="width:10%" align="center" data-bind="text: runDays"></td>
															<td style="width:10%" align="center" data-bind="text: trackName"></td>
												        	</tr>
												        </tbody>
													</table> 
											 	</div>
											</td>
										</tr>
									</tbody> 
						        </table>
			        		</div>   
			        	</div>
			        </div>
      		</div>
	   </div> 
	    <div id="active_highLine_cross_dialog" class="easyui-dialog"
	      title="交路调整"
		  data-options="iconCls:'icon-save'"
		   style="width: 1200px; height: 500px; padding: 10px;" >  
		      <div class="row" style="width:100%;height:90%"> 
		    	 <div class="pull-left" style="width: 27%;height:100%" >
				<!--分栏框开始-->    
					   <section class="panel panel-default" style="height:100%">
				        <div class="panel-heading">
				        	<span>
				              <i class="fa fa-table"></i>交路列表
						   </span>
						</div> 
				          <div class="panel-body" style="height:93%">
					         <div class="row" style="width:100%;height:95%"> 
						        	<select multiple="multiple" id="current_highLineCrosses" style="width:100%;height:100%" data-bind="options: highLineCrossRows, optionsText: 'crossName', selectedOptions:selectedHighLineCrossRows" >
						        	 
						        	</select>
							  </div>
							  <div class="row" style="margin-top:5px"> 
							    <a type="button"  style="margin-left:5px" class="btn btn-success" data-bind="click: deleteHighLineCrosses">删除</a>
							  </div>
						</div>
						</section>
						
				</div>
				<div class="pull-left" style="width: 25%;height:100%" >
				<!--分栏框开始-->    
				  <section class="panel panel-default" style="height:100%">
				        <div class="panel-heading">
				        	<span>
				              <i class="fa fa-table"></i>交路组合列表
						   </span>
						</div> 
				          <div class="panel-body" style="height:93%">
				             <div class="row" style="width:100%;height:95%"> 
						          <div class="pull-left" style="width:85%;height:100%"> 
							        	<select multiple="multiple" style="width:100%;height:100%" data-bind="options: acvtiveHighLineCrosses, optionsText: 'crossName', selectedOptions: selectedActiveHighLineCrossRows, event:{change: selectedActiveHighLineCrossChange} "></select>
								  </div>
								  <div class="pull-left" style="width:15%;height:100%"> 
								          <div style="height:40%;" > 
									      </div> 
									      <div style="height:60%;" > 
									        <a data-bind="click: activeCrossUp" style="margin-left: 2px"><i class="fa fa-arrow-up"></i>上</a><!-- fa fa-chevron-up blue -->
									        <p>&nbsp;</p>
									        <a data-bind="click: activeCrossDown" style="margin-left:2px;"><i class="fa fa-arrow-down"></i>下</a><!-- fa fa-chevron-down blue -->
									      </div> 
								  </div> 
							 </div>
							  <div class="row" style="margin-top:5px"> 
							    <a type="button"  style="margin-left:5px" class="btn btn-success" data-toggle="modal" data-target="#"  data-bind="click: cjHighLineCross">拆解</a>
								<a type="button"  style="margin-left:5px" class="btn btn-success" data-toggle="modal" data-target="#" data-bind="click: hbHighLineCrossConfirm">组合</a>
							  </div> 
						</div>
					</section>
				</div>
				<div class="pull-left" style="width: 45%;height:100%" >
				<!--分栏框开始-->    
				   <section class="panel panel-default" style="height:100%">
				        <div class="panel-heading">
				        	<span>
				              <i class="fa fa-table"></i>时刻点单
						   </span>
						</div> 
				          <div class="panel-body" style="height:90%"> 
					        	<table class="table table-bordered table-striped table-hover" id="plan_runline_table_trainLine">
							        <thead> 
							         <tr>
							          <th style="width:7%">站序</th>
					                  <th style="width:19%">站名</th> 
					                  <th style="width:10%">停时</th>
					                  <th style="width:20%">到达时间</th> 
					                  <th style="width:20%">出发时间</th>  
					                 </tr> 
							        </thead>
							        <tbody style="padding:0">
										 <tr style="padding:0">
										   <td colspan="9" style="padding:0">
												 <div id="allTimes_table" style="height: 360px; overflow-y:auto;"> 
													<table class="table table-bordered table-striped table-hover" > 
														 <tbody data-bind="foreach: activeTimes">
												           <tr>  
															<td style="width:7.5%" align="center" data-bind=" text: stnSort"></td>
															<td style="width:20%" data-bind="text: stnName, attr:{title: stnName}"></td>
															<td style="width:10%" align="center" data-bind="text: stepStr"></td> 
															<td style="width:20%" align="center" data-bind="text: sourceTime"></td>
															<td style="width:17%" align="center" data-bind="text: targetTime"></td>
												        	</tr>
												        </tbody>
													</table> 
											 	</div>
											</td>
										</tr>
									</tbody> 
						        </table>
						  </div> 
						</section>
				</div>
			</div>
			<div class="row" style="width:100%;" align="center">  
			   <div class="panel panel-default" style="padding-top:10px;padding-bottom:5px"> 
			      <a type="button"  style="margin-left:5px" class="btn btn-success" data-toggle="modal" data-target="#"  data-bind="click: submitHighLineCross">确定交路</a>
		        </div>
		     </div>
	    </div> 
	    <div id="hb_highLine_cross" class="easyui-dialog" title="合并交路"
			data-options="iconCls:'icon-save'"
			style="width: 400px; height: 280px; padding: 10px"> 
				 <div class="row" style="width: 100%; margin-top: 5px;margin-left:20px">
						<label for="exampleInputEmail3" class="control-label pull-left" >
										动车台:&nbsp;</label> 
						<div class="pull-left">
							<select style="width: 273px" id="input_cross_chart_id"
								class="form-control" data-bind="options:searchModle().accs, value: searchModle().acc, optionsText: 'name', optionsValue:'code', optionsCaption: ''">
							</select>
						</div>   
				 </div> 
				 <div class="row" style="width: 100%; margin-top: 5px;margin-left:20px">
						<label for="exampleInputEmail3" class="control-label pull-left">
										动车所:&nbsp;</label> 
						<div class="pull-left">
							<select style="width: 273px" id="input_cross_chart_id"
								class="form-control" data-bind="options:searchModle().tokenVehDepots, value: searchModle().tokenVehDepot, optionsText: 'name', optionsValue: 'code', optionsCaption: ''">
							</select>
						</div>   
				 </div>
				  <div class="row" style="width: 100%; margin-top: 5px;margin-left:20px">
						<label for="exampleInputEmail3" class="control-label pull-left">
										铁路线:&nbsp;</label> 
						<div class="pull-left">
							<select style="width: 273px" id="input_cross_chart_id"
								class="form-control" data-bind="options:searchModle().throughLines, value: searchModle().throughLine, optionsText: 'name',optionsValue:'code', optionsCaption: ''">
							</select>
						</div>   
				 </div>
				 <div class="row" style="width: 100%; margin-top: 5px;margin-left:20px">
						<label for="exampleInputEmail3" class="control-label pull-left">
										客运担当局:&nbsp;</label> 
						<div class="pull-left">
							<select style="width: 60px" id="input_cross_chart_id"
								class="form-control" data-bind="options:searchModle().tokenPsgBureaus, value: searchModle().tokenPsgBureau, optionsText: 'shortName',optionsValue:'code', optionsCaption: ''">
							</select>
						</div>
						<label for="exampleInputEmail3" style="margin-left:20px" class="control-label pull-left">
										 动车组车型:&nbsp;</label>
						<div class="pull-left">
							<select style="width: 100px" id="input_cross_chart_id"
								class="form-control" data-bind="options:searchModle().crhTypes, value: searchModle().crhType, optionsText: 'name',optionsValue:'code', optionsCaption: ''">
							</select>
						</div>   
				 </div>  
				  
				 <div class="row" style="width: 100%; margin-top: 5px;margin-left:20px">
						<label for="exampleInputEmail3" class="control-label pull-left">
										担当客运段:&nbsp;</label> 
						<div class="pull-left">
							<input class="form-control"  style="width:247px;" placeholder="" data-bind="value: searchModle().tokenPsgDept">
						</div>   
				 </div>   
				 <div class="row" style="margin: 10px 0 5px 0;margin-left:20px">
					<label for="exampleInputEmail2" class="control-label pull-left">来源:&nbsp;</label>
			        <div class="pull-left">
			           <input class="form-control"  style="width:283px;" placeholder="" data-bind="value: searchModle().createReason">
			        </div> 
			   </div> 
			   <div  class="row" style="width: 100%; margin-top: 10px;" align="center">
				     <a type="button" id="btn_fileToUpload"
						class="btn btn-success" data-toggle="modal" data-target="#" data-bind="click: $root.hbHighLineCrossYes">确定</a>
					 <a type="button" id="btn_fileToUpload"
						class="btn btn-success" data-toggle="modal" data-target="#" data-bind="click: $root.hbHighLineCrossCancel">取消</a>
				</div> 
		</div>  
</body>  
 <script type="text/html" id="tablefooter-short-template"> 
  <table style="width:100%;height:20px;">
    <tr style="width:100%;height:20px;">
     <td style="width:60%;height:20px;">
  		<span class="pull-left"><span data-bind="html: $root.currentTotalCount()"></span></span>
  	 </td>
     <td style="width:40%;height:20px;padding:0px;pading-bottom:-14">   
		  
     </td >
  </tr>
</table> 
</script> 


<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/html5.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/respond.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>

<script type="text/javascript" src="<%=basePath %>/assets/js/knockout.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.freezeheader.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/ajaxfileupload.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/knockout.pagemodle.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/highLine/highLine_cross.js"></script>  
<script type="text/javascript" src="<%=basePath %>/assets/js/datepicker.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.gritter.min.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/common.js"></script> 
<script type="text/javascript" src="<%=basePath%>/assets/js/respond.min.js"></script>
<script src="<%=basePath %>/assets/js/moment.min.js"></script>
<script src="<%=basePath %>/assets/lib/fishcomponent.js"></script>
<%-- <script type="text/javascript" src="<%=basePath%>/assets/js/trainplan/common.security.js"></script> --%> 

<script src="<%=basePath %>/assets/js/trainplan/util/fishcomponent.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/util/canvas.util.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/util/canvas.component.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/runPlan/canvas_rightmenu.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/runPlan/canvas_event_getvalue.js"></script>
<script type="text/javascript">
var basePath = "<%=basePath %>";
</script>

<script  type="text/html" id="runPlanTableDateHeader"> 
    <!-- ko if: $index() == 0 -->
 	<td></td>
 	<!-- /ko --> 
 	<td align='center' data-bind="text: day"></td>
</script>
<script  type="text/html" id="runPlanTableVlaues">
 <tr data-bind="foreach: runPlans">
    <!-- ko if: $index() == 0 --> 
 	<td data-bind="text: $parent.trainNbr"></td>
 	<!-- /ko -->  
 	<td  align='center' data-bind="text: runFlag, style:{'color': color}"></td>
 </tr> 
</script>
</html>

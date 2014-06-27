<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="org.apache.shiro.SecurityUtils,org.railway.com.trainplan.service.ShiroRealm,java.util.List" %>
<% 
	ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal(); 
	List<String> permissionList = user.getPermissionList();
	String userRolesString = "";
	for(String p : permissionList){
		userRolesString += userRolesString.equals("") ? p : "," + p;
	} 
	String basePath = request.getContextPath();
	System.out.println(basePath);
%>
<!DOCTYPE HTML>
<html lang="en">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>交路单元管理</title>
<!-- Bootstrap core CSS -->

<!--font-awesome-->
<link href="<%=basePath %>/assets/css/datepicker.css" rel="stylesheet">
<link href="<%=basePath %>/assets/easyui/themes/default/easyui.css"
	rel="stylesheet">
<link href="<%=basePath %>/assets/css/cross/custom-bootstrap.css" rel="stylesheet">
<link href="<%=basePath %>/assets/css/cross/cross.css" rel="stylesheet"> 
<link href="<%=basePath %>/assets/easyui/themes/icon.css" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="<%=basePath %>/assets/css/font-awesome.min.css" /> 
<!-- Custom styles for this template --> 
 
<link href="<%=basePath %>/assets/css/style.css" rel="stylesheet"> 
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/html5.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/respond.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>

<script type="text/javascript" src="<%=basePath %>/assets/js/knockout.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.freezeheader.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/ajaxfileupload.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/cross/crossunit.js"></script>  
<script type="text/javascript" src="<%=basePath %>/assets/js/datepicker.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.gritter.min.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/common.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/knockout.pagemodle.js"></script> 
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
.Iframe_body {
   padding: 12px 2%;
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
<script type="text/javascript">
var basePath = "<%=basePath %>";
var all_role = "<%=userRolesString %>";
console.log(all_role);
</script>
 

</head>
<body class="Iframe_body"  >
	
	<ol class="breadcrumb">
		<span><i class="fa fa-anchor"></i>当前位置:</span>
		<li><a href="#">交路单元管理</a></li>
	</ol>  
	<!--分栏框开始-->
	<div class="pull-left" style="width: 30%;">
		<!--分栏框开始-->  
			<div class="row" style="margin: 0px 10px 10px 10px;">  
									<div class="form-group"
										style="float: left; margin-left: 0px; margin-top: 0px;width: 100%"> 
									  <div class="row" style="width: 100%;" >
											<label for="exampleInputEmail3" class="control-label pull-left">
															方案:&nbsp;</label> 
											<div class="pull-left">
												<select style="width: 320px" id="input_cross_chart_id"
													class="form-control" data-bind="options:searchModle().charts, value: searchModle().chart, optionsText: 'name'">
												</select>
											</div>
										
									   </div>  
										<div class="row"  style="width: 100%; margin-top: 5px;">
											<label for="exampleInputEmail3" class="control-label pull-left" >
												车辆担当局:</label>
											<div class="pull-left" style="margin-left: 5px;">
												<select style="width:55px" class="form-control" data-bind="options:searchModle().bureaus, value: searchModle().bureau, optionsText: 'shortName', optionsValue:'code', optionsCaption: '' ,event:{change: bureauChange}"></select>
											</div>
											<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left: 15px;">
												始发路局:</label>
											<div class="pull-left" style="margin-left: 5px; ">
												<select style="width: 50px" class="form-control" data-bind="options:searchModle().startBureaus, value: searchModle().startBureau, optionsText: 'shortName', optionsValue:'code', optionsCaption: ''"></select>
											</div> 
											<input type="checkBox" class="pull-left" class="form-control"
												value="1" data-bind="checked: searchModle().currentBureanFlag"
												style="width: 20px; margin-top: 5px; margin-left:15px"
												class="form-control"> 
											<label for="exampleInputEmail5" class="control-label pull-left">
												本局相关</label> 
										</div>    
										<div class="row"  style="margin-top: 5px;">
											<label for="exampleInputEmail3" class="control-label pull-left" >
												铁路线类型:</label>
											<div class="pull-left" style="margin-left: 5px;">
											    <select  style="width:55px" class="form-control" data-bind="options: searchModle().highlingFlags, value: searchModle().highlingFlag, optionsText: 'text' , optionsCaption: ''"></select>
											</div>
											 <label for="exampleInputEmail3" class="control-label pull-left" style="margin-left: 15px;">
												 审核状态:</label>
											<div class="pull-left" style="margin-left: 5px;">
												<select style="width:50px" id="input_cross_sure_flag"
													class="form-control" data-bind="options: searchModle().checkFlags, value: searchModle().checkFlag, optionsText: 'text' , optionsCaption: ''">
												</select>
											</div>
											<!-- <a type="button" class="btn btn-success" data-toggle="modal" style="margin-left: 30px;"
														data-target="#" id="btn_cross_search"  data-bind="click: showUploadDlg">导入</a>  -->
											
										</div>  
										
										<div class="row"  style="margin-top: 5px;">
										    <label for="exampleInputEmail3" class="control-label pull-left">
												车次:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" style="width: 95px;"
											 		 id="input_cross_filter_trainNbr" data-bind=" value: searchModle().filterTrainNbr, event:{keyup: trainNbrChange}">
											</div> 
											 <label for="exampleInputEmail3" class="control-label pull-left" style="margin-left: 15px;" >
												生成状态:</label>
											<div class="pull-left" style="margin-left: 5px;">
												<select style="width:50px" id="input_cross_sure_flag"
													class="form-control" data-bind="options: searchModle().unitCreateFlags, value: searchModle().unitCreateFlag, optionsText: 'text' , optionsCaption: '' ">
												</select>
											</div>
												<a type="button" class="btn btn-success" data-toggle="modal" style="margin-left: 30px;"
														data-target="#" id="btn_cross_search"  data-bind="click: loadCrosses">查询</a>   
											
										</div>   
									</div>
								</div>
								<div class="row" >
									<div class="panel panel-default"> 
									<div class="table-responsive">
										<div class="form-group"
											style="margin-left: 20px;margin-top:10px">
											<a type="button" data-bind="attr:{class: searchModle().activeFlag() == 1 ? 'btn btn-success' : 'btn btn-success disabled'}, click: checkCrossInfo"  data-toggle="modal"
												data-target="#" id="btn_cross_sure">审核</a>
											<a  type="button" class="btn btn-success" data-toggle="modal"
												data-target="#" id="btn_cross_delete" style="margin-left: 2px;" data-bind="attr:{class: searchModle().activeFlag() == 1 ? 'btn btn-success' : 'btn btn-success disabled'}, click: deleteCrosses">删除</a>
											<a  type="button" class="btn btn-success" data-toggle="modal" style="margin-left: 2px;" 
												data-target="#" id="btn_cross_createCrossUnit" data-bind="attr:{class: searchModle().activeFlag() == 1 ? 'btn btn-success' : 'btn btn-success disabled'}, click: createUnitCrossInfo">生成车底</a>
											
										</div> 
										<span style="margin-bottom:5px;margin-left:5px;" data-bind="html: currentCross().relevantBureauShowValue"></span> 
										<table class="table table-bordered table-striped table-hover" style="margin-left:5px; margin-right:5px; width:98%"
												id="cross_table_crossInfo">
												<thead>
													<tr style="height: 25px"> 
														<th style="width: 10%" align="center"><input type="checkbox" style="margin-top:0" value="1" data-bind="checked: crossAllcheckBox, event:{change: selectCrosses}"></th>
														<th style="width: 8%" align="center">序号</th>
														<th style="width: 8%" align="center">局</th>
														<th style="width: 51%;" align="center">  
															    <label for="exampleInputEmail5" style="font-weight: bold;vertical-align: bottom;">交路名</label> 
																<select class="form-control" style="width: 56px;display:inline-block;" id="input_cross_filter_showFlag"
																	 data-bind="options: [{'code': 1, 'text': '简称'},{'code': 2, 'text': '全称'}], value: searchModle().shortNameFlag, optionsText: 'text', optionsValue: 'code'">
																</select>  
														</th>
														<th style="width: 8%" align="center">审核</th>
														<th style="width: 15%" align="center" colspan="2">生成</th> 
													</tr>
												</thead>
												<tbody style="padding:0">
													 <tr style="padding:0">
													   <td colspan="6" style="padding:0">
															 <div id="crossInfo_Data" style="height: 450px; overflow-y:auto;"> 
																<table class="table table-bordered table-striped table-hover" >
																	<tbody data-bind="foreach: crossRows.rows">
																		<tr data-bind=" visible: visiableRow, style:{color: $parent.currentCross().unitCrossId == unitCrossId ? 'blue':''}" >
																		    <td align="center" style="width: 9.5%"><input type="checkbox" value="1" data-bind="attr:{class: activeFlag() == 0 ? 'ckbox disabled' : ''}, event:{change: $parent.selectCross}, checked: selected"></td>
																			<td style="width: 10.5%" data-bind=" text: $parent.crossRows.currentIndex()+$index()+1 , click: $parent.showTrains"></td>
																			<td align="center" style="width: 8.5%" data-bind=" text: tokenVehBureauShowValue"></td>
																			<td style="width: 48.5%" data-bind="text: $parent.searchModle().shortNameFlag() == 1 ? shortName : crossName, click: $parent.showTrains , attr:{title: crossName()}"></td>
																			<td style="width: 10.5%" align="center" data-bind="style:{color:checkFlag() == 1 ? 'green' : ''},  text: checkFlag() == 1 ? '已' : '未' "></td>
																			<td style="width: 10%" align="center" data-bind="style:{color:unitCreateFlag() == 1 ? 'green' : ''}, text: unitCreateFlag() == 1 ? '已' : '未' "></td>
																		</tr> 
																	</tbody> 
																</table> 
														 	</div>
														</td>
													</tr>
												</tbody>				 
											</table>
										<div data-bind="template: { name: 'tablefooter-short-template', foreach: crossRows }" style="margin-bottom: 5px"></div>
									</div>
								</div> 
						 
		</div> 
	</div>
	
	<div class="pull-right" style="width: 69%;">
		 <div class="panel panel-default"> 
			<div class="row" style="margin: 10px 5px 5px 5px;">
			   <section class="panel panel-default">
			        <div class="panel-heading">
			        	<span>
			              <i class="fa fa-table"></i>交路信息   <a  type="button" style="margin-left: 15px;margin-top: -5px" class="btn btn-success" data-toggle="modal" data-target="#"
										id="cross_train_save" data-bind="attr:{class: searchModle().activeCurrentCrossFlag() == 1 ? 'btn btn-success' : 'btn btn-success disabled'}, click: saveCrossInfo"> 保存</a>
					   </span>
					</div> 
			          <div class="panel-body">
						<div class="row" >
							<form class="form-horizontal" role="form" data-bind="with: currentCross"> 
							<!-- <table>
								<tr>
									<td><label for="exampleInputEmail3"
										class="control-label pull-left"> 车次:&nbsp;</label></td>
									<td colspan="3"><div class="pull-left">
											<input type="text" class="form-control" style="width: 380px;"
												placeholder="输入车次查询" id="plan_construction_input_trainNbr">
										</div></td> 
								</tr>
								<tr>
									<td><label for="exampleInputEmail3"
											class="control-label pull-left"> 交路名:&nbsp;</label></td>
									<td><div class="pull-left">
											<input type="text" class="form-control" style="width: 100px;"
												id="plan_construction_input_trainNbr">
										</div></td>
									<td><label for="exampleInputEmail3"
										class="control-label pull-left"> 组数:&nbsp;</label></td>
									<td><div class="pull-left">
											<input type="text" class="form-control" style="width: 100px;"
												id="plan_construction_input_trainNbr">
										</div></td>
								</tr>
								 <tr>
									<td><label for="exampleInputEmail3"
											class="control-label pull-left"> 交路名:&nbsp;</label></td>
									<td style="width: 250px;">adfaa</td>
									<td><label for="exampleInputEmail3"
										class="control-label pull-left"> 组数:&nbsp;</label></td>
									<td style="width: 250px;"> afad</td>
								</tr>
								<tr>
									<td><label for="exampleInputEmail3"
											class="control-label pull-left"> 交路名:&nbsp;</label></td>
									<td><div class="pull-left">
											<input type="text" class="form-control" style="width: 100px;"
												id="plan_construction_input_trainNbr">
										</div></td>
									<td><label for="exampleInputEmail3"
										class="control-label pull-left"> 组数:&nbsp;</label></td>
									<td><div class="pull-left">
											<input type="text" class="form-control" style="width: 100px;"
												id="plan_construction_input_trainNbr">
										</div></td>
								</tr>
							</table> --> 
							<div class="row" style="margin: 0px 0 5px 0;"> 
										<label for="exampleInputEmail3"
											class="control-label pull-left"> 车底交路名:&nbsp;</label>
										<div class="pull-left" style="margin-left: 26px;">
											<input type="text" class="form-control" style="width: 470px;" data-bind="value: crossName"
												id="plan_construction_input_trainNbr" disabled>
										</div> 
										<label for="exampleInputEmail5" class="control-label pull-left" style="margin-left:40px">
											线路线型:</label> 
										<div class="pull-left">
											<input type="radio" class="pull-left" class="form-control" 
												style="width: 20px; margin-left: 5px; margin-top: 5px"
												class="form-control" data-bind="checked: highlineFlag" value="0" disabled>
										</div>
										<label for="exampleInputEmail5" class="control-label pull-left">
											普线</label> 
										<div class="pull-left">
											<input type="radio" class="pull-left" class="form-control" 
												style="width: 20px; margin-left: 5px; margin-top: 5px"
												class="form-control" value="1" data-bind="checked: highlineFlag" disabled>
										</div>
										<label for="exampleInputEmail5" class="control-label pull-left">
											高线</label> 
										<div class="pull-left">
											<input type="radio" class="pull-left" class="form-control" 
												style="width: 20px; margin-left: 5px; margin-top: 5px"
												class="form-control" value="2" data-bind="checked: highlineFlag" disabled>
										</div>
										<label for="exampleInputEmail5" class="control-label pull-left">
											混合</label> 
								</div>
								<div class="row" style="margin: 5px 0 0px 0;">
								   
									    <label for="exampleInputEmail3"
												class="control-label pull-left"> 备用套跑交路名:&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 470px;" data-bind="value: crossSpareName" disabled>
										</div> 
										<label for="exampleInputEmail5" style="margin-left: 40px;" class="control-label pull-left">
											开行状态:</label>
												
										<div class="pull-left">
											<input type="radio" class="pull-left" class="form-control"
												value="1" data-bind="checked: spareFlag"
												style="width: 20px; margin-left: 5px; margin-top: 5px"
												class="form-control" disabled>
										</div>
										<label for="exampleInputEmail5" class="control-label pull-left">
											开行</label>
	
										<div class="pull-left">
											<input type="radio" class="pull-left" class="form-control"
												value="2" data-bind="checked: spareFlag"
												style="width: 20px; margin-left: 5px; margin-top: 5px"
												class="form-control" disabled>
										</div>
										<label for="exampleInputEmail5" class="control-label pull-left">
											备用</label>
										<div class="pull-left">
											<input type="radio" class="pull-left" class="form-control"
												value="0" data-bind="checked: spareFlag"
												style="width: 20px; margin-left: 5px; margin-top: 5px"
												class="form-control" disabled>
										</div>
										<label for="exampleInputEmail5" class="control-label pull-left">
											停运</label> 
									
								</div>
								<div class="row" style="margin: 5px 0 0px 0;"> 
								    <div class="pull-left" style="width: 16%;"> 
								       <div class="row" style="margin: 5px 0 0px 0;"> 
											<label for="exampleInputEmail3"
												class="control-label pull-left"> 组数:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" style="width: 40px;" data-bind="value: groupTotalNbr" disabled>
											</div> 
										</div>
										<div class="row" style="margin: 5px 0 0px 0;"> 
											<label for="exampleInputEmail2" class="control-label pull-left">对数:&nbsp;</label>
											<input type="text" class="form-control" style="width: 40px;" data-bind="value: pairNbr">
										 
										</div> 
										<div class="row" style="margin: 5px 0 0px 0;"> 
											<label for="exampleInputEmail5"  class="control-label pull-left">
												开始:&nbsp;</label> 
											<input type="text" class="form-control" style="width: 70px;"
													placeholder=""  data-bind="value: crossStartDate"> 
										</div>
										<div class="row" style="margin: 5px 0 0px 0;"> 
											<label for="exampleInputEmail5" class="control-label pull-left">
												结束:&nbsp;</label> 
											<input type="text" class="form-control" style="width: 70px;"
													placeholder=""  data-bind="value: crossEndDate">
											 
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">  
											<input type="checkBox" class="pull-left" class="form-control"
												value="1" data-bind="checked: cutOld"
												style="width: 20px; margin-top: 5px"
												class="form-control"> 
											<label for="exampleInputEmail5" class="control-label pull-left">
												截断原交路</label>
										</div>
									</div>
									 <div class="pull-left" style="width: 28%;"> 
										 <div class="row" style="margin: 5px 0 0px 0;"> 
											<label for="exampleInputEmail5" class="control-label pull-left">
												普线规律:</label> 
											<input type="radio" class="pull-left" class="form-control"
													value="1" data-bind="checked: commonlineRule"
													style="width: 20px; margin-left: 5px; margin-top: 5px"
													class="form-control"> 
											<label for="exampleInputEmail5" class="control-label pull-left">
												每日</label> 
											<input type="radio" class="pull-left" class="form-control"
													value="2" data-bind="checked: commonlineRule"
													style="width: 20px; margin-left: 5px; margin-top: 5px"
													class="form-control"> 
											<label for="exampleInputEmail5" class="control-label pull-left">
												隔日</label>
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">  
											<label for="exampleInputEmail5"  class="control-label pull-left">
												高线规律:</label>	 
											   <input type="radio" class="pull-left" class="form-control"
													value="1" data-bind="checked: highlineRule"
													style="width: 20px; margin-left: 5px; margin-top: 5px"
													class="form-control"> 
												<label for="exampleInputEmail5" class="control-label pull-left">
												日常</label> 
												<input type="radio" class="pull-left" class="form-control"
													value="2" data-bind="checked: highlineRule"
													style="width: 20px; margin-left: 5px; margin-top: 5px"
													class="form-control"> 
												<label for="exampleInputEmail5" class="control-label pull-left">
													周末</label>  
												<input type="radio" class="pull-left" class="form-control"
														value="3" data-bind="checked: highlineRule"
														style="width: 20px; margin-left: 5px; margin-top: 5px"
														class="form-control">
												 
												<label for="exampleInputEmail5" class="control-label pull-left">
													高峰</label>  
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">  		
											<label for="exampleInputEmail5" class="control-label pull-left">
												指定星期:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" style="width: 71px;"
													placeholder=""   data-bind="value: appointWeek">
											</div>
										</div>
										 <div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail5" class="control-label pull-left">
												指定日期:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" style="width: 140px;"
													placeholder=""  data-bind="value: appointDay">
											</div> 
										</div> 
										<div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail5" class="control-label pull-left">
												指定周期:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" style="width: 140px;"
													placeholder=""  data-bind="value: appointPeriod">
											</div> 
										</div> 
								   </div>
								   
								   <div class="pull-left" style="width: 25%;"> 
								      <div class="row" style="margin: 5px 0 0px 0;">
										<label class="control-label pull-left" style="margin-left: 5px;"> 车辆担当局:&nbsp;</label>
										<div class="pull-left">
											<!-- <select style="width: 50px" class="form-control" data-bind="options: $parent.gloabBureaus, value: tokenVehBureau, optionsText: 'shortName', optionsValue:'code' , optionsCaption: ''"></select> -->
											<input type="text" class="form-control" disabled  style="width: 50px;"  data-bind="value: tokenVehBureauShowValue">
										</div>
									  </div>
									  <div class="row" style="margin: 5px 0 0px 0;">
										<label  class="control-label pull-left" > 车辆/动车段:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" style="width: 90px;" data-bind="value: tokenVehDept">
											</div>
									   </div>
									    <div class="row" style="margin: 5px 0 0px 0;">
											<label  class="control-label pull-left" style="margin-left: 30px;"> 动车所:&nbsp;</label>
												<div class="pull-left">
													<input type="text" class="form-control" style="width: 90px;" data-bind="value: tokenVehDepot">
												</div>
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail3" style="margin-left: 5px;"
													class="control-label pull-left"> 客运担当局:&nbsp;</label>
											<div class="pull-left">
												<!-- <input type="text" class="form-control" style="width: 30px;" data-bind="value: tokenPsgDept"> -->
												<input type="text" class="form-control disabled" style="width: 50px;" disabled  data-bind="value: tokenPsgBureauShowValue" >
												<!-- <select style="width: 50px" class="form-control" data-bind="options: $parent.gloabBureaus, value: tokenPsgBureau, optionsText: 'shortName', optionsValue:'code', optionsCaption: ''"></select> -->
											</div>
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail3" style="margin-left: 30px;"
												class="control-label pull-left"> 客运段:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" style="width: 90px;" data-bind="value: tokenPsgDept">
											</div>
										</div>
								   </div>
								   
								   <div class="pull-left" style="width: 30%;"> 
									   <div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail3"
												class="control-label pull-left" > 运行区段:&nbsp;</label>
											<input type="text" class="form-control" style="width: 182px;" data-bind="value: crossSection">
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail3"
												class="control-label pull-left"  style="margin-left: 13px;" > 经由线:&nbsp;</label>
											<input type="text" class="form-control" style="width: 182px;" data-bind="value: throughline">
										</div> 
										<div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail3" 
													class="control-label pull-left"> 机车类型:&nbsp;</label> 
											<input type="text" class="form-control pull-left" style="width: 40px;" data-bind="value: locoType">
											<label for="exampleInputEmail3" style="margin-left:10px"
													class="control-label pull-left"> 动车组车型:&nbsp;</label> 
											<input type="text" class="form-control pull-left" style="width: 60px;" data-bind="value: crhType">
										</div>
									<div class="row" style="margin: 5px 0 0px 0;">
									    <label for="exampleInputEmail3"  style="margin-left: 26px;"
													class="control-label pull-left"> 其他:&nbsp;</label> 
										<input type="checkbox" class="pull-left" class="form-control"
											value="1" data-bind="checked: elecSupply"
											style="width: 20px; margin-top: 5px;"
											class="form-control"> 
										<label for="exampleInputEmail5" class="control-label pull-left">
											供电</label> 
										<input type="checkbox" class="pull-left" class="form-control"
											value="1" data-bind="checked: dejCollect"
											style="width: 20px; margin-left: 5px; margin-top: 5px"
											class="form-control"> 
										<label for="exampleInputEmail5" class="control-label pull-left">
											集便</label> 
										<input type="checkbox" class="pull-left" class="form-control"
											value="1" data-bind="checked: airCondition"
											style="width: 20px; margin-left: 5px; margin-top: 5px"
											class="form-control"> 
										<label for="exampleInputEmail5" class="control-label pull-left">
											空调</label> 
									</div>  
									<div class="row" style="margin: 5px 0 0px 0;">
									     <label for="exampleInputEmail3"  style="margin-left: 26px;"
												class="control-label pull-left"> 备注:&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 182px;" data-bind="value: note">
										</div> 
									</div> 
								</div>   
						    </div> 
							<!-- <div class="row" style="margin: 5px 0 0px 0;">
							      <a type="button" style="margin-left: 15px"
										class="btn btn-success" data-toggle="modal" data-target="#"
										id="cross_train_save" data-bind="attr:{class: $parent.searchModle().activeCurrentCrossFlag() == 1 ? 'btn btn-success' : 'btn btn-success disabled'}, click: $parent.saveCrossInfo"> 保存</a>
							</div> -->
							<!--col-md-3 col-sm-4 col-xs-4-->
						</form>
					    </div>
					  </div>
				   </section>
				</div>
				<div class="row" style="margin: 15px 10px 10px 10px;">
					<div class="pull-left" style="width: 100%;">
					<section class="panel panel-default" >
				         <div class="panel-heading">
				            <span>
				              <i class="fa fa-table"></i>列车信息   
				                           
											<a  type="button" style="margin-left: 15px;margin-top: -5px" class="btn btn-success" data-toggle="modal" data-target="#"
											id="cross_train_save" data-bind="
											click: showCrossMapDlg">交路图</a>
											<a  type="button" style="margin-left: 5px;margin-top: -5px" class="btn btn-success" data-toggle="modal" data-target="#"
											id="cross_train_save" data-bind="click: showCrossTrainTimeDlg">时刻表</a>
						   </span>
				         </div>
				          <div class="panel-body" style="overflow-y:auto" >
							<div class="table-responsive" > 
								<table class="table table-bordered table-striped table-hover" 
									id="cross_trainInfo">
									<thead>
										<tr> 	
											<th style="width: 5%">组序</th> 
											<th style="width: 5%">车序</th>
											<th style="width: 8%">车次</th>  
											<th style="width: 7%">始发日期</th>
											<th style="width: 7%">终到日期</th>
											<th style="width: 8%">发站</th>
											<th style="width: 5%">发局</th>
											<th style="width: 8%">到站</th>
											<th style="width: 5%">到局</th>
											<th style="width: 5%">线型</th>
											<th style="width: 5%">间隔</th>
											<th style="width: 5%">状态</th> 
											<th style="width: 5%">备用套跑</th>
											<th style="width: 5%">高线规律</th>
											<th style="width: 5%">普线规律</th>
											<th style="width: 10%">特殊规律</th>
											<th style="width: 5%">交替车次</th>
											<th style="width: 9%">交替时间</th>
										</tr>
									</thead> 
									<tbody data-bind="foreach: trains" >
										<tr  data-bind="click: $parent.setCurrentTrain, style:{color: $parent.currentTrain() != null && $parent.currentTrain().trainNbr == trainNbr ? 'blue':''}">
											<td style="width: 5px" data-bind="text: groupSerialNbr"></td> 
											<td style="width: 5px" data-bind="text: trainSort"></td>
											<td style="width: 60px" data-bind="text: trainNbr"></td> 
											<td style="width: 60px" data-bind="text: runDate"></td> 
											<td style="width: 60px" data-bind="text: endDate"></td> 
											<td style="width: 100px" data-bind="text: startStn, attr:{title: startStn}"></td>
											<td style="width: 50px" data-bind="text: startBureau"></td>
											<td style="width: 100px" data-bind="text: endStn, attr:{title: endStn}"></td>
											<td style="width: 50px" data-bind="text: endBureau"></td>
											<td style="width: 50px" data-bind="text: highlineFlag"></td>
											<td style="width: 50px" data-bind="text: dayGap"></td>
											<td style="width: 50px" data-bind="text: spareFlag"></td> 
											<td style="width: 50px" data-bind="text: spareApplyFlage"></td>
											<td style="width: 50px" data-bind="text: highlineRule"></td>
											<td style="width: 50px" data-bind="text: commonLineRule"></td>
											<td style="width: 50px" data-bind="text: otherRule"></td>
											<td style="width: 50px" data-bind="text: alertNateTrainNbr"></td>
											<td style="width: 50px" data-bind="text: alertNateTime"></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div> 
					</section>
					</div>
					<!-- <div class="pull-right" style="width: 28%;margin: 0px 10px 10px 10px;"> -->
				
				 
				</div>
			</div>  
	 <!--交路图--> 
	<div id="cross_map_dlg" class="easyui-dialog" title="交路图"
		data-options="iconCls:'icon-save'"
		style="width: 800px; height: 400px;">
			<input id="parentParamIsShowJt" type="hidden" value="1">
			<input id="parentParamIsShowTrainTime" type="hidden" value="0">
		 <iframe style="width: 100%; height: 350px;border: 0;" src=""></iframe>
	</div> 
	
	 <!--列车新增和修改--> 
	<div id="cross_train_dlg" class="easyui-dialog" title="列车基本信息编辑"
		data-options="iconCls:'icon-save'"
		style="width: 400px; height: 500px; padding: 10px">
		  
	</div> 
	 
	<!--详情时刻表--> 
	<div id="cross_train_time_dlg" class="easyui-dialog" title="时刻表"
		data-options="iconCls:'icon-save'"
		style="width: 500px; height: 500px; padding: 10px; "> 
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
					      	<div class="table-responsive"> 
					            <table class="table table-bordered table-striped table-hover" id="plan_runline_table_trainLine">
							        <thead>
							        <tr>
							          <th style="width:5%">序号</th>
					                  <th style="width:20%">站名</th>
					                  <th style="width:5%">路局</th>
					                  <th style="width:15%">到达</th>
					                  <th style="width:15%">出发</th>
					                  <th style="width:15%">停时</th>
					                  <th style="width:10%">天数</th> 
					                  <th style="width:15%" colspan="2">股道</th>  
					                 </tr>
							        </thead>
							        <tbody style="padding:0">
										 <tr style="padding:0">
										   <td colspan="9" style="padding:0">
												 <div id="simpleTimes_table" style="height: 350px; overflow-y:auto;"> 
													<table class="table table-bordered table-striped table-hover" >
														 <tbody data-bind="foreach: simpleTimes">
												           <tr data-bind="visible: stationFlag != 'BTZ'">  
															<td style="width:7.5%" align="center" data-bind=" text: $index() + 1"></td>
															<td style="width:19%" data-bind="text: stnName, attr:{title: stnName}"></td>
															<td style="width:7.5%" align="center" data-bind="text: bureauShortName"></td>
															<td style="width:14.3%" align="center" data-bind="text: sourceTime"></td>
															<td style="width:14.3%" align="center" data-bind="text: targetTime"></td>
															<td style="width:15%" align="center" data-bind="text: stepStr"></td>
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
					      	<div class="table-responsive"> 
					            <table class="table table-bordered table-striped table-hover" id="plan_runline_table_trainLine">
							        <thead>
							        <tr>
							          <th style="width:5%">序号</th>
					                  <th style="width:20%">站名</th>
					                  <th style="width:5%">路局</th>
					                  <th style="width:15%">到达时间</th>
					                  <th style="width:15%">出发时间</th>
					                  <th style="width:15%">停留时间</th>
					                  <th style="width:10%">天数</th> 
					                  <th style="width:15%" colspan="2">股道</th>  
					                 </tr>
							        </thead>
							        <tbody style="padding:0">
										 <tr style="padding:0">
										   <td colspan="9" style="padding:0">
												 <div id="allTimes_table" style="height: 350px; overflow-y:auto;"> 
													<table class="table table-bordered table-striped table-hover" > 
														 <tbody data-bind="foreach: times">
												           <tr>  
															<td style="width:7.5%" align="center" data-bind=" text: $index() + 1"></td>
															<td style="width:19%" data-bind="text: stnName, attr:{title: stnName}"></td>
															<td style="width:7.5%" align="center" data-bind="text: bureauShortName"></td>
															<td style="width:14.3%" align="center" data-bind="text: sourceTime"></td>
															<td style="width:14.3%" align="center" data-bind="text: targetTime"></td>
															<td style="width:15%" align="center" data-bind="text: stepStr"></td>
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

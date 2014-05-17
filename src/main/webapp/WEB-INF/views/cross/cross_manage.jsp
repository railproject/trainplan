<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<% 
String basePath = request.getContextPath();
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
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>

<script type="text/javascript" src="<%=basePath %>/assets/js/knockout.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.jeditable.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.dataTables.editable.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.freezeheader.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/ajaxfileupload.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/cross/cross.js"></script>  
<script type="text/javascript" src="<%=basePath %>/assets/oldAssets/js/datepicker.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/oldAssets/js/jquery.gritter.min.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/common.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/util/zDrag.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/util/zDialog.js"></script>

 

 
</head>
<body class="Iframe_body"  >
	
	<ol class="breadcrumb">
		<span><i class="fa fa-anchor"></i>当前位置:</span>
		<li><a href="#">对数表维护</a></li>
	</ol> 
        
	<!--分栏框开始-->
	<div class="pull-left" style="width: 24%;">
		<!--分栏框开始-->
		<div id="plan_view_div_palnDayDetail" class="panel panel-default">
			<div class="row" style="margin: 10px 10px 10px 10px;">
			    <section class="panel panel-default">
			        <div class="panel-heading"><i class="fa fa-table"></i>导入对数表</div>
			        <div class="panel-body">
			        <form class="form-horizontal" role="form">    
				        <div class="row" style="width: 100%">
								<label for="exampleInputEmail3" class="control-label pull-left">
												方案:&nbsp;</label> 
								<div class="pull-left" style="width: 80%">
									<select style="width: 90%" id="input_cross_chart_id"
										class="form-control" data-bind="options:searchModle().charts, value: searchModle().chart, optionsText: 'name', optionsCaption: ''">
									</select>
								</div>  
						    </div> 
						   <div class="row" style="margin: 5px 0 5px 0;width: 100%">
								<label for="exampleInputEmail2" class="control-label pull-left">启用日期:&nbsp;</label>
						        <div class="pull-left" style="width: 50%">
						           <input class="form-control" id="cross_start_day" style="width:90%;" placeholder="" data-bind="value: searchModle().startDay">
						        </div>
						        <button class="btn btn-primary" type="button" style="margin-left: 20px;"
								id="btn_cross_upload" data-bind="click: showUploadDlg">导入EXCEL</button>
						     </div>
					</form>
			        </div>
			   </section> 
			</div>
			<div class="row" style="margin: 5px 10px 10px 10px;">
			    <section class="panel panel-default">
			        <div class="panel-heading"><i class="fa fa-table"></i>车底交路列表</div>
			        <div class="panel-body">
						<form class="form-horizontal" role="form"> 
							<div class="row" style="margin-top: 5px;width: 100%">
									<div class="form-group"
										style="float: left; margin-left: 0px; margin-top: 0px;width: 100%"> 
										<div class="row"  style="width: 100%">
											<label for="exampleInputEmail3" class="control-label pull-left" >
												车辆担当局:</label>
											<div class="pull-left" style="margin-left: 5px;width: 20%">
												<select style="width:90%" class="form-control" data-bind="options:searchModle().bureaus, value: searchModle().bureau, optionsText: 'shortName', optionsValue:'code', optionsCaption: '' "></select>
											</div>
											<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left: 20px;">
												始发局:</label>
											<div class="pull-left" style="margin-left: 5px;width: 20%">
											<select style="width: 90%" class="form-control" data-bind="options:searchModle().startBureaus, value: searchModle().startBureau, optionsText: 'shortName', optionsValue:'code', optionsCaption: ''"></select>
											</div> 
										</div>    
										<div class="row"  style="margin-top: 5px;width: 100%">
											<label for="exampleInputEmail3" class="control-label pull-left" >
												铁路线类型:</label>
											<div class="pull-left" style="margin-left: 5px;width: 20%">
											    <select  style="width:90%" class="form-control" data-bind="options: searchModle().highlingFlags, value: searchModle().highlingFlag, optionsText: 'text' , optionsCaption: ''"></select>
											</div>
											<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left: 33px;">
												状态:</label>
											<div class="pull-left" style="margin-left: 5px;width: 20%">
												<select style="width: 90%" id="input_cross_sure_flag"
													class="form-control" data-bind="options: searchModle().sureFlags, value: searchModle().sureFlag, optionsText: 'text' , optionsCaption: ''">
												</select>
											</div>
											
											<div class="pull-right">
												<a type="button" class="btn btn-success" data-toggle="modal"
													data-target="#" id="btn_cross_search" style="margin-right: 5px;" data-bind="click: loadCrosses">查询</a>
											</div>
										</div> 
										<hr style="margin-top: 8px;margin-bottom: 8px">
										<div class="row"  style="margin-top: 5px;">
										     
											<label for="exampleInputEmail3" class="control-label pull-left">
												车次:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" style="width: 100px;"
													placeholder="车次" id="input_cross_filter_trainNbr" data-bind=" value: searchModle().filterTrainNbr, event: { change: filterCrosses}">
											</div>
										 
											<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left: 20px;">
												交路名:&nbsp;</label>
											<div class="pull-left">
												<select style="width: 65px" id="input_cross_filter_showFlag"
													class="form-control" data-bind="options: [{'code': 1, 'text': '简称'},{'code': 2, 'text': '全称'}], value: searchModle().shortNameFlag, optionsText: 'text', optionsValue: 'code'">
												</select>
											</div>  
										</div>
									</div>
								</div>
								<div class="row" >
									<div class="table-responsive">
										<div class="form-group"
											style="margin-left: 10px;">
											<a type="button" class="btn btn-success" data-toggle="modal"
												data-target="#" id="btn_cross_sure">审核</a>
											<a type="button" class="btn btn-success" data-toggle="modal"
												data-target="#" id="btn_cross_delete">删除</a>
											  <a type="button" class="btn btn-success" data-toggle="modal"
												data-target="#" id="btn_cross_createCrossUnit" data-bind="click: createUnitCrossInfo">生成基本交路单元</a>
										</div> 
										<table class="table table-bordered table-striped table-hover" 
											id="cross_table_crossInfo">
											<thead>
												<tr style="height: 25px">
													<th style="width: 10%" align="center"><input type="checkbox" style="margin-top:0" value="1" data-bind="checked: crossAllcheckBox, event:{change: selectCrosses}"></th>
													<th style="width: 15%" align="center">序号</th>
													<th style="width: 60%" align="center">车底交路名</th>
													<th style="width: 15%" align="center">状态</th>
												</tr>
											</thead>
											<tbody data-bind="foreach: crossRows">
												<tr data-bind=" visible: visiableRow" >
												    <td align="center"><input type="checkbox" value="1" data-bind="event:{change: $parent.selectCross}, checked: selected"></td>
													<td data-bind=" text: $index()+1 , click: $parent.showTrains"></td>
													<td data-bind="text: $parent.searchModle().shortNameFlag() == 1 ? shortName : crossName, click: $parent.showTrains"></td>
													<td style="color: green;">已审核</td>
												</tr> 
											</tbody> 
											<tr data-bind="visiable: totalCount() > 0">
												<table>
												  <tr>
												  <!-- 共<span data-bind="html: totalCount()"></span>条 当前<span data-bind="html: (crossRows().length-(crossRows().length%50 == 0 ? 50 : crossRows().length%50))"></span>到<span data-bind="html: crossRows().length"></span></td><td colspan="2" data-bind="click: loadNCrosses"> -->
													<td>共<span data-bind="html: totalCount()"></span>条</td><td>当前<span data-bind="html: currentIndex()"></span>到<span data-bind="html: (currentIndex()%pageSize()==0 ? ((totalCount() == 0) ? 0 : currentIndex() + pageSize()) : currentIndex() + currentIndex()%pageSize())"></span></td><td data-bind="click: loadNCrosses"><span class="btn btn-success">下一页</span></td><td colspan="2" data-bind="click: loadPCrosses"><span class="btn btn-success">上一页</span></td>
												  </tr>
												</table>
											</tr>
										</table>
									</div>
								</div> 
						</form>
					 </div>
			   </section>
			</div>
		</div>
	</div>
	<div class="pull-right" style="width: 75%;">
		<div id="plan_view_div_palnDayDetail" class="panel panel-default"> 
			<div class="row" style="margin: 10px 10px 10px 10px;">
			   <section class="panel panel-default">
			        <div class="panel-heading"><i class="fa fa-table"></i>交路信息</div>
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
							<div class="pull-left" id="left_div">
								<div class="row" style="margin: 0px 0 5px 0;"> 
										<label for="exampleInputEmail3"
											class="control-label pull-left"> 车底交路名:&nbsp;</label>
										<div class="pull-left" style="margin-left: 26px;">
											<input type="text" class="form-control" style="width: 520px;" data-bind="value: crossName"
												id="plan_construction_input_trainNbr">
										</div> 
										<div class="pull-left">
											<input type="radio" class="pull-left" class="form-control" 
												style="width: 20px; margin-left: 38px; margin-top: 5px"
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
										<input type="text" class="form-control" style="width: 520px;" data-bind="value: crossSpareName">
									</div> 
									
								</div>
								<div class="row" style="margin: 5px 0 0px 0;"> 
									<label for="exampleInputEmail3"
										class="control-label pull-left"> 组数:&nbsp;</label>
									<div class="pull-left">
										<input type="text" class="form-control" style="width: 40px;" data-bind="value: groupTotalNbr">
									</div> 
									<label for="exampleInputEmail2" class="control-label pull-left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;对数:&nbsp;</label>
									<div class="pull-left">
										<input type="text" class="form-control" style="width: 40px;" data-bind="value: pairNbr">
									</div> 
									
									<label for="exampleInputEmail5" style="margin-left: 43px;" class="control-label pull-left">
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
										
									<div class="pull-left">
										<input type="checkBox" class="pull-left" class="form-control"
											value="1" data-bind="checked: cutOld"
											style="width: 20px; margin-left: 53px; margin-top: 5px"
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
										<input type="text" class="form-control" style="width: 60px;"
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
											<select style="width: 65px" class="form-control" data-bind="options: $parent.gloabBureaus, value: tokenVehBureau, optionsText: 'shortName', optionsValue:'code' , optionsCaption: ''"></select>
											<!-- <input type="text" class="form-control" style="width: 30px;"  data-bind="value: tokenVehBureau"> -->
										</div>
									<label  class="control-label pull-left" style=" margin-left: 20px;"> 车辆段/动车段:&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 110px;" data-bind="value: tokenVehDept">
										</div>
									<label  class="control-label pull-left" style=" margin-left: 20px;" > 动车所:&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 110px;" data-bind="value: tokenVehDepot">
										</div>
									<label for="exampleInputEmail3"
											class="control-label pull-left" style=" margin-left: 30px;" > 客运担当局:&nbsp;</label>
									<div class="pull-left">
										<!-- <input type="text" class="form-control" style="width: 30px;" data-bind="value: tokenPsgDept"> -->
										
										<select style="width: 65px" class="form-control" data-bind="options: $parent.gloabBureaus, value: tokenPsgBureau, optionsText: 'shortName', optionsValue:'code', optionsCaption: ''"></select>
									</div>
									<label for="exampleInputEmail3"
										class="control-label pull-left" style=" margin-left: 20px;" > 客运段:&nbsp;</label>
									<div class="pull-left">
										<input type="text" class="form-control" style="width: 110px;" data-bind="value: crossSection">
									</div>
								</div>
								<div class="row" style="margin: 5px 0 0px 0;"> 
									<label for="exampleInputEmail3"
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
									<label for="exampleInputEmail3" style="margin-left: 30px"
											class="control-label pull-left"> 备注:&nbsp;</label>
									<div class="pull-left">
										<input type="text" class="form-control" style="width: 235px;" data-bind="value: note">
									</div> 
									  <a type="button" style="margin-left: 15px"
										class="btn btn-success" data-toggle="modal" data-target="#"
										id="cross_train_save" data-bind="click: $parent.saveCrossInfo"> 保存</a>
								</div>  
								
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
							<!--col-md-3 col-sm-4 col-xs-4-->
						</form>
					    </div>
					  </div>
				   </section>
				</div>
				<div class="row" style="margin: 15px 10px 10px 10px;">
					<div class="pull-left" style="width: 100%;">
					<section class="panel panel-default" >
				         <div class="panel-heading"><i class="fa fa-table"></i>列车信息</div>
				          <div class="panel-body" style="overflow-y:auto">
							<div class="table-responsive">
								<div class="form-group"
									style="margin-left: 10px; margin-top: 5px;">
									  <a type="button"
										class="btn btn-success" data-toggle="modal" data-target="#"
										id="cross_train_add" data-bind="click: showCrossTrainDlg">添加</a> <a type="button"
										class="btn btn-success" data-toggle="modal" data-target="#"
										id="cross_train_save" data-bind="click: showCrossTrainDlg"> 修改</a> <a type="button"
										class="btn btn-success" data-toggle="modal" data-target="#"
										id="cross_train_delete">删除</a>
										 <a type="button"
										class="btn btn-success" data-toggle="modal" data-target="#"
										id="cross_train_delete" data-bind="click: showCrossTrainTimeDlg">时刻表</a>
										 <a type="button"
										class="btn btn-success" data-toggle="modal" data-target="#"
										id="cross_train_delete" data-bind="click: showCrossTrainTimeDlg">详点</a>
										 <a type="button"
										class="btn btn-success" data-toggle="modal" data-target="#"
										id="cross_train_delete" data-bind="click: showCrossMapDlg">交路图</a>
								</div>
								<table class="table table-bordered table-striped table-hover"
									id="cross_trainInfo">
									<thead>
										<tr>
											<th style="width: 5%">序号</th>
											<th style="width: 8%">车次</th>
											<th style="width: 8%">始发站</th>
											<th style="width: 5%">始发局</th>
											<th style="width: 8%">终到站</th>
											<th style="width: 5%">终到局</th>
											<th style="width: 5%">线型</th>
											<th style="width: 5%">间隔(天)</th>
											<th style="width: 5%">开行状态</th>
											<th style="width: 5%">交替车次</th>
											<th style="width: 11%">交替时间</th>
											<th style="width: 5%">备用套跑</th>
											<th style="width: 5%">高线规律</th>
											<th style="width: 5%">普线规律</th>
											<th style="width: 10%">特殊规律</th>
										</tr>
									</thead> 
									<tbody data-bind="foreach: trains" >
										<tr>
											<td style="width: 5px" data-bind="text: trainSort"></td>
											<td style="width: 60px" data-bind="text: trainNbr"></td>
											<td style="width: 100px" data-bind="text: startStn"></td>
											<td style="width: 50px" data-bind="text: startBureau"></td>
											<td style="width: 100px" data-bind="text: endStn"></td>
											<td style="width: 50px" data-bind="text: endBureau"></td>
											<td style="width: 50px" data-bind="text: highlineFlag"></td>
											<td style="width: 50px" data-bind="text: dayGap"></td>
											<td style="width: 50px" data-bind="text: spareFlag"></td>
											<td style="width: 50px" data-bind="text: alertNateTrainNbr"></td>
											<td style="width: 50px" data-bind="text: alertNateTime"></td>
											<td style="width: 50px" data-bind="text: spareApplyFlage"></td>
											<td style="width: 50px" data-bind="text: highlineRule"></td>
											<td style="width: 50px" data-bind="text: commonLineRule"></td>
											<td style="width: 50px" data-bind="text: otherRule"></td>
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
			</div>  
	 <!--交路图--> 
	<div id="cross_map_dlg" class="easyui-dialog" title="交路图"
		data-options="iconCls:'icon-save'"
		style="width: 800px; height: 600px; padding: 10px">
		 <iframe style="width: 800px; height: 600px;border: 0" src="assets/unit_cross_canvas/unit_cross_canvas.html"></iframe>
	</div> 
	
	 <!--列车新增和修改--> 
	<div id="cross_train_dlg" class="easyui-dialog" title="列车基本信息编辑"
		data-options="iconCls:'icon-save'"
		style="width: 400px; height: 500px; padding: 10px">
		  
	</div>  
    <!--导入弹窗--> 
	<div id="file_upload_dlg" class="easyui-dialog" title="上传对数文件"
		data-options="iconCls:'icon-save'"
		style="width: 400px; height: 200px; padding: 10px">
		<img id="loading" src="assets/images/loading.gif" style="display:none;">
		<form id="file_upload_id" name="file_upload_name" action="cross/fileUpload"
			method="post" enctype="multipart/form-data"> 
			<div>
				<input id="fileToUpload" type="file" size="45" name="fileToUpload"  name="fileName" />
			</div>
			<div>
			     <a type="button"
					class="btn btn-success" data-toggle="modal" data-target="#" data-bind="click: uploadCrossFile">上传</a>
				<!-- <input type="submit"  value="上传" data-bind=/> -->
			</div>
		</form>
	</div> 
	<!--详情时刻表--> 
	<div id="cross_train_time_dlg" class="easyui-dialog" title="时刻表"
		data-options="iconCls:'icon-save'"
		style="width: 600px; height: 500px; padding: 10px">
			<div id="cross_train_time_info">  
		          <div class="panel-body">
				    <div class="table-responsive"> 
						<table class="table table-bordered table-striped table-hover"
								data-options="singleSelect:true,collapsible:true,url:'datagrid_data1.json',method:'get'"
								title="Basic DataGrid" id="cross_train_timeInfo">
								<thead>
									<tr>
										<th style="width: 25px">序号</th>
										<th style="width: 60px">站名</th>
										<th data-options="field:'index',width:80"
											style="width: 60px">发点</th>
										<th data-options="field:'index',width:80"
											style="width: 60px">到点</th>
										<th data-options="field:'index',width:80"
											style="width: 60px">股道</th>
									</tr>
								</thead>
								<tbody>
								 
								</tbody>
							</table> 
					  </div> 
				</div>
			</div>
	   </div>

</body>
</html>

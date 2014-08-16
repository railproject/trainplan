<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="org.apache.shiro.SecurityUtils,org.railway.com.trainplan.service.ShiroRealm,java.util.List" %>
<% 
ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();

boolean isZgsUser = false;	//当前用户是否为总公司用户
if (user!=null && user.getBureau()==null) {
	isZgsUser = true;
}
String currentUserBureau = user.getBureau();
List<String> permissionList = user.getPermissionList();
String userRolesString = "";
for(String p : permissionList){
	userRolesString += userRolesString.equals("") ? p : "," + p;
} 
String basePath = request.getContextPath();
%>
<!DOCTYPE HTML>
<html lang="en">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>既有临客开行计划</title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />
<link href="<%=basePath %>/assets/easyui/themes/default/easyui.css" rel="stylesheet">
<link href="<%=basePath %>/assets/easyui/themes/icon.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath %>/assets/css/cross/cross.css">
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath %>/assets/css/table.scroll.css">


<script type="text/javascript">
var all_role = "<%=userRolesString %>";
var _isZgsUser = <%=isZgsUser%>;//当前用户是否为总公司用户
var currentUserBureau = "<%=currentUserBureau %>";
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
<body class="Iframe_body"  >
	
	<ol class="breadcrumb">
		<span><i class="fa fa-anchor"></i>当前位置:</span>
		<li><a href="#">编制计划 -> 开行计划管理 -> 临客开行计划</a></li>
	</ol>  
	   <div class="row" style="margin: 10px 10px 10px 10px;">  
	        <!--分栏框开始-->
		    <div class="pull-left" style="width: 30%;height:100%">
			<!--分栏框开始--> 
			      <div class="row" style="margin: 0px 0px 5px 5px;"> 
				        	<div> 
										<div class="row"  style="width: 100%;">
												<label for="exampleInputEmail3" class="control-label pull-left" >
													开始日期:</label>
												<div class="pull-left" style="margin-left: 5px;">
													<input type="text" class="form-control" style="width:75px;" placeholder="" id="runplan_input_startDate"  name="startDate" data-bind="value: searchModle().planStartDate" />
												</div>
												<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left: 20px;">
													截至日期:</label>
												<div class="pull-left" style="margin-left: 5px; ">
													<input type="text" class="form-control" style="width:75px;" placeholder="" id="runplan_input_endDate"  name="endDate" data-bind="value: searchModle().planEndDate" />
												</div> 
											</div>   
											<div class="row"  style="width: 100%; margin-top: 5px;">
												<label for="exampleInputEmail3" class="control-label pull-left" >
													车辆担当局:</label>
												<div class="pull-left" style="margin-left: 5px;">
													<select style="width:62px" class="form-control" data-bind="options:searchModle().bureaus, value: searchModle().bureau, optionsText: 'shortName', optionsValue:'code', optionsCaption: '' ,event:{change: bureauChange}"></select>
												</div>
												<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left: 20px;">
													始发路局:</label>
												<div class="pull-left" style="margin-left: 5px; ">
													<select style="width: 75px" class="form-control" data-bind="options:searchModle().startBureaus, value: searchModle().startBureau, optionsText: 'shortName', optionsValue:'code', optionsCaption: ''"></select>
												</div> 
											</div>     
											<div class="row"  style="margin-top: 5px;">
											    <label for="exampleInputEmail3" class="control-label pull-left">
													车次:&nbsp;</label>
												<div class="pull-left">
													<input type="text" class="form-control" style="width: 102px;"
												 		 id="input_cross_filter_trainNbr" data-bind=" value: searchModle().filterTrainNbr, event:{keyup: trainNbrChange}">
												</div> 
												 <label for="exampleInputEmail3" class="control-label pull-left" style="margin-left: 20px;" >
													本局相关:</label>
												<div class="pull-left" style="margin-left: 5px;">
													<input type="checkBox" class="pull-left" class="form-control"
													value="1" data-bind="checked: searchModle().currentBureanFlag"
													style="width: 20px;" class="form-control">
												</div>
												<div class="pull-left" style="margin-left: 10px;">
														<a type="button" class="btn btn-success" data-toggle="modal"
															data-target="#" id="btn_cross_search"  data-bind="click: loadCrosses"><i class="fa fa-search"></i>查询</a> 
													</div>
											</div>
										 
										 <div id="plan_cross_default_panel" class="panel panel-default" style="height:575px;margin-top:10px"> 
											<!-- <div class="row" style="margin-top:10px">
												<div class="form-group"
													style="margin-left: 20px;">
													<a type="button" data-bind="attr:{class: searchModle().checkActiveFlag() == 1 ? 'btn btn-success' : 'btn btn-success disabled'}, click: checkCrossInfo"  data-toggle="modal"
														data-target="#" id="btn_cross_sure">审核</a>
													<a  type="button" class="btn btn-success" data-toggle="modal" style="margin-left: 2px;" 
														data-target="#" id="btn_cross_createTrainLines" data-bind="attr:{class: searchModle().activeFlag() == 1 ? 'btn btn-success' : 'btn btn-success disabled'}, click: createTrainLines">生成运行线</a>
												</div> 
										   </div>  -->
										  <div style="margin-left:10px;margin-top:5px;margin-bottom:10px;">相关局: <span style="margin-top:5px;margin-left:5px;" data-bind="html: currentCross().relevantBureauShowValue()"></span></div> 
									      <div id="row_table" class="row" style="height:530px;margin-left:5px; margin-right:5px;overflow: auto;">
										     <div class="table-responsive" > 
												<table class="table table-bordered table-striped table-hover" id="yourTableID2" width="100%" border="0" cellspacing="0" cellpadding="0"  >
													<thead>
														<tr style="height: 25px"> 
															<th align="center" style="width: 25px">序号</th>
															<th align="center" style="width: 40px">担当</th>
															<th align="center" style="width: 100px">车次</th>
															<th align="center" style="width: 100px">始发站</th>
															<th align="center" style="width: 100px">终到站</th>
															<th align="center" style="width: 300px">来源</th>
														</tr>
													</thead>
													<tbody data-bind="foreach: planTrainLkRows">
														<tr data-bind="click: $parent.showTrains, style:{color: $parent.currentCross().planTrainId == planTrainId ? 'blue':''}" >
													        <td data-bind="text: $index() + 1"></td>
													        <td data-bind="text: tokenVehBureauShowValue"></td>
													        <td data-bind="text: trainNbr, attr:{title: trainNbr}"></td>
													        <td data-bind="text: startStn, attr:{title: startStn}"></td>
													        <td data-bind="text: endStn, attr:{title: endStn}"></td>
													        <td data-bind="text: cmdShortInfo, attr:{title: cmdShortInfo}"></td>
														</tr> 
													</tbody> 
												</table>
										</div>  
								  </div> 
								 </div> 
						 </div> 
					</div>
				</div> 
		 <div class="pull-right" style="width: 69%;">  
				<!-- Nav tabs -->
				<div class="panel panel-default" >  
					<ul class="nav nav-tabs" style="margin-top:10px;margin-left:5px;margin-right:5px;"  >
					  <li class="active"><a style="padding:3px 10px;" href="#home" data-toggle="tab">运行图</a></li>
					  <li style="float:right">  					 
						<input type="checkbox" class="pull-left" class="form-control" data-bind="checked: isShowRunPlans, event:{change: showRunPlans}"
							class="form-control"> 
										<label for="exampleInputEmail5" class="control-label pull-left">
										显示开行情况</label>  
									 </li>
					</ul> 
					<!-- Tab panes -->
					<div class="tab-content" >
					  <div class="tab-pane active" id="home"> 
					       <div> 
							 <div>
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
								                       
								         </div>
							          </form>
								    </div> 
							        <div id="canvas_parent_div" class="table-responsive" style="width:100%;height:560px;overflow-x:auto; overflow-y:auto;">
							        	<canvas id="canvas_event_getvalue"></canvas>
							        </div> 
							      </div> 
						</div> 
				  </div>
				  </div>  
				 </div> 
			</div>
		</div> 
   	 	<div class="row" class="panel-collapse" style="margin: 0px 5px 5px 16px;" >  
   	 	  <div class="panel panel-default">   
	 	    <div id="learn-more-content">
              <div class="panel-body" style="padding:5px;">
	 	   	    <ul class="nav nav-tabs" >
				  <li class="active"><a style="padding:3px 10px;" href="#runPlan" data-toggle="tab">开行情况</a></li> 
				</ul> 
				<!-- Tab panes -->
				<div class="tab-content" style="height:200px;overflow: auto">
				  <div class="tab-pane active" id="runPlan" > 
				  		 <div id="plan_view_div_palnDayDetail" class="panel panel-default"> 
								      <!--panle-heading-->
						      <div style="bapadding:10px;overflow: auto">
						      	<div class="table-responsive" > 
						          <table class="table table-bordered table-striped table-hover" id="run_plan_table">
							      <thead> 
							      	<tr data-bind="template: { name: 'runPlanTableDateHeader', foreach: planDays }"></tr>
							      	<tr data-bind="template: { name: 'runPlanTableWeekHeader', foreach: planDays }" ></tr>
							      </thead>
							      <tbody data-bind="template: { name: 'runPlanTableVlaues', foreach: trainPlans }"> 
							      </tbody> 
								  </table>
						        </div>  
						        </div>  
						      </div>
							</div> 
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
					            <table class="table table-bordered table-striped table-hover" id="plan_runline_table_trainLine_alltimes">
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
	   
	   
	   
	   
	<!--调整时刻表-->
	<div id="run_plan_train_times_edit_dialog" class="easyui-dialog" title="调整时刻表"
		data-options="iconCls:'icon-save'"
		style="width: 800px; height: 400px;overflow: hidden;">
		 <iframe style="width: 100%; height: 395px;border: 0;overflow: hidden;" src=""></iframe>
	</div>
	
	<div id="run_plan_train_crew_dialog" class="easyui-dialog" title="调整时刻表"
		data-options="iconCls:'icon-save'"
		style="width: 1200px; height: 400px;overflow: hidden;">
		 <iframe style="width: 100%; height: 395px;border: 0;overflow: hidden;" src=""></iframe>
	</div>
	
	   
	   

 
<!-- Custom styles for this template -->   
<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>	   
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.freezeheader.js"></script>  
<script type="text/javascript" src="<%=basePath %>/assets/js/chromatable_1.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/util/fishcomponent.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/util/canvas.util.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/util/canvas.component.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/knockout.pagemodle.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/runPlanLk/canvas_rightmenu.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/runPlanLk/canvas_event_runplanlk.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/runPlanLk/runPlanLk_main.js"></script>


<script>

    $(document).ready(function () {
        //$("#yourTableID2").chromatable({
            //width: "100%", // specify 100%, auto, or a fixed pixel amount
            //height: "580px",
            //scrolling: "yes" // must have the jquery-1.3.2.min.js script installed to use
        //});
        

        $("#plan_runline_table_trainLine").chromatable({
            width: "100%", // specify 100%, auto, or a fixed pixel amount
            height: "500px",
            scrolling: "yes" // must have the jquery-1.3.2.min.js script installed to use
        });
        

        $("#plan_runline_table_trainLine_alltimes").chromatable({
            width: "100%", // specify 100%, auto, or a fixed pixel amount
            height: "500px",
            scrolling: "yes" // must have the jquery-1.3.2.min.js script installed to use
        });
        
        
        
    });
</script>

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



<script  type="text/html" id="runPlanTableDateHeader"> 
   	<!-- ko if: $index() == 0 -->
 	<td rowspan="2" width="100px"></td>
 	<!-- /ko -->
 	<td align='center' data-bind="text: day"></td>
</script>
<script  type="text/html" id="runPlanTableWeekHeader">  
 	<td align='center' data-bind="html: week, style:{color: (weekDay==0||weekDay==6) ? 'blue':''}"></td>
</script>
<script  type="text/html" id="runPlanTableVlaues">
 <tr data-bind="foreach: runPlans">
    <!-- ko if: $index() == 0 --> 
 	<td data-bind="text: $parent.trainNbr, attr:{title: $parent.trainNbr}"></td>
 	<!-- /ko -->  
 	<td style="vertical-align: middle;" align='center' data-bind="html: runFlagStr"></td>
 </tr> 
</script>
</html>

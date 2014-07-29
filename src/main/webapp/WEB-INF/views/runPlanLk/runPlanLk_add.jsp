<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<% 
String basePath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>既有临客上图</title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath %>/assets/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath %>/assets/css/table.scroll.css">


<!-- 自动补全插件 -->
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>/assets/plugins/textextjs/css/textext.core.css"/>
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>/assets/plugins/textextjs/css/textext.plugin.tags.css"/>
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>/assets/plugins/textextjs/css/textext.plugin.autocomplete.css"/>

</head>
<body class="Iframe_body">
<ol class="breadcrumb">
  <span><i class="fa fa-anchor"></i>当前位置：</span>
  <li><a href="javascript:void(0);">既有临客上图</a></li>
</ol>
<!--以上为必须要的--> 
<div class="panel panel-default">
	<div class="panel-body">
		<div class="bs-example bs-example-tabs">
		      <ul id="myTab" class="nav nav-tabs">
		        <li class="active"><a href="#pageDiv_ddj" data-toggle="tab" data-bind="click:cleanPageData">本局担当</a></li>
		        <li class=""><a href="#pageDiv_bjxg" data-toggle="tab" data-bind="click:cleanPageData">外局担当</a></li>
		      </ul>
		    <div id="myTabContent" class="tab-content" >
		  	<!--tab1 开始-->
			  <div id="pageDiv_ddj" class="tab-pane fade active in" >
			  		<!--  -->
					  <div style="margin-right:-590px; float:left; width:100%;">
					    <!--分栏框开始-->
					    <div class="panel panel-default" style="margin-right:590px;">
							<div class="row" style="padding-top:10px;padding-bottom:10px;">
							  <form class="form-horizontal" role="form">
							  	<div class="row">
							  		<div class="pull-left">
								  		<div class="row">
								  			<div class="row" style="width: 100%; margin-top: 5px;">
										  		<div class="form-group" style="float:left;margin-left:20px;margin-bottom:0;">
										  			<label for="exampleInputEmail2" class="control-label pull-left">发令起始日期:&nbsp;</label>
												    <div class="pull-left">
												        <input id="runPlanLk_cmd_input_startDate" type="text" class="form-control" style="width:100px;" placeholder="">
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:12px;">&nbsp;&nbsp;局令号:&nbsp;</label>
												    <div class="pull-left">
												        <input type="text" class="form-control" style="width:100px;" data-bind="value: searchModle().cmdNbrBureau">
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:12px;">&nbsp;&nbsp;部令号:&nbsp;</label>
												    <div class="pull-left">
												        <input type="text" class="form-control" style="width:100px;" data-bind="value: searchModle().cmdNbrSuperior">
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:27px;">&nbsp;&nbsp;车次:&nbsp;</label>
												    <div class="pull-left">
												        <input type="text" class="form-control" style="width:100px;" data-bind="value: searchModle().trainNbr">
												    </div>
										  		</div>
										  	</div>
										  	<div class="row" style="width: 100%; margin-top: 5px;">
										  		<div class="form-group" style="float:left;margin-left:20px;margin-bottom:0;">
										  			<label for="exampleInputEmail2" class="control-label pull-left">发令终止日期:&nbsp;</label>
												    <div class="pull-left">
												        <input id="runPlanLk_cmd_input_endDate" type="text" class="form-control" style="width:100px;" placeholder="">
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left">&nbsp;&nbsp;命令类型:&nbsp;</label>
												    <div class="pull-left">
												    	<select class="form-control" style="width: 100px;display:inline-block;"
															 data-bind="options: [{'code': 'all', 'text': ''},{'code': '1', 'text': '既有线临客加开'},{'code': '2', 'text': '高铁临客加开'}], value: searchModle().cmdTypeOption, optionsText: 'text',optionsValue:'code'">
														</select>
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left">&nbsp;&nbsp;选线状态:&nbsp;</label>
												    <div class="pull-left">
												    	<select class="form-control" style="width: 100px;display:inline-block;"
															 data-bind="options: [{'code': 'all', 'text': ''},{'code': '1', 'text': '已'},{'code': '0', 'text': '未'}], value: searchModle().selectStateOption, optionsText: 'text',optionsValue:'code'">
														</select>
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left">&nbsp;&nbsp;生成状态:&nbsp;</label>
												    <div class="pull-left">
												    	<select class="form-control" style="width: 100px;display:inline-block;"
															 data-bind="options: [{'code': 'all', 'text': ''},{'code': '1', 'text': '已'},{'code': '0', 'text': '未'}], value: searchModle().createStateOption, optionsText: 'text',optionsValue:'code'">
														</select>
													</div>
										  			
										  		</div>
										  	</div>
										  	
								  		</div>
							  		</div>
							  		<div style="float:left;margin-left:20px;margin-top: 25px;margin-bottom:0;vertical-align: middle">
							  			<a type="button" href="#" class="btn btn-success" data-bind="click : queryListBjdd" style="float:left;margin-left:20px;margin-bottom:0;"><i class="fa fa-search"></i>查询</a>
							  		</div>
							  	</div>
							  </form>
							</div>
					      
					      <div class="panel-body">
					        <div class="row" style="margin-bottom:10px;">
					          <button type="button" class="btn btn-success btn-xs" data-toggle="modal" data-bind="click: loadTrainInfoFromJbt"><i class="fa fa-plus"></i>选线</button>
					          <button type="button" class="btn btn-success btn-xs" data-toggle="modal" data-bind="click: batchCreateRunPlanLine" data-target="#saveHightLineCrewModal"><i class="fa fa-pencil-square-o"></i> 生成开行计划</button>
					        </div>
					        <div class="table-responsive table-hover">
					          <table id ="runPlanLkCMD_table" class="table table-bordered table-striped table-hover">
					            <thead>
					              <tr>
					                <th class="text-center" style="vertical-align: middle;width:20px"><input id="checkbox_selectAll" type="checkbox" data-bind="checked: isSelectAll, event:{change: checkBoxSelectAllChange}"></th>
					                <th class="text-center" style="vertical-align: middle;width:35px">序号</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">命令类型</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">发令日期</th>
					                <th class="text-center" style="vertical-align: middle">局令号</th>
					                <th class="text-center" style="vertical-align: middle;width:30px"">项号</th>
					                <th class="text-center" style="vertical-align: middle">部令号</th>
					                <th class="text-center" style="vertical-align: middle">车次</th>
					                <th class="text-center" style="vertical-align: middle">始发站</th>
					                <th class="text-center" style="vertical-align: middle">终到站</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">起始日期</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">终止日期</th>
					                <th class="text-center" style="vertical-align: middle">规律</th>
					                <th class="text-center" style="vertical-align: middle">择日</th>
					                <th class="text-center" style="vertical-align: middle">途经局</th>
					                <th class="text-center" style="vertical-align: middle;width:30px">选线<br>状态</th>
					                <th class="text-center" style="vertical-align: middle;width:30px">生成<br>状态</th>
					              </tr>
					            </thead>
					            <tbody data-bind="foreach: runPlanLkCMDRows">
					              <tr data-bind="style:{color: $parent.currentCmdTxtMl().cmdTxtMlItemId == cmdTxtMlItemId ? 'blue':''}">
					              	<td><input name="cmd_list_checkbox" type="checkbox" data-bind="value : cmdTxtMlItemId"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: ($index() + 1)"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdType, attr:{title: cmdType}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdTime, attr:{title: cmdTime}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdNbrBureau, attr:{title: cmdNbrBureau}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdItem, attr:{title: cmdItem}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdNbrSuperior, attr:{title: cmdNbrSuperior}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: trainNbr, attr:{title: trainNbr}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: startStn, attr:{title: startStn}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: endStn, attr:{title: endStn}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: startDate, attr:{title: startDate}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: endDate, attr:{title: endDate}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: rule, attr:{title: rule}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: selectedDate, attr:{title: selectedDate}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: passBureau, attr:{title: passBureau}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: selectStateStr, attr:{title: selectStateStr}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: createStateStr, attr:{title: createStateStr}"></td>
					              </tr>
					            </tbody>
					          </table>
					        </div>
					      </div>
					      <!--panel-body--> 
					      
					    </div>
					    
					    <!--分栏框结束--> 
					  </div>
					  <!--乘务计划end--> 
					
					
					
					
					  <!--列车开行计划-->
					  <div class="pull-right" style="width:580px;"> 
					    <!--分栏框开始-->
					    <div class="panel panel-default">
					      <div class="panel-heading" >
					        <h3 class="panel-title" > <i class="fa fa-user-md"></i>列车时刻表</h3>
					      </div>
					      <!--panle-heading-->
					      <div class="panel-body" style="padding:5px 5px;">
							<div class="row" style="width: 100%; margin-top: 5px;margin-bottom: 5px;">
						  		<div class="form-group" style="float:left;margin-left:3px;margin-bottom:0;">
						  			<label for="exampleInputEmail2" class="control-label pull-left">途经局:&nbsp;</label>
								    <div class="pull-left">
								        <textarea id="runPlanLk_cmd_input_tjj" class="example" style="width:500px;height:23px" rows="1"></textarea>
									</div>
									<!-- <div class="pull-left" style="margin-left:345px;">
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: saveCmdTxtMl"><i class="fa fa-floppy-o"></i> 保存</button>
					          		</div> -->
						  		</div>
						  	</div>
						  	<div class="row" style="width: 100%; margin-top: 5px;margin-bottom: 5px;">
						  		<div class="form-group" style="float:left;margin-left:3px;margin-bottom:0;">
									<div class="pull-left" style="margin-left:3px;">
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: saveCmdTxtMl"><i class="fa fa-floppy-o"></i> 保存</button>
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: up"><i class="fa fa-arrow-up"></i> 上移</button>
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: down"><i class="fa fa-arrow-down"></i> 下移</button>
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: insertTrainStnRow"><i class="fa fa-plus"></i> 插入行</button>
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: addTrainStnRow"><i class="fa fa-plus"></i> 追加行</button>
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: deleteTrainStnRow"><i class="fa fa-minus-square"></i> 删除行</button>
					          		</div>
						  		</div>
						  	</div>
					        <div class="table-responsive table-hover" style="height: 700px; overflow-y:auto;">
					          <table id="runPlanLkCMD_trainStn_table" border="0" class="table table-bordered table-striped table-hover">
					            <thead>
					              <tr>
					                <th style="width:30px;">序号</th>
					                <th style="width:200px">站名</th>
					                <th style="width:80px">到达车次</th>
					                <th style="width:80px">出发车次</th>
					                <th style="width:40px">路局</th>
					                <th style="width:80px">到达时间</th>
					                <th style="width:80px">出发时间</th>
					                <th style="width:120px">股道</th>  
					                <th style="width:120px">站台</th>  
					              </tr>
					            </thead>
								<tbody data-bind="foreach: runPlanLkCMDTrainStnRows">
					              <tr data-bind="style:{color: $root.currentCmdTrainStn().childIndex == childIndex ? 'blue':''}">
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, text: ($index() + 1)"></td>
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: stnName}"><input class="form-control" data-bind="click: $parent.setCMDTrainStnCurrentRec, value: stnName"/></td>
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: arrTrainNbr}"><input class="form-control" data-bind="click: $parent.setCMDTrainStnCurrentRec, value: arrTrainNbr"/></td>
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: dptTrainNbr}"><input class="form-control" data-bind="click: $parent.setCMDTrainStnCurrentRec, value: dptTrainNbr"/></td>
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, text: stnBureau"></td>
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: arrTime}"><input class="form-control" data-bind="click: $parent.setCMDTrainStnCurrentRec, value: arrTime"/></td>
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: dptTime}"><input class="form-control" data-bind="click: $parent.setCMDTrainStnCurrentRec, value: dptTime"/></td>
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: trackNbr}"><input class="form-control" data-bind="click: $parent.setCMDTrainStnCurrentRec, value: trackNbr"/></td>
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: platform}"><input class="form-control" data-bind="click: $parent.setCMDTrainStnCurrentRec, value: platform"/></td>
					              </tr>
					            </tbody>
					          </table>
					        </div>
					      </div>
					      <!--panel-body--> 
					      
					    </div>
					    
					    <!--分栏框结束--> 
					  </div>
					  <!--列车开行计划 end--> 
  
  
			  		
			  		
			  		
			  		<!-- tab1内容结束 -->
			  </div>
		  	<!--tab1  结束--> 
	        <!--tab2   开始-->
	        <div id="pageDiv_bjxg" class="tab-pane fade">
	        	<!-- tab2内容开始 -->
	        		<!--  -->
					  <div style="margin-right:-590px; float:left; width:100%;">
					    <!--分栏框开始-->
					    <div class="panel panel-default" style="margin-right:590px;">
							<div class="row" style="padding-top:10px;padding-bottom:10px;">
							  <form class="form-horizontal" role="form">
							  	<div class="row">
							  		<div class="pull-left">
								  		<div class="row">
								  			<div class="row" style="width: 100%; margin-top: 5px;">
										  		<div class="form-group" style="float:left;margin-left:20px;margin-bottom:0;">
													<label for="exampleInputEmail2" class="control-label pull-left">发令起始日期:&nbsp;</label>
												    <div class="pull-left">
												        <input id="runPlanLk_cmd_input_startDate_wjdd" type="text" class="form-control" style="width:100px;" placeholder="">
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:12px;">&nbsp;&nbsp;部令号:&nbsp;</label>
												    <div class="pull-left">
												        <input type="text" class="form-control" style="width:100px;" data-bind="value: searchModle().cmdNbrSuperior">
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:27px;">&nbsp;&nbsp;车次:&nbsp;</label>
												    <div class="pull-left">
												        <input type="text" class="form-control" style="width:100px;" data-bind="value: searchModle().trainNbr">
												    </div>
													<label for="exampleInputEmail2" class="control-label pull-left">&nbsp;&nbsp;担当局:&nbsp;</label>
												    <div class="pull-left">
												    	<select style="width: 80px" class="form-control" data-bind="options:searchModle().bureauArray, value: searchModle().bureauOption, optionsText: 'text'"></select>
													</div>
										  		</div>
										  	</div>
										  	<div class="row" style="width: 100%; margin-top: 5px;">
										  		<div class="form-group" style="float:left;margin-left:20px;margin-bottom:0;">
													<label for="exampleInputEmail2" class="control-label pull-left">发令终止日期:&nbsp;</label>
												    <div class="pull-left">
												        <input id="runPlanLk_cmd_input_endDate_wjdd" type="text" class="form-control" style="width:100px;" placeholder="">
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left">&nbsp;&nbsp;命令类型:&nbsp;</label>
												    <div class="pull-left">
												    	<select class="form-control" style="width: 100px;display:inline-block;"
															 data-bind="options: [{'code': 'all', 'text': ''},{'code': '1', 'text': '既有线临客加开'},{'code': '2', 'text': '高铁临客加开'}], value: searchModle().cmdTypeOption, optionsText: 'text',optionsValue:'code'">
														</select>
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left">&nbsp;&nbsp;生成状态:&nbsp;</label>
												    <div class="pull-left">
												    	<select class="form-control" style="width: 100px;display:inline-block;"
															 data-bind="options: [{'code': 'all', 'text': ''},{'code': '1', 'text': '已'},{'code': '0', 'text': '未'}], value: searchModle().createStateOption, optionsText: 'text',optionsValue:'code'">
														</select>
													</div>
										  			
										  		</div>
										  	</div>
										  	
								  		</div>
							  		</div>
							  		<div style="float:left;margin-left:20px;margin-top: 25px;margin-bottom:0;vertical-align: middle">
							  			<a type="button" href="#" class="btn btn-success" data-bind="click : queryListWjdd" style="float:left;margin-left:20px;margin-bottom:0;"><i class="fa fa-search"></i>查询</a>
							  		</div>
							  	</div>
							  </form>
							</div>
					      
					      <div class="panel-body">
					        <div class="table-responsive table-hover">
					          <table id ="runPlanLkCMD_table_wjdd" class="table table-bordered table-striped table-hover">
					            <thead>
					              <tr>
					                <th class="text-center" style="vertical-align: middle;width:35px">序号</th>
					                <th class="text-center" style="vertical-align: middle">部令号</th>
					                <th class="text-center" style="vertical-align: middle">车次</th>
					                <th class="text-center" style="vertical-align: middle">始发站</th>
					                <th class="text-center" style="vertical-align: middle">终到站</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">起始日期</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">终止日期</th>
					                <th class="text-center" style="vertical-align: middle">规律</th>
					                <th class="text-center" style="vertical-align: middle">择日</th>
					                <th class="text-center" style="vertical-align: middle">途经局</th>
					                <th class="text-center" style="vertical-align: middle">担当局</th>
					                <th class="text-center" style="vertical-align: middle">局令号</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">发令日期</th>
					                <th class="text-center" style="vertical-align: middle;width:30px">生成<br>状态</th>
					              </tr>
					            </thead>
					            <tbody data-bind="foreach: runPlanLkCMDRows">
					              <tr data-bind="style:{color: $parent.currentCmdTxtMl().cmdTxtMlItemId == cmdTxtMlItemId ? 'blue':''}">
					                <td data-bind="click: $parent.setCurrentRec, text: ($index() + 1)"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdNbrSuperior, attr:{title: cmdNbrSuperior}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: trainNbr, attr:{title: trainNbr}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: startStn, attr:{title: startStn}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: endStn, attr:{title: endStn}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: startDate, attr:{title: startDate}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: endDate, attr:{title: endDate}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: rule, attr:{title: rule}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: selectedDate, attr:{title: selectedDate}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: passBureau, attr:{title: passBureau}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdBureau, attr:{title: cmdBureau}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdNbrBureau, attr:{title: cmdNbrBureau}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdTime, attr:{title: cmdTime}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: createStateStr, attr:{title: createStateStr}"></td>
					              </tr>
					            </tbody>
					          </table>
					        </div>
					      </div>
					      <!--panel-body--> 
					      
					    </div>
					    
					    <!--分栏框结束--> 
					  </div>
					  <!--乘务计划end--> 
					
					
					
					
					  <!--列车开行计划-->
					  <div class="pull-right" style="width:580px;"> 
					    <!--分栏框开始-->
					    <div class="panel panel-default">
					      <div class="panel-heading" >
					        <h3 class="panel-title" > <i class="fa fa-user-md"></i>列车时刻表</h3>
					      </div>
					      <!--panle-heading-->
					      <div class="panel-body" style="padding:5px 5px;">
							<div class="row" style="width: 100%; margin-top: 5px;margin-bottom: 5px;">
						  		<div class="form-group" style="float:left;margin-left:3px;margin-bottom:0;">
						  			<label for="exampleInputEmail2" class="control-label pull-left">途经局:&nbsp;</label>
								    <div class="pull-left">
								        <label id="runPlanLk_cmd_input_tjj_wjdd" class="control-label pull-left" data-bind="html:currentCmdTxtMl().passBureau" style="width:500px;height:23px"></label>
									</div>
									<!-- <div class="pull-left" style="margin-left:345px;">
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: saveCmdTxtMl"><i class="fa fa-floppy-o"></i> 保存</button>
					          		</div> -->
						  		</div>
						  	</div>
						  	<div class="row" style="width: 100%; margin-top: 5px;margin-bottom: 5px;">
						  		<div class="form-group" style="float:left;margin-left:3px;margin-bottom:0;">
									<div class="pull-left" style="margin-left:3px;">
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: saveOtherCmdTxtMl"><i class="fa fa-floppy-o"></i> 保存</button>
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: up"><i class="fa fa-arrow-up"></i> 上移</button>
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: down"><i class="fa fa-arrow-down"></i> 下移</button>
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: insertTrainStnRow"><i class="fa fa-plus"></i> 插入行</button>
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: addTrainStnRow"><i class="fa fa-plus"></i> 追加行</button>
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: deleteTrainStnRow"><i class="fa fa-minus-square"></i> 删除行</button>
					          		</div>
						  		</div>
						  	</div>
					        <div class="table-responsive table-hover" style="height: 700px; overflow-y:auto;">
					          <table id="runPlanLkCMD_trainStn_table" border="0" class="table table-bordered table-striped table-hover">
					            <thead>
					              <tr>
					                <th style="width:30px;">序号</th>
					                <th style="width:200px">站名</th>
					                <th style="width:80px">到达车次</th>
					                <th style="width:80px">出发车次</th>
					                <th style="width:40px">路局</th>
					                <th style="width:80px">到达时间</th>
					                <th style="width:80px">出发时间</th>
					                <th style="width:120px">股道</th>  
					                <th style="width:120px">站台</th>  
					              </tr>
					            </thead>
								<tbody data-bind="foreach: runPlanLkCMDTrainStnRows">
					              <tr data-bind="style:{color: $root.currentCmdTrainStn().childIndex == childIndex ? 'blue':''}">
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, text: ($index() + 1)"></td>
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: stnName}"><input class="form-control" data-bind="click: $parent.setCMDTrainStnCurrentRec, value: stnName"/></td>
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: arrTrainNbr}"><input class="form-control" data-bind="click: $parent.setCMDTrainStnCurrentRec, value: arrTrainNbr"/></td>
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: dptTrainNbr}"><input class="form-control" data-bind="click: $parent.setCMDTrainStnCurrentRec, value: dptTrainNbr"/></td>
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, text: stnBureau"></td>
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: arrTime}"><input class="form-control" data-bind="click: $parent.setCMDTrainStnCurrentRec, value: arrTime"/></td>
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: dptTime}"><input class="form-control" data-bind="click: $parent.setCMDTrainStnCurrentRec, value: dptTime"/></td>
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: trackNbr}"><input class="form-control" data-bind="click: $parent.setCMDTrainStnCurrentRec, value: trackNbr"/></td>
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: platform}"><input class="form-control" data-bind="click: $parent.setCMDTrainStnCurrentRec, value: platform"/></td>
					              </tr>
					            </tbody>
					          </table>
					        </div>
					      </div>
					      <!--panel-body--> 
					      
					    </div>
					    
					    <!--分栏框结束--> 
					  </div>
					  <!--列车开行计划 end--> 
  
	        		
	        	<!-- tab2内容结束 -->
	        </div>
	        <!--tab2  结束-->
	        
	        </div>
		</div>
	</div>
</div>


<!--选线按钮点击事件打开   基本图查询界面-->
<div id="jbt_traininfo_dialog" class="easyui-dialog" title="调整时刻表"
	data-options="iconCls:'icon-save'"
	style="width: 1200px; height: 500px;overflow: hidden;">
 	<iframe style="width: 100%; height: 395px;border: 0;overflow: hidden;" src=""></iframe>
</div>




<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>
<!-- 自动补全插件 -->
<script type="text/javascript" src="<%=basePath %>/assets/plugins/textextjs/js/textext.core.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/plugins/textextjs/js/textext.plugin.tags.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/plugins/textextjs/js/textext.plugin.autocomplete.js"></script>

<script type="text/javascript" src="<%=basePath %>/assets/js/chromatable_1.js"></script>

<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/runPlanLk/runPlanLk_cmd_add.js"></script>
</body>
</html>
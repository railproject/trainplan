<%@ page contentType="text/html;charset=UTF-8" %> 
<!DOCTYPE HTML>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>核查编制图定开行</title>
<!-- Bootstrap core CSS -->
<link href="assets/css/cross/custom-bootstrap.css" rel="stylesheet">
<!--font-awesome-->
<link href="assets/css/datepicker.css" rel="stylesheet">
<link href="assets/easyui/themes/default/easyui.css"
	rel="stylesheet">
<link href="assets/easyui/themes/icon.css" rel="stylesheet">
<link type="text/css" rel="stylesheet"
	href="assets/css/font-awesome.min.css" />
<link type="text/css" rel="stylesheet"
	href="assets/css/datepicker.css">
<!-- Custom styles for this template -->
<link href="assets/css/style.css" rel="stylesheet">
<script src="assets/js/jquery.js"></script>
<script src="assets/js/html5.js"></script>
<script src="assets/js/bootstrap.min.js"></script>
<script src="assets/js/respond.min.js"></script>
<script src="assets/js/jquery.dataTables.js"></script>
<script src="assets/easyui/jquery.easyui.min.js"></script>
<link href="assets/easyui/themes/icon.css" rel="stylesheet">
<link type="text/css" rel="stylesheet"
	href="assets/css/font-awesome.min.css" />
<link type="text/css" rel="stylesheet"
	href="assets/css/datepicker.css">
<!-- Custom styles for this template -->
<link href="assets/css/style.css" rel="stylesheet">
<script src="assets/easyui/jquery.easyui.min.js"></script>
<script src="assets/js/knockout.js"></script>
<script src="assets/js/jquery.jeditable.js"></script>
<script src="assets/js/jquery.dataTables.editable.js"></script>
<style type="text/css">
.form-control {  
	height: 20px;  
}

.btn { 
	padding: 1px 10px; 
	font-size: 11px;
}

.control-label{
	margin-top: 0;
	margin-bottom: 0;
	padding-top: 0px;
}

.form-control{
	padding: 1px 1px;
}

。tbody > tr > td, .table tfoot > tr > td {
	padding: 2px 3px;
	line-height: 0.628571429;
	vertical-align: top;
	border-top: 1px solid #d1d2d4;
}

.panel {
	margin-bottom: 0px;
}
 
th {
text-align: center;
}  
 
 
</style>


<script type="text/javascript">
	$(function() {
		$("#file_upload_dlg").dialog("close"); 

		function CrossModel() {
			var self = this;
			//列车列表
			self.trains = ko.observableArray();
			//交路列表
			self.rows = ko.observableArray();
			self.currentTrain = null;

			var rowLookup = {};
			var rowLooktrains = {};

			self.loadCrosses = function(crosses) {
				for ( var i = 0; i < crosses.length; i++) {
					var row = new CrossRow(crosses[i]);
					self.rows.push(row);
					rowLookup[row.name] = row;
				}
			};

			self.showTrains = function(row) {
				self.trains.remove(function(item) {
					return true;
				});
				self.currentTrain =  ko.observable(rowLookup[row.name]);
				var trains = rowLookup[row.name].trains;
				for ( var i = 0; i < trains.length; i++) {
					var row = new TrainRow(trains[i]);
					self.trains.push(row);
					rowLooktrains[row.name] = row;
				}

			};
		}
		;

		function CrossRow(data) {
			var self = this;
			self.index = data.index;
			self.name = data.name;
			self.trains = data.train;

			self.showTrains = function() {
				for ( var i = 0; i < self.trains.length; i++) {
					var row = new TrainRow(self.trains[i]);

				}
			};
		}
		;

		function TrainModel() {
			var self = this;
			self.rows = ko.observableArray();
			self.rowLookup = {};
		}

		function TrainRow(data) {
			var self = this;
			self.index = data.index;
			self.name = data.name;
			self.index2 = data.index2;
			self.index3 = data.index3;
			self.index4 = data.index4;
			self.index5 = data.index5;
			self.index6 = data.index6;
			self.index7 = data.index7;
			self.index8 = data.index8;
			self.index9 = data.index9;
			self.index10 = data.index10;

			self.update = function(newPrice) {

			};
		}
		;
		var cross = new CrossModel()
		ko.applyBindings(cross);
		cross.loadCrosses([ {
			"index" : 1,
			"name" : "G1",
			"train" : [ {
				"index" : "1",
				"name" : "G1",
				"index2" : "1",
				"index3" : "1",
				"index4" : "1",
				"index5" : "1",
				"index6" : "1",
				"index7" : "1",
				"index8" : "1",
				"index9" : "1",
				"index10" : "1"
			} ]
		}, {
			"index" : 2,
			"name" : "G2",
			"train" : [ {
				"index" : "1",
				"name" : "G2",
				"index2" : "1",
				"index3" : "1",
				"index4" : "1",
				"index5" : "1",
				"index6" : "1",
				"index7" : "1",
				"index8" : "1",
				"index9" : "1",
				"index10" : "1"
			} ]
		}, {
			"index" : 3,
			"name" : "G3",
			"train" : [ {
				"index" : "1",
				"name" : "G3",
				"index2" : "1",
				"index3" : "1",
				"index4" : "1",
				"index5" : "1",
				"index6" : "1",
				"index7" : "1",
				"index8" : "1",
				"index9" : "1",
				"index10" : "1"
			} ]
		}, {
			"index" : 4,
			"name" : "G4",
			"train" : [ {
				"index" : "1",
				"name" : "G4",
				"index2" : "1",
				"index3" : "1",
				"index4" : "1",
				"index5" : "1",
				"index6" : "1",
				"index7" : "1",
				"index8" : "1",
				"index9" : "1",
				"index10" : "1"
			} ]
		}, {
			"index" : 5,
			"name" : "G5",
			"train" : [ {
				"index" : "1",
				"name" : "G5",
				"index2" : "1",
				"index3" : "1",
				"index4" : "1",
				"index5" : "1",
				"index6" : "1",
				"index7" : "1",
				"index8" : "1",
				"index9" : "1",
				"index10" : "1"
			} ]
		} ]);

	})
	function openLogin() {
		$("#file_upload_dlg").dialog("open");
	}
</script>

</head>
<body class="Iframe_body" style="margin-left:50px;margin-right:50px;">
	<!--以上为必须要的-->

	<div id="file_upload_dlg" class="easyui-dialog" title="上传对数文件"
		data-options="iconCls:'icon-save'"
		style="width: 400px; height: 200px; padding: 10px">
		<form id="file_upload_id" name="file_upload_name" action="cross/fileUpload"
			method="post" enctype="multipart/form-data">
			<div>
				<input type="file"  name="fileName" />
			</div>
			<div>
				<input type="submit"  value="上传" />
			</div>
		</form>
	</div>
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
					        <div class="row" >
								<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left: 26px">
												方案:&nbsp;</label> 
								<div class="pull-left">
									<select style="width: 269px" id="input_plan_design_scheme"
										class="form-control">
									</select>
								</div>  
						  </div>
						   <div class="row" style="margin: 5px 0 5px 0;">
								<label for="exampleInputEmail2" class="control-label pull-left">启用日期:&nbsp;</label>
						        <div class="pull-left">
						           <input type="text" class="form-control" style="width:179px;" placeholder="" id="plan_construction_selectdate">
						        </div>
						        <button class="btn btn-primary" type="button" style="margin-left: 10px;"
								id="plan_construction_btnQuery" onclick="openLogin()">导入EXCEL</button>
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
								<div class="row">
									<div class="form-group"
										style="float: left; margin-left: 0px; margin-top: 0px;"> 
										<div class="row">
											<label for="exampleInputEmail3" class="control-label pull-left" >
												车辆担当局:</label>
											<div class="pull-left" style="margin-left: 5px;">
												<select style="width: 60px" id="input_plan_design_scheme"
													class="form-control">
												</select>
											</div>
											<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left: 20px;">
												始发局:</label>
											<div class="pull-left" style="margin-left: 5px;">
												<select style="width: 65px" id="input_plan_design_scheme"
													class="form-control">
												</select>
											</div>
										
										</div>   
										<div class="row"  style="margin-top: 5px;">
											<label for="exampleInputEmail3" class="control-label pull-left" >
												铁路线类型:</label>
											<div class="pull-left" style="margin-left: 5px;">
												<select style="width: 60px" id="input_plan_design_scheme"
													class="form-control">
													<option value="-1"></option>
													<option value="0">普线</option>
													<option value="1">高线</option>
													<option value="2">混合</option>
												</select>
											</div>
											<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left: 33px;">
												状态:</label>
											<div class="pull-left" style="margin-left: 5px;">
												<select style="width: 65px" id="input_plan_design_scheme"
													class="form-control">
													<option value="1">已审核</option>
													<option value="1">未审核</option>
												</select>
											</div>
											
											<div class="pull-right">
												<a type="button" class="btn btn-success" data-toggle="modal"
													data-target="#" id="plan_construction_search" style="margin-left: 15px;">查询</a>
											</div>
										</div>
								 
										<hr style="margin-top: 8px;margin-bottom: 8px">
										<div class="row"  style="margin-top: 5px;">
										     
											<label for="exampleInputEmail3" class="control-label pull-left">
												车次:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" style="width: 100px;"
													placeholder="车次" id="plan_construction_input_trainNbr">
											</div>
										 
											<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left: 20px;">
												交路名:&nbsp;</label>
											<div class="pull-left">
												<select style="width: 65px" id="input_plan_design_scheme"
													class="form-control">
													<option value="1">简称</option>
													<option value="1">全称</option>
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
												data-target="#" id="plan_construction_deleteCrossInfo">审核</a>
											<a type="button" class="btn btn-success" data-toggle="modal"
												data-target="#" id="plan_construction_deleteCrossInfo">删除</a>
											  <a type="button" class="btn btn-success" data-toggle="modal"
												data-target="#" id="plan_construction_createCrossLines">生成基本交路单元</a>
										</div> 
										<table class="table table-bordered table-striped table-hover" 
											id="plan_review_table_trainInfo">
											<thead>
												<tr style="height: 25px">
													<th style="width: 10%" align="center"><input type="checkbox" style="margin-top:0"></th>
													<th style="width: 15%" align="center">序号</th>
													<th style="width: 60%" align="center">车底交路名</th>
													<th style="width: 15%" align="center">状态</th>
												</tr>
											</thead>
											<tbody data-bind="foreach: rows">
												<tr data-bind="click: $parent.showTrains">
												    <td><input type="checkbox"></td>
													<td data-bind="text: index "></td>
													<td data-bind="text: name"></td>
													<td >已审核</td>
												</tr>
											</tbody>
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
							<form class="form-horizontal" role="form"> 
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
							<div class="pull-left">
								<div class="row" style="margin: 0px 0 5px 0;"> 
										<label for="exampleInputEmail3"
											class="control-label pull-left"> 车底交路名:&nbsp;</label>
										<div class="pull-left" style="margin-left: 26px;">
											<input type="text" class="form-control" style="width: 520px;"
												id="plan_construction_input_trainNbr">
										</div>
										<div class="pull-left">
											<input type="radio" class="pull-left" class="form-control"
												name="exampleInputEmail5"
												style="width: 20px; margin-left: 38px; margin-top: 5px"
												class="form-control">
										</div>
										<label for="exampleInputEmail5" class="control-label pull-left">
											普线</label> 
										<div class="pull-left">
											<input type="radio" class="pull-left" class="form-control"
												name="exampleInputEmail5"
												style="width: 20px; margin-left: 5px; margin-top: 5px"
												class="form-control">
										</div>
										<label for="exampleInputEmail5" class="control-label pull-left">
											高线</label> 
										<div class="pull-left">
											<input type="radio" class="pull-left" class="form-control"
												name="exampleInputEmail5"
												style="width: 20px; margin-left: 5px; margin-top: 5px"
												class="form-control">
										</div>
										<label for="exampleInputEmail5" class="control-label pull-left">
											混合</label> 
								</div>
								<div class="row" style="margin: 5px 0 0px 0;">
								    <label for="exampleInputEmail3"
											class="control-label pull-left"> 备用套跑交路名:&nbsp;</label>
									<div class="pull-left">
										<input type="text" class="form-control" style="width: 520px;"
											id="plan_construction_input_trainNbr">
									</div> 
									
								</div>
								<div class="row" style="margin: 5px 0 0px 0;"> 
									<label for="exampleInputEmail3"
										class="control-label pull-left"> 组数:&nbsp;</label>
									<div class="pull-left">
										<input type="text" class="form-control" style="width: 40px;">
									</div> 
									<label for="exampleInputEmail2" class="control-label pull-left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;对数:&nbsp;</label>
									<div class="pull-left">
										<input type="text" class="form-control" style="width: 40px;"
											placeholder="" id="plan_construction_selectdate">
									</div> 
									
									<label for="exampleInputEmail5" style="margin-left: 43px;" class="control-label pull-left">
										开行状态:</label>
											
									<div class="pull-left">
										<input type="radio" class="pull-left" class="form-control"
											name="exampleInputEmail5"
											style="width: 20px; margin-left: 5px; margin-top: 5px"
											class="form-control">
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left">
										开行</label>

									<div class="pull-left">
										<input type="radio" class="pull-left" class="form-control"
											name="exampleInputEmail5"
											style="width: 20px; margin-left: 5px; margin-top: 5px"
											class="form-control">
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left">
										备用</label>
									<div class="pull-left">
										<input type="radio" class="pull-left" class="form-control"
											name="exampleInputEmail5"
											style="width: 20px; margin-left: 5px; margin-top: 5px"
											class="form-control">
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left">
										停运</label> 
										
									<div class="pull-left">
										<input type="checkBox" class="pull-left" class="form-control"
											name="exampleInputEmail5"
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
											name="exampleInputEmail5"
											style="width: 20px; margin-left: 5px; margin-top: 5px"
											class="form-control">
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left">
										每日</label>
									<div class="pull-left">
										<input type="radio" class="pull-left" class="form-control"
											name="exampleInputEmail5"
											style="width: 20px; margin-left: 5px; margin-top: 5px"
											class="form-control">
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left">
										隔日</label>
									
									<label for="exampleInputEmail5" style="margin-left: 30px;" class="control-label pull-left">
										高线开行规律:</label>	
									<div class="pull-left">
										<input type="radio" class="pull-left" class="form-control"
											name="exampleInputEmail5"
											style="width: 20px; margin-left: 5px; margin-top: 5px"
											class="form-control">
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left">
										日常</label>
									<div class="pull-left">
										<input type="radio" class="pull-left" class="form-control"
											name="exampleInputEmail5"
											style="width: 20px; margin-left: 5px; margin-top: 5px"
											class="form-control">
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left">
										周末</label> 
									<div class="pull-left">
										<input type="radio" class="pull-left" class="form-control"
											name="exampleInputEmail5"
											style="width: 20px; margin-left: 5px; margin-top: 5px"
											class="form-control">
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left">
										高峰</label>  
									<label for="exampleInputEmail5" style="margin-left: 30px;" class="control-label pull-left">
										指定星期:&nbsp;</label>
									<div class="pull-left">
										<input type="text" class="form-control" style="width: 60px;"
											placeholder="" id="plan_construction_input_trainNbr">
									</div>
									
									<label for="exampleInputEmail5"  style="margin-left: 10px;" class="control-label pull-left">
										指定日期:&nbsp;</label>
									<div class="pull-left">
										<input type="text" class="form-control" style="width: 140px;"
											placeholder="" id="plan_construction_input_trainNbr">
									</div>
									 
									 
								</div>
								
								<div class="row" style="margin: 5px 0 0px 0;">
									<label for="exampleInputEmail3"
											class="control-label pull-left"> 车辆担当局:&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 30px;">
										</div>
									<label for="exampleInputEmail3"
											class="control-label pull-left" style=" margin-left: 20px;"> 车辆段/动车段:&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 110px;">
										</div>
									<label for="exampleInputEmail3"
											class="control-label pull-left" style=" margin-left: 20px;"> 动车所:&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 110px;">
										</div>
									<label for="exampleInputEmail3"
											class="control-label pull-left" style=" margin-left: 30px;"> 客运担当局:&nbsp;</label>
									<div class="pull-left">
										<input type="text" class="form-control" style="width: 30px;">
									</div>
									<label for="exampleInputEmail3"
										class="control-label pull-left" style=" margin-left: 20px;"> 客运段:&nbsp;</label>
									<div class="pull-left">
										<input type="text" class="form-control" style="width: 110px;">
									</div>
								</div>
								<div class="row" style="margin: 5px 0 0px 0;"> 
									<label for="exampleInputEmail3"
											class="control-label pull-left"> 机车类型:&nbsp;</label>
									<div class="pull-left">
										<input type="text" class="form-control" style="width: 50px;">
									</div>
									<label for="exampleInputEmail3"
											class="control-label pull-left" style=" margin-left: 13px;"> 动车组车型:&nbsp;</label>
									<div class="pull-left">
										<input type="text" class="form-control" style="width: 80px;">
									</div>
									<div class="pull-left">
										<input type="checkbox" class="pull-left" class="form-control"
											name="exampleInputEmail5"
											style="width: 20px; margin-left: 25px; margin-top: 5px"
											class="form-control">
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left">
										供电</label>
									<div class="pull-left">
										<input type="checkbox" class="pull-left" class="form-control"
											name="exampleInputEmail5"
											style="width: 20px; margin-left: 5px; margin-top: 5px"
											class="form-control">
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left">
										集便</label>
									<div class="pull-left">
										<input type="checkbox" class="pull-left" class="form-control"
											name="exampleInputEmail5"
											style="width: 20px; margin-left: 5px; margin-top: 5px"
											class="form-control">
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left">
										空调</label> 
									<label for="exampleInputEmail3" style="margin-left: 30px"
											class="control-label pull-left"> 备注:&nbsp;</label>
									<div class="pull-left">
										<input type="text" class="form-control" style="width: 235px;">
									</div> 
									  <a type="button" style="margin-left: 15px"
										class="btn btn-success" data-toggle="modal" data-target="#"
										id="cross_train_save"> 保存</a>
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
				<div class="row" style="margin: 15px 0 10px 10px;">
					<div class="pull-left" style="width: 100%%;">
					<section class="panel panel-default" >
				         <div class="panel-heading"><i class="fa fa-table"></i>列车信息</div>
				          <div class="panel-body" style="overflow-y:auto">
							<div class="table-responsive">
								<div class="form-group"
									style="margin-left: 10px; margin-top: 5px;">
									  <a type="button"
										class="btn btn-success" data-toggle="modal" data-target="#"
										id="cross_train_add">添加</a> <a type="button"
										class="btn btn-success" data-toggle="modal" data-target="#"
										id="cross_train_save"> 修改</a> <a type="button"
										class="btn btn-success" data-toggle="modal" data-target="#"
										id="cross_train_delete">删除</a>
										 <a type="button"
										class="btn btn-success" data-toggle="modal" data-target="#"
										id="cross_train_delete">时刻表</a>
										 <a type="button"
										class="btn btn-success" data-toggle="modal" data-target="#"
										id="cross_train_delete">详点</a>
										 <a type="button"
										class="btn btn-success" data-toggle="modal" data-target="#"
										id="cross_train_delete">交路图</a>
								</div>
								<table class="table table-bordered table-striped table-hover"
									id="cross_trainInfo">
									<thead>
										<tr>
											<th style="width: 3%">序号</th>
											<th style="width: 8%">车次</th>
											<th style="width: 8%">始发站</th>
											<th style="width: 5%">始发局</th>
											<th style="width: 8%">终到站</th>
											<th style="width: 5%">终到局</th>
											<th style="width: 5%">线型</th>
											<th style="width: 5%">间隔天数</th>
											<th style="width: 5%">开行状态</th>
											<th style="width: 5%">交替车次</th>
											<th style="width: 10%">交替时间</th>
											<th style="width: 5%%">备用套跑</th>
											<th style="width: 5%%">高线规律</th>
											<th style="width: 5%%">普线规律</th>
											<th style="width: 10%%">特殊规律</th>
										</tr>
									</thead>
									<tbody data-bind="foreach: trains">
										<tr>
											<td style="width: 5%" data-bind="text: $index"></td>
											<td style="width: 15%" data-bind="text: name"></td>
											<td style="width: 10%" data-bind="text: index2"></td>
											<td style="width: 5%" data-bind="text: index3"></td>
											<td style="width: 10%" data-bind="text: index4"></td>
											<td style="width: 5%" data-bind="text: index5"></td>
											<td style="width: 5%" data-bind="text: index6"></td>
											<td style="width: 5%" data-bind="text: index7"></td>
											<td style="width: 5%" data-bind="text: index8"></td>
											<td style="width: 15%" data-bind="text: index9"></td>
											<td style="width: 20%" data-bind="text: index10"></td>
											<td style="width: 5%" data-bind="text: index8"></td>
											<td style="width: 5%" data-bind="text: index9"></td>
											<td style="width: 5%" data-bind="text: index10"></td>
											<td style="width: 5%" data-bind="text: index10"></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div> 
					</section>
					</div>
					<!-- <div class="pull-right" style="width: 28%;margin: 0px 10px 10px 10px;"> -->
					<div style="display: none">
					  <section class="panel panel-default">
				         <div class="panel-heading"><i class="fa fa-table"></i>时刻表</div>
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
											<tr>
												<td>test</td>
												<td>test</td>
												<td>test</td>
												<td>test</td>
												<td>test</td>
											</tr>
											<tr>
												<td>test</td>
												<td>test</td>
												<td>test</td>
												<td>test</td>
												<td>test</td>
											</tr>
											<tr>
												<td>test</td>
												<td>test</td>
												<td>test</td>
												<td>test</td>
												<td>test</td>
											</tr>
											<tr>
												<td>test</td>
												<td>test</td>
												<td>test</td>
												<td>test</td>
												<td>test</td>
											</tr>
											<tr>
												<td>test</td>
												<td>test</td>
												<td>test</td>
												<td>test</td>
												<td>test</td>
											</tr>
										</tbody>
									</table> 
						  </div>
						</div> 
					</section>
					<!-- </div> -->
					</div>
				</div>
				</div>
			</div> 

</body>
</html>

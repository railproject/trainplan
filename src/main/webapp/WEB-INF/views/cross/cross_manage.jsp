<%@ page contentType="text/html;charset=UTF-8" %> 
<!DOCTYPE HTML>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>核查编制图定开行</title>
<!-- Bootstrap core CSS -->
<link href="assets/css/custom-bootstrap.css" rel="stylesheet">
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
			self.index = data.index
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
			var rowLookup = {};
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
<body class="Iframe_body">
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
		<span><i class="fa fa-anchor"></i>当前位置：</span>
		<li><a href="#">交路信息维护</a></li>
	</ol>
	
        
	<!--分栏框开始-->
	<div class="pull-left" style="width: 365px;">
		<!--分栏框开始-->
		<div id="plan_view_div_palnDayDetail" class="panel panel-default">
			<div class="row" style="margin: 15px 10px 10px 10px;">
			    <section class="panel panel-default">
			        <div class="panel-heading"><i class="fa fa-table"></i>方案名</div>
			        <div class="panel-body">
			        <form class="form-horizontal" role="form"> 
							 
							<button class="btn btn-primary" type="button"
								id="plan_construction_btnQuery">检查</button>
	
							<button class="btn btn-primary" type="button"
								id="plan_construction_btnQuery" onclick="openLogin()">上传</button>
							<div class="pull-left">
								<select style="width: 200px" id="input_plan_design_scheme"
									class="form-control">
								</select>
							</div> 
					</form>
			        </div>
			   </section>
				
			</div>
			<div class="row" style="margin: 15px 10px 10px 10px;">
			    <section class="panel panel-default">
			        <div class="panel-heading"><i class="fa fa-table"></i>交路列表</div>
			        <div class="panel-body">
						<form class="form-horizontal" role="form"> 
								<div class="row">
									<div class="form-group"
										style="float: left; margin-left: 10px; margin-top: 0px;">
										<div class="row" style="margin: 0px 0 10px 0;">
											<label for="exampleInputEmail3" class="control-label pull-left">
												车次：&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" style="width: 180px;"
													placeholder="输入车次查询" id="plan_construction_input_trainNbr">
											</div>
										</div>
										<div class="row" style="margin: 5px 0 10px 0;">
											<label for="exampleInputEmail3" class="control-label pull-left">
												始发局：&nbsp;</label>
											<div class="pull-left">
												<select style="width: 100px" id="input_plan_design_scheme"
													class="form-control">
												</select>
											</div>
										</div>
										<div class="row" style="margin: 5px 0 10px 0;">
											<label for="exampleInputEmail3" class="control-label pull-left">
												车辆担当局：&nbsp;</label>
											<div class="pull-left">
												<select style="width: 100px" id="input_plan_design_scheme"
													class="form-control">
												</select>
											</div>
											<div class="pull-right">
												<a type="button" class="btn btn-success" data-toggle="modal"
													data-target="#" id="plan_construction_search">查询</a>
											</div>
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail3" class="control-label pull-left">
												显示方式：&nbsp;</label>
											<div class="pull-left">
												<input type="radio" class="pull-left" class="form-control"
													name="exampleInputEmail5"
													style="width: 20px; margin-left: 10px; margin-top: 10px"
													class="form-control">
											</div>
											<label for="exampleInputEmail5" class="control-label pull-left">
												全称</label>
											<div class="pull-left">
												<input type="radio" checked class="pull-left"
													class="form-control" name="exampleInputEmail5"
													style="width: 20px; margin-left: 10px; margin-top: 10px"
													class="form-control">
											</div>
											<label for="exampleInputEmail5" class="control-label pull-left">
												简称</label>
										</div>
									</div>
								</div>
								<div class="row" style="margin: 0px 0 0px 0;">
									<div class="table-responsive">
										<div class="form-group"
											style="margin-left: 10px; margin-top: 5px;">
											<a type="button" class="btn btn-success" data-toggle="modal"
												data-target="#" id="plan_construction_deleteCrossInfo">删除</a>
											<a type="button" class="btn btn-success" data-toggle="modal"
												data-target="#" id="plan_construction_reflush">刷新</a> <a
												type="button" class="btn btn-success" data-toggle="modal"
												data-target="#" id="plan_construction_createCrossLines">生成交路信息</a>
										</div>
										<table class="table table-bordered table-striped table-hover"
											id="plan_review_table_trainInfo">
											<thead>
												<tr>
													<th style="width: 25px"><input type="checkbox"></th>
													<th>交路名</th>
												</tr>
											</thead>
											<tbody data-bind="foreach: rows">
												<tr data-bind="click: $parent.showTrains">
													<td data-bind="text: index "></td>
													<td data-bind="text: name"></td>
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
	<div class="pull-right">
		<div id="plan_view_div_palnDayDetail" class="panel panel-default"> 
			<div class="row" style="margin: 10px 10px 10px 10px;">
			   <section class="panel panel-default">
			        <div class="panel-heading"><i class="fa fa-table"></i>经由基本信息</div>
			          <div class="panel-body">
						<div class="row" >
							<form class="form-horizontal" role="form"> 
							<!-- <table>
								<tr>
									<td><label for="exampleInputEmail3"
										class="control-label pull-left"> 车次：&nbsp;</label></td>
									<td colspan="3"><div class="pull-left">
											<input type="text" class="form-control" style="width: 380px;"
												placeholder="输入车次查询" id="plan_construction_input_trainNbr">
										</div></td> 
								</tr>
								<tr>
									<td><label for="exampleInputEmail3"
											class="control-label pull-left"> 交路名：&nbsp;</label></td>
									<td><div class="pull-left">
											<input type="text" class="form-control" style="width: 100px;"
												id="plan_construction_input_trainNbr">
										</div></td>
									<td><label for="exampleInputEmail3"
										class="control-label pull-left"> 组数：&nbsp;</label></td>
									<td><div class="pull-left">
											<input type="text" class="form-control" style="width: 100px;"
												id="plan_construction_input_trainNbr">
										</div></td>
								</tr>
								 <tr>
									<td><label for="exampleInputEmail3"
											class="control-label pull-left"> 交路名：&nbsp;</label></td>
									<td style="width: 250px;">adfaa</td>
									<td><label for="exampleInputEmail3"
										class="control-label pull-left"> 组数：&nbsp;</label></td>
									<td style="width: 250px;"> afad</td>
								</tr>
								<tr>
									<td><label for="exampleInputEmail3"
											class="control-label pull-left"> 交路名：&nbsp;</label></td>
									<td><div class="pull-left">
											<input type="text" class="form-control" style="width: 100px;"
												id="plan_construction_input_trainNbr">
										</div></td>
									<td><label for="exampleInputEmail3"
										class="control-label pull-left"> 组数：&nbsp;</label></td>
									<td><div class="pull-left">
											<input type="text" class="form-control" style="width: 100px;"
												id="plan_construction_input_trainNbr">
										</div></td>
								</tr>
							</table> -->
							<div class="pull-left">
								<div class="row" style="margin: 5px 0 0px 0;">
									<div class="form-group"
										style="float: left; margin-left: 20px; margin-bottom: 0;">
										<label for="exampleInputEmail3"
											class="control-label pull-left"> 交路名：&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 100px;"
												id="plan_construction_input_trainNbr">
										</div>
									</div>
								</div>
								<div class="row" style="margin: 5px 0 0px 0;">
									<div class="form-group"
										style="float: left; margin-left: 20px; margin-bottom: 0;">
										<label for="exampleInputEmail3"
											class="control-label pull-left"> 组数：&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 50px;">
										</div>
									</div>
									<label for="exampleInputEmail2" class="control-label pull-left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;对数:&nbsp;</label>
									<div class="pull-left">
										<input type="text" class="form-control" style="width: 50px;"
											placeholder="" id="plan_construction_selectdate">
									</div>
									<div class="pull-left">
										<input type="checkBox" class="pull-left" class="form-control"
											name="exampleInputEmail5"
											style="width: 20px; margin-left: 20px; margin-top: 10px"
											class="form-control">
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left">
										是否切断久交路图</label>
									<div class="pull-left">
										<input type="radio" class="pull-left" class="form-control"
											name="exampleInputEmail5"
											style="width: 20px; margin-left: 30px; margin-top: 10px"
											class="form-control">
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left">
										高铁</label>
									<div class="pull-left">
										<input type="radio" class="pull-left" class="form-control"
											name="exampleInputEmail5"
											style="width: 20px; margin-left: 10px; margin-top: 10px"
											class="form-control">
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left">
										既有线</label>
								</div>
								<div class="row" style="margin: 5px 0 0px 0;">
									<div class="form-group"
										style="float: left; margin-left: 20px; margin-bottom: 0;">
										<label for="exampleInputEmail3"
											class="control-label pull-left"> 运行区段：&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 250px;">
										</div>
									</div>
									<div class="form-group"
										style="float: left; margin-left: 20px; margin-bottom: 0;">
										<label for="exampleInputEmail3"
											class="control-label pull-left"> 经由铁路线：&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 150px;">
										</div>
									</div>
								</div>
								<div class="row" style="margin: 5px 0 0px 0;">
									<div class="form-group"
										style="float: left; margin-left: 20px; margin-bottom: 0;">
										<label for="exampleInputEmail3"
											class="control-label pull-left"> 车辆担当局：&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 60px;">
										</div>
									</div>
									<div class="form-group"
										style="float: left; margin-left: 20px; margin-bottom: 0;">
										<label for="exampleInputEmail3"
											class="control-label pull-left"> 动车所：&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 60px;">
										</div>
									</div>
									<div class="form-group"
										style="float: left; margin-left: 20px; margin-bottom: 0;">
										<label for="exampleInputEmail3"
											class="control-label pull-left"> 客运担当局：&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 60px;">
										</div>
									</div>
									<div class="form-group"
										style="float: left; margin-left: 20px; margin-bottom: 0;">
										<label for="exampleInputEmail3"
											class="control-label pull-left"> 机车类型：&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 60px;">
										</div>
									</div>

								</div>
								<div class="row" style="margin: 5px 0 0px 0;">
									<div class="form-group"
										style="float: left; margin-left: 20px; margin-bottom: 0;">
										<label for="exampleInputEmail3"
											class="control-label pull-left"> 车辆段/动车段：&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 250px;">
										</div>
									</div>
									<div class="form-group"
										style="float: left; margin-left: 20px; margin-bottom: 0;">
										<label for="exampleInputEmail3"
											class="control-label pull-left"> 客运段：&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 150px;">
										</div>
									</div>
								</div>
								<div class="row" style="margin: 5px 0 0px 0;">
									<div class="pull-left">
										<input type="checkbox" class="pull-left" class="form-control"
											name="exampleInputEmail5"
											style="width: 20px; margin-left: 20px; margin-top: 10px"
											class="form-control">
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left">
										平日开</label>
									<div class="pull-left">
										<input type="checkbox" class="pull-left" class="form-control"
											name="exampleInputEmail5"
											style="width: 20px; margin-left: 20px; margin-top: 10px"
											class="form-control">
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left">
										周末开</label>
									<div class="pull-left">
										<input type="checkbox" class="pull-left" class="form-control"
											name="exampleInputEmail5"
											style="width: 20px; margin-left: 20px; margin-top: 10px"
											class="form-control">
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left">
										高峰开</label>

									<div class="pull-left">
										<input type="radio" class="pull-left" class="form-control"
											name="exampleInputEmail5"
											style="width: 20px; margin-left: 40px; margin-top: 10px"
											class="form-control">
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left">
										开行</label>

									<div class="pull-left">
										<input type="radio" class="pull-left" class="form-control"
											name="exampleInputEmail5"
											style="width: 20px; margin-left: 20px; margin-top: 10px"
											class="form-control">
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left">
										备用</label>
									<div class="pull-left">
										<input type="radio" class="pull-left" class="form-control"
											name="exampleInputEmail5"
											style="width: 20px; margin-left: 20px; margin-top: 10px"
											class="form-control">
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left">
										停运</label>
								</div>
								<!-- <div class="pull-left">
									<input type="checkBox" class="pull-left" class="form-control"
										name="exampleInputEmail5"
										style="width: 20px; margin-left: 10px; margin-top: 10px"
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
					<div class="pull-left" style="width: 68%;">
					<section class="panel panel-default">
				         <div class="panel-heading"><i class="fa fa-table"></i>经由基本信息</div>
				          <div class="panel-body">
							<div class="table-responsive">
								<div class="form-group"
									style="margin-left: 10px; margin-top: 5px;">
									<a type="button" class="btn btn-success" data-toggle="modal"
										data-target="#" id="cross_train_create">新建</a> <a type="button"
										class="btn btn-success" data-toggle="modal" data-target="#"
										id="cross_train_add">添加</a> <a type="button"
										class="btn btn-success" data-toggle="modal" data-target="#"
										id="cross_train_update">修改</a> <a type="button"
										class="btn btn-success" data-toggle="modal" data-target="#"
										id="cross_train_delete">删除</a> <a type="button"
										class="btn btn-success" data-toggle="modal" data-target="#"
										id="cross_train_save"> 保存</a>
								</div>
								<table class="table table-bordered table-striped table-hover"
									id="cross_trainInfo">
									<thead>
										<tr>
											<th style="width: 25px">序号</th>
											<th style="width: 100px">车次</th>
											<th style="width: 150px">始发站</th>
											<th style="width: 150px">始发局</th>
											<th style="width: 150px">终到站</th>
											<th style="width: 150px">终到局</th>
											<th style="width: 130px">高铁或既有线</th>
											<th style="width: 250px">间隔天数</th>
											<th style="width: 250px">备用及停运标记</th>
											<th style="width: 250px">交替车次</th>
											<th style="width: 250px">交替时间</th>
										</tr>
									</thead>
									<tbody data-bind="foreach: trains">
										<tr>
											<td data-bind="text: $index"></td>
											<td data-bind="text: name"></td>
											<td data-bind="text: index2"></td>
											<td data-bind="text: index3"></td>
											<td data-bind="text: index4"></td>
											<td data-bind="text: index5"></td>
											<td data-bind="text: index6"></td>
											<td data-bind="text: index7"></td>
											<td data-bind="text: index8"></td>
											<td data-bind="text: index9"></td>
											<td data-bind="text: index10"></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div> 
					</section>
					</div>
					<div class="pull-right" style="width: 30%;margin: 0px 10px 10px 10px;">
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
					</div>
				</div>
				</div>
			</div> 

</body>
</html>

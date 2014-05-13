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
<link href="assets/css/cross/cross.css" rel="stylesheet">

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
<script src="js/cross.js"></script> 
<script type="text/javascript">

$(function() {
	$("#file_upload_dlg").dialog("close"); 
	$("#cross_train_time_dlg").dialog("close"); 
	
	var cross = new CrossModel();
	ko.applyBindings(cross); 
	 
	cross.init();
	cross.loadCrosses([ {
		"index" : 1,
		"crossName" : "G1"
	}, {
		"index" : 2,
		"crossName" : "G2"
	}, {
		"index" : 3,
		"crossName" : "G3" 
	}, {
		"index" : 4,
		"crossName" : "G4"
	}, {
		"index" : 5,
		"crossName" : "G5"
	} ]);

});




function CrossModel() {
	var self = this;
		//列车列表
	self.trains = ko.observableArray();
	//交路列表
	self.crossRows = ko.observableArray(); 
	
	self.gloabBureaus = []; 
	
	//车辆担当局
	self.searchModle = ko.observable(new searchModle());
	
	// cross基础信息中的下拉列表  
	self.loadBureau = function(bureaus){   
		for ( var i = 0; i < bureaus.length; i++) {  
			self.tokenVehBureaus.push(new BureausRow(bureaus[i])); 
			if(bureaus[i].code == self.tokenVehBureau()){
				self.tokenVehBureau(bureaus[i]);
				break;
			}
		} 
	}; 
	
	 
	//当前选中的交路对象
	self.currentCross = ko.observable(new CrossRow({"crossId":"1",
		"crossName":"", 
		"chartId":"",
		"chartName":"",
		"crossStartDate":"",
		"crossEndDate":"",
		"crossSpareName":"",
		"alterNateDate":"",
		"alterNateTranNbr":"",
		"spareFlag":"",
		"cutOld":"",
		"groupTotalNbr":"",
		"pairNbr":"",
		"highlineFlag":"",
		"highlineRule":"",
		"commonlineRule":"",
		"appointWeek":"",
		"appointDay":"",
		"crossSection":"",
		"throughline":"",
		"startBureau":"",
		"tokenVehBureau":"",
		"tokenVehDept":"",
		"tokenVehDepot":"",
		"tokenPsgBureau":"",
		"tokenPsgDept":"",
		"tokenPsgDepot":"",
		"locoType":"",
		"crhType":"",
		"elecSupply":"",
		"dejCollect":"",
		"airCondition":"",
		"note":"", 
		"createPeople":"", 
		"createPeopleOrg":"",  
		"createTime":""}));
	
	self.currentIndex = 0;
	
	self.pageSize = 50; 
	
	self.cross_totalCount = 0;
	//currentIndex 
	
	var rowLookup = {};  
	
	self.init = function(){  
		self.gloabBureaus = [{"shortName": "上", "code": "S"}, {"shortName": "京", "code": "B"}, {"shortName": "广", "code": "G"}];
		self.searchModle().loadBureau(self.gloabBureaus); 
		self.searchModle().loadChats([{"name":"方案1", "chartId": "1234"},{"name":"方案2", "chartId": "1235"}])
		
		/* $.ajax({
			url : "cross/queryBureau",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({
				runDate : _plan_construction_selectdate.val(),//开行日期
				trainNbr : train.value, //车次号 
				currentIndex : self.currentIndex,
				pageSize : self.pageSize
			}),
			success : function(result) {    
				if (result != null && result != "undefind" && result.code == "0") {
					if (result.data !=null) { 
						$.each(result.data,function(n, bureau){
							var row = new BureausRow(bureau);
							self.gloabBureaus.push(row); 
						});
					}
					 
				} else {
					showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
				} 
			},
			error : function() {
				showErrorDialog("接口调用失败");
			},
			complete : function(){
				commonJsScreenUnLock();
			}
	    }); */
		
		
	};
	
	self.loadCrosses = function(crosses) {  
		$.each(crosses,function(n, crossInfo){
			var row = new CrossRow(crossInfo);
			self.crossRows.push(row);
			rowLookup[row.crossName] = row;
		}); 
// 	$.ajax({
//		url : "cross/queryCrosses",
//		cache : false,
//		type : "POST",
//		dataType : "json",
//		contentType : "application/json",
//		data :JSON.stringify({
//			runDate : _plan_construction_selectdate.val(),//开行日期
//			trainNbr : train.value, //车次号 
//			currentIndex : self.currentIndex,
//			pageSize : self.pageSize
//		}),
//		success : function(result) {    
//			if (result != null && result != "undefind" && result.code == "0") {
//				if (result.data !=null) { 
//					$.each(result.data,function(n, crossInfo){
//						var row = new CrossRow(crossInfo);
//						self.crossRows.push(row);
//						rowLookup[row.name] = row;
//					});
//				}
//				 
//			} else {
//				showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
//			} 
//		},
//		error : function() {
//			showErrorDialog("接口调用失败");
//		},
//		complete : function(){
//			commonJsScreenUnLock();
//		}
//	});
	
};

	self.saveCrossInfo = function() { 
		alert(self.currentCross().tokenVehBureau())
	}
	self.showTrains = function(row) {
		var bureauCode = self.searchModle().bureau(); 
		var highlingFlag = self.searchModle().highlingFlag().value; 
		var sureFlag = self.searchModle().sureFlag().value; 
		var startBureauCode = self.searchModle().startBureau(); 
		
		alert(self.searchModle().chart().chartId)
		 
		self.trains.remove(function(item) {
			return true;
		}); 
		self.currentCross(new CrossRow({"crossId":"1",
			"crossName":"1", 
			"chartId":"1",
			"chartName":"1",
			"crossStartDate":"1",
			"crossEndDate":"1",
			"crossSpareName":"1",
			"alterNateDate":"1",
			"alterNateTranNbr":"1",
			"spareFlag":"1",
			"cutOld":"1",
			"groupTotalNbr":"1",
			"pairNbr":"1",
			"highlineFlag":"1",
			"highlineRule":"1",
			"commonlineRule":"1",
			"appointWeek":"1",
			"appointDay":"1",
			"crossSection":"1",
			"throughline":"1",
			"startBureau":"1",
			"tokenVehBureau":"B",
			"tokenVehDept":"1",
			"tokenVehDepot":"1",
			"tokenPsgBureau":"1",
			"tokenPsgDept":"1",
			"tokenPsgDepot":"1",
			"locoType":"1",
			"crhType":"1",
			"elecSupply":"1",
			"dejCollect":"1",
			"airCondition":"1",
			"note":"1", 
			"createPeople":"1", 
			"createPeopleOrg":"1",  
			"createTime":"1"}));
		
		self.currentCross().loadBureau(self.gloabBureaus);
		
		var trains = [{"crossTainId ":"1",
			"crossId":"1",
			"trainSort":"1",
			"baseTrainId":"1",
			"trainNbr":"1",
			"startStn":"1",
			"startBureau":"1",
			"endStn":"1",
			"endBureau":"1",
			"dayGap":"1",
			"alertNateTrainNbr":"1",
			"alertNateTime":"1",
			"spareFlag":"1",
			"spareApplyFlage":"1",
			"highlineFlag":"1",
			"highlineRule":"1",
			"commonLineRule":"1",
			"appointWeek":"1",
			"appointDay":"1"}];
		
		$.each(trains,function(n, crossInfo){
			var row = new TrainRow(crossInfo);
			self.trains.push(row); 
		});
		  
	/* 	$.ajax({
			url : "cross/queryCrosses",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({ 
				crossId : row.crossId //车次号  
			}),
			success : function(result) {    
				if (result != null && result != "undefind" && result.code == "0") {
					if (result.data !=null && result.data.length > 0) {  
						self.currentCross(new CrossRow(result.data[0]));
						if(result.data[0].trains != null){
							$.each(result.data,function(n, crossInfo){
								var row = new TrainRow(crossInfo);
								self.trains.push(row); 
							});
						}
					}
					 
				} else {
					showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
				} 
			},
			error : function() {
				showErrorDialog("接口调用失败");
			},
			complete : function(){
				commonJsScreenUnLock();
			}
		});
	    self.currentTrain =  ko.observable(rowLookup[row.crossName]);
		var trains = rowLookup[row.name].trains;
		for ( var i = 0; i < trains.length; i++) {
			var row = new TrainRow(trains[i]);
			self.trains.push(row);
			rowLooktrains[row.crossName] = row;
		} 
	
	  */
		
	}; 
} ;


function FileUploadModle(){
	self = this;
	self.charts = ko.observableArray();
	self.startDate = ko.observable(); 
	
	self.loadChart = function(charts){
		for ( var i = 0; i < charts.length; i++) {
			var row = new CrossRow(charts[i]);
			self.charts.push(row); 
		} 
	};
} 

function searchModle(){
	self = this;  
	self.bureaus = ko.observableArray();  
	self.startBureaus = ko.observableArray();
	
	self.charts = ko.observableArray();
	 
	self.highlingFlags = [{"value": "-1", "text": ""},{"value": "0", "text": "普线"},{"value": "1", "text": "高线"},{"value": "2", "text": "混合"}];
	self.sureFlags = [{"value": "-1", "text": ""},{"value": "0", "text": "已审核"},{"value": "1", "text": "未审核"}];
	
	self.highlingFlag = ko.observable();
	
	self.sureFlag = ko.observable();
	
	self.bureau = ko.observable();
	
	self.chart =  ko.observable();
	
	self.startDay = ko.observable();
	
	self.startBureau = ko.observable();
	
	self.loadBureau = function(bureaus){   
		for ( var i = 0; i < bureaus.length; i++) {  
			self.bureaus.push(new BureausRow(bureaus[i])); 
			self.startBureaus.push(new BureausRow(bureaus[i]));  
		} 
	}; 
	
	self.loadChats = function(charts){   
		for ( var i = 0; i < charts.length; i++) {  
			self.charts.push(charts[i]);  
		} 
	}; 
	
}

function BureausRow(data) {
	var self = this;  
	self.shortName = data.shortName;   
	self.code = data.code;   
	//方案ID 
}

function CrossRow(data) {
	var self = this;
	
	self.id = data.chartId;
	self.chartName = data.crossName; 
	//方案ID 
}

function CrossRow(data) {
	var self = this; 
	self.id = data.chartId;
	self.chartName = data.crossName; 
	//方案ID 
}

function CrossRow(data) {
	var self = this; 
	self.crossId = data.crossId;
	self.crossName = ko.observable(data.crossName);  
	
	//方案ID
	self.chartId = ko.observable(data.chartId);
	self.chartName = ko.observable(data.chartName);
	self.crossStartDate = ko.observable(data.crossStartDate);
	self.crossEndDate = ko.observable(data.crossEndDate);
	self.crossSpareName = ko.observable(data.crossSpareName);
	self.alterNateDate = ko.observable(data.alterNateDate);
	self.alterNateTranNbr = ko.observable(data.alterNateTranNbr);
	self.spareFlag = ko.observable(data.spareFlag);
	self.cutOld = ko.observable(data.cutOld);
	self.groupTotalNbr = ko.observable(data.groupTotalNbr);
	self.pairNbr = ko.observable(data.pairNbr);
	self.highlineFlag = ko.observable(data.highlineFlag);
	self.highlineRule = ko.observable(data.highlineRule);
	self.commonlineRule = ko.observable(data.commonlineRule);
	self.appointWeek = ko.observable(data.appointWeek);
	self.appointDay = ko.observable(data.appointDay);
	self.crossSection = ko.observable(data.crossSection);
	self.throughline = ko.observable(data.throughline);
	self.startBureau = ko.observable(data.startBureau); 
	//车辆担当局 
	self.tokenVehBureau = ko.observable(data.tokenVehBureau);
	
	
	self.tokenVehDept = ko.observable(data.tokenVehDept);
	self.tokenVehDepot = ko.observable(data.tokenVehDepot);
	self.tokenPsgBureau = ko.observable(data.tokenPsgBureau);
	self.tokenPsgDept = ko.observable(data.tokenPsgDept);
	self.tokenPsgDepot = ko.observable(data.tokenPsgDepot);
	self.locoType = ko.observable(data.locoType);
	self.crhType = ko.observable(data.crhType);
	self.elecSupply = ko.observable(data.elecSupply);
	self.dejCollect = ko.observable(data.dejCollect);
	self.airCondition = ko.observable(data.airCondition);
	self.note = ko.observable(data.note);  
};

function TrainModel() {
	var self = this;
	self.rows = ko.observableArray();
	self.rowLookup = {};
}

function TrainRow(data) {
	var self = this; 
	self.crossTainId  = data.crossTainId;//BASE_CROSS_TRAIN_ID
	self.crossId = data.crossId;//BASE_CROSS_ID
	self.trainSort = data.trainSort;//TRAIN_SORT
	self.baseTrainId = data.baseTrainId
	self.trainNbr = data.trainNbr;//TRAIN_NBR
	self.startStn = data.startStn;//START_STN
	self.startBureau = data.startBureau;//START_BUREAU
	self.endStn = data.endStn;//END_STN
	self.endBureau = data.endBureau;//END_BUREAU
	self.dayGap = data.dayGap;//DAY_GAP
	self.alertNateTrainNbr = data.alertNateTrainNbr;//ALTERNATE_TRAIN_NBR
	self.alertNateTime = data.alertNateTime;//ALTERNATE_TIME
	self.spareFlag = data.spareFlag;//SPARE_FLAG
	self.spareApplyFlage = data.spareApplyFlage;//SPARE_APPLY_FLAG
	self.highlineFlag = data.highlineFlag ;//HIGHLINE_FLAG
	self.highlineRule = data.highlineRule;//HIGHLINE_RULE
	self.commonLineRule = data.commonLineRule;//COMMONLINE_RULE
	self.appointWeek = data.appointWeek;//APPOINT_WEEK
	self.appointDay = data.appointDay;//APPOINT_DAY 
	
	self.otherRule = self.appointWeek + " " + self.appointDay;

} ;


function openLogin() {
	$("#file_upload_dlg").dialog("open");
}
</script>
</head>
<body class="Iframe_body" style="margin-left:50px;margin-right:50px;">
	
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
									<select style="width: 269px" id="input_cross_chart_id"
										class="form-control" data-bind="options:searchModle().charts, value: searchModle().chart, optionsText: 'name'">
									</select>
								</div>  
						  </div> 
						   <div class="row" style="margin: 5px 0 5px 0;">
								<label for="exampleInputEmail2" class="control-label pull-left">启用日期:&nbsp;</label>
						        <div class="pull-left">
						           <input class="form-control" style="width:179px;" placeholder="" data-bind="value: searchModle().startDay">
						        </div>
						        <button class="btn btn-primary" type="button" style="margin-left: 10px;"
								id="btn_cross_upload" onclick="openLogin()">导入EXCEL</button>
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
												<select style="width: 65px" class="form-control" data-bind="options:searchModle().bureaus, value: searchModle().bureau, optionsText: 'shortName', optionsValue:'code'"></select>
											</div>
											<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left: 20px;">
												始发局:</label>
											<div class="pull-left" style="margin-left: 5px;">
											<select style="width: 65px" class="form-control" data-bind="options:searchModle().startBureaus, value: searchModle().startBureau, optionsText: 'shortName', optionsValue:'code'"></select>
											</div> 
										</div>    
										<div class="row"  style="margin-top: 5px;">
											<label for="exampleInputEmail3" class="control-label pull-left" >
												铁路线类型:</label>
											<div class="pull-left" style="margin-left: 5px;">
											    <select  style="width: 60px" class="form-control" data-bind="options: searchModle().highlingFlags, value: searchModle().highlingFlag, optionsText: 'text'"></select>
											</div>
											<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left: 33px;">
												状态:</label>
											<div class="pull-left" style="margin-left: 5px;">
												<select style="width: 65px" id="input_cross_sure_flag"
													class="form-control" data-bind="options: searchModle().sureFlags, value: searchModle().sureFlag, optionsText: 'text'">
												</select>
											</div>
											
											<div class="pull-right">
												<a type="button" class="btn btn-success" data-toggle="modal"
													data-target="#" id="btn_cross_search" style="margin-left: 15px;" data-bind="click: loadCrosses">查询</a>
											</div>
										</div>
								 
										<hr style="margin-top: 8px;margin-bottom: 8px">
										<div class="row"  style="margin-top: 5px;">
										     
											<label for="exampleInputEmail3" class="control-label pull-left">
												车次:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" style="width: 100px;"
													placeholder="车次" id="input_cross_filter_trainNbr">
											</div>
										 
											<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left: 20px;">
												交路名:&nbsp;</label>
											<div class="pull-left">
												<select style="width: 65px" id="input_cross_filter_showFlag"
													class="form-control">
													<option value="1">简称</option>
													<option value="2">全称</option>
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
												data-target="#" id="btn_cross_createCrossUnit">生成基本交路单元</a>
										</div> 
										<table class="table table-bordered table-striped table-hover" 
											id="cross_table_crossInfo">
											<thead>
												<tr style="height: 25px">
													<th style="width: 10%" align="center"><input type="checkbox" style="margin-top:0"></th>
													<th style="width: 15%" align="center">序号</th>
													<th style="width: 60%" align="center">车底交路名</th>
													<th style="width: 15%" align="center">状态</th>
												</tr>
											</thead>
											<tbody data-bind="foreach: crossRows">
												<tr data-bind="click: $parent.showTrains">
												    <td><input type="checkbox"></td>
													<td data-bind="text: $index "></td>
													<td data-bind="text: crossName"></td>
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
							<div class="pull-left">
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
											<select style="width: 65px" class="form-control" data-bind="options: $parent.gloabBureaus, value: tokenVehBureau, optionsText: 'shortName', optionsValue:'code'"></select>
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
										<input type="text" class="form-control" style="width: 30px;" data-bind="value: tokenPsgDept">
										<select style="width: 65px" class="form-control" data-bind="options: $parent.gloabBureaus, value: tokenPsgDept, optionsText: 'shortName', optionsValue:'code'"></select>
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
				<div class="row" style="margin: 15px 0 10px 10px;">
					<div class="pull-left" style="width: 100%;">
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
    <!--导入弹窗--> 
	<div id="file_upload_dlg" class="easyui-dialog" title="上传对数文件"
		data-options="iconCls:'icon-save'"
		style="width: 400px; height: 200px; padding: 10px">
		<form id="file_upload_id" name="file_upload_name" action="cross/fileUpload"
			method="post" enctype="multipart/form-data">
			<input type="text"  name="chartId" data-bind="value: searchModle().chart()? searchModle().chart().chartId: ''"/>
			<input type="text"  name="startDay" data-bind="value: searchModle().startDay() ? searchModle().startDay(): ''"/>
			<div>
				<input type="file"  name="fileName" />
			</div>
			<div>
				<input type="submit"  value="上传" />
			</div>
		</form>
	</div>
	<!--详情时刻表--> 
	<div id="cross_train_time_dlg" class="easyui-dialog" title="时刻表"
		data-options="iconCls:'icon-save'"
		style="width: 600px; height: 500px; padding: 10px">
			<div id="cross_train_time_info" style="display: none">  
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

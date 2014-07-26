$(function() { 
	
	var appModel = new ApplicationModel();
	ko.applyBindings(appModel); 
	 
	appModel.init();   
});  

function SelectCheckModle(){
	var self = this; 
	
	self.crossAllcheckBox = ko.observable(0);
	
	self.setCurrentCross = function(cross){
		self.currentCross(cross);
		if(self.searchModle().showCrossMap() == 1){
			$("#cross_map_dlg").find("iframe").attr("src", "cross/provideCrossChartData?crossId=" + cross.crossId);
		}
	}; 
	
	 
	self.selectCross = function(row){
//		self.crossAllcheckBox();
		if(row.selected() == 0){
			self.crossAllcheckBox(1);
			$.each(self.crossRows.rows(), function(i, crossRow){ 
				if(crossRow.selected() != 1 && crossRow != row){
					self.allcheckBox(0);
					return false;
				}  
			}); 
		}else{
			self.crossAllcheckBox(0);
		} 
	}; 
	
}

function ApplicationModel() {

	var _tabType = "";//bjdd(本局担当)   wjdd（外局担当）
	var self = this;
	//列车列表
	self.trains = ko.observableArray();
	

	self.trainAllTimes = ko.observableArray();//保存列车详情时刻所有数据，用于简图复选框点击事件列表值变更来源
	
	self.trainLines = ko.observableArray();
	//交路列表   
	self.gloabBureaus = [];   
	//车辆担当局
	self.searchModle = ko.observable(new searchModle()); 
	
	self.currentTrain = ko.observable(); 
	
	self.currdate =function(){
		var d = new Date();
		var year = d.getFullYear();    //获取完整的年份(4位,1970-????)
		var month = d.getMonth()+1;       //获取当前月份(0-11,0代表1月)
		var days = d.getDate(); 
		month = ("" + month).length == 1 ? "0" + month : month;
		days = ("" + days).length == 1 ? "0" + days : days;
		return year+"-"+month+"-"+days;
	};
	
	
	
	self.init = function(){

		commonJsScreenLock(2);
		$("#run_plan_train_times_canvas_dialog").dialog("close");
		_tabType = $("#tabType_hidden").val();//bjdd(本局担当)   wjdd（外局担当）
		if (_tabType == "wjdd") {
			$("#bjdd_btn_useTrainStnAndTime").hide();//隐藏套用经由及时刻按钮
		}
		
		 $.ajax({
				url : basePath+"/jbtcx/querySchemes",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({}),
				success : function(result) {    
					if (result != null && result != "undefind" && result.code == "0") {
						if (result.data !=null) { 
							self.searchModle().loadChats(result.data); 
						} 
					} else {
						showErrorDialog("获取方案失败");
					} 
				},
				error : function() {
					showErrorDialog("获取方案失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
		    });
	    $.ajax({
			url : basePath+"/plan/getFullStationInfo",
			cache : false,
			type : "GET",
			dataType : "json",
			contentType : "application/json", 
			success : function(result) {    
				if (result != null && result != "undefind" && result.code == "0") { 
					self.searchModle().loadBureau(result.data); 
					if (result.data !=null) { 
						$.each(result.data,function(n, bureau){  
							self.gloabBureaus.push({"shortName": bureau.ljjc, "code": bureau.ljpym}); 
						});
					} 
				} else {
					showErrorDialog("");
				} 
			},
			error : function() {
				showErrorDialog("接口调用失败");
			},
			complete : function(){
				commonJsScreenUnLock();
			}
	    });
	    
	    
	    /**
	     * 详情时刻表中 简图复选框点击事件
	     */
	    $("#input_checkbox_stationType_jt").click(function(){
		    var _stationTypeArray = [];
			$("[name='input_checkbox_stationType']").each(function(){
				if($(this).is(":checked")) {
					//查看简图 只包含始发、终到
					_stationTypeArray = ["SFZ","FJK","TZ","ZDZ"];
					//_stationTypeArray.push($(this).val());
				} else {
					//显示所有 包含始发、终到、分界口、停站、不停站
					_stationTypeArray = ["SFZ","FJK","BTZ","TZ","ZDZ"];//"0","FJK","TZ","BT"
				}
		    });
			

			self.trainLines.remove(function(item){
				return true;
			});
			
			$.each(self.trainAllTimes(), function(i, n){
				if ($.inArray(n.stationFlag, _stationTypeArray) > -1) {
					self.trainLines.push(n);
				}
				
				if(i == self.trainAllTimes().length - 1){
					$("#plan_runline_table_trainLine").freezeHeader(); 
				}
			});
	    });
		
	};  
	
	self.trainNbrChange = function(n,  event){
		self.searchModle().trainNbr(event.target.value.toUpperCase());
	};
	
	self.loadTrains = function(){
		commonJsScreenLock(1);
		self.trainRows.loadRows();
	};
	self.loadTrainsForPage = function(startIndex, endIndex) {  
		var trainNbr = self.searchModle().trainNbr();  
		var startBureauShortName = self.searchModle().startBureau();  
		var endBureauShortName = self.searchModle().endBureau();   
		var chart = self.searchModle().chart(); 
		var fuzzyFlag = self.searchModle().fuzzyFlag();
		
		if(chart == null){
			showErrorDialog("请选择方案");
			return;
		} 
		$.ajax({
				url : basePath+"/jbtcx/queryTrains",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					startBureauShortName : startBureauShortName, 
					endBureauShortName : endBureauShortName,
					trainNbr : trainNbr,
					fuzzyFlag : fuzzyFlag,
					chartId : chart.chartId,
					rownumstart : startIndex,
					rownumend : endIndex
				}),
				success : function(result) {  
					if (result != null && result != "undefind" && result.code == "0") {  
							if (result.data !=null) {   
								var rows = [];
								$.each(result.data.data,function(n, crossInfo){
									rows.push(new TrainRow(crossInfo));  
								}); 
								 $("#plan_runline_table_trainInfo").freezeHeader();  
								self.trainRows.loadPageRows(result.data.totalRecord, rows);
							}
						 
						 
					} else {
						showErrorDialog("获取车底失败");
					};
				},
				error : function() {
					showErrorDialog("获取车底失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
	}; 
	
	self.trainRows = new PageModle(50, self.loadTrainsForPage);
	 
	self.showTrainTimes = function(row) {
		self.currentTrain(row);
		self.trainAllTimes.remove(function(item){
			return true;
		});
		self.trainLines.remove(function(item){
			return true;
		});
		if(row.times().length > 0){ 
			$.each(row.times(), function(i, n){
				self.trainLines.push(n);
				if(i == row.times().length - 1){
					$("#plan_runline_table_trainLine").freezeHeader(); 
				}
			}) ;
			 
		}else{
			commonJsScreenLock(1);
			$.ajax({
				url : basePath+"/jbtcx/queryTrainTimes",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({   
					trainId : row.id
				}),
				success : function(result) {  
					if (result != null && result != "undefind" && result.code == "0") {  
						row.loadTimes(result.data);
						var _stationTypeArray = [];
						$("[name='input_checkbox_stationType']").each(function(){
							if($(this).is(":checked")) {
								//查看简图 只包含始发、终到
								_stationTypeArray = ["SFZ","FJK","TZ","ZDZ"];
								//_stationTypeArray.push($(this).val());
							} else {
								//显示所有 包含始发、终到、分界口、停站、不停站
								_stationTypeArray = ["SFZ","FJK","BTZ","TZ","ZDZ"];//"0","FJK","TZ","BT"
							}
					    });
						
						$.each(row.times(), function(i, n){
							self.trainAllTimes.push(n);
							
							if ($.inArray(n.stationFlag, _stationTypeArray) > -1) {
								self.trainLines.push(n);
							}
							
							if(i == row.times().length - 1){
								$("#plan_runline_table_trainLine").freezeHeader(); 
							}
						});
					} else {
						showErrorDialog("获取列车时刻表失败");
					};
				},
				error : function() {
					showErrorDialog("获取列车时刻表失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
		};
		
		
		if(!$("#run_plan_train_times_canvas_dialog").is(":hidden")){
			self.showTrainTimeCanvas();
		};
		
	};  
	
	self.fuzzyChange = function(){
		if(self.searchModle().fuzzyFlag() == 0){
			self.searchModle().fuzzyFlag(1);
		}else{
			self.searchModle().fuzzyFlag(0);
		}
	};
	
	
	
	/**
	 * 图形化显示列车运行时刻
	 */
	self.showTrainTimeCanvas = function(){
		if (self.currentTrain() == null || self.currentTrain().id ==null || self.currentTrain().id =="undefind") {
			showWarningDialog("请选择列车记录");
			return;
		}

		$("#run_plan_train_times_canvas_dialog").find("iframe").attr("src", basePath+"/jbtcx/getTrainTimeCanvasPage?planTrainId=" + self.currentTrain().id+"&trainNbr="+self.currentTrain().name);
		$('#run_plan_train_times_canvas_dialog').dialog({title: "列车运行时刻图 &nbsp;&nbsp;&nbsp;车次："+self.currentTrain().name, autoOpen: true, modal: false, draggable: true, resizable:true,
			onResize:function() {
				var iframeBox = $("#run_plan_train_times_canvas_dialog").find("iframe");
				var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
				var WH = $('#run_plan_train_times_canvas_dialog').height();
				var WW = $('#run_plan_train_times_canvas_dialog').width();
                if (isChrome) {
                	iframeBox.css({ "height": (WH) + "px"});
                	iframeBox.css({ "min-height": (WH) + "px"});
                	iframeBox.attr("width", (WW));

                }else{
                	iframeBox.css({ "height": (WH)  + "px"});
                	iframeBox.css({ "min-height": (WH) + "px"});
                	iframeBox.attr("width", (WW));
                }
		}});
	};
	
	
	/**
	 * 套用经由按钮点击事件
	 * 
	 * 将详情时刻表站名引用至临客命令 时刻表
	 * @author Think
	 * @since 2014-07-25
	 */
	self.useTrainStn = function() {
		if (self.trainAllTimes().length == 0) {
			showWarningDialog("请先选择列车记录");
			return;
		}
		
		if(_tabType == "bjdd") {//bjdd(本局担当)   wjdd（外局担当）
			//清除
			window.parent.runPlanLkCmdPageModel.runPlanLkCMDTrainStnRows.remove(function(item) {
				return true;
			});
		}
		var _LkCMDTrain_len = window.parent.runPlanLkCmdPageModel.runPlanLkCMDTrainStnRows().length;
		
		$.each(self.trainAllTimes(), function(i, n){
			n.childIndex = _LkCMDTrain_len+i;	//增加childIndex属性
			window.parent.runPlanLkCmdPageModel.runPlanLkCMDTrainStnRows.push(new window.parent.runPlanLkCmdPageModel.cmdTrainStnTimeRow(toCmdLkTrainTimeRow(n, "2")));
		});
		

		//设置套用的基本图车次id
		window.parent.runPlanLkCmdPageModel.useBaseTrainId(self.currentTrain().id);
		
	};
	
	
	

	/**
	 * 套用经由及时刻按钮点击事件
	 * 
	 * 将详情时刻表站名及时刻引用至临客命令 时刻表
	 * @author Think
	 * @since 2014-07-25
	 */
	self.useTrainStnAndTime = function() {
		if (self.trainAllTimes().length == 0) {
			showWarningDialog("请先选择列车记录");
			return;
		}
		

		if(_tabType == "bjdd") {//bjdd(本局担当)   wjdd（外局担当）
			//清除
			window.parent.runPlanLkCmdPageModel.runPlanLkCMDTrainStnRows.remove(function(item) {
				return true;
			});
		}

		var _LkCMDTrain_len = window.parent.runPlanLkCmdPageModel.runPlanLkCMDTrainStnRows().length;
		
		$.each(self.trainAllTimes(), function(i, n){
			n.childIndex = _LkCMDTrain_len+i;	//增加childIndex属性
			window.parent.runPlanLkCmdPageModel.runPlanLkCMDTrainStnRows.push(new window.parent.runPlanLkCmdPageModel.cmdTrainStnTimeRow(toCmdLkTrainTimeRow(n, "1")));
		});
		

		//设置套用的基本图车次id
		window.parent.runPlanLkCmdPageModel.useBaseTrainId(self.currentTrain().id);
	};
	
	
	/**
	 * 将详细时刻数据转换为临客命令时刻格式
	 * 
	 * useType   1:套用经由及时刻，赋值所有信息
	 * 			2:套用经由，只赋值站名
	 */
	function toCmdLkTrainTimeRow(trainTimeRowData, useType) {
		var cmdLkData = {};
		cmdLkData.childIndex = trainTimeRowData.childIndex;
		cmdLkData.stnName = trainTimeRowData.stnName;
		cmdLkData.stnBureau = useType=="1"?trainTimeRowData.bureauShortName:"";
		cmdLkData.arrTime = useType=="1"?trainTimeRowData.sourceTime:"";
		cmdLkData.dptTime = useType=="1"?trainTimeRowData.targetTime:"";
		cmdLkData.trackNbr = useType=="1"?trainTimeRowData.trackName:"";
		cmdLkData.platform = "";
		cmdLkData.arrTrainNbr = "";
		cmdLkData.dptTrainNbr = "";
		
		return cmdLkData;
	};
	
}

function searchModle(){
	
	self = this;   
	
	self.startBureaus = ko.observableArray(); 
	
	self.endBureaus = ko.observableArray(); 
	
	self.charts = ko.observableArray(); 
 
	self.startBureau = ko.observable();
	
	self.endBureau = ko.observable();
	
	self.trainNbr = ko.observable(); 
	
	self.chart = ko.observable(); 
	
	self.fuzzyFlag = ko.observable(1); 
	
	self.loadBureau = function(bureaus){   
		for ( var i = 0; i < bureaus.length; i++) {   
			self.startBureaus.push(new BureausRow(bureaus[i]));  
			self.endBureaus.push(new BureausRow(bureaus[i]));
		} 
	}; 
	
	self.loadChats = function(charts){   
		for ( var i = 0; i < charts.length; i++) {   
			self.charts.push({"chartId": charts[i].schemeId, "name": charts[i].schemeName});  
		} 
	}; 
	
}

function BureausRow(data) {
	var self = this;  
	self.shortName = data.ljjc;   
	self.code = data.ljpym;  
} 

function filterValue(value){
	return value == null || value == "null" ? "--" : value;
}
function TrainTimeRow(data) { 
	var self = this; 
	self.index = data.childIndex + 1;
	self.stnName = filterValue(data.stnName);
	self.bureauShortName = filterValue(data.bureauShortName);
	self.sourceTime = filterValue(data.arrTime);
	self.targetTime = filterValue(data.dptTime);
	self.stepStr = GetDateDiff(data); 
	self.trackName = filterValue(data.trackName);  
	self.runDays = data.runDays;
	self.stationFlag = data.stationFlag;
	 
}; 
function GetDateDiff(data)
{ 
	if(data.childIndex == 0)
		return "";
	else if(data.dptTime == '-'){
		return "";
	} 
	var startTime = new Date("1977-7-7 " + data.arrTime);
	var endTime = new Date("1977-7-7 " + data.dptTime);  
	var result = "";
	
	var date3=endTime.getTime()-startTime.getTime(); //时间差的毫秒数 
	
	//计算出相差天数
	var days=Math.floor(date3/(24*3600*1000));
	
	result += days > 0 ? days + "天" : "";  
	//计算出小时数
	var leave1=date3%(24*3600*1000);     //计算天数后剩余的毫秒数
	var hours=Math.floor(leave1/(3600*1000));
	
	result += hours > 0 ? hours + "小时" : ""; 
	
	//计算相差分钟数
	var leave2=leave1%(3600*1000);        //计算小时数后剩余的毫秒数
	var minutes=Math.floor(leave2/(60*1000));
	
	result += minutes > 0 ? minutes + "分" : "";
	//计算相差秒数
	var leave3=leave2%(60*1000);          //计算分钟数后剩余的毫秒数
	var seconds=Math.round(leave3/1000);
	
	result += seconds > 0 ? seconds + "秒" : "";  
	 
	return result == "" ? "" : result; 
};
function TrainRow(data) {   
	var self = this;  
	self.id = data.planTrainId;
	self.name = data.trainNbr; 
	self.times = ko.observableArray();  
	self.selected  = ko.observable();  
	self.startBureau = data.startBureau; 
	self.startStn =  data.startStn; 
	self.sourceTime = filterValue(data.startTimeStr); 
	self.endStn = data.endStn; 
	self.endBureau = data.endBureau; 
	self.routingBureau = data.routingBureauShortName; 
	self.runDays = data.relativeTargetTimeDay;  
	 
	self.targetTime =  filterValue(data.endTimeStr); 
	
	self.loadTimes = function(times){
		$.each(times, function(i, n){ 
			self.times.push(new TrainTimeRow(n));
		});
	}; 
	
} ; 
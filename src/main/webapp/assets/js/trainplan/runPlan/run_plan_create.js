var canvasData = {};  
var cross = null;
$(function() { 
	
	cross = new CrossModel();
	ko.applyBindings(cross); 
	
	cross.init();   
});

var highlingFlags = [{"value": "0", "text": "普线"},{"value": "1", "text": "高线"},{"value": "2", "text": "混合"}];
var checkFlags = [{"value": "1", "text": "已"},{"value": "0", "text": "未"}];
var unitCreateFlags = [{"value": "0", "text": "未"}, {"value": "1", "text": "已"},{"value": "2", "text": "全"}];
var highlingrules = [{"value": "1", "text": "平日"},{"value": "2", "text": "周末"},{"value": "3", "text": "高峰"}];
var commonlinerules = [{"value": "1", "text": "每日"},{"value": "2", "text": "隔日"}];
 
var _cross_role_key_pre = "JHPT.KYJH.JHBZ.";
function hasActiveRole(bureau){ 
	var roleKey = _cross_role_key_pre + bureau;
	return all_role.indexOf(roleKey) > -1; 
}


//{unitCrossId:"1",status: "1"}
function updateTrainRunPlanStatus(message){  
	var runPlan = $.parseJSON(message);
	console.log("---------  更新交路的当前状态 ----------"+message);
	cross.updateTrainRunPlanStatus(runPlan);
	
}
//{unitCrossId:"1",trainNbr: "G1", day:"20140723", runFlag: "1"}; 
function updateTrainRunPlanDayFlag(data){ 
	var runPlan = $.parseJSON(data);
//	console.log("---------  更新车次每天当前状态 ----------"+data);
	cross.updateTrainRunPlanDayFlag(runPlan);
}

var gloabBureaus = [];

function CrossModel() {
	var self = this;
		//列车列表
	self.trains = ko.observableArray();
	
	self.stns = ko.observableArray();
	//交路列表 
	self.crossAllcheckBox = ko.observable(0); 
	//当前展示的列车信息
	self.trainPlans = ko.observableArray();
	//表头的数据列表
	self.planDays = ko.observableArray(); 
	//路局列表
	self.gloabBureaus = [];    
	//车辆担当局
	self.searchModle = ko.observable(new searchModle());  
	//数据表格的宽度
	self.runPlanTableWidth = ko.computed(function(){
		return self.planDays().length * 40 + 80 + 'px';
	});
	//总共需要生成的数量
	self.createRunPlanTotalCount = ko.observable(0);
	//已生成数量
	self.createRunPlanCompletedCount = ko.observable(0);
	
	//已生成数量
	self.createRunPlanErrorCount = ko.observable(0);
	//生成说明文字
	self.completedMessage = ko.computed(function(){ 
		return self.createRunPlanTotalCount() == 0 ? "" : "选中：" + self.createRunPlanTotalCount() + "个交路，目前已成功生成：" + self.createRunPlanCompletedCount() + "个交路的开行计划，另有：" + self.createRunPlanErrorCount() + "个交路生成开行计划失败";
	});
	
	//更新列车某天的开行情况
	self.updateTrainRunPlanDayFlag = function(runPlan){
		$.each(self.trainPlans(), function(x, n){ 
			if(n.unitCrossId == runPlan.unitCrossId && n.trainNbr == runPlan.trainNbr){
				for(var i = 0; i < n.runPlans().length; i++){ 
					if(n.runPlans()[i].day.replace(/-/g, "") == runPlan.day){
						n.runPlans()[i].runFlag(runPlan.runFlag);
						break;
					};
				};
				return false;
			};
		});
	};
	//更新交路的当前状态
	self.updateTrainRunPlanStatus = function(runPlan){
		if(runPlan.status == 2){
			self.createRunPlanCompletedCount(self.createRunPlanCompletedCount() + 1);
		}else if(runPlan.status == -1){
			self.createRunPlanErrorCount(self.createRunPlanErrorCount() + 1);
		}
		$.each(self.trainPlans(), function(x, n){
			if(n.unitCrossId == runPlan.unitCrossId && n.trainSort == 0){ 
				n.createStatus(runPlan.status);
				return false;
			};
		});
	}; 
	
	self.dragRunPlan = function(n,event){
		$(event.target).dialog("open"); 
	};  
	
	//全选按钮
	self.selectCrosses = function(){
		$.each(self.trainPlans(), function(i, crossRow){ 
			if(self.crossAllcheckBox() == 1){
				if(crossRow.trainSort == 0){
					crossRow.selected(0);
				} 
			}else{ 
				if(crossRow.trainSort == 0){
					crossRow.selected(1);
				}    
			}  
		});  
	};
	
	//数据checkbox点击事件
	self.selectCross = function(row){ 
		if(row.selected() == 0){  
			self.crossAllcheckBox(1);   
			$.each(self.trainPlans(), function(i, crossRow){   
				if(crossRow.trainSort == 0 && crossRow.selected() != 1 && crossRow != row){
					self.crossAllcheckBox(0);
					return false;
				}  
			});  
		}else{
			self.crossAllcheckBox(0); 
		}; 
	};
	
	
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
	
	//列车输入框的大写转换事件
	self.trainNbrChange = function(n,  event){
		self.searchModle().filterTrainNbr(event.target.value.toUpperCase());
	}; 
	 
	//currentIndex 
	//格式化出yyyy-MM-dd这样的字符串
	self.currdate =function(){
		var d = new Date();
		var year = d.getFullYear();    //获取完整的年份(4位,1970-????)
		var month = d.getMonth()+1;       //获取当前月份(0-11,0代表1月)
		var days = d.getDate(); 
		month = ("" + month).length == 1 ? "0" + month : month;
		days = ("" + days).length == 1 ? "0" + days : days;
		return year+"-"+month+"-"+days;
	};
	//格式化出MMdd这样的字符串
	self.dayHeader =function(d){ 
	 
		var month = d.getMonth()+1;       //获取当前月份(0-11,0代表1月)
		var days = d.getDate(); 
		month = ("" + month).length == 1 ? "0" + month : month;
		days = ("" + days).length == 1 ? "0" + days : days;
		return month + "-"+ days;
	};
	//格式化出一、二
	self.getWeek = function(d){
		 var week = ""; 
		//获取当前星期X(0-6,0代表星期天)
		if(d.getDay()==0)          week="日"; 
		if(d.getDay()==1)          week="一"; 
		if(d.getDay()==2)          week="二"; 
		if(d.getDay()==3)          week="三"; 
		if(d.getDay()==4)          week="四"; 
		if(d.getDay()==5)          week="五"; 
		if(d.getDay()==6)          week="六"; 
		
		return week;
	 };
	
	 //获取30天后的时日期
	self.get40Date = function(){
		var d = new Date();
		d.setDate(d.getDate() + 30);
		
		var year = d.getFullYear();    //获取完整的年份(4位,1970-????)
		var month = d.getMonth()+1;       //获取当前月份(0-11,0代表1月)
		var days = d.getDate(); 
		month = ("" + month).length == 1 ? "0" + month : month;
		days = ("" + days).length == 1 ? "0" + days : days;
		return year + "-" + month + "-" + days;
	}; 
	
	self.init = function(){   
		
		$("#runplan_input_startDate").datepicker();
		$("#runplan_input_endDate").datepicker();
		//x放大2倍 
		 
		self.searchModle().planStartDate(self.currdate());
		self.searchModle().planEndDate(self.get40Date());
		
		commonJsScreenLock(2);
		//获取方案列表
		 $.ajax({
				url : "../jbtcx/querySchemes",
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
		//获取路局列表
	    $.ajax({
			url : "../plan/getFullStationInfo",
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
							gloabBureaus.push({"shortName": bureau.ljjc, "code": bureau.ljpym});
						});
					} 
				} else {
					showErrorDialog("获取路局列表失败");
				} 
			},
			error : function() {
				showErrorDialog("获取路局列表失败");
			},
			complete : function(){ 
				commonJsScreenUnLock(); 
			}
	    }); 
	    
	    self.initDataHeader();
	};  
	//查询所有的交路单元，因为没有使用提供未分页的查询函数，这里设置一个无法达到的总数5000后台提供以后可以替换掉
	self.loadCrosses = function(){
		self.loadCrosseForPage(0, 5000);
	};
	//初始化数据表的表头数据
	self.initDataHeader = function(){
		var startDate = $("#runplan_input_startDate").val(); 
		var endDate =  $("#runplan_input_endDate").val();   

		self.crossAllcheckBox(0);
		var currentTime = new Date(startDate);
		var endTime = new Date(endDate);
		endTime.setDate(endTime.getDate() + 10); 
		var endTimeStr = self.dayHeader(endTime);  
		self.planDays.remove(function(item) {
			return true;
		});  
		self.trainPlans.remove(function(item) {
			return true;
		});
		
		self.planDays.push({"day": self.dayHeader(currentTime).replace(/-/g, ""), "week": self.getWeek(currentTime)}); 
		while(self.dayHeader(currentTime) != endTimeStr){
			currentTime.setDate(currentTime.getDate() + 1); 
			self.planDays.push({"day": self.dayHeader(currentTime).replace(/-/g, ""), "week": self.getWeek(currentTime)}); 
		} 
		
		
	};
	//查询所有的交路单元
	self.loadCrosseForPage = function(startIndex, endIndex) {  
	 		commonJsScreenLock();
		 
		var bureauCode = self.searchModle().bureau();  
		var trainNbr = self.searchModle().filterTrainNbr();  
		var startBureauCode = self.searchModle().startBureau();   
		var chart = self.searchModle().chart();   
		var startDate = $("#runplan_input_startDate").val(); 
		var endDate =  $("#runplan_input_endDate").val();  
		
		self.initDataHeader();
		 
		$.ajax({
				url : "../cross/getUnitCrossInfo",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({ 
					tokenVehBureau : bureauCode, 
					startBureau : startBureauCode,
					trainNbr : trainNbr,
					chartId : chart.chartId,
					rownumstart : startIndex, 
					rownumend : endIndex,
				}),
				success : function(result) {    
 
					if (result != null && result != "undefind" && result.code == "0") { 
						if(result.data.data != null){
							$.each(result.data.data,function(n, crossInfo){
								var trainPlanData = {
										crossName: crossInfo.crossName, 
										unitCrossId: crossInfo.unitCrossId,
										tokenVehBureau: crossInfo.tokenVehBureau,
										startDate: startDate,
										endDate: endDate, 
										trainSort: 0,
										chirldrenIndex : n//用于界面显示序号
								};
								//默认吧交路作为第一条记录
								self.trainPlans.push(new TrainRunPlanRow(trainPlanData));
								var crossNames = crossInfo.crossName.split("-");
								//把交路拆分成车，然后依次添加在她的后面
								for(var i = 0; i < crossNames.length; i++){
									var trainPlanData = {
											crossName: crossInfo.crossName, 
											startDate: startDate,
											endDate: endDate,
											unitCrossId: crossInfo.unitCrossId,
											trainNbr: crossNames[i],
											trainSort: i + 1,
											tokenVehBureau: crossInfo.tokenVehBureau,
									};
									self.trainPlans.push(new TrainRunPlanRow(trainPlanData));
								} ; 
							});  
						}   
						 
					} else {
						showErrorDialog("获取交路单元信息失败");
					};
				},
				error : function() {
					showErrorDialog("获取交路单元信息失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			});  
	};
	//必须定义在load函数之后
	self.crossRows = new PageModle(50, self.loadCrosseForPage);  
   
	//生成运行线
	self.createTrainLines = function(){  
		 var crossIds = [];
		 var createCrosses = [];
		 var startDate = $("#runplan_input_startDate").val(); 
		 var endDate =  $("#runplan_input_endDate").val();  
		 var days = GetDays(startDate, endDate); 
		 var chart = self.searchModle().chart();
	     if(chart == null){
	    	showErrorDialog("请选择一个方案"); 
	    	return;
	     }  
		 
		 //重置生成总数和已生成数
		 self.createRunPlanTotalCount(createCrosses.length); 
		 self.createRunPlanCompletedCount(0);

		 var crosses = self.trainPlans();
		 for(var i = 0; i < crosses.length; i++){   
			if(crosses[i].selected() == 1){  
				crossIds.push(crosses[i].unitCrossId);
				crosses[i].createStatus(3);//1:正在生成    2：已生成   3：等待生成
				createCrosses.push(crosses[i]); 
			 }
		 }
		 if(crossIds.length == 0){
			 showWarningDialog("未选中数据");
			 return;
		 }
		 
		 
		 commonJsScreenLock();
		 $.ajax({
				url : "../runPlan/plantrain/gen",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					baseChartId: chart.chartId,
					startDate: startDate.replace(/-/g, ""), 
					days: days + 1, 
					unitcrossId: crossIds,
					msgReceiveUrl: "/trainplan/runPlan/runPlanCreate"}),
				success : function(result) { 
					if(result != null && result.length >= 0){ 
						showSuccessDialog("正在生成开行计划");
					}else{
						showErrorDialog("生成开行计划失败");
					}
				},
				error : function() {
					showErrorDialog("生成开行计划失败");
					 for(var i = 0; i < createCrosses.length; i++){   
						 createCrosses.createStatus(0);
					 }  
				},
				complete : function(){ 
					commonJsScreenUnLock();
				}
			}); 
	}; 
	
	self.bureauChange = function(){ 
		if(hasActiveRole(self.searchModle().bureau())){
			self.searchModle().activeFlag(1);  
		}else if(self.searchModle().activeFlag() == 1){
			self.searchModle().activeFlag(0); 
		}
	};  
	 
	self.removeArrayValue = function(arr, value){
		var index = -1;
		for(var i = 0 ; i < arr.length; i++){
			if(value == arr[i]){
				index = i;
				break;
			}
		}
		if(index > -1){
			arr.splice(index, 1);
		}
	};
	
	self.drawFlagChange = function(a, n){  
		if(n.target.checked){
			self.searchModle().drawFlags.push(n.target.value);
		}else{ 
			self.removeArrayValue(self.searchModle().drawFlags(), n.target.value);
		} 
	};
	
	self.filterCrosses = function(){
		var filterCheckFlag = self.searchModle().filterCheckFlag();  
		var filterUnitCreateFlag = self.searchModle().filterUnitCreateFlag(); 
		$.each(self.crossRows.rows(),function(n, crossRow){
			  if(crossRow.checkFlag() == filterCheckFlag || crossRow.unitCreateFlag() == filterUnitCreateFlag){
				  crossRow.visiableRow(true);
			  }else{
				  crossRow.visiableRow(false);
			  }
				 
		  }); 
	}; 
}

function searchModle(){
	self = this;  
	 
	self.activeFlag = ko.observable(0);   
	 
	self.planStartDate = ko.observable(); 
	
	self.planEndDate = ko.observable();
	
	self.bureaus = ko.observableArray();
	
	self.startBureaus = ko.observableArray();
	
	self.charts = ko.observableArray();
	  
	self.bureau = ko.observable();
	 
	self.chart =  ko.observable(); 
	
	self.startBureau = ko.observable();
	
	self.filterTrainNbr = ko.observable(); 
	
	self.loadBureau = function(bureaus){   
		for ( var i = 0; i < bureaus.length; i++) {  
			self.bureaus.push(new BureausRow(bureaus[i])); 
			self.startBureaus.push(new BureausRow(bureaus[i]));  
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
	//方案ID 
}   

function TrainModel() {
	var self = this;
	self.rows = ko.observableArray();
	self.rowLookup = {};
} 
 
function filterValue(value){
	return value == null || value == "null" ? "--" : value;
};

function TrainRunPlanRow(data){
	var self = this; 
	
	self.chirldrenIndex = data.chirldrenIndex;//用于界面序号显示
	self.unitCrossId = data.unitCrossId;
	self.trainNbr = data.trainNbr;
	self.crossName = data.crossName;
	self.runPlans =  ko.observableArray();  
	self.selected = ko.observable(0); 
	
	self.createStatus = ko.observable(0); 
	//车辆担当局 
	self.tokenVehBureau = ko.observable(data.tokenVehBureau); 
	//车辆担当局 
	self.tokenVehBureauShowValue = ko.computed(function(){ 
			var result = "";
			if(data.tokenVehBureau != null && data.tokenVehBureau != "null"){
				 var bs = data.tokenVehBureau.split("、"); 
				 result = data.tokenVehBureau;
				 for(var j = 0; j < bs.length; j++){
					 for(var i = 0; i < gloabBureaus.length; i++){
						 if(bs[j] == gloabBureaus[i].code){
							 result = result.replace(bs[j], gloabBureaus[i].shortName);
							 break;
						 };
					 };
				 }; 
			 }
			 return result; 
	});
	
	self.createStatusShowValue = ko.computed(function(){
		switch (self.createStatus()) {
			case 0: 
				return self.crossName;//"";
				break;
			case 3: 
				return self.crossName+"&nbsp;&nbsp;(等待生成)";//"";
				break;
			case 1: 
				return self.crossName+"&nbsp;&nbsp;<span class='label label-info'>(正在生成。。。。。。)</span>";//"(正在生成。。。。。。)";
				break;
			case 2: 
				return self.crossName+"&nbsp;&nbsp;<span class='label label-success'>(已生成开行计划)</span>";//"(已生成开行计划)";
				break;	
			case -1: 
				return self.crossName+"&nbsp;&nbsp;<span class='label label-danger'>(发生异常)</span>";//"(发生异常)";
				break;
			default: 
				return self.crossName;//'';
				break;
		} 
	});
	 
	self.rowspan = ko.computed(function(){ 
		if( data.trainSort == 1){
			return data.crossName.split("-").length;
		}else{
			return 1;
		}
	}); 
	
	self.colspan = ko.computed(function(){ 
		 return self.runPlans().length + 2;
	}); 
	
	self.trainSort = data.trainSort;  
	
	self.yyyyMMdd = function(d){ 
		var year = d.getFullYear();   
		var month = d.getMonth()+1;       //获取当前月份(0-11,0代表1月)
		var days = d.getDate(); 
		month = ("" + month).length == 1 ? "0" + month : month;
		days = ("" + days).length == 1 ? "0" + days : days;
		return year + "-" + month + "-" + days;
	};
	//初始化开行计划的列表
	if(data.runPlans == null){
		var currentTime = new Date(data.startDate); 
		var endTime = new Date(data.endDate);
		endTime.setDate(endTime.getDate() + 10); 
		var endTimeStr = self.yyyyMMdd(endTime);    
	 
		self.runPlans.remove(function(item) {
			return true;
		});   
		var curDay = self.yyyyMMdd(currentTime); 
		self.runPlans.push(new RunPlanRow({"day": curDay, "runFlag": null})); 
		while(curDay != endTimeStr){
			currentTime.setDate(currentTime.getDate() + 1); 
			curDay = self.yyyyMMdd(currentTime);
			self.runPlans.push(new RunPlanRow({"day": curDay, "runFlag": null})); 
		} 
	}  
}

function RunPlanRow(data){
	var self = this; 
	self.color = ko.observable("gray");
	self.day = data.day;
	self.runFlag = ko.observable(""); 
	
	self.runFlagShowValue = ko.computed(function(){ 
		switch (self.runFlag()) {
		case 9:
			self.color("gray");
			return "<span class='label label-danger'>停</span>";//"停";
			break;
		case 1:
			self.color("green");
			return "<span class='label label-success'>开</span>";//"开";
			break;
		case 2:
			self.color("blue");
			return "<span class='label label-info'>备</span>";//"备";
			break;
		default: 
			return '';
			break;
		}
	});
} 

function GetDays(data1, data2)
{  
	var startTime = new Date(data1);
	var endTime = new Date(data2);   
	
	var date3=endTime.getTime()-startTime.getTime(); //时间差的毫秒数  
	//计算出相差天数
	var days = Math.floor(date3/(24*3600*1000));
	
	return days;
}
function GetDateDiff(data)
{ 
	 if(data.childIndex == 0 
			 || data.dptTime == '-' 
			 || data.dptTime == null 
			 || data.arrTime == null
			 || data.arrTime == '-'){
		return "";
	} 
	var startTime = new Date(data.arrTime);
	var endTime = new Date(data.dptTime);  
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
 
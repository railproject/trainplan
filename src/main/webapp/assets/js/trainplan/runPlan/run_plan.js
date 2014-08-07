var canvasData = {};  
var cross = null;
$(function() { 
	
	cross = new CrossModel();
	ko.applyBindings(cross); 
	
	cross.init();   
});

var highlingFlags = [{"value": "0", "text": "普线"},{"value": "1", "text": "高线"},{"value": "2", "text": "混合"}];
var checkFlags = [{"value": "1", "text": "部分审核"},{"value": "2", "text": "全部审核"},{"value": "0", "text": "未审核"}];
var unitCreateFlags = [{"value": "0", "text": "未"}, {"value": "1", "text": "已"},{"value": "2", "text": "全"}];
var highlingrules = [{"value": "1", "text": "平日"},{"value": "2", "text": "周末"},{"value": "3", "text": "高峰"}];
var commonlinerules = [{"value": "1", "text": "每日"},{"value": "2", "text": "隔日"}];
 
var _cross_role_key_pre = "JHPT.KYJH.JHBZ.";
function hasActiveRole(bureau){ 
	var roleKey = _cross_role_key_pre + bureau;
	return all_role.indexOf(roleKey) > -1; 
}

var gloabBureaus = [];

function CrossModel() {
	var self = this;
		//列车列表
	self.trains = ko.observableArray();
	
	self.stns = ko.observableArray();
	//交路列表 
	self.crossAllcheckBox = ko.observable(0);
	
	self.trainPlans = ko.observableArray();
	
	self.planDays = ko.observableArray(); 
	
	self.gloabBureaus = [];  
	
	self.times = ko.observableArray();
	
	self.simpleTimes = ko.observableArray();
	
	self.runPlanCanvasPage = new RunPlanCanvasPage(self);
	self.currentTrainInfoMessage = ko.observable("");
	self.currentTrain = ko.observable();
	self.currentPlanCrossId = ko.observable("");//当前选择交路planid 用于车底交路图tab
	
	//车辆担当局
	self.searchModle = ko.observable(new searchModle());
	
	self.planCrossRows =  ko.observableArray();
	
	self.loadRunPlans = function(crossId){ 
		var startDate = $("#runplan_input_startDate").val(); 
		var endDate =  $("#runplan_input_endDate").val();
		
		var currentTime = new Date(startDate);
		var endTime = endDate.substring(5);  
		self.planDays.remove(function(item) {
			return true;
		});   
		self.planDays.push({"day": self.dayHeader(currentTime)}); 
		while(self.dayHeader(currentTime) != endTime){
			currentTime.setDate(currentTime.getDate() + 1); 
			self.planDays.push({"day": self.dayHeader(currentTime)}); 
		} 
		 $.ajax({
				url : basePath+"/runPlan/getRunPlans",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					startDay:startDate.replace(/-/g, ""),
					endDay: endDate.replace(/-/g, ""),
					planCrossId: crossId
				}),
				success : function(result) {    
					if (result != null && result != "undefind" && result.code == "0") {
						self.trainPlans.remove(function(item) {
							return true;
						});
						$.each(result.data,function(i, n){
							self.trainPlans.push(new TrainRunPlanRow(n));
						});
						 
					} else {
						showErrorDialog("获取运行规律失败");
					} 
				},
				error : function() {
					showErrorDialog("接口调用失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
		    }); 
	}; 
	
	self.trainRunPlanChange = function(row, event){ 
		console.log(row);
		console.log(event.target.name);
		console.log("trainRunPlanChange test");
	};
	
	self.dragRunPlan = function(n,event){
		$(event.target).dialog("open");
		
	};
	

	
	self.loadStns = function(currentTrain){  
		self.times.remove(function(item){
			return true;
		});
		self.simpleTimes.remove(function(item){
			return true;
		});
		commonJsScreenLock();
		 $.ajax({ 
				url : basePath+"/jbtcx/queryPlanLineTrainTimes",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					trainId: currentTrain.obj.planTrainId
				}),
				success : function(result) {    
					if (result != null && result != "undefind" && result.code == "0") { 
							var message = "车次：" + currentTrain.obj.trainName + "&nbsp;&nbsp;&nbsp;";
							
							$.each(result.data, function(i, n){
								var timeRow = new TrainTimeRow(n); 
								self.times.push(timeRow); 
								if(n.stationFlag != 'BTZ'){
									self.simpleTimes.push(timeRow); 
								}
								if(i == 0){
									message += n.stnName;
								}else if(i == result.data.length - 1){
									self.currentTrainInfoMessage(message + "——" + n.stnName);
									if($("#run_plan_train_times").is(":hidden")){
										$("#run_plan_train_times").dialog({top:10, draggable: true, resizable:true, onResize:function() {
											var simpleTimes_table = $("#simpleTimes_table");
											var allTimes_table = $("#allTimes_table");
											var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
											var WH = $('#run_plan_train_times').height() - 100;
											var WW = $('#run_plan_train_times').width();
								            if (isChrome) {
								            	simpleTimes_table.css({ "height": (WH) + "px"});
								            	simpleTimes_table.css({ "min-height": (WH) + "px"});
								            	simpleTimes_table.attr("width", (WW));
								            	
								            	allTimes_table.css({ "height": (WH) + "px"});
								            	allTimes_table.css({ "min-height": (WH) + "px"});
								            	allTimes_table.attr("width", (WW));

								            }else{
								            	simpleTimes_table.css({ "height": (WH)  + "px"});
								            	simpleTimes_table.css({ "min-height": (WH) + "px"});
								            	simpleTimes_table.attr("width", (WW));
								            	
								            	allTimes_table.css({ "height": (WH) + "px"});
								            	allTimes_table.css({ "min-height": (WH) + "px"});
								            	allTimes_table.attr("width", (WW));
								            }
										}});
									} 
								}  
							}); 
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
		   
		// $("#run_plan_train_times").dialog("open");
	};
	
	
	/**
	 * 用于调整时刻表
	 * @param currentTrain
	 */
	self.loadTrainAllStns = function(currentTrain){
//		if($('#run_plan_train_times_edit_dialog').is(":hidden")){
			$("#run_plan_train_times_edit_dialog").find("iframe").attr("src", basePath+"/runPlan/trainRunTimePage?trainNbr="+currentTrain.trainName+"&trainPlanId=" + currentTrain.planTrainId);
			$('#run_plan_train_times_edit_dialog').dialog({title: "编辑列车运行时刻", autoOpen: true, modal: false, draggable: true, resizable:true,
				onResize:function() {
					var iframeBox = $("#run_plan_train_times_edit_dialog").find("iframe");
					var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
					var WH = $('#run_plan_train_times_edit_dialog').height();
					var WW = $('#run_plan_train_times_edit_dialog').width();
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
//		}
	};
	
	
	/**
	 * 查看列车乘务信息
	 * @param currentTrain
	 */
	self.loadTrainPersonnel = function(currentTrain){
//		if($('#run_plan_train_crew_dialog').is(":hidden")){
			var _param = "trainNbr="+currentTrain.trainName+"&runDate=" + currentTrain.startDate+"&startStn="+currentTrain.startStn+"&endStn="+currentTrain.endStn;
			var _title = "车次："+currentTrain.trainName
						+"&nbsp;&nbsp;&nbsp;&nbsp;"  + currentTrain.startStn + "&nbsp;→&nbsp;" +  currentTrain.endStn
						+"&nbsp;&nbsp;&nbsp;&nbsp;" + "始发时间：" + currentTrain.startDate
						+"&nbsp;&nbsp;&nbsp;&nbsp;" + "终到时间：" + currentTrain.endDate;

			$("#run_plan_train_crew_dialog").find("iframe").attr("src", basePath+"/runPlan/trainCrewPage?"+_param);
			$('#run_plan_train_crew_dialog').dialog({title: "乘务信息【"+_title+"】", autoOpen: true, modal: false, draggable: true, resizable:true,
				onResize:function() {
					var iframeBox = $("#run_plan_train_crew_dialog").find("iframe");
					var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
					var WH = $('#run_plan_train_crew_dialog').height();
					var WW = $('#run_plan_train_crew_dialog').width();
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
//		}
	};
	
	
	self.setCurrentTrain = function(train){ 
		self.currentTrain(train); 
	};
	
	self.setCurrentCross = function(cross){
//		if(hasActiveRole(cross.tokenVehBureau()) && self.searchModle().activeFlag() == 0){
//			self.searchModle().activeFlag(1);  
//		}else if(!hasActiveRole(cross.tokenVehBureau()) && self.searchModle().activeFlag() == 1){
//			self.searchModle().activeFlag(0); 
//		} 
		self.currentCross(cross); 
	};
	
	
	self.selectCrosses = function(){
//		self.crossAllcheckBox(); 
		$.each(self.planCrossRows(), function(i, crossRow){ 
			if(self.crossAllcheckBox() == 1){
				crossRow.selected(0);
				self.searchModle().activeFlag(0);
				self.searchModle().checkActiveFlag(0); 
			}else{
				if(hasActiveRole(crossRow.tokenVehBureau())){ 
					crossRow.selected(1); 
					self.searchModle().activeFlag(1);  
				}
				//可以审核的
				if(crossRow.checkActiveFlag() == 1){
					crossRow.selected(1); 
					self.searchModle().checkActiveFlag(1); 
				}
			}  
		});  
	};
	
	self.selectCross = function(row){ 
//		self.crossAllcheckBox();  
		if(row.selected() == 0){  
			self.crossAllcheckBox(1);   
			if(row.activeFlag() == 1){ 
				self.searchModle().activeFlag(1);
				$.each(self.planCrossRows(), function(i, crossRow){   
					if(crossRow.selected() != 1 && crossRow != row && crossRow.activeFlag() == 1){
						self.crossAllcheckBox(0);
						return false;
					}  
				}); 
			} 
			if(row.checkActiveFlag() == 1){
				self.searchModle().checkActiveFlag(1);
				$.each(self.planCrossRows(), function(i, crossRow){   
					if(crossRow.selected() != 1 && crossRow != row && crossRow.checkActiveFlag() == 1){
						self.crossAllcheckBox(0);
						return false;
					}  
				}); 
				 
			};  
		}else{
			self.crossAllcheckBox(0);
			if(row.activeFlag() == 1){
				self.searchModle().activeFlag(0); 
				$.each(self.planCrossRows(), function(i, crossRow){   
					if(crossRow.selected() == 1 && crossRow != row && crossRow.activeFlag() == 1){
						self.searchModle().activeFlag(1);  
						 return false;
						//可以审核的  
					}  
				}); 
			}
			if(row.checkActiveFlag() == 1){
				self.searchModle().checkActiveFlag(0);
				$.each(self.planCrossRows(), function(i, crossRow){   
					if(crossRow.selected() == 1 && crossRow != row && crossRow.checkActiveFlag() == 1){
						self.searchModle().checkActiveFlag(1);   
						return false;
					};  
				}); 
			}; 
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
	self.filterCrosses = function(){  
		 var filterTrainNbr = self.searchModle().filterTrainNbr();
		 filterTrainNbr = filterTrainNbr || filterTrainNbr != "" ? filterTrainNbr.toUpperCase() : "all";
		 if(filterTrainNbr == "all"){
			 $.each(self.crossRows.rows(),function(n, crossRow){
				  crossRow.visiableRow(true);
			  });
		 }else{
			 $.each(self.crossRows.rows(),function(n, crossRow){
				 if(crossRow.crossName().indexOf(filterTrainNbr) > -1){
					 crossRow.visiableRow(true);
				 }else{
					 crossRow.visiableRow(false);
				 } 
			  }); 
		 };
	};  
	 
	self.defualtCross = {"crossId":"",
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
		"appointPeriod":"",
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
		"createTime":""};
	//当前选中的交路对象
	self.currentCross = ko.observable(new CrossRow(self.defualtCross)); 
	 
	//currentIndex 
	self.currdate =function(){
		var d = new Date();
		var year = d.getFullYear();    //获取完整的年份(4位,1970-????)
		var month = d.getMonth()+1;       //获取当前月份(0-11,0代表1月)
		var days = d.getDate(); 
		month = ("" + month).length == 1 ? "0" + month : month;
		days = ("" + days).length == 1 ? "0" + days : days;
		return year+"-"+month+"-"+days;
	};
	
	self.dayHeader =function(d){ 
	 
		var month = d.getMonth()+1;       //获取当前月份(0-11,0代表1月)
		var days = d.getDate(); 
		month = ("" + month).length == 1 ? "0" + month : month;
		days = ("" + days).length == 1 ? "0" + days : days;
		return month + "-"+ days;
	};
	
	self.get40Date = function(){
		var d = new Date();
		d.setDate(d.getDate()+35);// + 30
		
		var year = d.getFullYear();    //获取完整的年份(4位,1970-????)
		var month = d.getMonth()+1;       //获取当前月份(0-11,0代表1月)
		var days = d.getDate(); 
		month = ("" + month).length == 1 ? "0" + month : month;
		days = ("" + days).length == 1 ? "0" + days : days;
		return year + "-" + month + "-" + days;
	};
	
	
	
	self.init = function(){  
 
		$("#run_plan_train_times").dialog("close");
		$("#run_plan_train_times_edit_dialog").dialog("close");
		$("#run_plan_train_crew_dialog").dialog("close");
 
		$("#runplan_input_startDate").datepicker();
		$("#runplan_input_endDate").datepicker();
		//x放大2倍
		$("#canvas_event_btn_x_magnification").click(function(){
			if (currentXScaleCount == 32) {
				showErrorDialog("当前已经不支持继续放大啦！");
				return;
			}
			
			//必须清除
			lineList = [];	//列车线对象封装类  用于保存列车线元素，以便重新绘图
			jlList = [];	//用于保存交路数据元素，以便重新绘图
			
			//计算画布比例及倍数
			currentXScale = currentXScale/2;
			currentXScaleCount = currentXScaleCount*2;

			$("#canvas_event_label_xscale").text(currentXScaleCount);
			self.runPlanCanvasPage.clearChart();	//清除画布
			self.runPlanCanvasPage.drawChart({
					 xScale : currentXScale,			//x轴缩放比例
					 xScaleCount : currentXScaleCount,	//x轴放大总倍数
					 yScale : currentYScale,		//y轴缩放比例
					 stationTypeArray:self.searchModle().drawFlags()
				 });
			
		});
		
		//x缩小2倍
		$("#canvas_event_btn_x_shrink").click(function(){
			if (currentXScaleCount == 0.25) {
				showErrorDialog("当前已经不支持继续缩小啦！");
				return;
			}
			
			//必须清除
			lineList = [];	//列车线对象封装类  用于保存列车线元素，以便重新绘图
			jlList = [];	//用于保存交路数据元素，以便重新绘图

			//计算画布比例及倍数
			currentXScale = currentXScale*2;
			currentXScaleCount = currentXScaleCount/2;

			$("#canvas_event_label_xscale").text(currentXScaleCount);
			self.runPlanCanvasPage.clearChart();	//清除画布
			self.runPlanCanvasPage.drawChart({
				 xScale : currentXScale,			//x轴缩放比例
				 xScaleCount : currentXScaleCount,	//x轴放大总倍数
				 yScale : currentYScale,			//y轴缩放比例
				 stationTypeArray:self.searchModle().drawFlags()
			 });
		});
		//y放大2倍
		$("#canvas_event_btn_y_magnification").click(function(){
			if (currentYScaleCount == 8) {
				showErrorDialog("当前已经不支持继续放大啦！");
				return;
			}
			
			//必须清除
			lineList = [];	//列车线对象封装类  用于保存列车线元素，以便重新绘图
			jlList = [];	//用于保存交路数据元素，以便重新绘图
			
			//计算画布比例及倍数
			currentYScale = currentYScale/2;
			currentYScaleCount = currentYScaleCount*2;

			$("#canvas_event_label_yscale").text(currentYScaleCount);
			self.runPlanCanvasPage.clearChart();	//清除画布
			self.runPlanCanvasPage.drawChart({
					 xScale : currentXScale,			//x轴缩放比例
					 xScaleCount : currentXScaleCount,	//x轴放大总倍数
					 yScale : currentYScale,			//y轴缩放比例
					 stationTypeArray:self.searchModle().drawFlags()
				 }); 
		});
		
		//y缩小2倍
		$("#canvas_event_btn_y_shrink").click(function(){
			if (currentYScaleCount == 0.25) {
				showErrorDialog("当前已经不支持继续缩小啦！");
				return;
			}
			
			//必须清除
			lineList = [];	//列车线对象封装类  用于保存列车线元素，以便重新绘图
			jlList = [];	//用于保存交路数据元素，以便重新绘图

			//计算画布比例及倍数
			currentYScale = currentYScale*2;
			currentYScaleCount = currentYScaleCount/2;

			$("#canvas_event_label_yscale").text(currentYScaleCount);
			self.runPlanCanvasPage.clearChart();	//清除画布
			self.runPlanCanvasPage.drawChart({
				 xScale : currentXScale,			//x轴缩放比例
				 xScaleCount : currentXScaleCount,	//x轴放大总倍数
				 yScale : currentYScale,			//y轴缩放比例
				 stationTypeArray:self.searchModle().drawFlags()
			 });
		}); 
		self.runPlanCanvasPage.initPage(); 
		
		self.searchModle().startDay(self.currdate()); 
		self.searchModle().planStartDate(self.currdate());
		self.searchModle().planEndDate(self.get40Date());
		
		commonJsScreenLock(2);
		var initFlag = 0;
		//获取当期系统日期 
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
		
		
	};  
	
	self.loadCrosses = function(){
		self.loadCrosseForPage();
	};
	self.loadCrosseForPage = function(startIndex, endIndex) {  
	 
		commonJsScreenLock();

		self.currentPlanCrossId("");//清除当前选中交路中id
		var bureauCode = self.searchModle().bureau(); 
		var highlingFlag = self.searchModle().highlingFlag();
		var trainNbr = self.searchModle().filterTrainNbr(); 
		var checkFlag = self.searchModle().checkFlag();
		var unitCreateFlag = self.searchModle().unitCreateFlag();
		var chart = self.searchModle().chart();
		var startBureauCode = self.searchModle().startBureau();  
//		var planStartDate = self.searchModle().planStartDate(); 
//		var planEndDate = self.searchModle().planEndDate(); 
		 var planStartDate = $("#runplan_input_startDate").val();
			
		 var planEndDate =  $("#runplan_input_endDate").val();
		 var currentBureanFlag = self.searchModle().currentBureanFlag() ? '1' : '0';   
		 self.searchModle().checkActiveFlag(0); 
		 if(hasActiveRole(bureauCode) && self.searchModle().activeFlag() == 0){
			self.searchModle().activeFlag(1);  
		}else if(!hasActiveRole(bureauCode) && self.searchModle().activeFlag() == 1){
			self.searchModle().activeFlag(0); 
		} 
		 
		
//		if(chart == null){
//			showErrorDialog("请选择方案!");
//			commonJsScreenUnLock();
//			return;
//		}
		self.planCrossRows.remove(function(item) {
			return true;
		}); 
		$.ajax({
				url : basePath+"/runPlan/getPlanCross",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					tokenVehBureau : bureauCode, 
//					highlineFlag : highlingFlag == null ? null : highlingFlag.value,  
					checkFlag : checkFlag == null ? null : checkFlag.value,
					startBureau : startBureauCode,
					unitCreateFlag :  unitCreateFlag == null ? null : unitCreateFlag.value,
							chartId : chart == null ? null: chart.chartId,
					trainNbr : trainNbr,
					startTime : (planStartDate != null ? planStartDate : self.currdate()).replace(/-/g, ''),
					endTime : (planEndDate != null ? planEndDate : self.get40Date()).replace(/-/g, ''),
					currentBureanFlag : currentBureanFlag
					
					  
				}),
				success : function(result) {    
 
					if (result != null && result != "undefind" && result.code == "0") {
						//var rows = [];
						if(result.data != null){ 
							
							$.each(result.data,function(n, crossInfo){ 
								self.planCrossRows.push(new CrossRow(crossInfo));  
							}); 
							//self.crossRows.loadPageRows(result.data.totalRecord, rows);
						} 
						
						 $("#cross_table_crossInfo").freezeHeader(); 
						 
					} else {
						showErrorDialog("获取车底交路列表失败");
					};
				},
				error : function() {
					showErrorDialog("获取车底交路列表失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
	};
	//必须定义在load函数之后
	self.crossRows = new PageModle(50, self.loadCrosseForPage);
	
	self.saveCrossInfo = function() { 
		alert(self.currentCross().tokenVehBureau());
	};
	 
	self.showUploadDlg = function(){
		
		$("#file_upload_dlg").dialog("open"); 
	};
	
	self.showRunPlans = function(){  
		if($('#learn-more-content').is(":visible")){
			$('#learn-more-content').hide();
			$('#plan_cross_default_panel').css({height: '700px'});
			$('#plan_cross_panel_body').css({height: '585px'});
			$('#plan_train_panel_body').css({height: '585px'});
			$('#canvas_parent_div').css({height: '700px'});
		}else{
			 $('#learn-more-content').show(); 
			 $('#plan_cross_default_panel').css({height: '530px'});
			 $('#plan_cross_panel_body').css({height: '415px'});
			 $('#plan_train_panel_body').css({height: '415px'});
			 $('#canvas_parent_div').css({height:'530px'});
		}
	    
	};
	
	self.showDialog = function(id, title){
		$('#' + id).dialog({ title:  title, autoOpen: true, height:600,width: 800, modal: false, draggable: true, resizable:true })
	};
	
	self.showCrossTrainDlg = function(){
		$("#cross_train_dlg").dialog("open");
	};
	
	self.showCrossTrainTimeDlg = function(){
		
		$("#run_plan_train_times").dialog({inline: false, top:10});
	};
	
	self.trainNbrChange = function(n,  event){
		self.searchModle().filterTrainNbr(event.target.value.toUpperCase());
	};
	
	self.showTrainTimes = function(row) {
		self.currentTrain(row);
		if(self.tabIndex() != 1) {//当前显示非车底交路图tab
			return;
		}
		
//		commonJsScreenLock(3);
//		self.createCrossMap();//加载车底交路图
		
		self.runPlanCanvasPage.reDrawByTrainNbr(row.trainNbr);
//		self.stns.remove(function(item){
//			return true;
//		});
//		if(row.times().length > 0){ 
//			$.each(row.times(), function(i, n){
//				self.stns.push(n); 
//			}) ;
//			 
//		}else{
//			$.ajax({
//				url : basePath+"/jbtcx/queryTrainTimes",
//				cache : false,
//				type : "POST",
//				dataType : "json",
//				contentType : "application/json",
//				data :JSON.stringify({   
//					trainId : row.baseTrainId
//				}),
//				success : function(result) {  
//					if (result != null && result != "undefind" && result.code == "0") {  
//						row.loadTimes(result.data);  
//						$.each(row.times(), function(i, n){
//							self.stns.push(n); 
//						});
//					} else {
//						showErrorDialog("获取列车时刻表失败");
//					};
//				},
//				error : function() {
//					showErrorDialog("获取列车时刻表失败");
//				},
//				complete : function(){
//					commonJsScreenUnLock();
//				}
//			}); 
//		}
		
	};  
	
	self.deleteCrosses = function(){ 
		var crossIds = "";
		var crosses = self.planCrossRows(); 
		var delCrosses = [];
		for(var i = 0; i < crosses.length; i++){ 
			if(crosses[i].selected() == 1 && hasActiveRole(crosses[i].tokenVehBureau())){ 
				crossIds += (crossIds == "" ? "" : ",");
				crossIds += crosses[i].planCrossId();  
				delCrosses.push(crosses[i]); 
			}else if(crosses[i].selected() == 1 && !hasActiveRole(crosses[i].tokenVehBureau())){
				showWarningDialog("你没有权限删除:" + crosses[i].crossName());
				return;
			}  
		}
		if(crossIds == ""){
			showErrorDialog("没有可删除的记录");
			return;
		}
		
		showConfirmDiv("提示", "你确定要执行删除操作?", function (r) { 
	        if (r) { 
				$.ajax({
					url : basePath+"/runPlan/deletePlanCrosses",
					cache : false,
					type : "POST",
					dataType : "json",
					contentType : "application/json",
					data :JSON.stringify({  
						planCrossIds : crossIds
					}),
					success : function(result) {     
						if(result.code == 0){
							$.each(delCrosses, function(i, n){ 
								self.planCrossRows.remove(n); 
							});
							showSuccessDialog("删除车底交路成功"); 
						}else{
							showErrorDialog("删除车底交路失败");
						};
					}
				}); 
	        };
		});
	};
	
	self.createTrainLines = function(){  
		var crossIds = "";
		var delCrosses = [];
		var crosses = self.planCrossRows();
		for(var i = 0; i < crosses.length; i++){  
			if(crosses[i].selected() == 1 && crosses[i].checkFlag() == 2 && hasActiveRole(crosses[i].tokenVehBureau())){ 
				crossIds += (crossIds == "" ? "" : ",");
				crossIds += crosses[i].planCrossId();  
				delCrosses.push(crosses[i]); 
			}else if(crosses[i].selected() == 1){
				if(!hasActiveRole(crosses[i].tokenVehBureau())){
					showWarningDialog("你没有权限生成:" + crosses[i].crossName() + " 的运行线");
					return;
				}else if(crosses[i].checkFlag() != 2){
					showWarningDialog(crosses[i].crossName() + "经由局未全部审核");
					return;
				} 
			}  
			
		}  
		 var planStartDate = $("#runplan_input_startDate").val();
			
		 var planEndDate =  $("#runplan_input_endDate").val();
		 if(crossIds == ""){
			 showWarningDialog("未选中数据");
		 }
		 commonJsScreenLock();
		 $.ajax({
				url : basePath+"/runPlan/handleTrainLinesWithCross",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					planCrossIds : crossIds,
					startDate : (planStartDate != null ? planStartDate : self.currdate()).replace(/-/g, ''),
					endDate : (planEndDate != null ? planEndDate : self.get40Date()).replace(/-/g, '')
				}),
				success : function(result) {     
					if(result.code == 0){ 
						showSuccessDialog("正在生成运行线");
					}else{
						showErrorDialog("生成运行线失败");
					}
				},
				error : function() {
					showErrorDialog("生成运行线失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
	};
	self.clearData = function(){ 
		 self.currentCross(new CrossRow({"crossId":"",
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
				"appointPeriod":"",
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
		 self.stns.remove(function(item){
			return true;
		 });
		 self.planCrossRows.remove(function(item){
			return true;
		 });
		 
		 self.trainPlans.remove(function(item){
			return true;
		 }); 
		self.planDays.remove(function(item){
			return true;
		 }); 
		 self.trains.remove(function(item){
			return true;
		 });  
		 self.currentTrain = ko.observable();
	};
	self.bureauChange = function(){ 
		if(hasActiveRole(self.searchModle().bureau())){
			self.searchModle().activeFlag(1); 
			self.clearData();
		}else if(self.searchModle().activeFlag() == 1){
			self.searchModle().activeFlag(0);
			self.clearData();
		}
	};
	
	//审核
	self.checkCrossInfo = function(){ 
		var crossIds = "";
		var checkedCrosses = [];
		var crosses = self.planCrossRows();
		for(var i = 0; i < crosses.length; i++){  
			if(crosses[i].checkFlag() == 2 && crosses[i].selected() == 1){ 
				showWarningDialog(crosses[i].crossName() + "已审核"); 
				return;
			}else if(crosses[i].checkedBureau() != null && crosses[i].checkedBureau().indexOf(currentUserBureau) > -1 && crosses[i].selected() == 1){  
				showWarningDialog(crosses[i].crossName() + "本局已审核"); 
				return;
			}else if(crosses[i].selected() == 1){
				crossIds += (crossIds == "" ? "" : ";");
				crossIds += crosses[i].planCrossId() + "#" + crosses[i].relevantBureau();
				checkedCrosses.push(crosses[i]); 
			}; 
		}  
		var planStartDate = $("#runplan_input_startDate").val(); 
		var planEndDate =  $("#runplan_input_endDate").val();
		
		if(crossIds == ""){
			showWarningDialog("没有可审核的");
			return;
		}
		commonJsScreenLock();
		 $.ajax({
				url : basePath+"/runPlan/checkCrossRunLine",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({   
					startTime : (planStartDate != null ? planStartDate : self.currdate()).replace(/-/g, ''),
					endTime : (planEndDate != null ? planEndDate : self.get40Date()).replace(/-/g, ''),
					planCrossIds : crossIds
				}),
				success : function(result) {     
					if(result.code == 0){
						$.each(checkedCrosses, function(i, n){
							n.checkedBureau(n.checkedBureau() + "," + currentUserBureau); 
						});
						showSuccessDialog("审核成功");
					}else{
						showErrorDialog("审核失败");
					}
				},
				error : function() {
					showErrorDialog("审核失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
		
	};
	
	self.createCrossMap = function(){
		if (self.currentPlanCrossId() == "" || self.currentPlanCrossId()=="undefined") {
			showWarningDialog("请先选择一条交路信息");
			return;
		}
//		 var planStartDate = self.searchModle().planStartDate();
//		
//		 var planEndDate = self.searchModle().planEndDate();

		self.runPlanCanvasPage.clearChart();	//清除画布
		 var planStartDate = $("#runplan_input_startDate").val();
			
		 var planEndDate =  $("#runplan_input_endDate").val();
		 
		 $.ajax({
				url : basePath+"/cross/createCrossMap",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					planCrossId : self.currentPlanCrossId(),  
					startTime : planStartDate != null ? planStartDate : self.currdate(),
					endTime : planEndDate != null ? planEndDate : self.get40Date()
				}),
				success : function(result) {    
					if (result != null && result != "undefind" && result.code == "0") {
						if (result.data !=null) {   
							canvasData = {
									grid: $.parseJSON(result.data.gridData),
									jlData: $.parseJSON(result.data.myJlData)
							};   
							self.runPlanCanvasPage.drawChart({stationTypeArray:self.searchModle().drawFlags()}); 
						}
						 
					} else {
						showErrorDialog("获取车底交路绘图数据失败");
					} 
				},
				error : function() {
					showErrorDialog("获取车底交路绘图数据失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
	};
	
	

	//标签页序号
	self.tabIndex = ko.observable(0);
	/**
	 * 交路基本信息tab页切换事件
	 */
	self.toCrossTab = function() {
		self.tabIndex(0);
	};
	

	/**
	 * 车底交路图tab页切换事件
	 */
	self.toRunMapTab = function() {
		self.tabIndex(1);
		if (self.currentPlanCrossId() != "" && self.currentPlanCrossId()!="undefined") {
			commonJsScreenLock();//锁屏
			self.createCrossMap();//加载车底交路图
		}
	};
	
	
	self.showTrains = function(row) {   
		self.setCurrentCross(row); 
		self.currentPlanCrossId(row.planCrossId());//用于车底交路图tab
		
		if(self.tabIndex() == 1) {//当前显示车底交路图tab
			commonJsScreenLock(3);
			self.createCrossMap();//加载车底交路图
		} else {
			commonJsScreenLock(2);
		}

//		self.createCrossMap(row);//加载车底交路图
//		self.stns.remove(function(item) {
//			return true;
//		});
		self.trains.remove(function(item) {
			return true;
		});
		
		var trains = row.crossName().split("-");
		
		$.each(trains, function(n, trainNbr){
			var row = new TrainRow({"trainNbr": trainNbr});
			self.trains.push(row); 
		}); 
		 
		self.loadRunPlans(row.planCrossId()); 
		
		 $.ajax({
				url : basePath+"/cross/getPlanCrossInfo",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					planCrossId : row.planCrossId()
				}),
				success : function(result) {
					if (result != null && result != "undefind" && result.code == "0") {
						if (typeof result.data =="object") {
							self.setCurrentCross(new CrossRow(result.data)); 
						}else{
							self.setCurrentCross(new CrossRow(self.defualtCross));
							showWarningDialog("没有找打对应的交路被找到");
						}
						 
					} else {
						showErrorDialog("获取列车列表失败");
					} 
				},
				error : function() {
					showErrorDialog("获取列车列表失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
		
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
		self.runPlanCanvasPage.drawChart({stationTypeArray:self.searchModle().drawFlags()}); 
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
	
	self.checkActiveFlag = ko.observable(0);  
	
	self.activeCurrentCrossFlag = ko.observable(0);  
	
	self.drawFlags =ko.observableArray(['0']); 
	
	self.planStartDate = ko.observable();
	
	self.currentBureanFlag = ko.observable(0);
	
	self.planEndDate = ko.observable();
	
	self.bureaus = ko.observableArray();
	
	self.startBureaus = ko.observableArray();
	
	self.charts = ko.observableArray();
	 
	self.highlingFlags = highlingFlags;
	
	self.unitCreateFlags = unitCreateFlags; 
	
	self.filterCheckFlag = ko.observable(0);
	
	self.filterUnitCreateFlag = ko.observable(0);
		
	self.checkFlags = checkFlags;
	
	self.highlingFlag = ko.observable();
	
	self.checkFlag = ko.observable(); 
	 
	self.unitCreateFlag = ko.observable(); 
	
	self.bureau = ko.observable();
	 
	self.chart =  ko.observable();
	
	self.startDay = ko.observable();
	
	self.startBureau = ko.observable();
	
	self.filterTrainNbr = ko.observable();
	
	self.showCrossMap = ko.observable(0);
	
	self.shortNameFlag = ko.observable(2);
	
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

 

function CrossRow(data) {
	var self = this; 
	
	if(data == null){
		return ;
	}
	
	self.visiableRow =  ko.observable(true); 
	
	self.selected =  ko.observable(0); 
	
	self.baseCrossId = data.baseCrossId; 
	
	self.crossId = data.crossId; 
	
	self.shortNameFlag =  ko.observable(true);
	
	self.planCrossId = ko.observable(data.planCrossId);  
	 
	self.crossName = ko.observable(data.planCrossName == null ? data.crossName : data.planCrossName);   
	
	self.shortName = ko.computed(function(){
		if(data.planCrossName != null){
			trainNbrs = data.planCrossName.split('-');
			if(trainNbrs.length > 2){
				return trainNbrs[0] + '-......-' + trainNbrs[trainNbrs.length-1];
			}else{
				return data.planCrossName;
			}
		}else{
			return "";
		}
		
	});  
	
	self.checkFlag = ko.observable(data.checkType);
	
	self.unitCreateFlag = ko.observable(data.unitCreateFlag);
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
	self.appointPeriod = ko.observable(data.appointPeriod); 
	self.crossSection = ko.observable(data.crossSection);
	self.throughline = ko.observable(data.throughline);
	self.startBureau = ko.observable(data.startBureau); 
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
	
	self.relevantBureauShowValue =  ko.computed(function(){ 
		var result = "";
		 if(data.relevantBureau != null && data.relevantBureau != "null"){  
			 for(var j = 0; j < data.relevantBureau.length; j++){
				 for(var i = 0; i < gloabBureaus.length; i++){
					 if(data.relevantBureau.substring(j, j + 1) == gloabBureaus[i].code){
						 result += result == "" ? gloabBureaus[i].shortName : "、" + gloabBureaus[i].shortName;
						 break;
					 };
				 };
			 }; 
		 } 
		 return  result; 
	});
	
	self.relevantBureau =  ko.observable(data.relevantBureau);
	
	self.checkedBureau = ko.observable(data.checkedBureau);
	
	self.checkedBureauShowValue =  ko.computed(function(){ 
		var result = "";
		 if(self.checkedBureau() != null && self.checkedBureau() != "null"){  
			 var bs = self.checkedBureau().split(","); 
			 for(var j = 0; j < bs.length; j++){
				 for(var i = 0; i < gloabBureaus.length; i++){
					 if(bs[j] == gloabBureaus[i].code){
						 result += result == "" ? gloabBureaus[i].shortName : "、" + gloabBureaus[i].shortName;
						 break;
					 };
				 };
			 };
		 } 
		 return result; 
	}); 
	self.activeFlag = ko.computed(function(){
		return hasActiveRole(data.tokenVehBureau);
	});   
	
	self.checkActiveFlag = ko.computed(function(){
//		return 1;
		return data.relevantBureau != null ? (data.relevantBureau.indexOf(currentUserBureau) > -1 ? 1 : 0) : 0;
	}); 
//	'fa fa-check-square-o' : 'fa fa-pencil-square-o'
	
	self.checkCss = ko.computed(function(){ 
		if(self.checkFlag() != 2 
				&& data.relevantBureau != null 
				&& data.relevantBureau.indexOf(currentUserBureau) > -1 
				){//和当前局相关并且被当前局审核过了的
			if(self.checkedBureau() != null){
				var checkedBureau = self.checkedBureau().split(",");
				var temp = "";
				for(var i = 0; i < self.relevantBureau().length; i++){
					if(self.checkedBureau().indexOf(self.relevantBureau().substring(i, i + 1)) > -1){
						temp += self.relevantBureau().substring(i, i + 1);
					};
				} 
				if(temp == self.relevantBureau()){
					self.checkFlag(2);
					return "fa fa-check-square-o green";
				}else if(self.checkedBureau().indexOf(currentUserBureau) > -1){
					return "fa fa-pencil-square-o green";
				}else{
					return "fa fa-pencil-square-o red";
				};
			}else{
				return "fa fa-pencil-square-o red";
			}; 
		}else if(self.checkFlag() != 2 
				&& (data.relevantBureau == null || (data.relevantBureau != null
				&& data.relevantBureau.indexOf(currentUserBureau) < 0))){//和当前局无关 未被完全审核的
			return "fa fa-pencil-square-o gray";
		}else if(self.checkFlag() == 2){
			if(data.relevantBureau == null || (data.relevantBureau != null
						&& data.relevantBureau.indexOf(currentUserBureau) < 0)){
				return "fa fa-check-square-o gray";
			}
			return "fa fa-check-square-o green";
		}; 
	}); 
	self.tokenVehDept = ko.observable(data.tokenVehDept);
	self.tokenVehDepot = ko.observable(data.tokenVehDepot);
	self.tokenPsgBureau = ko.observable(data.tokenPsgBureau);
	self.tokenPsgBureauShowValue = ko.computed(function(){ 
		var result = "";
		 if(data.tokenPsgBureau != null && data.tokenPsgBureau != "null"){
			 var bs = data.tokenPsgBureau.split("、"); 
			 result = data.tokenPsgBureau;
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
	self.tokenPsgDept = ko.observable(data.tokenPsgDept);
	self.tokenPsgDepot = ko.observable(data.tokenPsgDepot);
	self.locoType = ko.observable(data.locoType);
	self.crhType = ko.observable(data.crhType);
	self.elecSupply = ko.observable(data.elecSupply);
	self.dejCollect = ko.observable(data.dejCollect);
	self.airCondition = ko.observable(data.airCondition);
	self.note = ko.observable(data.note);  
};
//end 交路row定义


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
	self.baseTrainId = data.baseTrainId;
	self.trainNbr = data.trainNbr;//TRAIN_NBR
	self.startStn = data.startStn;//START_STN
	
	self.times = ko.observableArray(); 
	self.simpleTimes = ko.observableArray(); 
	self.loadTimes = function(times){
		$.each(times, function(i, n){ 
			var timeRow = new TrainTimeRow(n);
			self.times.push(timeRow);
			if(n.stationFlag != 'BTZ'){
				self.simpleTimes.push(timeRow);
			}
		});
	}; 
} ;
function filterValue(value){
	return value == null || value == "null" ? "--" : value;
};

function TrainRunPlanRow(data){
	var self = this; 
	self.trainNbr = data.trainNbr;
	self.runPlans =  ko.observableArray();
	
	if(data.runPlans !=null){
		$.each(data.runPlans, function(i, n){
			self.runPlans.push(new RunPlanRow(n));
		});
	}
}

function RunPlanRow(data){
	var self = this; 
	self.color = ko.observable("gray");
	self.runFlagStr = ko.computed(function(){ 
		switch (data.runFlag) {
		case '9':
			self.color("gray");
			return "<span class='label label-danger'>停</span>";//"停";
			break;
		case '1':
			self.color("green");
			return "<span class='label label-success'>开</span>";//"开";
			break;
		case '2':
			self.color("blue");
			return "<span class='label label-info'>备</span>";//"备";
			break;
		default: 
			return '';
			break;
		}
	}); 
}

function TrainTimeRow(data) { 
	var self = this; 
	self.index = data.childIndex + 1; 
	self.stnName = filterValue(data.stnName);
	self.bureauShortName = filterValue(data.bureauShortName);
	self.sourceTime = filterValue(data.arrTime != null ? data.arrTime.replace(/-/g, "").substring(4) : "");
	self.targetTime = filterValue(data.dptTime != null ? data.dptTime.replace(/-/g, "").substring(4) : "");
	self.stepStr = GetDateDiff(data); 
	self.trackName = filterValue(data.trackName);  
	self.runDays = data.runDays;
	self.stationFlag = data.stationFlag;
	 
}; 
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
function openLogin() {
	$("#file_upload_dlg").dialog("open");
}
 
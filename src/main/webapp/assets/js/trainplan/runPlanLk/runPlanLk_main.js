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
	
	//车辆担当局
	self.searchModle = ko.observable(new searchModle());
	
	self.planTrainLkRows =  ko.observableArray();	//临客开行计划list
	
	
	/**
	 * 加载开行情况
	 * @param crossId
	 */
	self.loadRunPlans = function(_trainNbr){
		console.log("----------------    加载开行情况       ------------------");
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
				url : basePath+"/runPlanLk/getTrainLkRunPlans",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					startDate:startDate.replace(/-/g, ""),
					endDate: endDate.replace(/-/g, ""),
					trainNbr: _trainNbr
				}),
				success : function(result) {

					console.dir(result);
					console.log("----------------    加载开行情况       ------------------");
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
		console.dir(currentTrain);
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
	 
	self.defualtCross = {"planTrainId":"",
		"tokenVehBureau":"", 
		"startBureauShortName":"",
		"trainNbr":"",
		"cmdShortInfo":"",
		"startStn":"",
		"endStn":""};
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
		d.setDate(d.getDate() + 40);
		
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
		
		
		self.runPlanCanvasPage.initPage(); 
		
		self.searchModle().startDay(self.currdate()); 
		self.searchModle().planStartDate(self.currdate());
		self.searchModle().planEndDate(self.get40Date());
		
		commonJsScreenLock();
		//获取路局列表   始发局及担当局
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
		self.loadPlanTrainLkPage();
	};
	
	
	
	/**
	 * 查询临客开行计划信息
	 * @param startIndex
	 * @param endIndex
	 */
	self.loadPlanTrainLkPage = function(startIndex, endIndex) {  
		commonJsScreenLock();//锁屏
		var planStartDate = $("#runplan_input_startDate").val();//开始日期
		var planEndDate =  $("#runplan_input_endDate").val();//截至日期
		var currentBureanFlag = self.searchModle().currentBureanFlag() ? '1' : '0';//本局相关
		
		
		self.planTrainLkRows.remove(function(item) {
			return true;
		});
		$.ajax({
				url : basePath+"/runPlanLk/getPlanTrainLkInfo",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					tokenVehBureau : self.searchModle().bureau(),//车辆担当局
					trainNbr : self.searchModle().filterTrainNbr(),//车次
					startBureau : self.searchModle().startBureau(),//始发局,
					startDate : (planStartDate != null ? planStartDate : self.currdate()).replace(/-/g, ''),//开始日期
					endDate : (planEndDate != null ? planEndDate : self.get40Date()).replace(/-/g, ''),//截至日期
					isRelationBureau : currentBureanFlag//本局相关
				}),
				success : function(result) {
					console.log("===============");
					console.dir(result);
					if (result != null && result != "undefind" && result.code == "0") {
						if(result.data != null){
							$.each(result.data,function(n, crossInfo){
								self.planTrainLkRows.push(new CrossRow(crossInfo));
							});
						}
					} else {
						showErrorDialog("获取临客开行计划信息列表失败");
					};
				},
				error : function() {
					showErrorDialog("获取临客开行计划信息列表失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			});
	};
	//必须定义在load函数之后
	self.crossRows = new PageModle(50, self.loadPlanTrainLkPage);
	
	self.saveCrossInfo = function() { 
		alert(self.currentCross().tokenVehBureau());
	};
	 
	self.showUploadDlg = function(){
		
		$("#file_upload_dlg").dialog("open"); 
	};
	
	self.showRunPlans = function(){  
		if($('#learn-more-content').is(":visible")){
			$('#learn-more-content').hide();
			$('#plan_cross_default_panel').css({height: '620px'});
			$('#plan_cross_panel_body').css({height: '490px'});
			$('#plan_train_panel_body').css({height: '490px'});
			$('#canvas_parent_div').css({height: '630px'});
		}else{
			 $('#learn-more-content').show(); 
			 $('#plan_cross_default_panel').css({height: '520px'});
			 $('#plan_cross_panel_body').css({height: '390px'});
			 $('#plan_train_panel_body').css({height: '390px'});
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
	
	
	
	self.clearData = function(){ 
		 self.currentCross(new CrossRow(self.defualtCross())); 
		 self.stns.remove(function(item){
			return true;
		 });
		 self.planTrainLkRows.remove(function(item){
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
	
	
	self.createCrossMap = function(row){
		console.log("----------------    画图------------------  row.planTrainId="+row.planTrainId()+"     row.trainNbr="+row.trainNbr());
		
		
		self.runPlanCanvasPage.clearChart();	//清除画布
		var planStartDate = $("#runplan_input_startDate").val();
		
		var planEndDate =  $("#runplan_input_endDate").val();
		 
		 $.ajax({
				url : basePath+"/runPlanLk/getTrainLkInfoForPlanTrainId",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					planTrainId : row.planTrainId(),
					trainNbr : row.trainNbr()
				}),
				success : function(result) {
					console.dir(result);
					console.log("----------------    画图------------------");
					if (result != null && result != "undefind" && result.code == "0") {
						if (result.data !=null) {
							canvasData = {
									grid: result.data.gridData,//$.parseJSON(result.data.gridData),
									jlData: result.data.myJlData//$.parseJSON(result.data.myJlData)
							};   
							self.runPlanCanvasPage.drawChart({stationTypeArray:self.searchModle().drawFlags()}); 
						}
						 
					} else {
						showErrorDialog("获取车底绘图数据失败");
					} 
				},
				error : function() {
					showErrorDialog("获取车底绘图数据失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
	};
	self.showTrains = function(row) {   
		self.setCurrentCross(row); 
		commonJsScreenLock(2);//锁屏2次
		self.createCrossMap(row);
		
		self.trains.remove(function(item) {
			return true;
		});
		
		
		//加载开行情况
		self.loadRunPlans(row.trainNbr());
		
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
	
	
	self.planTrainId = ko.observable(data.planTrainId);//id
	self.tokenVehBureau = ko.observable(data.tokenVehBureau);//车辆担当局
	self.startBureauShortName = ko.observable(data.startBureauShortName); //始发局
	self.trainNbr = ko.observable(data.trainNbr); //车次
	self.cmdShortInfo = ko.observable(data.cmdShortInfo); //来源   命令简要信息(发令日期+命令号+命令子项号)
	self.startStn = ko.observable(data.startStn);//车辆始发站
	self.endStn = ko.observable(data.endStn); //终到站
	
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
	
	//列表记录行点击事件 显示相关局
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
	
	self.relevantBureau =  ko.observable(data.relevantBureau);//相关局
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
	self.runFlag = ko.computed(function(){ 
		switch (data.runFlag) {
		case '0':
			self.color("gray");
			return "停";
			break;
		case '1':
			self.color("green");
			return "开";
			break;
		case '2':
			self.color("blue");
			return "备";
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
 
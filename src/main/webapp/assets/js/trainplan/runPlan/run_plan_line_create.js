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
	cross.updateTrainRunPlanStatus(runPlan);
	
}
//{unitCrossId:"1",trainNbr: "G1", day:"20140723", runFlag: "1"}; 
function updateTrainRunPlanDayFlag(data){ 
	var runPlan = $.parseJSON(data);
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
	
	self.gloabBureaus = [];    
	//车辆担当局
	self.searchModle = ko.observable(new searchModle());  
	
	self.runPlanTableWidth = ko.computed(function(){
		return self.planDays().length * 40 + 80 + 'px';
	});
	
	self.createRunPlanTotalCount = ko.observable(0);
	
	self.createRunPlanCompletedCount = ko.observable(0);
	
	self.createRunPlanErrorCount = ko.observable(0);
	
	self.selectedRunPlan = ko.observableArray();
	
	self.selectRunPlan = function(row){ 
		if(row.selected() == 0){
			row.selected(1);
			self.selectedRunPlan.push(row);
		}else{
			self.selectedRunPlan.remove(row);
			row.selected(0);
		}  
	};
	
	self.completedMessage = ko.computed(function(){ 
		return self.createRunPlanTotalCount() == 0 ? "" : "选中：" + self.createRunPlanTotalCount() + "个计划，目前已成功生成：" + self.createRunPlanCompletedCount() + "个运行线，另有：" + self.createRunPlanErrorCount() + "个运行线失败";
	});
	
	self.updateTrainRunPlanDayFlag = function(runPlan){ 
		$.each(self.selectedRunPlan(), function(x, n){ 
			if(n.planTrainId == runPlan.planTrainId){
				n.createFlag(runPlan.createFlag);
				return false;
			};
		});
	};
	
	self.updateTrainRunPlanStatus = function(runPlan){
		if(runPlan.createFlag == 1){
			self.createRunPlanCompletedCount(self.createRunPlanCompletedCount() + 1);
		}else{
			self.createRunPlanErrorCount(self.createRunPlanErrorCount() + 1);
		}
		$.each(self.selectedRunPlan(), function(x, n){ 
			if(n.planTrainId() == runPlan.planTrainId){
				n.selected(0);
				n.createFlag(runPlan.createFlag);
				return false;
			};
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
	
	self.selectCrosses = function(){
		$.each(self.trainPlans(), function(i, crossRow){ 
			if(self.crossAllcheckBox() == 1){
				crossRow.selected(0); 
				$.each(self.trainPlans(), function(i, train){ 
						$.each(train.runPlans(), function(z, n){
							if(n.createFlag() == 0){
								n.selected(0);
								self.selectedRunPlan.remove(n);
							} 
						}); 
				});
			}else{ 
				crossRow.selected(1);   
				$.each(self.trainPlans(), function(i, train){ 
					$.each(train.runPlans(), function(z, n){
						if(n.createFlag() == 0){
							n.selected(1);
							self.selectedRunPlan().push(n);
						} 
					}); 
			   });
			}  
		});  
	};
	
	self.selectCross = function(row){ 
		if(row.selected() == 0){  
			self.crossAllcheckBox(1);   
			$.each(self.trainPlans(), function(i, crossRow){   
				if(crossRow.selected() != 1 && crossRow != row){
					self.crossAllcheckBox(0);
					return false;
				} 
			});  
			$.each(self.trainPlans(), function(i, train){
				if(train.planTrainId == row.planTrainId){
					$.each(train.runPlans(), function(z, n){
						if(n.createFlag() == 0){
							n.selected(1);
							self.selectedRunPlan().push(n);
						} 
					});
				}
			});
			
		}else{
			self.crossAllcheckBox(0); 
			$.each(self.trainPlans(), function(i, train){
				if(train.planTrainId == row.planTrainId){
					$.each(train.runPlans(), function(z, n){
						if(n.createFlag() == 0){
							n.selected(0);
							self.selectedRunPlan.remove(n);
						} 
					});
				}
			});
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
	
	self.trainNbrChange = function(n,  event){
		self.searchModle().filterTrainNbr(event.target.value.toUpperCase());
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
 
		$("#run_plan_train_times").dialog("close"); 
 
		$("#runplan_input_startDate").datepicker();
		$("#runplan_input_endDate").datepicker();
		//x放大2倍 
		self.searchModle().startDay(self.currdate()); 
		self.searchModle().planStartDate(self.currdate());
		self.searchModle().planEndDate(self.get40Date());
		
		commonJsScreenLock(2);
		//获取当期系统日期 
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
	self.initDataHeader = function(){
		var startDate = $("#runplan_input_startDate").val(); 
		var endDate =  $("#runplan_input_endDate").val();   
		
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
		var highlingFlag = self.searchModle().highlingFlag();
		var trainNbr = self.searchModle().filterTrainNbr(); 
		var checkFlag = self.searchModle().checkFlag();
		var unitCreateFlag = self.searchModle().unitCreateFlag();
		var startBureauCode = self.searchModle().startBureau();   
		var chart = self.searchModle().chart();   
		var startDate = $("#runplan_input_startDate").val(); 
		var endDate =  $("#runplan_input_endDate").val();  
		
		self.createRunPlanTotalCount(0);
		
		self.createRunPlanCompletedCount(0);
		
		self.createRunPlanErrorCount(0);
		
		self.initDataHeader();
		 
		$.ajax({
				url : "../runPlan/getTrainRunPlansForCreateLine",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({ 
					tokenVehBureau : bureauCode, 
					startBureau : startBureauCode,
					trainNbr : trainNbr,
					startDay : startDate.replace(/-/g, ""),
					endDay : endDate.replace(/-/g, ""),
					rownumstart : startIndex, 
					rownumend : endIndex,
				}),
				success : function(result) {    
                    var trainPlans = {};
					if (result != null && result != "undefind" && result.code == "0") { 
						console.log(result.data);
						 $.each(result.data, function(z, n){ 
							 var planCross = trainPlans[n.planCrossId];
							 if(planCross == null){
								 var trainPlanData = {
											crossName: n.crossName, 
											planCrossId: n.planCrossId,
											tokenVehBureau: n.tokenVehBureau,
											startDate: startDate,
											endDate: endDate, 
											baseTrainId: n.baseTrainId,
											createFlag: 0,
											trainSort: 0 
									};
									//默认吧交路作为第一条记录
								    var planCross = new TrainRunPlanRow(trainPlanData);
									self.trainPlans.push(planCross);
									var crossNames = n.crossName.split("-");
									//把交路拆分成车，然后依次添加在她的后面
									for(var i = 0; i < crossNames.length; i++){
										var trainPlanData = {
												crossName: n.crossName, 
												startDate: startDate,
												endDate: endDate,
												planCrossId: n.planCrossId,
												trainNbr: crossNames[i], 
												trainSort: i + 1,
												createFlag: 0,
												tokenVehBureau: n.tokenVehBureau
										};
										self.trainPlans.push(new TrainRunPlanRow(trainPlanData));
									} ; 
									trainPlans[n.planCrossId] = planCross; 
							 }else{
								 $.each(self.trainPlans(), function(x, t){ 
										if(t.planCrossId == n.planCrossId && t.trainNbr == n.trainNbr){
											for(var i = 0; i < t.runPlans().length; i++){ 
												if(t.runPlans()[i].day.replace(/-/g, "") == n.runDay){ 
													t.runPlans()[i].runFlag(parseInt(n.runFlag));
													t.runPlans()[i].createFlag(parseInt(n.createFlag));
													t.runPlans()[i].planTrainId(n.planTrainId);
													t.runPlans()[i].baseTrainId(n.baseTrainId);
													break;
												};
											};
											return false;
										};
									});
							 } 
						 });
						
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
   
	
	self.createTrainLines = function(){   
		 var planTrains = []; 
		 
		 for(var i = 0; i < self.selectedRunPlan().length; i++){
			 planTrains.push({planTrainId: self.selectedRunPlan()[i].planTrainId(),
				 baseTrainId: self.selectedRunPlan()[i].baseTrainId(),
				 day: self.selectedRunPlan()[i].day});
		 }
		 
		 self.createRunPlanTotalCount(self.selectedRunPlan().length);
		 
		 self.createRunPlanCompletedCount(0);
			
		 self.createRunPlanErrorCount(0);
		 
		 commonJsScreenLock();
		 $.ajax({
				url : "../runPlan/createRunPlanForPlanTrain",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					planTrains:  planTrains, 
					msgReceiveUrl: "/trainplan/runPlan/runPlanLineCreate"}),
				success : function(result) { 
					if(result != null && result.code == 0){ 
						showSuccessDialog("正在生成开行计划");
					}else{
						showErrorDialog("生成开行计划失败");
					}
				},
				error : function() {
					showErrorDialog("生成开行计划失败"); 
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
				url : "runPlan/checkCrossRunLine",
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
//	{unitCrossId:"1",trainNbr: "G1", day:"20140723", runFlag: "1"}; 
//	{unitCrossId:"1",status: "1"}
	self.createCrossMap = function(row){ 
		
//		 var planStartDate = self.searchModle().planStartDate();
//		
//		 var planEndDate = self.searchModle().planEndDate(); 
		 var planStartDate = $("#runplan_input_startDate").val();
			
		 var planEndDate =  $("#runplan_input_endDate").val();
		 
		 $.ajax({
				url : "cross/createCrossMap",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					planCrossId : row.planCrossId(),  
					startTime : planStartDate != null ? planStartDate : self.currdate(),
					endTime : planEndDate != null ? planEndDate : self.get40Date()
				}),
				success : function(result) {    
					if (result != null && result != "undefind" && result.code == "0") {
						if (result.data !=null) {   
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
	self.showTrains = function(row) {   
		self.setCurrentCross(row); 
		commonJsScreenLock(3);
		self.createCrossMap(row);
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
				url : "cross/getPlanCrossInfo",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					planCrossId : row.planCrossId()
				}),
				success : function(result) {    
					if (result != null && result != "undefind" && result.code == "0") {
						if (result.data !=null && result.data.length > 0) {   
							if(result.data[0].crossInfo != null){
								self.setCurrentCross(new CrossRow(result.data[0].crossInfo)); 
							}else{ 
								self.setCurrentCross(new CrossRow(self.defualtCross));
								showWarningDialog("没有找打对应的交路被找到");
							}  
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
	
	self.planCrossId = data.planCrossId;
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
			return "";
			break;
		case 1: 
			return "(正在生成。。。。。。)";
			break;
		case 2: 
			return "(已生成开行计划)";
			break;
		default: 
			return '';
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
		self.runPlans.push(new RunPlanRow({"day": curDay, "runFlag": null, "createFlag": null, planCrossId: data.planCrossId, baseChartId: data.baseChartId})); 
		while(curDay != endTimeStr){
			currentTime.setDate(currentTime.getDate() + 1); 
			curDay = self.yyyyMMdd(currentTime);
			self.runPlans.push(new RunPlanRow({"day": curDay, "runFlag": null, "createFlag": null, planCrossId: data.planCrossId, baseChartId: data.baseChartId})); 
		} 
	}  
}

function RunPlanRow(data){
	var self = this;  
	self.day = data.day;
	self.runFlag = ko.observable("");  
	self.createFlag = ko.observable("");  
	self.planCrossId = data.planCrossId;
	self.planTrainId = ko.observable("");  
	self.baseTrainId =  ko.observable("");  
	self.selected = ko.observable(0);
	self.color = ko.computed(function(){
		if(self.createFlag() == 1){
			return "green";
		}else{
			return"gray";
		} 
	});
	self.runFlagShowValue = ko.computed(function(){ 
		switch (self.runFlag()) {
		case 0: 
			return "停";
			break;
		case 1: 
			return "开";
			break;
		case 2: 
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
function openLogin() {
	$("#file_upload_dlg").dialog("open");
}
 
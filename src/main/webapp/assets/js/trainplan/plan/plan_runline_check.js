$(function() { 
	
	var appModel = new ApplicationModel();
	ko.applyBindings(appModel); 
	 
	appModel.init();   
});  

function SelectCheckModle(){
	var self = this;
	var selectRows = [];
	
	self.crossAllcheckBox = ko.observable(0);
	
	self.setCurrentCross = function(cross){
		self.currentCross(cross);
		if(self.searchModle().showCrossMap() == 1){
			$("#cross_map_dlg").find("iframe").attr("src", "cross/provideCrossChartData?crossId=" + cross.crossId);
		}
	};
	
	self.selectCross = function(row){
//		self.crossAllcheckBox();
		console.log(row.selected());
		if(row.selected() == 0){
			self.crossAllcheckBox(1);
			$.each(self.crossRows.rows(), function(i, crossRow){ 
				console.log("==="+ crossRow.selected());
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
	
	var self = this;
	//列车列表
	self.trains = ko.observableArray();
	
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
		 $.ajax({
				url : "plan/getSchemeList",
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
			url : "plan/getFullStationInfo",
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
		
		
	};  
	
	self.loadTrains = function(){
		self.loadTrainsForPage();
	};
	self.loadTrainsForPage = function(startIndex, endIndex) {  
		var trainNbr = self.searchModle().trainNbr();  
		var startBureauCode = self.searchModle().startBureau();  
		
		var chart = self.searchModle().chart(); 
		  
		$.ajax({
				url : "jbtcx/queryTrains",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					startBureau : startBureauCode, 
					trainNbr : trainNbr,
					chartId : chart.chartId
				}),
				success : function(result) {    
					if (result != null && result != "undefind" && result.code == "0") { 
							if (result.data !=null) {  
								console.log(result.data)
								$.each(result.data,function(n,constructionObj){  
									var result = $.parseJSON(constructionObj);
									if(result.code == "-1"){
										showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
										commonJsScreenUnLock();
										return;
									}
									var temp = result.data[0];
									
									$.each(result.data, function(n, temp){
										var train = new TrainRow(temp) ;
										
										var skarr = []; 
										if(temp.scheduleDto.sourceItemDto != null){
											skarr.push(temp.scheduleDto.sourceItemDto); 
										}   
										
										if(temp.scheduleDto.routeItemDtos != null && temp.scheduleDto.routeItemDtos.length > 0){
											$.each(temp.scheduleDto.routeItemDtos,function(i, a){ 
												skarr.push(a);
											});
										} 
										if(temp.scheduleDto.targetItemDto != null){
											skarr.push(temp.scheduleDto.targetItemDto);
										} 
										
										skarr.sort(function(a, b){  
											return a.index - b.index;
										}); 
										
										train.loadTimes(skarr);
										
										self.trains.push(train); 
										
									});
									
									if(temp == null){ 
										showErrorDialog("没有查到该列车，请核对后重新输入查询");  
										commonJsScreenUnLock();
										return;
									}
									
								});
							}
						 
						 
					} else {
						showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
					};
				},
				error : function() {
					showErrorDialog("接口调用失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
	}; 
	 
	self.showTrainTimes = function(row) {
		console.log(row.times());
		self.trainLines.remove(function(item){
			return true;
		});
		$.each(row.times(), function(i, n){
			self.trainLines.push(n);
		}); 
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
	
	self.loadBureau = function(bureaus){   
		for ( var i = 0; i < bureaus.length; i++) {   
			console.log(bureaus[i]);
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
	console.log(data);
	var self = this;
	self.id = data.id;
	self.index = data.index + 1;
	self.stnName = filterValue(data.name);
	self.bureauShortName = filterValue(data.bureauShortName);
	self.sourceTime = data.sourceTimeDto2 != null ? data.sourceTimeDto2.split(':').slice(1, 3).join(":") : '--';
	self.targetTime = data.targetTimeDto2 != null ? data.targetTimeDto2.split(':').slice(1, 3).join(":") : '--';
	self.stepStr = GetDateDiff(data); 
	self.trackName = filterValue(data.trackName);  
	 
}; 
function GetDateDiff(data)
{ 
	if(data.sourceTimeDto2 == null || data.targetTimeDto2 == null){
		return '';
	}
	var startTime = new Date();
	var endTime = new Date();
	
	startTime.setDate(data.sourceTimeDto2.split(":")[0]);
	startTime.setHours(data.sourceTimeDto2.split(":")[1], data.sourceTimeDto2.split(":")[2], data.sourceTimeDto2.split(":")[3], 0);
	
	endTime.setDate(data.targetTimeDto2.split(":")[0]);
	endTime.setHours(data.targetTimeDto2.split(":")[1], data.targetTimeDto2.split(":")[2], data.targetTimeDto2.split(":")[3], 0);
	 
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
	 
	return result == "" ? "不停靠" : result; 
};
function TrainRow(data) {   
	var self = this;  
	console.log(data)
	self.id = data.id;
	self.name = data.name; 
	self.times = ko.observableArray();  
	self.selected  = ko.observable();  
	self.startBureau = ''; 
	self.startStn = ''; 
	self.sourceTime = '';
	self.endStn = '';
	self.targetTime = '';
	
	self.loadTimes = function(times){
		$.each(times, function(i, n){
			if(i == 0){
				self.startBureau = n.bureauName;
				self.startStn = n.name;
				self.sourceTime =  n.sourceTimeDto2== null ? '--': n.sourceTimeDto2.split(':').slice(1, 3).join(":");
			}else if(i == times.length - 1){
				self.endStn = n.name;
				self.targetTime = n.sourceTimeDto2== null ? '--': n.sourceTimeDto2.split(':').slice(1, 3).join(":");
			}
			self.times.push(new TrainTimeRow(n));
		});
	};
} ; 

$(function() {
	$("#file_upload_dlg").dialog("close"); 
	$("#cross_train_time_dlg").dialog("close"); 
	
	var cross = new CrossModel();
	ko.applyBindings(cross); 
	 
	cross.init();   
});

var highlingFlags = [{"value": 0, "text": "普线"},{"value": 1, "text": "高线"},{"value": 2, "text": "混合"}];
var sureFlags = [{"value": "0", "text": "已审核"},{"value": 1, "text": "未审核"}];
var highlingrules = [{"value": 1, "text": "平日"},{"value": 2, "text": "周末"},{"value": 3, "text": "高峰"}];
var commonlinerules = [{"value": 1, "text": "每日"},{"value": 2, "text": "隔日"}];
 


var gloabBureaus = [];

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
	self.filterCrosses = function(){  
		 var filterTrainNbr = self.searchModle().filterTrainNbr();
		 filterTrainNbr = filterTrainNbr || filterTrainNbr != "" ? filterTrainNbr.toUpperCase() : "all";
		 if(filterTrainNbr == "all"){
			 $.each(self.crossRows(),function(n, crossRow){
				  crossRow.visiableRow(true);
			  });
		 }else{
			 $.each(self.crossRows(),function(n, crossRow){
				 if(crossRow.crossName().indexOf(filterTrainNbr) > -1){
					 crossRow.visiableRow(true);
				 }else{
					 crossRow.visiableRow(false);
				 } 
			  }); 
		 };
	};
	
	self.createUnitPlain = function(){ 
		var crossIds = "";
		for(var i = 0; i < self.crossRows().length; i++){ 
			if(self.crossRows()[i].selected() == 1){ 
				crossIds += (crossIds == "" ? "" : ",");
				crossIds += self.crossRows()[i].crossId;
			}  
		}
		 $.ajax({
				url : "cross/updateUnitCrossId",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					crossIds : crossIds
				}),
				success : function(result) {    
					if(result.code == 0){
						showSuccessDialog("更新成功");
					}else{
						showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
					}
				}
			}); 
	};
	
	
	self.uploadCrossFile = function(){ 
	        //starting setting some animation when the ajax starts and completes
	        $("#loading")
	        .ajaxStart(function(){
	            $(this).show();
	        })
	        .ajaxComplete(function(){
	            $(this).hide();
	        });  
	        $.ajaxFileUpload
	        ({
                url:'cross/fileUpload?chartId=' + self.searchModle().chart().chartId + "&startDay="+ self.searchModle().startDay(),
                secureuri:false,
                fileElementId:'fileToUpload',
                type : "POST",
                dataType: 'json', 
                success: function (data, status)
                { 
                     alert(112312);
                },
                error: function (data, status, e)
                {
                    alert(e);
                }
            }); 
	        return true;
	} ;
	
	 
	//当前选中的交路对象
	self.currentCross = ko.observable(new CrossRow({"unitCrossId":"1",
		"unitCrossName":"", 
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
	
	self.init = function(){  
		//self.gloabBureaus = [{"shortName": "上", "code": "S"}, {"shortName": "京", "code": "B"}, {"shortName": "广", "code": "G"}];
		//self.searchModle().loadBureau(self.gloabBureaus); 
		//self.searchModle().loadChats([{"name":"方案1", "chartId": "1234"},{"name":"方案2", "chartId": "1235"}])
		 
		 $.ajax({
				url : "../plan/getSchemeList",
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
	self.loadCrosses = function(action) {  
		/* $.each(crosses,function(n, crossInfo){
			var row = new CrossRow(crossInfo);
			self.crossRows.push(row);
			rowLookup[row.crossName] = row;
		});  */
		var bureauCode = self.searchModle().bureau(); 
		var highlingFlag = self.searchModle().highlingFlag(); 
		var sureFlag = self.searchModle().sureFlag(); 
		var startBureauCode = self.searchModle().startBureau(); 
		var chart = self.searchModle().chart();
		
		var currentIndex = 0; 
		 // 如果是重新查询
		 if(action == 1){
			 currentIndex = self.crossRows().length;
		 }
		
		 $.ajax({
				url : "../cross/getUnitCrossInfo",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({ 
					tokenVehBureau : bureauCode, 
					highlineFlag : highlingFlag == null ? null : highlingFlag.value,  
					sureFlag : sureFlag == "-1" ? null : sureFlag,  
					startBureau : startBureauCode,
					rownumstart : currentIndex,
					chartId : chart == null ? null : chart.chartId,
					rownumend : currentIndex + self.pageSize
				}),
				success : function(result) {    
					if(action == 0){ 
						$.each(self.crossRows(),function(n, crossRow){
							crossRow.visiableRow(false);
						});
					}else{ 
						self.crossRows.remove(function(item) {
							return true;
						});  
					} 
					if (result != null && result != "undefind" && result.code == "0") {
						if (result.data !=null && result.data.length > 0) {   
							if(result.data[0] != null){  
								if(result.data[0] != null){  
									$.each(result.data,function(n, crossInfo){
										self.crossRows.push(new CrossRow(crossInfo)); 
										
									}); 
								} 
							}
							 $("#cross_table_crossInfo").freezeHeader(); 
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

	self.saveCrossInfo = function() { 
		alert(self.currentCross().tokenVehBureau())
	};
	
	self.showUploadDlg = function(){
		$("#file_upload_dlg").dialog("open");
	}; 
	
	self.showTrains = function(row) {   
		self.trains.remove(function(item) {
			return true;
		});   
		 $.ajax({
				url : "../cross/getUnitCrossTrainInfo",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					unitCrossId : row.crossId
				}),
				success : function(result) {    
					if (result != null && result != "undefind" && result.code == "0") {
						if (result.data !=null && result.data.length > 0) {   
							
							self.currentCross(new CrossRow(result.data[0].unitCrossInfo));
							if(result.data[0].unitCrossTrainInfo != null){
								$.each(result.data[0].unitCrossTrainInfo,function(n, crossInfo){
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
		
	};   
}

function searchModle(){
	self = this;  
	self.bureaus = ko.observableArray();  
	self.startBureaus = ko.observableArray();
	
	self.charts = ko.observableArray();
	 
	self.highlingFlags = highlingFlags;
	self.sureFlags = sureFlags;
	
	self.highlingFlag = ko.observable();
	
	self.sureFlag = ko.observable();
	
	self.bureau = ko.observable();
	 
	self.chart =  ko.observable();
	
	self.startDay = ko.observable();
	
	self.startBureau = ko.observable();
	
	self.filterTrainNbr = ko.observable();
	
	self.shortNameFlag = ko.observable(1);
	
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
	
	self.visiableRow =  ko.observable(true);  
	
	console.log(data.unitCrossId);
	
	self.crossId = data.unitCrossId; 
	
	self.shortNameFlag =  ko.observable(true); 
	 
	self.crossName = ko.observable(data.marshallingName);  
	
	self.shortName = ko.computed(function(){
		trainNbrs = data.marshallingName != null ? data.marshallingName.split('-') : [];
		if(trainNbrs.length > 3){
			return trainNbrs[0] + '...' + trainNbrs[trainNbrs.length-1];
		}else{
			return data.marshallingName;
		}
	}); 
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
	
	self.groupSertalNbr = ko.observable(data.groupSertalNbr);
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
	self.crossTainId  = data.unitCrossTainId;//BASE_CROSS_TRAIN_ID
	self.crossId = data.crossId;//BASE_CROSS_ID
	self.trainSort = data.trainSort;//TRAIN_SORT
	self.baseTrainId = data.baseTrainId;
	self.trainNbr = data.trainNbr;//TRAIN_NBR
	self.startStn = data.startStn;//START_STN
	//self.startBureau = data.startBureau;//START_BUREAU 
	self.startBureau = ko.computed(function(){
		for(var i = 0; i < gloabBureaus.length; i++){
			if(gloabBureaus[i].code == data.startBureau){ 
				return gloabBureaus[i].shortName;
			}
		} 
	});
	self.endStn = data.endStn;//END_STN
	//self.endBureau = data.endBureau;//END_BUREAU
	self.endBureau = ko.computed(function(){
		for(var i = 0; i < gloabBureaus.length; i++){
			if(gloabBureaus[i].code == data.endBureau){ 
				return gloabBureaus[i].shortName;
			}
		} 
	});
	self.dayGap = data.dayGap;//DAY_GAP
	self.alertNateTrainNbr = data.alertNateTrainNbr;//ALTERNATE_TRAIN_NBR
	self.alertNateTime = data.alertNateTime;//ALTERNATE_TIME
	//self.spareFlag = data.spareFlag;//SPARE_FLAG
	self.spareFlag = ko.computed(function(){ 
		switch (data.spareFlag) {
			case 0:
				return "停运";
				break;
			case 1:
				return "开行";
				break;
			case 2:
				return "备用";
				break;
			default:
				break;
		}
	});
	self.spareApplyFlage =  ko.computed(function(){ 
		return self.spareApplyFlage == 1 ? "是" : "否";
	});
	//SPARE_APPLY_FLAG
	//self.highlineFlag = data.highlineFlag ;//HIGHLINE_FLAG 
	self.highlineFlag = ko.computed(function(){  
			switch (data.highlineFlag) {
				case 0:
					return "普线";
					break;
				case 1:
					return "高线";
					break;
				case 2:
					return "混合";
					break;
				default:
					break;
			}
	});
//	var highlingFlags = [{"value": -1, "text": ""},{"value": 0, "text": "普线"},{"value": 1, "text": "高线"},{"value": 2, "text": "混合"}];
//	var sureFlags = [{"value": -1, "text": ""},{"value": "0", "text": "已审核"},{"value": 1, "text": "未审核"}];
//	var highlingrules = [{"value": 1, "text": "平日"},{"value": 2, "text": "周末"},{"value": 3, "text": "高峰"}];
//	var commonlinerules = [{"value": 1, "text": "每日"},{"value": 2, "text": "隔日"}];
	self.highlineRule = ko.computed(function(){  
			switch (data.highlineRule) {
				case 1: 
					return "平日";
					break;
				case 2:
					return "周末";
					break;
				case 3:
					return "高峰";
					break;
				default:
					break;
		   }
	});
	self.commonLineRule = ko.computed(function(){ 
		switch (data.commonLineRule) {
			case 1:
				return "每日";
				break;
			case 2:
				return "隔日";
				break; 
			default:
				break;
		}
	});
	self.appointWeek = data.appointWeek;//APPOINT_WEEK
	self.appointDay = data.appointDay;//APPOINT_DAY 
	
	self.otherRule = self.appointWeek == null ? "" : self.appointWeek + " " + self.appointDay == null ? "" : self.appointDay;

} ; 

 
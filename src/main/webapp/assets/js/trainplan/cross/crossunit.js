$(function() { 
	var cross = new CrossModel();
	ko.applyBindings(cross); 
	 
	cross.init();   
});

var highlingFlags = [{"value": 0, "text": "普线"},{"value": 1, "text": "高线"},{"value": 2, "text": "混合"}];
var checkFlags = [{"value": '1', "text": "已"},{"value": "0", "text": "未"}];
var unitCreateFlags = [{"value": "1", "text": "已"},{"value": 0, "text": "未"}];
var highlingrules = [{"value": 1, "text": "平日"},{"value": 2, "text": "周末"},{"value": 3, "text": "高峰"}];
var commonlinerules = [{"value": 1, "text": "每日"},{"value": 2, "text": "隔日"}];
 
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
	//交路列表 
	self.crossAllcheckBox = ko.observable(0);
	
	self.gloabBureaus = [];  
	
	self.currentTrain =  ko.observable();
	
	self.currentTrainInfoMessage = ko.observable("");
	
	self.times = ko.observableArray();
	
	//车辆担当局
	self.searchModle = ko.observable(new searchModle()); 
	self.defaultCrossTemp = {"crossId":"",
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
		"appointPeriod":"",
		"locoType":"",
		"crhType":"",
		"elecSupply":"",
		"dejCollect":"",
		"airCondition":"",
		"note":"", 
		"createPeople":"", 
		"createPeopleOrg":"",  
		"createTime":""};
	self.clearData = function(){
		 self.crossRows.clear(); 
		 self.currentCross(new CrossRow(self.defaultCrossTemp)); 
		 self.times.remove(function(item){
			return true;
		 });
		 
		 self.trains.remove(function(item){
			return true;
		 });  
		 self.currentTrain = ko.observable();
	};
	
	self.setCurrentTrain = function(row){
		self.currentTrain(row);
		self.currentTrainInfoMessage("车次：" + row.trainNbr + "&nbsp;&nbsp;&nbsp;" + row.startStn + "——" + row.endStn);
		self.times.remove(function(item){
			return true;
		});
		if(row.times().length > 0){ 
			$.each(row.times(), function(i, n){
				self.times.push(n); 
			}) ;
			 
		}else{
			$.ajax({
				url : "../jbtcx/queryTrainTimes",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({   
					trainId : row.baseTrainId
				}),
				success : function(result) {  
					if (result != null && result != "undefind" && result.code == "0") {  
						row.loadTimes(result.data);  
						$.each(row.times(), function(i, n){
							self.times.push(n); 
						});
					}  
				},
				error : function() {
				 
				},
				complete : function(){
					 
				}
			}); 
		}
	};
	
	self.setCurrentCross = function(cross){
		if(hasActiveRole(cross.tokenVehBureau()) && self.searchModle().activeCurrentCrossFlag() == 0){
			self.searchModle().activeCurrentCrossFlag(1);  
		}else if(!hasActiveRole(cross.tokenVehBureau()) && self.searchModle().activeCurrentCrossFlag() == 1){
			self.searchModle().activeCurrentCrossFlag(0); 
		} 
		self.currentCross(cross);
		if(!$('#cross_map_dlg').is(":hidden")){
			$("#cross_map_dlg").find("iframe").attr("src", "../cross/provideUnitCrossChartData?unitCrossId=" + cross.unitCrossId);
			$("#cross_map_dlg").dialog("setTitle", "交路单元图     交路名:" + self.currentCross().crossName());
		}
	};
	
	self.trainNbrChange = function(n,  event){
		self.searchModle().filterTrainNbr(event.target.value.toUpperCase());
	};
	
	self.selectCrosses = function(){
//		self.crossAllcheckBox(); 
		$.each(self.crossRows.rows(), function(i, crossRow){ 
			if(self.crossAllcheckBox() == 1){
				crossRow.selected(0);
				self.searchModle().activeFlag(0);
			}else{
				if(hasActiveRole(crossRow.tokenVehBureau())){ 
					crossRow.selected(1); 
					self.searchModle().activeFlag(1);
				}
			}  
		});  
	};
	
	self.selectCross = function(row){ 
//		self.crossAllcheckBox();  
		if(row.selected() == 0){  
			self.crossAllcheckBox(1);  
			self.searchModle().activeFlag(1);
			$.each(self.crossRows.rows(), function(i, crossRow){  
				if(crossRow.selected() != 1 && crossRow != row && crossRow.activeFlag() == 1){
					self.crossAllcheckBox(0);
					return false;
				}  
			}); 
		}else{
			self.searchModle().activeFlag(0);  
			self.crossAllcheckBox(0);
			$.each(self.crossRows.rows(), function(i, crossRow){  
				if(crossRow.selected() == 1 && crossRow != row && crossRow.activeFlag() == 1){
					self.searchModle().activeFlag(1);
					return false;
				}  
			}); 
		} 
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
	 
	//当前选中的交路对象
	self.currentCross = ko.observable(new CrossRow(self.defaultCrossTemp)); 
	
	self.showTrainTimes = function(row) {
		self.currentTrain(row);
		self.runPlanCanvasPage.reDrawByTrainNbr(row.trainNbr);
		self.stns.remove(function(item){
			return true;
		});
		if(row.times().length > 0){ 
			$.each(row.times(), function(i, n){
				self.stns.push(n); 
			}) ;
			 
		}else{
			$.ajax({
				url : "../jbtcx/queryTrainTimes",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({   
					trainId : row.baseTrainId
				}),
				success : function(result) {  
					if (result != null && result != "undefind" && result.code == "0") {  
						row.loadTimes(result.data);  
						$.each(row.times(), function(i, n){
							self.stns.push(n); 
						});
					} else {
						showErrorDialog("获取列车详点失败");
					};
				},
				error : function() {
					showErrorDialog("获取列车详点失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
		}
		
	};  
	 
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
	
	$("#cross_map_dlg").dialog({
	    onClose:function(){
	    		self.searchModle().showCrossMap(0);
	       }
	 });
	
	
	self.init = function(){  
		//self.gloabBureaus = [{"shortName": "上", "code": "S"}, {"shortName": "京", "code": "B"}, {"shortName": "广", "code": "G"}];
		//self.searchModle().loadBureau(self.gloabBureaus); 
		//self.searchModle().loadChats([{"name":"方案1", "chartId": "1234"},{"name":"方案2", "chartId": "1235"}])
		$("#cross_map_dlg").dialog({
		    onClose:function(){
		    		self.searchModle().showCrossMap(0);
		       }
		   });
		
		
		$("#file_upload_dlg").dialog("close"); 
		$("#cross_train_time_dlg").dialog("close");
		$("#cross_map_dlg").dialog("close"); 
		$("#cross_train_dlg").dialog("close");
		$("#cross_train_time_dlg").dialog("close"); 
		$("#cross_start_day").datepicker();
		  
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
						showErrorDialog("获取方案列表失败");
					} 
				},
				error : function() {
					showErrorDialog("获取方案列表失败");
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
		
		
	};  
	
	self.loadCrosses = function(){
		self.crossRows.loadRows();
	};
	self.loadCrosseForPage = function(startIndex, endIndex) {   
		self.crossAllcheckBox(0);
		commonJsScreenLock();
		/* $.each(crosses,function(n, crossInfo){
			var row = new CrossRow(crossInfo);
			self.crossRows.push(row);
			rowLookup[row.crossName] = row;
		});  */
		
		
		var bureauCode = self.searchModle().bureau(); 
		var highlingFlag = self.searchModle().highlingFlag();
		var trainNbr = self.searchModle().filterTrainNbr(); 
		var checkFlag = self.searchModle().checkFlag();
		var unitCreateFlag = self.searchModle().unitCreateFlag();
		var startBureauCode = self.searchModle().startBureau();  
		var currentBureanFlag = self.searchModle().currentBureanFlag() ? '1' : '0'; 
		
		var chart = self.searchModle().chart(); 
		
		if(hasActiveRole(bureauCode) && self.searchModle().activeFlag() == 0){
			self.searchModle().activeFlag(1);  
		}else if(!hasActiveRole(bureauCode) && self.searchModle().activeFlag() == 1){
			self.searchModle().activeFlag(0); 
		} 
		
		if(chart == null){
			showErrorDialog("请选择方案!");
			commonJsScreenUnLock();
			return;
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
					checkFlag : checkFlag == null ? null : checkFlag.value,
					startBureau : startBureauCode,
					unitCreateFlag :  unitCreateFlag == null ? null : unitCreateFlag.value,
							chartId : chart == null ? null: chart.chartId,
					trainNbr : trainNbr,
					rownumstart : startIndex, 
					rownumend : endIndex,
					currentBureanFlag : currentBureanFlag
				}),
				success : function(result) {    
 
					if (result != null && result != "undefind" && result.code == "0") {
						var rows = [];
						if(result.data.data != null){  
							$.each(result.data.data,function(n, crossInfo){
								rows.push(new CrossRow(crossInfo));  
							});   
							self.crossRows.loadPageRows(result.data.totalRecord, rows);
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

	self.crossRows = new PageModle(200, self.loadCrosseForPage);
	
	self.saveCrossInfo = function() { 
		alert(self.currentCross().tokenVehBureau())
	};
	 
	self.showUploadDlg = function(){
		$("#file_upload_dlg").dialog("open");
//		var diag = new Dialog();
//		diag.Title = "上传对数文件";
//		diag.Width = 400;
//		diag.Height = 200;
//		diag.InnerHtml = $("#file_upload_dlg").html();
//		//diag.URL = "javascript:void(document.write(\'这是弹出窗口中的内容\'))";
//		diag.show();
	};
	
	self.showCrossMapDlg = function(n, e){ 
		if(self.currentCross().unitCrossId == '' || self.currentCross().unitCrossId == undefined || self.currentCross().unitCrossId == "undefined"){
			showWarningDialog("请先选择交路单元");  
			return;
		}
		var unitCrossId = self.currentCross().unitCrossId; 
		if($('#cross_map_dlg').is(":hidden")){
			$("#cross_map_dlg").find("iframe").attr("src", "../cross/provideUnitCrossChartData?unitCrossId=" + unitCrossId);
			$("#cross_map_dlg").dialog({title: "交路单元图     交路名:" + self.currentCross().crossName(),draggable: true, resizable:true});
		};
	};  
	
	self.showCrossTrainDlg = function(){
		$("#cross_train_dlg").dialog("open");
	};
	
	self.showCrossTrainTimeDlg = function(){
		if(self.currentTrain() == null){
			showWarningDialog("请先选择列车");
			return;
		}
		$("#cross_train_time_dlg").dialog({draggable: true, resizable:true});
	};
	
	self.deleteCrosses = function(){
		var crossIds = "";
		var crosses = self.crossRows.rows(); 
		var delCrosses = [];
		for(var i = 0; i < crosses.length; i++){ 
			if(crosses[i].selected() == 1){ 
				crossIds += (crossIds == "" ? "" : ",");
				crossIds += crosses[i].unitCrossId; 
				delCrosses.push(crosses[i]);
			}; 
		} 
		if(crossIds == ""){
			showErrorDialog("没有可删除的记录");
			return;
		}
		showConfirmDiv("提示", "你确定要执行删除操作?", function (r) { 
	        if (r) { 
				$.ajax({
					url : "../cross/deleteUnitCorssInfo",
					cache : false,
					type : "POST",
					dataType : "json",
					contentType : "application/json",
					data :JSON.stringify({  
						unitCrossIds : crossIds
					}),
					success : function(result) {     
						if(result.code == 0){ 
							self.crossRows.addLoadRows(delCrosses);  
							showSuccessDialog("删除交路单元成功"); 
						}else{
							showErrorDialog("删除交路单元失败");
						}
					}
				}); 
	        }
		});
	};
	
	self.createUnitCrossInfo = function(){ 
//		var crossIds = "";
//		var crosses = self.crossRows.rows();
//		for(var i = 0; i < crosses.length; i++){ 
//			if(crosses[i].selected() == 1){ 
//				crossIds += (crossIds == "" ? "" : ",");
//				crossIds += crosses[i].crossId;
//			}
//		} 
		var crossIds = "";
		var delCrosses = [];
		var crosses = self.crossRows.rows();
		for(var i = 0; i < crosses.length; i++){
			if(crosses[i].checkFlag() == 0 && crosses[i].selected() == 1){
				showErrorDialog("你选择了未审核的记录，请先审核");
				return;
			}else if(crosses[i].unitCreateFlag() == 1 && crosses[i].selected() == 1){
				showErrorDialog("不能重复生成");
				return;
			}else if(crosses[i].checkFlag() == 1 && crosses[i].selected() == 1){
				crossIds += (crossIds == "" ? "" : ",");
				crossIds += crosses[i].unitCrossId;
				delCrosses.push( crosses[i]);
			};
		} 
		if(crossIds == ""){
			showErrorDialog("没有选中数据"); 
			return;
		} 
		commonJsScreenLock();
		 $.ajax({
				url : "../cross/updateUnitCrossId",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					unitCrossIds : crossIds 
				}),
				success : function(result) {     
					if(result.code == 0){ 
						for(var j = 0; j < result.data.length; j++){
							for(var i = 0; i < delCrosses.length; i++){
								if(result.data[j].unitCrossId == delCrosses[i].unitCrossId){
									delCrosses[i].unitCreateFlag(result.data[j].flag);
								 }
							}
						} 
						if(result.data.length == 0){
							showErrorDialog("更新失败");
							return;
						}else if(result.data.length < delCrosses.length)  
						    showSuccessDialog("部分更新成功");  
					   }else{
						   showSuccessDialog("更新成功");  
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
	
	self.bureauChange = function(){
		if(hasActiveRole(self.searchModle().bureau())){
			self.searchModle().activeFlag(1); 
			self.clearData();
		}else if(self.searchModle().activeFlag() == 1){
			self.searchModle().activeFlag(0);
			self.clearData();
		}
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
					unitCrossId : row.unitCrossId  
				}),
				success : function(result) {    
					if (result != null && result != "undefind" && result.code == "0") {
						if (result.data !=null && result.data.length > 0) {  
							self.setCurrentCross(new CrossRow(result.data[0].crossinfo));
//							self.currentCross(new CrossRow(result.data[0].crossInfo));
							
							if(result.data[0].unitCrossTrainInfo != null){
								for(var i = 0; i < result.data[0].unitCrossTrainInfo.length; i++){
									var row = new TrainRow(result.data[0].unitCrossTrainInfo[i]); 
									self.trains.push(row); 
								} 
							}
						}
						 
					} else {
						showErrorDialog("获取列车信息失败");
					} 
				},
				error : function() {
					showErrorDialog("获取列车信息失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
		
	};  
	
	self.checkCrossInfo = function(){
		
		var crossIds = "";
		var updateCrosses = [];
		var crosses = self.crossRows.rows();
		for(var i = 0; i < crosses.length; i++){ 
			if(crosses[i].checkFlag() == 1 && crosses[i].selected() == 1){  
				showErrorDialog("不能重复审核"); 
				return;
			}else if(crosses[i].checkFlag() == 0 && crosses[i].selected() == 1){
				crossIds += (crossIds == "" ? "" : ",");
				crossIds += crosses[i].unitCrossId;
				updateCrosses.push(crosses[i]); 
			} 
		}  
		if(crossIds == ""){
			showErrorDialog("没有可审核的");
			return;
		}
		commonJsScreenLock(); 
		 $.ajax({
				url : "../cross/checkUnitCorssInfo",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					unitCrossIds : crossIds
				}),
				success : function(result) {     
					if(result.code == 0){
						$.each(updateCrosses, function(i, n){ 
							n.checkFlag("1");
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
	
	self.activeCurrentCrossFlag = ko.observable(0);
	
	self.bureaus = ko.observableArray();  
	self.startBureaus = ko.observableArray();
	
	self.charts = ko.observableArray();
	 
	self.highlingFlags = highlingFlags;
	
	self.unitCreateFlags = unitCreateFlags; 
	
	self.filterCheckFlag = ko.observable(0);
	
	self.filterUnitCreateFlag = ko.observable(0);
	
	self.currentBureanFlag = ko.observable(0);
		
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
	self.data = data;
	self.visiableRow =  ko.observable(true); 
	
	self.selected =  ko.observable(0); 
	
	self.unitCrossId = data.unitCrossId;
	
	self.shortNameFlag =  ko.observable(true);
	
	self.crossName = ko.observable(data.crossName);  
	 
	self.shortName = ko.computed(function(){
		if(data.crossName ==  null){
			return "";
		}
		trainNbrs = data.crossName.split('-');
		if(trainNbrs.length > 2){
			return trainNbrs[0] + '-......-' + trainNbrs[trainNbrs.length-1];
		}else{
			return data.crossName;
		}
	});  
 
	self.checkFlag = ko.observable(data.checkFlag);
	
	self.unitCreateFlag = ko.observable(data.unitCreateFlag == null ? '0' : data.unitCreateFlag);
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
						 }
					 }
				 } 
			 }
			 return result; 
	});
	
	self.activeFlag = ko.computed(function(){
		return hasActiveRole(data.tokenVehBureau);
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
					 }
				 }
			 } 
		 }
		 return result; 
	});
	self.relevantBureauShowValue =  ko.computed(function(){ 
		var result = "";
		 if(self.data.relevantBureau != null && self.data.relevantBureau != "null"){  
			 for(var j = 0; j < self.data.relevantBureau.length; j++){
				 for(var i = 0; i < gloabBureaus.length; i++){
					 if(self.data.relevantBureau.substring(j, j + 1) == gloabBureaus[i].code){
						 result += result == "" ? gloabBureaus[i].shortName : "、" + gloabBureaus[i].shortName;
						 break;
					 }
				 }
			 } 
		 } 
		 return  result == "" ? "" : "相关局：&nbsp;" + result; 
	});
	self.relevantBureau = ko.observable(data.relevantBureau);
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
	self.baseTrainId = data.baseTrainId;
	self.runDate = data.runDate;
	self.endDate = data.endDate;
	self.trainNbr = data.trainNbr;//TRAIN_NBR
	self.startStn = data.startStn;//START_STN
	self.groupSerialNbr = data.groupSerialNbr;//GROUP_SERIAL_NBR
	self.marshallingName = data.marshallingName;
	
	self.times = ko.observableArray();  
	self.loadTimes = function(times){
		$.each(times, function(i, n){ 
			self.times.push(new TrainTimeRow(n));
		});
	}; 
	
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
	self.alertNateTime =  ko.computed(function(){
		 return data.alertNateTime == null ? "": data.alertNateTime.substring(5).replace(/-/g, "").substring(0, data.alertNateTime.substring(5).replace(/-/g, "").length - 3);
	});//ALTERNATE_TIME
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
		return data.spareApplyFlage == 1 ? "是" : "";
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


function filterValue(value){
	return value == null || value == "null" ? "--" : value;
}

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

function openLogin() {
	$("#file_upload_dlg").dialog("open");
}

$(window.document).scroll(function () {
	    var scrolltop = $(document).scrollTop(); 
});
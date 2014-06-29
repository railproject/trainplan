var HightLineCrewSjPage = function () {
	/**
	 * 页面加载时执行
	 * public
	 */
	this.initPage = function () {
		$("#crew_input_rundate").datepicker();
		
		//页面元素绑定
		var pageModel = new PageModel();
		ko.applyBindings(pageModel);
		
		pageModel.initPageData();
	};
	
	
	
	
	
	/**
	 * 查询model
	 * 
	 * private
	 */
	function SearchModle(){
		
		var _self = this;
		
		/**
		 * 查询条件
		 */
		_self.runDate =  ko.observable();		//日期
		_self.trainNbr = ko.observable();		//车次号
		

		
		_self.initData = function() {
			_self.runDate(currdate());
		};
		
		//currentIndex 
		function currdate(){
			var d = new Date();
			var year = d.getFullYear();    //获取完整的年份(4位,1970-????)
			var month = d.getMonth()+1;       //获取当前月份(0-11,0代表1月)
			var days = d.getDate(); 
			month = ("" + month).length == 1 ? "0" + month : month;
			days = ("" + days).length == 1 ? "0" + days : days;
			return year+"-"+month+"-"+days;
		};
		
	};
	
	
	
	/**
	 * 乘务计划model用于新增、修改
	 */
	function HighlineCrewModel() {
		var _self = this;
		var crewHighlineId = ko.observable();
		var crewDate = ko.observable();
		var crewBureau = ko.observable();
		var crewType = ko.observable();
		var crewCross = ko.observable();
		var crewGroup = ko.observable();
		var throughLine = ko.observable();
		var name1 = ko.observable();
		var tel1 = ko.observable();
		var identity1 = ko.observable();
		var name2 = ko.observable();
		var tel2 = ko.observable();
		var identity2 = ko.observable();
		var note = ko.observable();
		var recordPeople = ko.observable();
		var recordPeopleOrg = ko.observable();
		var recordTime = ko.observable();
		var submitType = ko.observable();
		
		_self.update = function (obj) {
			if (obj == null) {
				crewHighlineId = "";
				crewDate = "";
				crewBureau = "";
				crewType = "";
				crewCross = "";
				crewGroup = "";
				throughLine = "";
				name1 = "";
				tel1 = "";
				identity1 = "";
				name2 = "";
				tel2 = "";
				identity2 = "";
				note = "";
				recordPeople = "";
				recordPeopleOrg = "";
				recordTime = "";
				submitType = "";
			} else {
				crewHighlineId = obj.crewHighlineId;
				crewDate = obj.crewDate;
				crewBureau = obj.crewBureau;
				crewType = obj.crewType;
				crewCross = obj.crewCross;
				crewGroup = obj.crewGroup;
				throughLine = obj.throughLine;
				name1 = obj.name1;
				tel1 = obj.tel1;
				identity1 = obj.identity1;
				name2 = obj.name2;
				tel2 = obj.tel2;
				identity2 = obj.identity2;
				note = obj.note;
				recordPeople = obj.recordPeople;
				recordPeopleOrg = obj.recordPeopleOrg;
				recordTime = obj.recordTime;
				submitType = obj.submitType;
			}
		};
	};
	
	
	
	/**
	 * 页面元素绑定对象
	 * 
	 * private
	 */
	function PageModel() {
		var _self = this;
		_self.searchModle = ko.observable(new SearchModle());		//页面查询对象
		_self.planTrainRows = new PageModle(200, loadPlanDataForPage);		//页面开行计划列表对象
		_self.hightLineCrewSjRows = new PageModle(200, loadHightLineCrewSjDataForPage);		//页面司机乘务计划列表对象
		_self.hightLineCrewModel = ko.observable(new HighlineCrewModel());	//用于乘务计划新增、修改
		
		
		/**
		 * 初始化查询条件
		 */
		_self.initPageData = function() {
			_self.searchModle().initData();
		};
		

		/**
		 * 查询按钮事件
		 */
		_self.queryList = function(){
			commonJsScreenLock(2);
			//1.查询开行计划
			_self.planTrainRows.loadRows();	//loadRows为分页组件中方法

			//2.查询司机乘务计划信息
			_self.hightLineCrewSjRows.loadRows();	//loadRows为分页组件中方法
		};
		
		
		/**
		 * 新增页面打开时
		 */
		_self.onAddOpen = function() {
			_self.hightLineCrewModel.update(null);
		};
		
		
		

		/**
		 * 新增页面打开时
		 */
		_self.onEditOpen = function() {
			_self.hightLineCrewModel.update(null);
		};
		
		

		/**
		 * 发布按钮事件
		 */
		_self.sendCrew = function(){
			commonJsScreenLock();
			$.ajax({
				url : basePath+"/crew/highline/send",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					crewType : "2",//乘务类型（1车长、2司机、3机械师）
					crewDate : _self.searchModle().runDate()
				}),
				success : function(result) {

					console.log("================result=================");
					console.dir(result);
					if (result != null && typeof result == "object" && result.code == "0") {
						 
					} else {
						showErrorDialog("提交司机乘务计划信息失败");
					};
				},
				error : function() {
					showErrorDialog("提交司机乘务计划信息失败");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
			});
			
		};
		
		

		/**
		 * 新增乘务计划
		 */
		_self.addHightLineCrew = function(){
			commonJsScreenLock();
			$.ajax({
				url : basePath+"/crew/highline/add",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					crewType : "2",//乘务类型（1车长、2司机、3机械师）
					crewDate : _self.searchModle().runDate(),
					crewCross : $("#add_crewCross").val(),
					crewCross : $("#add_crewCross").val(),
					crewGroup : $("#add_crewGroup").val(),
					throughLine : $("#add_throughLine").val(),
					name1 : $("#add_name1").val(),
					tel1 : $("#add_tel1").val(),
					identity1 : $("#add_identity1").val(),
					name2 : $("#add_name2").val(),
					tel2 : $("#add_tel2").val(),
					identity2 : $("#add_identity2").val(),
					note : $("#add_note").val()
				}),
				success : function(result) {

					console.log("================result=================");
					console.dir(result);
					if (result != null && typeof result == "object" && result.code == "0") {
						 
					} else {
						showErrorDialog("保存司机乘务计划信息失败");
					};
				},
				error : function() {
					showErrorDialog("保存司机乘务计划信息失败");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
			});
		};
		
		

		/**
		 * 修改乘务计划
		 */
		_self.updateHightLineCrew = function(){
			commonJsScreenLock();
			$.ajax({
				url : basePath+"/crew/highline/update",
				cache : false,
				type : "PUT",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					crewHighlineId : $("#update_crewHighlineId").val(),
					crewType : "2",//乘务类型（1车长、2司机、3机械师）
					crewDate : _self.searchModle().runDate(),
					crewCross : $("#add_crewCross").val(),
					crewCross : $("#add_crewCross").val(),
					crewGroup : $("#add_crewGroup").val(),
					throughLine : $("#add_throughLine").val(),
					name1 : $("#add_name1").val(),
					tel1 : $("#add_tel1").val(),
					identity1 : $("#add_identity1").val(),
					name2 : $("#add_name2").val(),
					tel2 : $("#add_tel2").val(),
					identity2 : $("#add_identity2").val(),
					note : $("#add_note").val()
				}),
				success : function(result) {

					console.log("================result=================");
					console.dir(result);
					if (result != null && typeof result == "object" && result.code == "0") {
						 
					} else {
						showErrorDialog("保存司机乘务计划信息失败");
					};
				},
				error : function() {
					showErrorDialog("保存司机乘务计划信息失败");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
			});
		};
		
		
		
		
		/**
		 * 分页查询开行计划列表
		 */
		function loadPlanDataForPage() {
			var _runDate = _self.searchModle().runDate();	//运行日期
			var _trainNbr = _self.searchModle().trainNbr()!="undefined"?_self.searchModle().trainNbr():"";//车次
			
			console.log("_runDate="+_runDate+"	_trainNbr="+_trainNbr);
			
			if(_runDate == null || typeof _runDate!="string"){
				showErrorDialog("请选择日期!");
				commonJsScreenUnLock();
				return;
			}
			
			$.ajax({
				url : basePath+"/crew/highline/getRunLineListForRunDate",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					runDate : _runDate,
					trainNbr : _trainNbr
				}),
				success : function(result) {
 
					if (result != null && typeof result == "object" && result.code == "0") {
						var rows = [];
						if(result.data.data != null){
							$.each(result.data.data,function(n, obj){
								rows.push(obj);
							});
							_self.planTrainRows.loadPageRows(result.data.totalRecord, rows);
						}
						
					} else {
						showErrorDialog("获取开行计划信息失败");
					};
				},
				error : function() {
					showErrorDialog("获取开行计划信息失败");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
			});
		};
		
		
		
		/**
		 * 分页查询司机乘务计划列表
		 */
		function loadHightLineCrewSjDataForPage() {
			
			var _runDate = _self.searchModle().runDate();	//运行日期
			var _trainNbr = _self.searchModle().trainNbr();	//车次
			
			console.log("_runDate="+_runDate+"	_trainNbr="+_trainNbr);
			
			if(_runDate == null || typeof _runDate!="string"){
				showErrorDialog("请选择日期!");
				commonJsScreenUnLock();
				return;
			}
			
			$.ajax({
				url : basePath+"/crew/highline/getHighlineCrewListForRunDate",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					crewType : "2",	//乘务类型（1车长、2司机、3机械师）
					runDate : _runDate,
					trainNbr : _trainNbr
				}),
				success : function(result) {
 
					if (result != null && typeof result == "object" && result.code == "0") {
						var rows = [];
						if(result.data.data != null){
							$.each(result.data.data,function(n, obj){
								rows.push(obj);
							});
							_self.hightLineCrewSjRows.loadPageRows(result.data.totalRecord, rows);
						}
						 
					} else {
						showErrorDialog("获取司机乘务计划信息失败");
					};
				},
				error : function() {
					showErrorDialog("获取司机乘务计划信息失败");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
			});
		};
		
		
		
	};
	
	
	
	
	
	
	
};




$(function() {
	new HightLineCrewSjPage().initPage();
});
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
		_self.crewHighlineId = ko.observable();
		_self.crewDate = ko.observable();
		_self.crewBureau = ko.observable();
		_self.crewType = ko.observable();
		_self.crewCross = ko.observable();
		_self.crewGroup = ko.observable();
		_self.throughLine = ko.observable();
		_self.name1 = ko.observable();
		_self.tel1 = ko.observable();
		_self.identity1 = ko.observable();
		_self.name2 = ko.observable();
		_self.tel2 = ko.observable();
		_self.identity2 = ko.observable();
		_self.note = ko.observable();
		_self.recordPeople = ko.observable();
		_self.recordPeopleOrg = ko.observable();
		_self.recordTime = ko.observable();
		_self.submitType = ko.observable();
		
		_self.update = function (obj) {
			if (obj == null) {
				_self.crewHighlineId("");
				_self.crewDate("");
				_self.crewBureau("");
				_self.crewType("");
				_self.crewCross("");
				_self.crewGroup("");
				_self.throughLine("");
				_self.name1("");
				_self.tel1("");
				_self.identity1("");
				_self.name2("");
				_self.tel2("");
				_self.identity2("");
				_self.note("");
				_self.recordPeople("");
				_self.recordPeopleOrg("");
				_self.recordTime("");
				_self.submitType("");
			} else {
				_self.crewHighlineId(obj.crewHighlineId);
				_self.crewDate(obj.crewDate);
				_self.crewBureau(obj.crewBureau);
				_self.crewType(obj.crewType);
				_self.crewCross(obj.crewCross);
				_self.crewGroup(obj.crewGroup);
				_self.throughLine(obj.throughLine);
				_self.name1(obj.name1);
				_self.tel1(obj.tel1);
				_self.identity1(obj.identity1);
				_self.name2(obj.name2);
				_self.tel2(obj.tel2);
				_self.identity2(obj.identity2);
				_self.note(obj.note);
				_self.recordPeople(obj.recordPeople);
				_self.recordPeopleOrg(obj.recordPeopleOrg);
				_self.recordTime(obj.recordTime);
				_self.submitType(obj.submitType);
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
		_self.hightLineCrewSjRows = new PageModle(200, loadHightLineCrewSjDataForPage);		//页面机械师乘务计划列表对象
		_self.hightLineCrewModel = ko.observable(new HighlineCrewModel());	//用于乘务计划新增、修改
		_self.hightLineCrewModelTitle = ko.observable();	//用于乘务计划新增、修改窗口标题
		_self.hightLineCrewSaveFlag = ko.observable();		//用于乘务计划新增、修改标识
		
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

			//2.查询机械师乘务计划信息
			_self.hightLineCrewSjRows.loadRows();	//loadRows为分页组件中方法
		};
		
		
		/**
		 * 新增页面打开时
		 */
		_self.onAddOpen = function() {
			_self.hightLineCrewSaveFlag("add");
			_self.hightLineCrewModelTitle("新增机械师乘务计划");
			_self.hightLineCrewModel().update(null);
		};
		
		
		

		/**
		 * 修改页面打开时
		 */
		_self.onEditOpen = function() {
			_self.hightLineCrewSaveFlag("update");
			_self.hightLineCrewModelTitle("修改机械师乘务计划");
			var currentCrewHighlineId = "";
			$("[name='crew_checkbox']").each(function(){
				if($(this).is(":checked")) {
					currentCrewHighlineId = $(this).val();
					return false; //跳出循环
				}
		    });
			
			if (currentCrewHighlineId == "") {
				showWarningDialog("请选择一条乘务计划记录");
				return;
			}
			
			
			commonJsScreenLock();
			$.ajax({
				url : basePath+"/crew/highline/getHighLineCrew",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					crewHighLineId : currentCrewHighlineId
				}),
				success : function(result) {
					if (result != null && typeof result == "object" && result.code == "0") {
						_self.hightLineCrewModel().update(result.data);
					} else {
						showErrorDialog("获取机械师乘务计划信息失败");
					};
				},
				error : function() {
					showErrorDialog("获取机械师乘务计划信息失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			});
			
			
		};
		
		
		
		
		
		
		
		
		
		
		
		/**
		 * 删除
		 */
		_self.deleteHightLineCrew = function() {
			var currentCrewHighlineId = "";
			$("[name='crew_checkbox']").each(function(){
				if($(this).is(":checked")) {
					currentCrewHighlineId += "'"+$(this).val()+"',";
					//return false; //跳出循环
				}
		    });
			
			if (currentCrewHighlineId == "") {
				showWarningDialog("请选择一条乘务计划记录");
				return;
			} else {
				currentCrewHighlineId = currentCrewHighlineId.substring(0, currentCrewHighlineId.lastIndexOf(","));
			}

			
			commonJsScreenLock(2);
			$.ajax({
				url : basePath+"/crew/highline/deleteHighLineCrewInfo",
				cache : false,
				type : "DELETE",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					crewHighLineId : currentCrewHighlineId
				}),
				success : function(result) {
					if (result != null && typeof result == "object" && result.code == "0") {
						//2.查询机械师乘务计划信息
						_self.hightLineCrewSjRows.loadRows();	//loadRows为分页组件中方法
						showSuccessDialog("成功删除机械师乘务计划信息");
					} else {
						commonJsScreenUnLock(1);
						showErrorDialog("删除机械师乘务计划信息失败");
					};
				},
				error : function() {
					commonJsScreenUnLock(1);
					showErrorDialog("删除机械师乘务计划信息失败");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
			});
			
			
		};
		
		
		

		/**
		 * 发布按钮事件
		 */
		_self.sendCrew = function(){
			commonJsScreenLock(2);
			$.ajax({
				url : basePath+"/crew/highline/updateSubmitType",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					crewType : "3",//乘务类型（1车长、2司机、3机械师）
					crewDate : $("#crew_input_rundate").val()//_self.searchModle().runDate()
				}),
				success : function(result) {
					if (result != null && typeof result == "object" && result.code == "0") {
						//2.查询机械师乘务计划信息
						_self.hightLineCrewSjRows.loadRows();	//loadRows为分页组件中方法
						showSuccessDialog("提交成功");
					} else {
						commonJsScreenUnLock(1);
						showErrorDialog("提交机械师乘务计划信息失败");
					};
				},
				error : function() {
					commonJsScreenUnLock(1);
					showErrorDialog("提交机械师乘务计划信息失败");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
			});
			
		};
		
		

		/**
		 * 新增/修改乘务计划
		 */
		_self.saveHightLineCrew = function(){
			commonJsScreenLock(2);
			var _url = "";
			var _type = "";
			if (_self.hightLineCrewSaveFlag() == "add") {
				_url = basePath+"/crew/highline/add";
				_type = "POST";
			} else if (_self.hightLineCrewSaveFlag() == "update") {
				_url = basePath+"/crew/highline/update";
				_type : "PUT";
			}
			
			$.ajax({
				url : _url,
				cache : false,
				type : _type,
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					crewHighlineId : _self.hightLineCrewModel().crewHighlineId(),
					crewType : "3",//乘务类型（1车长、2司机、3机械师）
					crewDate : $("#crew_input_rundate").val(),//_self.searchModle().runDate(),
					crewCross : _self.hightLineCrewModel().crewCross(),
					crewGroup : _self.hightLineCrewModel().crewGroup(),
					throughLine : _self.hightLineCrewModel().throughLine(),
					name1 : _self.hightLineCrewModel().name1(),
					tel1 : _self.hightLineCrewModel().tel1(),
					identity1 : _self.hightLineCrewModel().identity1(),
					name2 : _self.hightLineCrewModel().name2(),
					tel2 : _self.hightLineCrewModel().tel2(),
					identity2 : _self.hightLineCrewModel().identity2(),
					note : _self.hightLineCrewModel().note()
				}),
				success : function(result) {
					if (result != null && typeof result == "object" && result.code == "0") {
						//2.查询机械师乘务计划信息
						_self.hightLineCrewSjRows.loadRows();	//loadRows为分页组件中方法
						showSuccessDialog("保存成功");
					} else {
						commonJsScreenUnLock(1);
						showErrorDialog("保存机械师乘务计划信息失败");
					};
				},
				error : function() {
					commonJsScreenUnLock(1);
					showErrorDialog("保存机械师乘务计划信息失败");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
			});
		};
		
		
		
		
		
		
		/**
		 * 分页查询开行计划列表
		 */
		function loadPlanDataForPage(startIndex, endIndex) {
			var _runDate = $("#crew_input_rundate").val();//_self.searchModle().runDate();	//运行日期
			var _trainNbr =_self.searchModle().trainNbr()!="undefined"?_self.searchModle().trainNbr():"";//车次
			
			
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
					trainNbr : _trainNbr,
					rownumstart : startIndex, 
					rownumend : endIndex
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
		 * 分页查询机械师乘务计划列表
		 */
		function loadHightLineCrewSjDataForPage(startIndex, endIndex) {
			
			var _runDate = $("#crew_input_rundate").val();//_self.searchModle().runDate();	//运行日期
			var _trainNbr = _self.searchModle().trainNbr();	//车次
			
			
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
					crewType : "3",	//乘务类型（1车长、2司机、3机械师）
					crewDate : _runDate,
					trainNbr : _trainNbr,
					rownumstart : startIndex, 
					rownumend : endIndex
				}),
				success : function(result) {
 
					if (result != null && typeof result == "object" && result.code == "0") {
						var rows = [];
						if(result.data.data != null){
							$.each(result.data.data,function(n, obj){
								if (obj.submitType != null && obj.submitType==0) {
									obj.submitTypeStr = "<span class='label label-danger'>草稿</span>";
								} else if (obj.submitType != null && obj.submitType==1) {
									obj.submitTypeStr = "<span class='label label-success'>已提交</span>";
								} else {
									obj.submitTypeStr = "";
								}
								rows.push(obj);
							});
							_self.hightLineCrewSjRows.loadPageRows(result.data.totalRecord, rows);
						}
						 
					} else {
						showErrorDialog("获取机械师乘务计划信息失败");
					};
				},
				error : function() {
					showErrorDialog("获取机械师乘务计划信息失败");
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
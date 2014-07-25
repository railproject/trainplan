/**
 * 临客命令js
 * 
 */

var runPlanLkCmdPageModel = null;//界面绑定元素对象
var RunPlanLkCmdPage = function () {
	/**
	 * 页面加载时执行
	 * public
	 */
	this.initPage = function () {

		$("#jbt_traininfo_dialog").dialog("close");
		
		$("#runPlanLk_cmd_input_startDate").datepicker();
		$("#runPlanLk_cmd_input_endDate").datepicker();
		
//		//锁定命令列表table表头
//		$("#runPlanLkCMD_table").chromatable({
//            width: "100%", // specify 100%, auto, or a fixed pixel amount
//            height: "620px",
//            scrolling: "yes" // must have the jquery-1.3.2.min.js script installed to use
//        });
//		//锁定时刻表列表table表头
//		$("#runPlanLkCMD_trainStn_table").chromatable({
//            width: "100%", // specify 100%, auto, or a fixed pixel amount
//            height: "660px",
//            scrolling: "yes" // must have the jquery-1.3.2.min.js script installed to use
//        });
		
		
		//页面元素绑定
		runPlanLkCmdPageModel = new PageModel();
		ko.applyBindings(runPlanLkCmdPageModel);
		
		runPlanLkCmdPageModel.initPageData();
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
		_self.bureauArray = ko.observableArray();	//路局简称list
		_self.cmdNbrBureau = ko.observable("");		//路局命令号
		_self.cmdNbrSuperior = ko.observable("");		//总公司命令号
		_self.trainNbr = ko.observable("");		//车次号
		_self.cmdTypeOption = ko.observable();		//命令类型下拉框选项 (既有加开；既有停运；高铁加开；高铁停运)
		_self.selectStateOption = ko.observable();		//选线状态	下拉框选项 
		_self.createStateOption = ko.observable();		//生成开行计划状态	下拉框选项 
		
		

		/**
		 * 初始化查询条件值
		 */
		_self.initData = function() {
			commonJsScreenLock(1);
			$("#runPlanLk_cmd_input_startDate").val(currdate());
			$("#runPlanLk_cmd_input_endDate").val(currdate());

			
			_self.loadBureauData();//加载路局下拉框数据
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
		
		
		/**
		 * 加载路局下拉框数据
		 */
		_self.loadBureauData = function() {
			$.ajax({
				url : basePath+"/plan/getFullStationInfo",
				cache : false,
				type : "GET",
				dataType : "json",
				contentType : "application/json",
				success : function(result) {
					if (result != null && result != "undefind" && result.code == "0") {
						if (result.data !=null) {
							for ( var i = 0; i < result.data.length; i++) {
								_self.bureauArray.push(result.data[i].ljjc);
							}
							renderInputTjj();//渲染途经局自动补全输入框
						}
						
					} else {
						showErrorDialog("获取路局列表失败");
					} 
				},
				error : function() {
					showErrorDialog("获取路局列表失败");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
		    });
		};
		
		
		/**
		 * 渲染途经局自动补全输入框
		 */
		function renderInputTjj() {
			$('#runPlanLk_cmd_input_tjj')
	        .textext({
	            plugins : 'tags autocomplete'
	        })
	        .bind('getSuggestions', function(e, data)
	        {
	                textext = $(e.target).textext()[0],
	                query = (data ? data.query : '') || ''
	                ;

	            $(this).trigger(
	                'setSuggestions',
	                { result : textext.itemManager().filter(_self.bureauArray(), query) }
	            );
	        });
		};
		
		
		
		
		
	};
	
	
	
	
	
	
	/**
	 * 页面元素绑定对象
	 * 
	 * private
	 */
	function PageModel() {
		var _self = this;
		/**
		 * 时刻表列表行对象
		 * @param data
		 */
		_self.cmdTrainStnTimeRow = function(data) {
			var _self = this;
			_self.childIndex = ko.observable(data.childIndex);
			_self.stnName = ko.observable(data.stnName);
			_self.arrTrainNbr = ko.observable(data.arrTrainNbr);
			_self.dptTrainNbr = ko.observable(data.dptTrainNbr);
			_self.stnBureau = ko.observable(data.stnBureau);
			_self.arrTime = ko.observable(data.arrTime);
			_self.dptTime = ko.observable(data.dptTime);
			_self.trackNbr = ko.observable(data.trackNbr);
			_self.platform = ko.observable(data.platform);
			
		};
		
		
		_self.searchModle = ko.observable(new SearchModle());		//页面查询对象
		_self.runPlanLkCMDRows = ko.observableArray();		//页面命令列表
		_self.runPlanLkCMDTrainStnRows = ko.observableArray();		//页面命令stn列表

		_self.isSelectAll = ko.observable(false);	//本局担当命令列表是否全选 	全选标识  默认false
		_self.currentCmdTxtMl = ko.observable();//命令列表选中行记录
		_self.currentCmdTrainStn = ko.observable(new _self.cmdTrainStnTimeRow({
			childIndex:-1,
			stnName:"",
			arrTrainNbr:"",
			dptTrainNbr:"",
			stnBureau:"",
			arrTime:"",
			dptTime:"",
			trackNbr:"",
			platform:"",
		}));//列车时刻表选中行记录  用于上下移动
		
		
		
		
		
		
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
			//清除历史值
			_self.currentCmdTxtMl({"cmdTxtMlId":""});
			_self.currentCmdTrainStn(new _self.cmdTrainStnTimeRow({
					childIndex:-1,
					stnName:"",
					arrTrainNbr:"",
					dptTrainNbr:"",
					stnBureau:"",
					arrTime:"",
					dptTime:"",
					trackNbr:"",
					platform:"",
			}));
			_self.runPlanLkCMDTrainStnRows.remove(function(item) {
				return true;
			});
			
			commonJsScreenLock();

			//2.查询命令信息
			loadDataForPage();	//loadRows方法
		};
		
		
		
		/**
		 * 查询临客命令列表
		 */
		function loadDataForPage() {
			$.ajax({
				url : basePath+"/runPlanLk/getCmdTrainInfo",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
//					cmdBureau : _self.searchModle().cmdBureau().value,	//发令局 (发令局、担当局局码)
					startDate : moment($("#runPlanLk_cmd_input_startDate").val()).format("YYYY-MM-DD"),//起始日期
					endDate : moment($("#runPlanLk_cmd_input_endDate").val()).format("YYYY-MM-DD"),//终止日期
					cmdNbrBureau : _self.searchModle().cmdNbrBureau(),//路局命令号
					cmdNbrSuperior : _self.searchModle().cmdNbrSuperior(),//总公司命令号
					trainNbr : _self.searchModle().trainNbr(),//车次
					cmdType : _self.searchModle().cmdTypeOption(),//命令类型 (既有加开；既有停运；高铁加开；高铁停运)
					selectState : _self.searchModle().selectStateOption(),//选线状态
					createState : _self.searchModle().createStateOption()//生成开行计划状态
				}),
				success : function(result) {
 
					if (result != null && typeof result == "object" && result.code == "0") {
						if(result.data != null){
							$.each(result.data,function(n, obj){
								
								//选线状态0：未选择 1：已选择
								if (obj.selectState == "1") {
									obj.selectState = "已";
								} else if (obj.selectState == "0") {
									obj.selectState = "未";
								} else {
									obj.selectState = "";
								}

								//生成状态0：未生成 1：已生成
								if (obj.createState == "1") {
									obj.createState = "已";
								} else if (obj.createState == "0") {
									obj.createState = "未";
								} else {
									obj.createState = "";
								}
								
								//增加isSelect属性  便于全选复选框事件	默认0=false
								obj.isSelect = 0;
								
								_self.runPlanLkCMDRows.push(obj);
							});
						}
						 
					} else {
						showErrorDialog("获取临客命令信息失败");
					};
				},
				error : function() {
					showErrorDialog("获取临客命令信息失败");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
			});
		};
		
		
		
		
		
		/**
		 * 命令列表全选复选框change事件
		 */
		_self.checkBoxSelectAllChange = function() {
			console.log("~~~~~~~~ 全选标识  _self.isSelectAll="+_self.isSelectAll());
			if (!_self.isSelectAll()) {//全选     将runPlanLkCMDRows中isSelect属性设置为1
				console.log("全选");
				$.each(_self.runPlanLkCMDRows(), function(i, row){
					row.isSelect = 1;
				});
			} else {//全不选    将runPlanLkCMDRows中isSelect属性设置为0
				console.log("全不选");
				$.each(_self.runPlanLkCMDRows(), function(i, row){
					row.isSelect = 0;
				});
			}
		};
		
		

		/**
		 * 命令列表行点击事件
		 */
		_self.setCurrentRec = function(row) {
			console.log("~~~~~~~~~~~~~~~ 命令列表行点击事件  ~~~~~~~~~~~~~~~~~~~");
			console.dir(row);
			_self.currentCmdTxtMl(row);//.cmdTxtMlId);
			
			//查询cmdTrainStn信息
			loadCmdTrainStnInfo(row.cmdTrainId);
		};
		

		/**
		 * 时刻列表行点击事件
		 */
		_self.setCMDTrainStnCurrentRec = function(row) {
			console.log("~~~~~~~~~~~~~~~ 时刻列表行点击事件  ~~~~~~~~~~~~~~~~~~~");
			console.dir(row.childIndex());
			_self.currentCmdTrainStn(row);
		};
		
		
		
		
		/**
		 * 查询cmdTrainStn信息
		 */
		function loadCmdTrainStnInfo(_cmdTrainId) {
			alert(_cmdTrainId);
			if(_cmdTrainId==null || _cmdTrainId=="undefind") {
				return;
			}
			commonJsScreenLock();
			
			$.ajax({
				url : basePath+"/runPlanLk/getCmdTrainStnInfo",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					cmdTrainId : _cmdTrainId
				}),
				success : function(result) {
 
					if (result != null && typeof result == "object" && result.code == "0") {
						if(result.data != null){
							$.each(result.data,function(n, obj){
								obj.childIndex = n;	//增加元素在集合中的序号属性，便于调整顺序
								_self.runPlanLkCMDTrainStnRows.push(new _self.cmdTrainStnTimeRow(obj));
							});
						}
						 
					} else {
						showErrorDialog("获取列车时刻信息失败");
					};
				},
				error : function() {
					showErrorDialog("获取列车时刻信息失败");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
			});
		};
		
		
		
		
		
		
		
		
		/**
		 * 选线按钮点击事件	打开基本图查询界面
		 * 
		 * tabType=bjdd (本局担当)
		 * @param 
		 */
		_self.loadTrainInfoFromJbt = function(currentTrain){
			if(_self.currentCmdTxtMl()==null || _self.currentCmdTxtMl().cmdTxtMlId=="") {
				showWarningDialog("请选择临客命令记录");
				return;
			}
			
//			if($('#run_plan_train_times_edit_dialog').is(":hidden")){
				$("#jbt_traininfo_dialog").find("iframe").attr("src", basePath+"/runPlanLk/jbtTrainInfoPage?tabType=bjdd");
				$('#jbt_traininfo_dialog').dialog({title: "选择车次", autoOpen: true, modal: false, draggable: true, resizable:true,
					onResize:function() {
						var iframeBox = $("#jbt_traininfo_dialog").find("iframe");
						var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
						var WH = $('#jbt_traininfo_dialog').height();
						var WW = $('#jbt_traininfo_dialog').width();
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
//			}
		};
		
		

		
		/**
		 * 保存按钮点击事件
		 */
		_self.saveCmdTxtMl = function() {
			if(_self.currentCmdTxtMl()==null || _self.currentCmdTxtMl().cmdTxtMlId=="") {
				showWarningDialog("请选择临客命令记录");
				return;
			}
			if(_self.runPlanLkCMDTrainStnRows().length==0) {
				showWarningDialog("请补充列车时刻数据");
				return;
			}
			commonJsScreenLock();
			
			$.ajax({
				url : basePath+"/runPlanLk/saveLkTrainTimes",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					cmdTrainMap : _self.currentCmdTxtMl(),
					cmdTrainStnList : _self.runPlanLkCMDTrainStnRows()
				}),
				success : function(result) {
					
					if (result != null && typeof result == "object" && result.code == "0") {
						showSuccessDialog("保存成功");
					} else {
						showErrorDialog("保存失败");
					};
				},
				error : function() {
					showErrorDialog("保存失败");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
			});
		};
		
		
		/**
		 * 上移
		 */
		_self.up = function() {
			if(_self.currentCmdTrainStn()==null || _self.currentCmdTrainStn()=="undefind") {
				showWarningDialog("请选择时刻表中需要调整顺序的记录");
				return;
			}
			
			if(_self.currentCmdTrainStn().childIndex() == 0) {
				showWarningDialog("当前记录顺序号已经为最小，不能执行上移操作");
				return;
			}
			
//			var _maxLen = _self.runPlanLkCMDTrainStnRows().length -1;
//			if(_self.currentCmdTrainStn().childIndex() == _maxLen) {
//				showWarningDialog("当前记录顺序号已经为最大，不能执行下移操作");
//				return;
//			}
			
			//设置序号
			var _arrayList = _self.runPlanLkCMDTrainStnRows();
			for(var i=0;i<_arrayList.length;i++) {
				if(i == (_self.currentCmdTrainStn().childIndex())) {
					
					_arrayList[i].childIndex(i-1);
					_arrayList[i-1].childIndex(i);
					_self.runPlanLkCMDTrainStnRows.splice(i - 1, 2, _arrayList[i], _arrayList[i-1]);
					break;
				}
			}
		};
		
		
		
		/**
		 * 下移
		 */
		_self.down = function() {
			if(_self.currentCmdTrainStn()==null || _self.currentCmdTrainStn()=="undefind") {
				showWarningDialog("请选择时刻表中需要调整顺序的记录");
				return;
			}

			var _maxLen = _self.runPlanLkCMDTrainStnRows().length -1;
			if(_self.currentCmdTrainStn().childIndex() == _maxLen) {
				showWarningDialog("当前记录顺序号已经为最大，不能执行下移操作");
				return;
			}
			
			//设置序号
			var _arrayList = _self.runPlanLkCMDTrainStnRows();
			for(var i=0;i<_arrayList.length;i++) {
				if(i == (_self.currentCmdTrainStn().childIndex())) {
					
					_arrayList[i].childIndex(i+1);
					_arrayList[i+1].childIndex(i);
					_self.runPlanLkCMDTrainStnRows.splice(i , 2, _arrayList[i+1], _arrayList[i]);
					break;
				}
			}
		};
		
	};
	
	
	
	
	
	
	
};




$(function() {
	new RunPlanLkCmdPage().initPage();
});
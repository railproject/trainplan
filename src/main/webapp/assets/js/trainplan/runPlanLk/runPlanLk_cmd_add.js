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
		$("#runPlanLk_cmd_input_startDate_wjdd").datepicker();//外局担当
		$("#runPlanLk_cmd_input_endDate_wjdd").datepicker();//外局担当
		
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
		_self.bureauOption = ko.observable();		//路局下拉框选项
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

			$("#runPlanLk_cmd_input_startDate_wjdd").val(currdate());//外局担当
			$("#runPlanLk_cmd_input_endDate_wjdd").val(currdate());//外局担当

			
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
						console.dir(result);
						if (result.data !=null) {
							for ( var i = 0; i < result.data.length; i++) {
								_self.bureauArray.push({"value": result.data[i].ljpym, "text": result.data[i].ljjc});
							}
							
							//暂时不启用  自动补齐插件
							//renderInputTjj();//渲染途经局自动补全输入框
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
//			_self.data = data;
			_self.childIndex = ko.observable(data.childIndex);
			_self.stnName = ko.observable(data.stnName);
			_self.arrTrainNbr = ko.observable(data.arrTrainNbr);
			_self.dptTrainNbr = ko.observable(data.dptTrainNbr);
			_self.stnBureau = ko.observable(data.stnBureau);
			_self.arrTime = ko.observable(data.arrTime);
			_self.dptTime = ko.observable(data.dptTime);
			_self.trackNbr = ko.observable(data.trackNbr);
			_self.platform = ko.observable(data.platform);
			_self.arrRunDays = ko.observable(data.arrRunDays);
			_self.dptRunDays = ko.observable(data.dptRunDays);
			
		};
		
		
		
		_self.searchModle = ko.observable(new SearchModle());		//页面查询对象
		_self.runPlanLkCMDRows = ko.observableArray();				//页面命令列表
		_self.runPlanLkCMDTrainStnRows = ko.observableArray();		//页面命令时刻列表

		_self.isSelectAll = ko.observable(false);	//本局担当命令列表是否全选 	全选标识  默认false
		_self.currentCmdTxtMl = ko.observable({"cmdTxtMlItemId":"", "passBureau":""});//命令列表选中行记录
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
			arrRunDays:0,
			dptRunDays:0
		}));//列车时刻表选中行记录  用于上下移动
		

		//套用的基本图车次id
		_self.useBaseTrainId = ko.observable();
		_self.useBaseTrainIdFunc = ko.computed(function(){
			//当页面命令时刻列表 无数据时 套用列车id置空
			if(_self.runPlanLkCMDTrainStnRows.length == 0){
				_self.useBaseTrainId("");
				return "";
			}
			
		});
		
		_self.input_tjj_wjdd = ko.computed(function(){
			console.dir(_self.currentCmdTxtMl());
			console.log("&&&&&&  typeof _self.currentCmdTxtMl()="+typeof _self.currentCmdTxtMl()+"      _self.currentCmdTxtMl().passBureau=="+_self.currentCmdTxtMl().passBureau)
			if (typeof _self.currentCmdTxtMl()=="object" && _self.currentCmdTxtMl() != "undefined" && _self.currentCmdTxtMl().passBureau != "undefined") {
				return _self.currentCmdTxtMl().passBureau;	//临客命令列表当前选中值   途径局
			} else {
				return "";
			}
			
		});

		
		
		
		/**
		 * 初始化查询条件
		 */
		_self.initPageData = function() {
			_self.searchModle().initData();
		};
		
		
		/**
		 * 清除时刻列表当前选中值
		 */
		_self.cleanCurrentCmdTrainStn = function() {
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
				arrRunDays:0,
				dptRunDays:0
		}));
		};
		
		
		/**
		 * tab标签点击事件
		 * 
		 * 清空页面数据
		 */
		_self.cleanPageData = function() {
			//清除历史值
			_self.currentCmdTxtMl({"cmdTxtMlItemId":""});
			_self.cleanCurrentCmdTrainStn();//清除时刻列表当前选中值
			
			_self.runPlanLkCMDRows.remove(function(item) {
				return true;
			});
			_self.runPlanLkCMDTrainStnRows.remove(function(item) {
				return true;
			});
		};
		

		/**
		 * 本局担当tab查询按钮事件
		 */
		_self.queryListBjdd = function(){
			_self.cleanPageData();

			//2.查询本局担当命令信息
			loadDataForBjddPage();	//loadRows方法
		};
		
		
		
		/**
		 * 查询本局担当临客命令列表
		 */
		function loadDataForBjddPage() {
			commonJsScreenLock();
			$.ajax({
				url : basePath+"/runPlanLk/getCmdTrainInfo",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
//					cmdBureau : _self.searchModle().bureauOption().value,//发令局 (发令局、担当局局码)
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
									obj.selectStateStr = "已";
								} else if (obj.selectState == "0") {
									obj.selectStateStr = "未";
								} else {
									obj.selectStateStr = "";
								}

								//生成状态0：未生成 1：已生成
								if (obj.createState == "1") {
									obj.createStateStr = "已";
								} else if (obj.createState == "0") {
									obj.createStateStr = "未";
								} else {
									obj.createStateStr = "";
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
		 * 外局担当tab查询按钮事件
		 */
		_self.queryListWjdd = function(){
			_self.cleanPageData();

			//2.查询外局担当命令信息
			loadDataForWjddPage();	//loadRows方法
		};
		
		
		
		/**
		 * 查询外局担当临客命令列表
		 */
		function loadDataForWjddPage() {

			_self.currentCmdTxtMl().passBureau = "2222";
			console.dir(_self.currentCmdTxtMl().passBureau);
			
			commonJsScreenLock();
			
			console.log("~~~~~~~~~  "+_self.input_tjj_wjdd());
			
			$.ajax({
				url : basePath+"/runPlanLk/getOtherCmdTrainInfo",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					cmdBureau : _self.searchModle().bureauOption().value,//发令局 (发令局、担当局局码)
					startDate : moment($("#runPlanLk_cmd_input_startDate_wjdd").val()).format("YYYY-MM-DD"),//起始日期
					endDate : moment($("#runPlanLk_cmd_input_endDate_wjdd").val()).format("YYYY-MM-DD"),//终止日期
//					cmdNbrBureau : _self.searchModle().cmdNbrBureau(),//路局命令号
					cmdNbrSuperior : _self.searchModle().cmdNbrSuperior(),//总公司命令号
					trainNbr : _self.searchModle().trainNbr(),//车次
					cmdType : _self.searchModle().cmdTypeOption(),//命令类型 (既有加开；既有停运；高铁加开；高铁停运)
					createState : _self.searchModle().createStateOption()//生成开行计划状态
				}),
				success : function(result) {
 
					if (result != null && typeof result == "object" && result.code == "0") {
						if(result.data != null){
							$.each(result.data,function(n, obj){
								
								//选线状态0：未选择 1：已选择
								if (obj.selectState == "1") {
									obj.selectStateStr = "已";
								} else if (obj.selectState == "0") {
									obj.selectStateStr = "未";
								} else {
									obj.selectStateStr = "";
								}

								//生成状态0：未生成 1：已生成
								if (obj.createState == "1") {
									obj.createStateStr = "已";
								} else if (obj.createState == "0") {
									obj.createStateStr = "未";
								} else {
									obj.createStateStr = "";
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
//			console.log("~~~~~~~~ 全选标识  _self.isSelectAll="+_self.isSelectAll());
			if (!_self.isSelectAll()) {//全选     将runPlanLkCMDRows中isSelect属性设置为1
//				console.log("全选");
				$.each(_self.runPlanLkCMDRows(), function(i, row){
					row.isSelect = 1;
				});
			} else {//全不选    将runPlanLkCMDRows中isSelect属性设置为0
//				console.log("全不选");
				$.each(_self.runPlanLkCMDRows(), function(i, row){
					row.isSelect = 0;
				});
			}
		};
		
		

		/**
		 * 命令列表行点击事件
		 */
		_self.setCurrentRec = function(row) {
			row.isSelect = 1;
			_self.currentCmdTxtMl(row);
			
			//清除时刻表列表行选中值及时刻表列表数据
			_self.cleanCurrentCmdTrainStn();//清除时刻列表当前选中值
			
			_self.runPlanLkCMDTrainStnRows.remove(function(item) {
				return true;
			});
			
			//查询cmdTrainStn信息
			loadCmdTrainStnInfo(row.cmdTrainId);
		};
		

		/**
		 * 时刻列表行点击事件
		 */
		_self.setCMDTrainStnCurrentRec = function(row) {
			_self.currentCmdTrainStn(row);
		};
		
		
		
		
		/**
		 * 查询cmdTrainStn信息
		 */
		function loadCmdTrainStnInfo(_cmdTrainId) {
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
			if(_self.currentCmdTxtMl()==null || _self.currentCmdTxtMl().cmdTxtMlItemId=="") {
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
		 * 本局担当保存按钮点击事件
		 */
		_self.saveCmdTxtMl = function() {
			if(_self.currentCmdTxtMl()==null || _self.currentCmdTxtMl().cmdTxtMlItemId=="") {
				showWarningDialog("请选择临客命令记录");
				return;
			}
			if(_self.runPlanLkCMDTrainStnRows().length==0) {
				showWarningDialog("请补充列车时刻数据");
				return;
			}
			commonJsScreenLock();
			

			_self.currentCmdTxtMl().passBureau = $("#runPlanLk_cmd_input_tjj").val();//设置途经局
			var _saveTrainStnArray = [];
			$.each(_self.runPlanLkCMDTrainStnRows(),function(n, obj){
				_saveTrainStnArray.push(ko.toJSON(obj));
			});
			
			
			
			
			$.ajax({
				url : basePath+"/runPlanLk/saveLkTrainTimes",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					cmdTrainMap : _self.currentCmdTxtMl(),
					cmdTrainStnList : _saveTrainStnArray,
					baseTrainId : _self.useBaseTrainId()	//套用的基本图车次id
					
				}),
				success : function(result) {
					
					if (result != null && typeof result == "object" && result.code == "0") {
						showSuccessDialog("保存成功");
						//清除
						_self.runPlanLkCMDRows.remove(function(item) {
							return true;
						});
						
						loadDataForPage();
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
		 * 本局担当保存按钮点击事件
		 */
		_self.saveOtherCmdTxtMl = function() {
			if(_self.currentCmdTxtMl()==null || _self.currentCmdTxtMl().cmdTxtMlItemId=="") {
				showWarningDialog("请选择临客命令记录");
				return;
			}
			if(_self.runPlanLkCMDTrainStnRows().length==0) {
				showWarningDialog("请补充列车时刻数据");
				return;
			}
			commonJsScreenLock();
			
			var _saveTrainStnArray = [];
			$.each(_self.runPlanLkCMDTrainStnRows(),function(n, obj){
				_saveTrainStnArray.push(ko.toJSON(obj));
			});
			
			$.ajax({
				url : basePath+"/runPlanLk/saveOtherLkTrainTimes",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					cmdTrainMap : _self.currentCmdTxtMl(),
					cmdTrainStnList : _saveTrainStnArray
					
				}),
				success : function(result) {
					
					if (result != null && typeof result == "object" && result.code == "0") {
						showSuccessDialog("保存成功");
//						//清除
//						_self.runPlanLkCMDRows.remove(function(item) {
//							return true;
//						});
//						
//						loadDataForPage();
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
			if(_self.currentCmdTrainStn()==null || _self.currentCmdTrainStn()=="undefind" || _self.currentCmdTrainStn().childIndex()<0) {
				showWarningDialog("请选择时刻表中需要调整顺序的记录");
				return;
			}
			
			if(_self.currentCmdTrainStn().childIndex() == 0) {
				showWarningDialog("当前记录顺序号已经为最小，不能执行上移操作");
				return;
			}
			
			
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
			if(_self.currentCmdTrainStn()==null || _self.currentCmdTrainStn()=="undefind" || _self.currentCmdTrainStn().childIndex()<0) {
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
		
		
		

		
		/**
		 * 生成运行线按钮点击事件
		 */
		_self.batchCreateRunPlanLine = function() {
			var _saveCmdTrainArray = [];
			$.each(_self.runPlanLkCMDRows(),function(n, obj){
				if (obj.isSelect == 1) {
					_saveCmdTrainArray.push(obj.cmdTrainId);
				}
				
				if (obj.isExsitStn == "0") {
					showWarningDialog(" 发令日期【"+cmdTime+"】 局令号【"+cmdNbrBureau+"】 项号【"+cmdItem+"】的临客命令尚未设置时刻表数据，不能生成开行计划");
					return;
				}
			});
			
			
			if(_saveCmdTrainArray.length==0) {
				showWarningDialog("请选择临客命令记录");
				return;
			}
			commonJsScreenLock();
			

			_self.currentCmdTxtMl().passBureau = $("#runPlanLk_cmd_input_tjj").val();//设置途经局
			var _saveTrainStnArray = [];
			$.each(_self.runPlanLkCMDTrainStnRows(),function(n, obj){
				_saveTrainStnArray.push(ko.toJSON(obj));
			});
			
			
			
			
			$.ajax({
				url : basePath+"/runPlanLk/batchCreateRunPlanLine",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					cmdTrainIds : _saveCmdTrainArray,
					
				}),
				success : function(result) {
					
					if (result != null && typeof result == "object" && result.code == "0") {
						showSuccessDialog("生成运行线成功");
						//清除
						_self.runPlanLkCMDRows.remove(function(item) {
							return true;
						});
						
						loadDataForPage();
					} else {
						showErrorDialog("生成运行线失败");
					};
				},
				error : function() {
					showErrorDialog("生成运行线失败");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
			});
		};
		
		
		

		/**
		 * 插入行
		 * 追加到当前选中行之前
		 */
		_self.insertTrainStnRow = function() {
			var _maxRowLen = _self.runPlanLkCMDTrainStnRows().length;//追加记录前集合大小

			//1.当_maxRowLen==0时，直接push
			if (_maxRowLen == 0) {
				_self.runPlanLkCMDTrainStnRows.push(new _self.cmdTrainStnTimeRow({
					childIndex:0,
					stnName:"",
					arrTrainNbr:"",
					dptTrainNbr:"",
					stnBureau:"",
					arrTime:"",
					dptTime:"",
					trackNbr:"",
					platform:"",
					arrRunDays:0,
					dptRunDays:0
				}));
				return;
			}
			
			
			if(_self.currentCmdTrainStn()==null || _self.currentCmdTrainStn()=="undefind" 
				|| _self.currentCmdTrainStn().childIndex()<0) {
				showWarningDialog("请选择时刻表中需要插入记录的位置");
				return;
			}

			//2.1.增加行	splice(start,deleteCount,val1,val2,...)：从start位置开始删除deleteCount项，并从该位置起插入val1,val2,... 
			_self.runPlanLkCMDTrainStnRows.splice(_self.currentCmdTrainStn().childIndex(), 0, new _self.cmdTrainStnTimeRow({
				childIndex:_self.currentCmdTrainStn().childIndex(),
				stnName:"",
				arrTrainNbr:"",
				dptTrainNbr:"",
				stnBureau:"",
				arrTime:"",
				dptTime:"",
				trackNbr:"",
				platform:"",
				arrRunDays:0,
				dptRunDays:0
			}));

			//2.2.设置序号
			for(var i=0;i<_self.runPlanLkCMDTrainStnRows().length;i++) {
				if(i > (_self.currentCmdTrainStn().childIndex())) {//从新插入记录位置开始childIndex  + 1
					_self.runPlanLkCMDTrainStnRows()[i].childIndex(_self.runPlanLkCMDTrainStnRows()[i].childIndex()+1);//当前行位置后的所有记录childIndex加1
				}
			}
		};
		
		

		/**
		 * 追加行
		 * 追加到当前选中行之后
		 */
		_self.addTrainStnRow = function() {
			var _maxRowLen = _self.runPlanLkCMDTrainStnRows().length;//追加记录前集合大小

			//1.当_maxRowLen==0时，直接push
			if (_maxRowLen == 0) {
				_self.runPlanLkCMDTrainStnRows.push(new _self.cmdTrainStnTimeRow({
					childIndex:0,
					stnName:"",
					arrTrainNbr:"",
					dptTrainNbr:"",
					stnBureau:"",
					arrTime:"",
					dptTime:"",
					trackNbr:"",
					platform:"",
					arrRunDays:0,
					dptRunDays:0
				}));
				return;
			}
			
			
			if(_self.currentCmdTrainStn()==null || _self.currentCmdTrainStn()=="undefind" 
				|| _self.currentCmdTrainStn().childIndex()<0) {
				showWarningDialog("请选择时刻表中需要追加记录的位置");
				return;
			}

			//2.1.增加行	splice(start,deleteCount,val1,val2,...)：从start位置开始删除deleteCount项，并从该位置起插入val1,val2,... 
			_self.runPlanLkCMDTrainStnRows.splice(_self.currentCmdTrainStn().childIndex()+1, 0, new _self.cmdTrainStnTimeRow({
				childIndex:_self.currentCmdTrainStn().childIndex()+1,
				stnName:"",
				arrTrainNbr:"",
				dptTrainNbr:"",
				stnBureau:"",
				arrTime:"",
				dptTime:"",
				trackNbr:"",
				platform:"",
				arrRunDays:0,
				dptRunDays:0
			}));

			//2.2.设置序号
			if(_self.currentCmdTrainStn().childIndex() != _maxRowLen-1) {//当前选中行不为增加前集合中最后一行   则移除后集合需要重新排序
				for(var i=0;i<_self.runPlanLkCMDTrainStnRows().length;i++) {
					if(i > (_self.currentCmdTrainStn().childIndex()+1)) {//从新插入记录位置开始childIndex  + 1
						_self.runPlanLkCMDTrainStnRows()[i].childIndex(_self.runPlanLkCMDTrainStnRows()[i].childIndex()+1);//当前行位置后的所有记录childIndex加1
					}
				}
			}
		};
		
		

		/**
		 * 删除行
		 */
		_self.deleteTrainStnRow = function() {
			if(_self.currentCmdTrainStn()==null || _self.currentCmdTrainStn()=="undefind" || _self.currentCmdTrainStn().childIndex()<0) {
				showWarningDialog("请选择时刻表中需要移除的记录");
				return;
			}
			var _maxRowLen = _self.runPlanLkCMDTrainStnRows().length;
			
			//从集合中移除
			_self.runPlanLkCMDTrainStnRows.remove(_self.currentCmdTrainStn());
			
			if(_self.currentCmdTrainStn().childIndex() != _maxRowLen-1) {//移除行不为移除前集合中最后一行   则移除后集合需要重新排序
				//重新设置childIndex
				$.each(_self.runPlanLkCMDTrainStnRows(),function(n, obj){
					obj.childIndex(n);
				});
			}
			
			//清除当前选中项值
			_self.cleanCurrentCmdTrainStn();//清除时刻列表当前选中值
		};
		
		
		
		
		
	};
	
	
	
	
	
	
	
};




$(function() {
	new RunPlanLkCmdPage().initPage();
});
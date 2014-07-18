/**
 * 调整列车时刻表
 * @author Denglj
*/



var _trainUpdateList = [];
var TrainRunTimePage = function () {
	/**
	 * 页面加载时执行
	 * public
	 */
	this.initPage = function () {
		//页面元素绑定
		var pageModel = new PageModel();
		ko.applyBindings(pageModel);
		
		pageModel.initPageData();
	};
	
	
	/**
	 * 页面元素绑定对象
	 * 
	 * private
	 */
	function PageModel() {
		var _self = this;
		

		_self.trainUpdateList = ko.observableArray();	//列车经由站列表
		
		_self.trainStns = ko.observableArray();	//列车经由站列表
		_self.currentTrainInfoMessage = ko.observable("");

		
		
		
		/**
		 * 保存
		 */
		_self.saveTrainTime = function() {
			commonJsScreenLock();
			$.ajax({
				url : basePath+"/jbtcx/editPlanLineTrainTimes",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify(_trainUpdateList),
				success : function(result) {
					if (result != null && result != "undefind" && result.code == "0") {
			            showSuccessDialog("保存成功");
			            

						_self.resetPageStatus();	//重置页面状态
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
		
		
		
		/**
		 * 重置页面元素状态
		 */
		_self.resetPageStatus = function() {
			for (var i=0; i<_self.trainStns().length;i++) {
				var _oldObj = _self.trainStns()[i];
				for (var j=0;j<_trainUpdateList.length;j++) {

					if (_oldObj.planTrainStnId() == _trainUpdateList[j].planTrainStnId) {
						_oldObj.isChangeValue(0);	//改变该行颜色
						break;
					}
				}
			}
			
			//状态改变完成后 清空临时保存的修改对象
			_trainUpdateList = [];
		};
		
		
		/**
		 * 初始化
		 */
		_self.initPageData = function() {
			//清空待保存list
			_trainUpdateList = [];
			
			
			commonJsScreenLock();
			$.ajax({
				url : basePath+"/jbtcx/queryPlanLineTrainTimes",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					trainId: $("#trainRunTime_trainPlanId_hidden").val()
				}),
				success : function(result) {
					if (result != null && result != "undefind" && result.code == "0") {
						var message = "车次："+$("#trainRunTime_trainNbr_hidden").val()+"&nbsp;&nbsp;&nbsp;&nbsp;" + result.data[0].stnName + "&nbsp;→&nbsp;" +  result.data[result.data.length-1].stnName;
						_self.currentTrainInfoMessage(message);
					
						$.each(result.data, function(i, obj){
							_self.trainStns.push(new TrainTimeRow(obj)); 
						});
						
			            // 表头固定
						
			            $("#div_form").freezeHeader();
			            $("#table_run_plan_train_times_edit").freezeHeader();
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
		
		
		
		
	};
	
	
	/**
	 * 列表行对象
	 * @param data
	 */
	function TrainTimeRow(data) {
		var _self = this;
		_self.data = data;
		_self.planTrainStnId = ko.observable(data.planTrainStnId);
		_self.index = ko.observable(data.childIndex + 1);
		_self.stnName = ko.observable(filterValue(data.stnName));
		_self.bureauShortName = ko.observable(filterValue(data.bureauShortName));
		_self.arrTime = ko.observable(filterValue(data.arrTime));
		_self.dptTime = ko.observable(filterValue(data.dptTime));
		_self.stepStr = ko.observable(GetDateDiff(data));
		_self.trackName = ko.observable(filterValue(data.trackName));
		_self.runDays = ko.observable(data.runDays);
		_self.stationFlag = ko.observable(data.stationFlag);
		_self.isChangeValue = ko.observable(0);
		
		/**
		 * 到达时间变更
		 */
		_self.onArrTimeChange = function() {
			_self.data.arrTime = _self.arrTime();
			_self.stepStr(GetDateDiff(_self.data));
			_self.data.stepStr = _self.stepStr();
			_self.isChangeValue(1);
			
			var isAdd = true;	//是否将该行记录增加到需要保存数组中
			for (var i=0;i<_trainUpdateList.length;i++) {
				var _tempData = _trainUpdateList[i];
				if (_tempData.planTrainStnId == _self.planTrainStnId()) {
					_tempData.arrTime = _self.arrTime();
					_tempData.stepStr = _self.stepStr();
					
					isAdd = false;
					break;
				}
			}
			
			if (isAdd) {
				_trainUpdateList.push(_self.data);
			}
		};
		

		/**
		 * 出发时间变更
		 */
		_self.onDeptTimeChange = function() {
			_self.data.dptTime = _self.dptTime();
			_self.stepStr(GetDateDiff(_self.data));
			_self.data.stepStr = _self.stepStr();
			_self.isChangeValue(1);

			var isAdd = true;	//是否将该行记录增加到需要保存数组中
			for (var i=0;i<_trainUpdateList.length;i++) {
				var _tempData = _trainUpdateList[i];
				if (_tempData.planTrainStnId == _self.planTrainStnId()) {
					_tempData.dptTime = _self.dptTime();
					_tempData.stepStr = _self.stepStr();
					
					isAdd = false;
					break;
				}
			}
			
			if (isAdd) {
				_trainUpdateList.push(_self.data);
			}
		};
		

		/**
		 * 股道变更
		 */
		_self.onTrackNameChange = function() {
			_self.data.trackName = _self.trackName();
			_self.isChangeValue(1);
			

			var isAdd = true;	//是否将该行记录增加到需要保存数组中
			for (var i=0;i<_trainUpdateList.length;i++) {
				var _tempData = _trainUpdateList[i];
				if (_tempData.planTrainStnId == _self.planTrainStnId()) {
					_tempData.trackName = _self.trackName();
					
					isAdd = false;
					break;
				}
			}
			
			if (isAdd) {
				_trainUpdateList.push(_self.data);
			}
		};
		
	};

	
	/**
	 * 计算时间差
	 * @param data
	 * @returns
	 */
	function GetDateDiff(data) {
		if(data.dptTime == '-' 
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
	
	
	
	function filterValue(value){
		return value == null || value == "null" ? "--" : value;
	};
	
	
};






$(function() {
	new TrainRunTimePage().initPage();
});

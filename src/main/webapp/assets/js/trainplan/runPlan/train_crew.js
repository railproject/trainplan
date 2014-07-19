/**
 * 查看列车乘务信息
 * @author Denglj
*/



var TrainCrewPage = function () {
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
		
		var _message = "车次："+$("#trainCrew_trainNbr_hidden").val()+"&nbsp;&nbsp;&nbsp;&nbsp;"
					+"开行日期：" + $("#trainCrew_runDate_hidden").val()
					+"&nbsp;&nbsp;&nbsp;&nbsp;"  + $("#trainCrew_startStn_hidden").val() + "&nbsp;→&nbsp;" +  $("#trainCrew_endStn_hidden").val();
									
		_self.trainCrewRows = ko.observableArray();	//列车经由站列表
		_self.currentTrainInfoMessage = ko.observable(_message);

		
		
		/**
		 * 初始化
		 */
		_self.initPageData = function() {
			commonJsScreenLock();
			$.ajax({
				url : basePath+"/crew/highline/getHighlineCrewForCrewDateAndTrainNbr",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					trainNbr: $("#trainCrew_trainNbr_hidden").val(),
					runDate: moment($("#trainCrew_runDate_hidden").val()).format("YYYYMMDD")
				}),
				success : function(result) {
					if (result != null && result != "undefind" && result.code == "0") {
					
						$.each(result.data, function(i, obj){
							_self.trainCrewRows.push(obj); 
						});
						
			            // 表头固定
						
			            $("#div_form").freezeHeader();
			            $("#table_run_plan_train_crew_edit").freezeHeader();
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
	
	
};






$(function() {
	new TrainCrewPage().initPage();
});

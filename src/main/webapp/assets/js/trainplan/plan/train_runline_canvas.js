var canvas = null;//画布
var context = null;//上下文


$(function(){
	var canvas = document.getElementById("div_canvas");
	context = canvas.getContext('2d');
	
	
	
	function getCanvasData() {
		$.ajax({
			url : basePath+"/jbtcx/getTrainTimeInfoByPlanTrainId",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({
				planTrainId : $("#input_hidden_planTrainId").val()
			}),
			success : function(result) {    
				if (result != null && result != "undefind" && result.code == "0") {
					self.trainPlans.remove(function(item) {
						return true;
					});   
					$.each(result.data,function(i, n){
						self.trainPlans.push(new TrainRunPlanRow(n));
					});
					 
				} else {
					showErrorDialog("获取运行规律失败");
				} 
			},
			error : function() {
				showErrorDialog("接口调用失败");
			},
			complete : function(){
				commonJsScreenUnLock();
			}
	    });
	}
	
	
	
	var myCanvasComponent = new MyCanvasComponent(context, gridData.data.days, gridData.data.crossStns);
	
	
	//绘制客运开行计划
	//1.绘制网格
	myCanvasComponent.drawGrid("green");
	
	//2.绘制运行线
	new myCanvasComponent.drawTrainRunLine(true, "#8236ac", trainRunLineCanvasData.data).drawLine(context);

	//3.绘制运行线
//	new myCanvasComponent.drawTrainRunLine(true, "#72b5d2", jl_error_trainRunLineCanvasData.data).drawLine(context);
	
	
	
	
});





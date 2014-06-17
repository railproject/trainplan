var _MyCanvas = null;

var canvas = null;
var context = null;
var _canvas_select_groupSerialNbr = null;
var _currentGroupSerialNbr = "";		//当前组号

var currentXScale = 10;	//x轴缩放比例
var currentXScaleCount = 1;//x轴放大总倍数
var currentYScale = 1;	//y轴缩放比例
var currentYScaleCount = 1;//y轴放大总倍数
var dataIsNull = false;
var myIsDrawTrainTime = false;	//是否绘制列车经由站到达及出发时间
var pageModel = null;			//knockout绑定对象

var MyCanvas = function(){
	/**
	 * public
	 */
	this.clearChart = function() {
		//清除画布所有元素
		context.clearRect(0,0,canvas.width,canvas.height);
	};
	
	
	function initGroupSerialNbrCombox(groupSerialNbrComboxData) {
		_canvas_select_groupSerialNbr.empty();
		_canvas_select_groupSerialNbr.append("<option value='' selected='selected'>全部</option>");
		for(var j=0;j<groupSerialNbrComboxData.length;j++) {
			if (groupSerialNbrComboxData[j].value == _currentGroupSerialNbr) {
				_canvas_select_groupSerialNbr.append("<option selected='selected' value='"+groupSerialNbrComboxData[j].value+"'>"+groupSerialNbrComboxData[j].text+"</option>");
			} else {
				_canvas_select_groupSerialNbr.append("<option value='"+groupSerialNbrComboxData[j].value+"'>"+groupSerialNbrComboxData[j].text+"</option>");
			}
			
		}
		
	};
	
	
	this.drawGraph = function (contextParam, scale) {
		var _groupSerialNbrComboxData = [];
		
		var booleanDrawJlStartAndEnd = true;	//是否绘制交路起止标记
		var myCanvasComponent = new MyCanvasComponent(contextParam, canvasData.grid.days, canvasData.grid.crossStns, scale);
		//1.绘制网格
		myCanvasComponent.drawGrid("green");
		
		//2.绘制交路线
		for (var i=0, _len=canvasData.jlData.length; i<_len; i++) {
			var _obj = canvasData.jlData[i];
			var _color = getRandomColor();
			var _lenJlTrain=_obj.trains.length;
			
			//临时保存车组号
			if (_obj.groupSerialNbr!=null && _obj.groupSerialNbr!="undefine") {
				_groupSerialNbrComboxData.push({
					value:_obj.groupSerialNbr,
					text:myCanvasComponent.convertGroupSerialNbr(_obj.groupSerialNbr)+"组"
				});
			}
			
			
			if (scale && scale.currentGroupSerialNbr && scale.currentGroupSerialNbr!="undefine"
				&&scale.currentGroupSerialNbr!="" && scale.currentGroupSerialNbr!=_obj.groupSerialNbr) {
				continue;
			}
			
			//2.1 绘制交路列车运行线
			for (var j=0; j<_lenJlTrain; j++) {
				new myCanvasComponent.drawTrainRunLine(false, _color, _obj.trains[j]).drawLine(contextParam);
			}
			
			//2.2 绘制交路接续关系
			myCanvasComponent.drawJxgx(_color, _obj.jxgx);
			
			
			if (booleanDrawJlStartAndEnd){
				myCanvasComponent.drawJlStartAndEnd(_color, _obj.trains);
			}
			
			
		}
		
		
		//3.渲染组号下拉框数据
		initGroupSerialNbrCombox(_groupSerialNbrComboxData);
		
	};
	
	
	
};




$(function(){
	if(canvasData==null || canvasData.grid==null) {
		showErrorDialog("当前不存在列车数据！");
		dataIsNull = true;
		return;
	}
	_canvas_select_groupSerialNbr = $("#canvas_select_groupSerialNbr");
	canvas = document.getElementById("unit_cross_canvas");
	context = canvas.getContext('2d');

	_MyCanvas = new MyCanvas();
	_MyCanvas.drawGraph(context);
	
	
	
	$("#canvas_select_groupSerialNbr").change(function(){
		_currentGroupSerialNbr = _canvas_select_groupSerialNbr.val();
		
		_MyCanvas.drawGraph(context,{
			isDrawTrainTime:myIsDrawTrainTime,			//是否绘制列车经由站到达及出发时间
			xScale : currentXScale,						//x轴缩放比例
			xScaleCount : currentXScaleCount,			//x轴放大总倍数
			yScale : currentYScale,						//y轴缩放比例
			currentGroupSerialNbr : _currentGroupSerialNbr	//当前组号
		});
	});
	
	//显示停站时刻 复选框事件
	$("#canvas_checkbox_trainTime").click(function(){
		if (dataIsNull) {
			showErrorDialog("当前不存在列车数据！");
			return;
		}

		_MyCanvas.clearChart();	//清除画布
		if($("#canvas_checkbox_trainTime").is(":checked")){
			myIsDrawTrainTime = true;
			_MyCanvas.drawGraph(context,{
				isDrawTrainTime:myIsDrawTrainTime,			//是否绘制列车经由站到达及出发时间
				xScale : currentXScale,			//x轴缩放比例
				xScaleCount : currentXScaleCount,	//x轴放大总倍数
				yScale : currentYScale			//y轴缩放比例
			});
		} else {
			myIsDrawTrainTime = false;
			_MyCanvas.drawGraph(context,{
				isDrawTrainTime:myIsDrawTrainTime,			//是否绘制列车经由站到达及出发时间
				xScale : currentXScale,			//x轴缩放比例
				xScaleCount : currentXScaleCount,	//x轴放大总倍数
				yScale : currentYScale			//y轴缩放比例
			});
		}
		
	});
	
	
	//x放大2倍
	$("#canvas_event_btn_x_magnification").click(function(){
		if (currentXScaleCount == 32) {
			showErrorDialog("当前已经不支持继续放大啦！");
			return;
		}
		if (dataIsNull) {
			showErrorDialog("当前不存在列车数据！");
			return;
		}
		
		//计算画布比例及倍数
		currentXScale = currentXScale/2;
		currentXScaleCount = currentXScaleCount*2;
		
		if($("#canvas_checkbox_trainTime").is(":checked")){
			
		}

		$("#canvas_event_label_xscale").text(currentXScaleCount);
		_MyCanvas.clearChart();	//清除画布
		_MyCanvas.drawGraph(context,{
				isDrawTrainTime:myIsDrawTrainTime,			//是否绘制列车经由站到达及出发时间
				 xScale : currentXScale,			//x轴缩放比例
				 xScaleCount : currentXScaleCount,	//x轴放大总倍数
				 yScale : currentYScale			//y轴缩放比例
			 });
		
	});
	
	//x缩小2倍
	$("#canvas_event_btn_x_shrink").click(function(){
		if (currentXScaleCount == 0.25) {
			showErrorDialog("当前已经不支持继续缩小啦！");
			return;
		}
		if (dataIsNull) {
			showErrorDialog("当前不存在列车数据！");
			return;
		}
		

		//计算画布比例及倍数
		currentXScale = currentXScale*2;
		currentXScaleCount = currentXScaleCount/2;

		$("#canvas_event_label_xscale").text(currentXScaleCount);
		_MyCanvas.clearChart();	//清除画布
		_MyCanvas.drawGraph(context,{
			isDrawTrainTime:myIsDrawTrainTime,			//是否绘制列车经由站到达及出发时间
			 xScale : currentXScale,			//x轴缩放比例
			 xScaleCount : currentXScaleCount,	//x轴放大总倍数
			 yScale : currentYScale			//y轴缩放比例
		 });
	});
	
	//y放大2倍
	$("#canvas_event_btn_y_magnification").click(function(){
		if (currentYScaleCount == 8) {
			showErrorDialog("当前已经不支持继续放大啦！");
			return;
		}
		if (dataIsNull) {
			showErrorDialog("当前不存在列车数据！");
			return;
		}
		
		
		//计算画布比例及倍数
		currentYScale = currentYScale/2;
		currentYScaleCount = currentYScaleCount*2;

		$("#canvas_event_label_yscale").text(currentYScaleCount);
		_MyCanvas.clearChart();	//清除画布
		_MyCanvas.drawGraph(context,{
				isDrawTrainTime:myIsDrawTrainTime,			//是否绘制列车经由站到达及出发时间
				 xScale : currentXScale,			//x轴缩放比例
				 xScaleCount : currentXScaleCount,	//x轴放大总倍数
				 yScale : currentYScale			//y轴缩放比例
			 });
		
	});
	
	//y缩小2倍
	$("#canvas_event_btn_y_shrink").click(function(){
		if (currentYScaleCount == 0.25) {
			showErrorDialog("当前已经不支持继续缩小啦！");
			return;
		}
		if (dataIsNull) {
			showErrorDialog("当前不存在列车数据！");
			return;
		}
		

		//计算画布比例及倍数
		currentYScale = currentYScale*2;
		currentYScaleCount = currentYScaleCount/2;

		$("#canvas_event_label_yscale").text(currentYScaleCount);
		_MyCanvas.clearChart();	//清除画布
		_MyCanvas.drawGraph(context,{
			isDrawTrainTime:myIsDrawTrainTime,			//是否绘制列车经由站到达及出发时间
			 xScale : currentXScale,			//x轴缩放比例
			 xScaleCount : currentXScaleCount,	//x轴放大总倍数
			 yScale : currentYScale			//y轴缩放比例
		 });
	});
	
	
});




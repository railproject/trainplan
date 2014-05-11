var _MyCanvas = null;

var MyCanvas = function(){
	
	this.drawGraph = function (contextParam) {
		var _xScale = 10;	//x轴时间（分钟）转换为长度像素px的除数
		var _stepX = 1;		//每一分钟X轴步长为5px
		var _stepY = 50;	//y轴步长为20px； 每站高度间隔
		var _startX = 100;	//x开始位置
		var _startY = 100;	//y开始位置
		
		var myCanvasComponent = new MyCanvasComponent(contextParam, _startX,_startY,_stepX,_stepY,_xScale,gridData.data.days, gridData.data.crossStns);
		//1.绘制网格
		myCanvasComponent.drawGrid("green");
		
		
		//2.绘制交路线
//		for (var i=0, _len=myJlData.data.length; i<_len; i++) {
//			var _obj = myJlData.data[i];
//			var _color = getRandomColor();
//			
//			//2.1 绘制交路列车运行线
//			for (var j=0, _lenTrain=_obj.trains.length; j<_lenTrain; j++) {
//				myCanvasComponent.drawTrainRunLine(_color, _obj.trains[j]);
//			}
//			
//			//2.2 绘制交路接续关系
//			myCanvasComponent.drawJxgx(_color, _obj.jxgx);
//		}
		
		
		//绘制单个列车
		myCanvasComponent.drawTrainRunLine(true,getRandomColor(), myTrainData.data);
		
	};
	
};





$(function(){
	
	
	var canvas = document.getElementById("text_canvas");

	_MyCanvas = new MyCanvas();
	_MyCanvas.drawGraph(canvas.getContext('2d'));
//	var buf = [];
//	for(var i = 0; i < 100; i++){  buf.push(i.toString());}
//	var all = buf.join("");
//	console.dir(all);
	
	
	
});





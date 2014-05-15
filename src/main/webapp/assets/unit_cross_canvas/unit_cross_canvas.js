var _MyCanvas = null;

var MyCanvas = function(){
	
	this.drawGraph = function (contextParam) {
		var booleanDrawJlStartAndEnd = true;	//是否绘制交路起止标记
		var myCanvasComponent = new MyCanvasComponent(contextParam, gridData.data.days, gridData.data.crossStns);
		//1.绘制网格
		myCanvasComponent.drawGrid("green");
		
		//2.绘制交路线
		for (var i=0, _len=myJlData.data.length; i<_len; i++) {
			var _obj = myJlData.data[i];
			var _color = getRandomColor();
			var _lenJlTrain=_obj.trains.length;
			
			//2.1 绘制交路列车运行线
			for (var j=0; j<_lenJlTrain; j++) {
				myCanvasComponent.drawTrainRunLine(false, _color, _obj.trains[j]);
			}
			
			//2.2 绘制交路接续关系
			myCanvasComponent.drawJxgx(_color, _obj.jxgx);
			
			
			if (booleanDrawJlStartAndEnd){
				myCanvasComponent.drawJlStartAndEnd(_color, _obj.trains);
			}
			
			
		}
		
		
		//绘制单个列车
//		console.log(getRandomColor());
//		myCanvasComponent.drawTrainRunLine(true,getRandomColor(), myTrainData.data);
		
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








$(function(){
	console.dir(canvasData);
	var canvas = document.getElementById("trainplan_canvas");
	var context = canvas.getContext('2d');
	var myCanvasComponent = new MyCanvasComponent(context, canvasData.grid.days, canvasData.grid.crossStns);
	
	$("#runline_ky").click(function() {
		context.clearRect(0,0,canvas.width,canvas.height);
		//1.绘制网格
		myCanvasComponent.drawGrid("green");
		
		if($("#runline_ky").is(':checked')){
			//绘制客运开行计划
			myCanvasComponent.drawTrainRunLine(true, "#8236ac", canvasData.runplan);
		}
		if ($("#runline_jbt").is(':checked')) {
			//绘制客运开行计划
			myCanvasComponent.drawTrainRunLine(true, "#72b5d2", canvasData.runline);
		}
    });
	
	$("#runline_jbt").click(function() {
		context.clearRect(0,0,canvas.width,canvas.height);
		//1.绘制网格
		myCanvasComponent.drawGrid("green");
		
		if($("#runline_ky").is(':checked')){
			//绘制客运开行计划
			myCanvasComponent.drawTrainRunLine(true, "#8236ac", canvasData.runplan);
		}
		if ($("#runline_jbt").is(':checked')) {
			//绘制运行线
			myCanvasComponent.drawTrainRunLine(true, "#72b5d2", canvasData.runline);
		}
		
    });
	
	
	//绘制客运开行计划
	//1.绘制网格
	myCanvasComponent.drawGrid("green");
	
	//2.绘制客运开行计划
	myCanvasComponent.drawTrainRunLine(true, "#8236ac", canvasData.runplan);

	//3.绘制运行线
	myCanvasComponent.drawTrainRunLine(true, "#72b5d2", canvasData.runline);
});





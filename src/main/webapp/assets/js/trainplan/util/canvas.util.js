/**
 * 产生随机颜色
 */
function getRandomColor() {
	return '#'+('00000'+(Math.random()*0x1000000<<0).toString(16)).slice(-6);
	//"rgba("+parseInt(10*Math.random())+","+parseInt(100*Math.random())+","+parseInt(50*Math.random())+","+Math.random()+")";
};

/**
 * 画竖线
 * @param context
 * @param lineWidth
 * @param color
 * @param x1
 * @param y1
 * @param y2
 */
function myCanvasDrawFullLineY(context,lineWidth, color, x1, y1, y2) {
	myCanvasDrawLine(context,lineWidth, color, x1, y1, x1, y2);
}

/**
 * 画线
 * @param context
 * @param lineWidth
 * @param color
 * @param fromX
 * @param fromY
 * @param toX
 * @param toY
 */
function myCanvasDrawLine(context,lineWidth, color, fromX, fromY, toX, toY) {
	context.beginPath();
	
	context.strokeStyle = color;//"#eee";
	context.lineWidth = lineWidth;
	context.moveTo(fromX, fromY);
	context.lineTo(toX, toY);
	context.stroke();//绘画
	context.closePath();//关闭path
}


/**
 * 画竖虚线
 * @param context
 * @param lineWidth
 * @param color
 * @param fromX
 * @param fromY
 * @param toX
 * @param toY
 * @param pattern
 */
function myCanvasDrawDashLineY(context,lineWidth, color, fromX, fromY, toX, toY, pattern) {
	context.strokeStyle = color;//"#eee";
	context.lineWidth = lineWidth;
	context.dashedLineTo(fromX, fromY, toX, toY, pattern);//竖虚线
}


/**
 * 画圆
 * @param context
 * @param circle {x:10,y:20,radius:20,strokeStyle:"rgba(0,0,0,0.5)",fillStyle:"rgba(80,190,240,0.6)"}
 */
function myCanvasDrawCircle(context, circle) {
	context.beginPath();
	context.save();
	context.strokeStyle = circle.color;
	context.fillStyle = circle.fillStyle;
	context.arc(circle.x,circle.y,circle.radius,0,Math.PI*2,false);
	

	context.stroke();
	context.fill();
	context.restore();
	context.closePath();
};



function windowToCanvas(canvas, x, y) {
	var bbox = canvas.getBoundingClientRect();
	return {
		x:(x-bbox.left)*(canvas.width/bbox.width),
		y:(y-bbox.top)*(canvas.height/bbox.height)
	};
};



function myCanvasFillTextWithColor(context, colorParam, textObj) {
	context.beginPath();
	context.strokeStyle = colorParam;
	context.fillStyle = colorParam;
	// 设置字体
	context.font = "Bold 14px Arial";
	// 设置对齐方式
	context.textAlign = "center";
	context.fillText(textObj.text, textObj.fromX, textObj.fromY);
};


function myCanvasFillText(context, textObj) {
	context.beginPath();
	// 设置字体
	context.font = "Bold 12px Arial";
	// 设置对齐方式
	context.textAlign = "center";
	context.fillText(textObj.text, textObj.fromX, textObj.fromY);
};





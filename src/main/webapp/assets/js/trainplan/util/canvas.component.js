
/**
 * @param xDateArray 交路经由站数组 [{runDate:"20140510",runDateText:"2014-05-10"},{runDate:"20140511",runDateText:"2014-05-11"}]//日期段，x轴刻度
 * @param stnArray 交路经由站数组 [{stnName:"成都"},{stnName:"遂宁"},{stnName:"南充"},{stnName:"蓬安"},{stnName:"营山"}]
 */
var MyCanvasComponent = function(context,startX,startY,stepX,stepY,xScale,xDateArray, stnArray) {
	var _context = context;
	var _xDateArray = xDateArray;
	var _stnArray = stnArray;
	var _startX = startX;
	var _startY = startY;
	var _stepX = stepX;
	var _stepY = stepY;
	var _xScale = xScale;	//x轴缩放比例 x轴时间（分钟）转换为长度像素px的除数
	var _endX = _stepX*_xDateArray.length*24*60/_xScale + _startX;	//x轴 日期范围最大刻度值
	
	//设置画布x、y长度   +100是为了方便显示边缘线
	_context.canvas.width =  _endX + 100;
	_context.canvas.height = _stnArray.length*_stepY + _startY +100;
	
	/**
	 * 获取站点y标高度 stepY倍数
	 * private
	 * @param stnName 站点名称
	 */
	this.getStnArcYIndex = function(stnName) {
		for(var i=0, _len=_stnArray.length; i <_len; i++) {
			if (_stnArray[i].stnName == stnName) {
				return i;
			}
		}
	};
	
	
	/**
	 * 获取站点x标宽度 oneDayWidth倍数
	 * private
	 * @param runDate 运行日期 yyyyMMdd
	 */
	this.getStnArcXIndex = function(runDate) {
		for(var i=0, _len=_xDateArray.length; i <_len; i++) {
			if (_xDateArray[i].runDate == runDate) {
				return i;
			}
		}
	};
	
	
	
	
	/**
	 * 获取x坐标
	 * private
	 * @param runDate 	运行日期 yyyyMMdd
	 * @param time 		HH:mm 小时:分钟
	 */
	this.getX = function(runDate, time) {
		var _oneDayWidth = _stepX*24*60/_xScale;	//一天的x宽度
		var _dayWidth = this.getStnArcXIndex(runDate)*_oneDayWidth;	//x平移天数刻度
		var _timeArray = time.split(":");
		var _minuteWidth = (parseInt(_timeArray[0])*60 + parseInt(_timeArray[1]))*_stepX/_xScale;
		var _x = startX + _dayWidth + _minuteWidth;
		return _x;
	};
	
	
	/**
	 * 获取y坐标
	 * private
	 * @param stnName 站名
	 */
	this.getY = function(stnName) {
		var _y = _startY + this.getStnArcYIndex(stnName)*_stepY +15;		//该车站Y标
		return _y;
	};
	
	
	
	
	/**
	 * 绘制网格X
	 * private
	 * @param color	//颜色
	 */
	this.drawGridX = function(color) {
		var _fillTextStartX = _startX - 80;	//站台名称开始点X
		var _xDashedLineEnd = _endX+20; 	//每站虚线（横向）x终点    +20是为了造成延伸效果
		var _y = 0;
		for(var i=0, _len = _stnArray.length;i<_len;i++) {
			var _obj = _stnArray[i];
			_y = _startY+i*_stepY;//
			_context.lineWidth = 1;
			_context.strokeStyle = color;//"green";
			myCanvasFillText(_context, {
				text : _obj.stnName,
				fromX : _fillTextStartX,
				fromY : _y+20
			});
			_context.dashedLineTo(_startX-10, _y+15, _xDashedLineEnd, _y+15, 10);//横虚线     10:虚线间隔10px
		}
	};
	
	
	
	/**
	 * 绘制网格Y
	 * private
	 * @param color	//颜色
	 */
	this.drawGridY = function(color) {
		//画竖线
		var _oneDayWidth = _stepX*24*60/_xScale;
		var _oclock6Width = 6*60*_stepX/_xScale;
		var _oclock12Width = 12*60*_stepX/_xScale;
		var _oclock18Width = 18*60*_stepX/_xScale;
		
		
		for (var i = 0,_len=_xDateArray.length; i<_len; i++) {
			//0点
			myCanvasDrawFullLineY(_context, 2, color, _startX+i*_oneDayWidth, _startY, _context.canvas.height);
			myCanvasFillText(_context, {
				text : "0点",//(i+1)+"日"
				fromX : _startX+i*_oneDayWidth,
				fromY : _startY-10
			});
			myCanvasFillText(_context, {
				text : _xDateArray[i].runDateText,
				fromX : (_startX+i*_oneDayWidth + _oclock12Width),
				fromY : _startY-30
			});
			
			
			//6点 虚线    6*60*minuteStepX
			myCanvasDrawDashLineY(_context, 1, color,_startX+_oclock6Width+i*_oneDayWidth,_startY, _startX+_oclock6Width+i*_oneDayWidth, _context.canvas.height, 10);
			
			//12点    12*60*minuteStepX
			myCanvasDrawFullLineY(_context, 1, color,_startX+_oclock12Width+i*_oneDayWidth,_startY, _context.canvas.height);
			
			//18点 虚线    18*60*minuteStepX
			myCanvasDrawDashLineY(_context, 1, color,_startX+_oclock18Width+i*_oneDayWidth,_startY, _startX+_oclock18Width+i*_oneDayWidth, _context.canvas.height, 10);
			
			if (i == _len-1) {
				//24点
				myCanvasDrawFullLineY(_context, 2, color, _startX+(i+1)*_oneDayWidth, _startY, _context.canvas.height);
				//this.fillText((i+2)+"日", paramObj.startX+(i+1)*oneDayWidth-8, paramObj.startY-15);
				myCanvasFillText(_context, {
					text : "0点",//(i+1)+"日"
					fromX : _startX+(i+1)*_oneDayWidth,
					fromY : _startY-10
				});
			}
		}
	};
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 绘制网格
	 * public
	 * @param color 网格颜色
	 */
	this.drawGrid = function(color) {
		this.drawGridX(color);	//绘制x轴
		this.drawGridY(color);	//绘制y轴
	};
	
	
	
	/**
	 * 绘制列车线
	 * public
	 * @param flag	//是否画起止箭头标识 true/flase
	 * @param color //线条颜色 #ffffff
	 * @param {
					trainStnArray:[
									{runDays:0,runDate:"20140510",stnName:"成都",arrTime:"19:18",dptTime:"19:18",stayTime:0},
									{runDays:0,runDate:"20140510",stnName:"遂宁",arrTime:"21:19",dptTime:"21:33",stayTime:14},
									{runDays:0,runDate:"20140510",stnName:"南充",arrTime:"22:15",dptTime:"22:22",stayTime:7},
									{runDays:0,runDate:"20140510",stnName:"蓬安",arrTime:"22:49",dptTime:"22:54",stayTime:5},
									{runDays:0,runDate:"20140510",stnName:"营山",arrTime:"23:06",dptTime:"23:11",stayTime:5},
									{runDays:1,runDate:"20140511",stnName:"达州",arrTime:"00:13",dptTime:"00:23",stayTime:10},
									{runDays:1,runDate:"20140511",stnName:"安康",arrTime:"03:12",dptTime:"03:32",stayTime:20},
									{runDays:1,runDate:"20140511",stnName:"华山",arrTime:"07:45",dptTime:"07:51",stayTime:6},
									{runDays:1,runDate:"20140511",stnName:"北京西",arrTime:"21:07",dptTime:"21:07",stayTime:0}
		    	        	  ],//列车经由站信息
					trainName : "K818"	//列车名称
				}
	 */
	this.drawTrainRunLine = function(flag, colorParam, paramObj) {
		var _y = 0;					//到达站和出发站的y坐标
		var _dayWidth = 0;			//x平移天数刻度
		var _parentDeptStn = {};	//上一站出发点x y坐标
		
		var _arrMinuteArray = [];	//到达点小时+分钟
		var _arrMinuteWidth = 0;	//到达点x平移分钟刻度
		var _arrTimeX = 0;			//到达点x标

		var _dptMinuteArray = [];	//出发点小时+分钟
		var _dptMinuteWidth = 0;	//出发点x平移分钟刻度
		var _dptTimeX = 0;			//出发点x标

		var _oneDayWidth = _stepX*24*60/_xScale;	//一天的x宽度
		var _len = paramObj.trainStns.length;
		
		
		if (flag && _len > 0) {
			if(paramObj.startStn == paramObj.trainStns[0].stnName) {//列车经由第一站==始发站   则绘制开始标记
				this.drawTrainStartArrows(colorParam, paramObj.trainStns[0]);
			}
			
			if(paramObj.endStn == paramObj.trainStns[_len-1].stnName) {//列车经由最后一站==终到站   则绘制终到标记
				this.drawTrainEndArrows(colorParam, paramObj.trainStns[_len-1]);
			}
		}
		
		
		
		

		for(var i=0; i<_len;i++) {
			var _obj = paramObj.trainStns[i];
			_y = this.getY(_obj.stnName);	//该车站Y标
			_arrTimeX = this.getX(_obj.runDate, _obj.arrTime);//计算到达点x标
			_dptTimeX = this.getX(_obj.runDate, _obj.dptTime);//计算出发点x标

			//绘制到达点
			myCanvasDrawCircle(_context, {
				x:_arrTimeX,
				y:_y,
				radius:2,//半径
				strokeStyle: colorParam,
				fillStyle:colorParam
			});
			
			//绘制出发点
			myCanvasDrawCircle(_context, {
				x:_dptTimeX,
				y:_y,
				radius:2,	//半径
				strokeStyle:colorParam,
				fillStyle:colorParam
			});

			if (i == 0) {
				//绘制列车名称
				myCanvasFillTextWithColor(_context, colorParam, {
					text : paramObj.trainName,
					fromX : _dptTimeX,
					fromY : _y-15
				});
			} else {
				//连接上一站出发点到本站到达点
				myCanvasDrawLine(_context, 2, colorParam, _parentDeptStn.x, _parentDeptStn.y, _arrTimeX, _y);
			}
			
			//连接本站到达点和出发点
			myCanvasDrawLine(_context, 2, colorParam, _arrTimeX, _y, _dptTimeX, _y);

			//上一站出发点x y坐标
			_parentDeptStn = {x:_dptTimeX, y: _y};
		}
	};
	
	
	this.drawTrainStartArrows = function(colorParam, stnObj) {
		var offsetY = 0;
		var directionY = this.getDirectionY(stnObj.stnName);
		if ("up" == directionY) {
			offsetY = -10;
		} else if ("down" == directionY) {
			offsetY = 10;
		}
		
		//坐标定位
		fromX = this.getX(stnObj.runDate, stnObj.dptTime);
		fromY = this.getY(stnObj.stnName);
		
		//绘线
		myCanvasDrawLine(_context, 2, colorParam, fromX-5, fromY+offsetY, fromX+5, fromY+offsetY);
		myCanvasDrawLine(_context, 2, colorParam, fromX, fromY+offsetY, fromX, fromY);
	};
	
	
	this.drawTrainEndArrows = function(colorParam, stnObj) {
		//坐标定位
		fromX = this.getX(stnObj.runDate, stnObj.dptTime);
		fromY = this.getY(stnObj.stnName);
		
		var offsetY = 0;
		var directionY = this.getDirectionY(stnObj.stnName);
		
		if ("up" == directionY) {
			offsetY = -10;
			//绘线
			myCanvasDrawLine(_context, 2, colorParam, fromX, fromY, fromX, fromY+offsetY);//竖线
			myCanvasDrawLine(_context, 2, colorParam, fromX-5, fromY+offsetY, fromX+5, fromY+offsetY);//三角形横线
			myCanvasDrawLine(_context, 2, colorParam, fromX-5, fromY+offsetY, fromX, fromY+offsetY-10);//三角形左斜线
			myCanvasDrawLine(_context, 2, colorParam, fromX, fromY+offsetY-10, fromX+5, fromY+offsetY);//三角形右斜线
		} else if ("down" == directionY) {
			offsetY = 10;

			//绘线
			myCanvasDrawLine(_context, 2, colorParam, fromX, fromY, fromX, fromY+offsetY);//竖线
			myCanvasDrawLine(_context, 2, colorParam, fromX-5, fromY+offsetY, fromX+5, fromY+offsetY);//三角形横线
			myCanvasDrawLine(_context, 2, colorParam, fromX-5, fromY+offsetY, fromX, fromY+offsetY+10);//三角形左斜线
			myCanvasDrawLine(_context, 2, colorParam, fromX, fromY+offsetY+10, fromX+5, fromY+offsetY);//三角形右斜线
		}
		
		
	};
	
	
	
	/**
	 * 交路接续关系线、列车起止站标记  y坐标方向
	 * 规则说明：当stnName = 交路经由站数组中第一个站时，y向上
	 * 		当stnName = 交路经由站数组中最后一个站时，y向下
	 * private
	 */
	this.getDirectionY = function(stnName) {
		var _len = _stnArray.length;
		if (_len == 0) return "";	//经由站为空

		if(_stnArray[0].stnName == stnName) {
			return "up";//up
		} else if(_stnArray[_len-1].stnName == stnName) {
			return "down";//down
		}
		return "";
	}
	
	
	/**
	 * 绘制交路接续关系
	 * public
	 * @param color
	 * @param 接续关系数组 
	 * 			[
	    	        {fromStnName:"北京西",fromRunDate:"20140511",fromTime:"21:07",toStnName:"北京西",toRunDate:"20140512",toTime:"08:35"},
	    	        {fromStnName:"成都",fromRunDate:"20140513",fromTime:"12:30",toStnName:"成都",toRunDate:"20140513",toTime:"19:18"},
	    	        {fromStnName:"北京西",fromRunDate:"20140514",fromTime:"21:07",toStnName:"北京西",toRunDate:"20140515",toTime:"08:35"}
				]
	 */
	this.drawJxgx = function(colorParam, jxgxArray) {
		var fromX = 0;
		var fromY = 0;
		var toX = 0;
		var toY = 0;
		var offsetY = 0;	//Y偏移量 用于绘制交路接续方向  down:10px、up:-10px
		for (var i=0, _len = jxgxArray.length; i<_len; i++) {
			var jxgxObj = jxgxArray[i];
			var directionY = this.getDirectionY(jxgxObj.fromStnName);
			if ("up" == directionY) {
				offsetY = -10;
			} else if ("down" == directionY) {
				offsetY = 10;
			}
			
			//坐标定位
			fromX = this.getX(jxgxObj.fromRunDate, jxgxObj.fromTime);
			fromY = this.getY(jxgxObj.fromStnName);
			toX = this.getX(jxgxObj.toRunDate, jxgxObj.toTime);
			toY = this.getY(jxgxObj.toStnName);
			
			//绘线
			myCanvasDrawLine(_context, 2, colorParam, fromX, fromY, fromX, fromY+offsetY);
			myCanvasDrawLine(_context, 2, colorParam, fromX, fromY+offsetY, toX, fromY+offsetY);
			myCanvasDrawLine(_context, 2, colorParam, toX, fromY+offsetY, toX, fromY);
		}
	};
	
	
}
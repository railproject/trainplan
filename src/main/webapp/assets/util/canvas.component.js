
/**
 * @param context 画布对象
 * @param startX x起始位置
 * @param startY y起始位置
 * @param stepX x步长
 * @param stepY y步长
 * @param xScale x缩放比例 x轴时间（分钟）转换为长度像素px的除数 （建议整数：1、2、10等）
 * @param xDateArray 日期数组 [{runDate:"20140510",runDateText:"2014-05-10"},{runDate:"20140511",runDateText:"2014-05-11"}]//日期段，x轴刻度
 * @param stnArray 交路经由站数组 [{stnName:"成都"},{stnName:"遂宁"},{stnName:"南充"},{stnName:"蓬安"},{stnName:"营山"}]
 */
var MyCanvasComponent = function(context, xDateArray, stnArray) {
	//function(context,startX,startY,stepX,stepY,xScale,xDateArray, stnArray)
	var _context = context;
	var _xDateArray = xDateArray;
	var _stnArray = stnArray;
	var _stnOffsetY = 45;	//第一个站横虚线y对于_startY（画布y起始位置）的偏移量
	var _startX = 100;	//默认100 x开始位置
	var _startY = 100;	//默认100 y开始位置
	var _stepX = 1;		//默认1	x步长 每一分钟X轴步长为1px
	var _stepY = 50;	//默认100 y步长
	var _xScale = 10;	//默认10 x轴缩放比例 x轴时间（分钟）转换为长度像素px的除数
	var _drawYMoreFlag = false;	//默认false	每一天x长度范围内是否绘制更多竖线 true/false
	var _endX = 1000;	//x轴 日期范围最大刻度值
	var _endY = 1000;	//y轴 结束刻度
	
	
	//================ 初始化赋值 ================
//	this.initVariables = function() {
		
		if (_xDateArray.length <=2) {
			_xScale = 2;	//x轴缩放比例 x轴时间（分钟）转换为长度像素px的除数
			_drawYMoreFlag = true;	//每一天x长度范围内需要绘制更多竖线
		}

		_endX = _stepX*_xDateArray.length*24*60/_xScale + _startX;	//x轴 日期范围最大刻度值
		_endY = _stnArray.length*_stepY + _startY + _stnOffsetY;
		//设置画布x、y长度   +100是为了方便显示边缘线
		_context.canvas.width =  _endX + 100;
		_context.canvas.height = _endY +100;
//	};
	
	
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
		var _x = _startX + _dayWidth + _minuteWidth;
		return _x;
	};
	
	
	/**
	 * 获取y坐标
	 * private
	 * @param stnName 站名
	 */
	this.getY = function(stnName) {
		var _y = _startY + this.getStnArcYIndex(stnName)*_stepY +_stnOffsetY;		//该车站Y标
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
			_y = _startY+i*_stepY+_stnOffsetY;//
			_context.lineWidth = 1;
			_context.strokeStyle = color;//"green";
			myCanvasFillText(_context, {
				text : _obj.stnName,
				fromX : _fillTextStartX,
				fromY : _y+5
			});
			_context.dashedLineTo(_startX-10, _y, _xDashedLineEnd, _y, 10);//横虚线     10:虚线间隔10px
		}
	};
	
	
	/**
	 * 
	 * private
	 */
	this.drawY = function(color, lineWidth, text, fromX, dashFlag) {
		if (dashFlag) {
			myCanvasDrawDashLineY(_context, lineWidth, color,fromX,_startY, fromX, _endY, 10);
		} else {
			myCanvasDrawFullLineY(_context, lineWidth, color, fromX, _startY, _endY);
		}
		//顶端显示
		myCanvasFillText(_context, {
			text : text,//0 6 12 18 
			fromX : fromX,
			fromY : _startY-5
		});
		//底端显示
		myCanvasFillText(_context, {
			text : text,//0 6 12 18 
			fromX : fromX,
			fromY : _endY+15
		});
	};
	
	
	
	
	/**
	 * 绘制网格Y _xDateArray2天以上， 每天绘制5条线(包含0点、6点、12点、18点、0点)
	 * 
	 * 为避免循环中做逻辑if判断（会降低性能）。所以未与drawGridMoreY合并
	 * private
	 * @param color	//颜色
	 */
	this.drawGridY = function(color) {
		//画竖线
		var _oneHourWidth = 60*_stepX/_xScale;
		var _oclock6Width = 6*_oneHourWidth;
		var _oclock12Width = 12*_oneHourWidth;
		var _oclock18Width = 18*_oneHourWidth;
		var _oneDayWidth = 24*_oneHourWidth;
		
		for (var i = 0,_len=_xDateArray.length; i<_len; i++) {
			//网格顶端显示 2014-05-12 一天范围内的 日期文本显示 
			myCanvasFillText(_context, {
				text : _xDateArray[i].runDateText,
				fromX : (_startX+i*_oneDayWidth + _oclock12Width),
				fromY : _startY-35
			});
			//网格底端显示 2014-05-12
			myCanvasFillText(_context, {
				text : _xDateArray[i].runDateText,
				fromX : (_startX+i*_oneDayWidth + _oclock12Width),
				fromY : _endY +35
			});
			
			
			this.drawY(color, 2, "0", _startX+i*_oneDayWidth, false);//0点
			this.drawY(color, 0.5, "6", _startX+_oclock6Width+i*_oneDayWidth, true);//6点
			this.drawY(color, 1, "12", _startX+_oclock12Width+i*_oneDayWidth, false);//12点
			this.drawY(color, 0.5, "18", _startX+_oclock18Width+i*_oneDayWidth, true);//18点
			
			//x结束位置 再画一条竖线
			if (i == _len-1) {
				this.drawY(color, 2, "0", _startX+(i+1)*_oneDayWidth, false);//24点
			}
		}
	};
	
	
	
	/**
	 * 绘制网格Y _xDateArray2天以下， 每天绘制5条线(包含0点、6点、12点、18点、0点)
	 * 
	 * 为避免循环中做逻辑if判断（会降低性能）。所以未与drawGridY合并
	 * private
	 * @param color	//颜色
	 */
	this.drawGridMoreY = function(color) {
		//画竖线
		var _oneHourWidth = 60*_stepX/_xScale;
		var _oclock3Width = 3*_oneHourWidth;
		var _oclock6Width = 6*_oneHourWidth;
		var _oclock9Width = 9*_oneHourWidth;
		var _oclock12Width = 12*_oneHourWidth;
		var _oclock15Width = 15*_oneHourWidth;
		var _oclock18Width = 18*_oneHourWidth;
		var _oclock21Width = 21*_oneHourWidth;
		var _oneDayWidth = 24*_oneHourWidth;
		
		for (var i = 0,_len=_xDateArray.length; i<_len; i++) {
			//一天范围内的 日期文本显示  如：2014-05-12
			myCanvasFillText(_context, {
				text : _xDateArray[i].runDateText,
				fromX : (_startX+i*_oneDayWidth + _oclock12Width),
				fromY : _startY-30
			});
			
			
			this.drawY(color, 2, "0", _startX+i*_oneDayWidth, false);//0点
			this.drawY(color, 0.5, "3", _startX+_oclock3Width+i*_oneDayWidth, true);//3点
			this.drawY(color, 1, "6", _startX+_oclock6Width+i*_oneDayWidth, true);//6点
			this.drawY(color, 0.5, "9", _startX+_oclock9Width+i*_oneDayWidth, true);//9点
			this.drawY(color, 1, "12", _startX+_oclock12Width+i*_oneDayWidth, false);//12点
			this.drawY(color, 0.5, "15", _startX+_oclock15Width+i*_oneDayWidth, true);//15点
			this.drawY(color, 1, "18", _startX+_oclock18Width+i*_oneDayWidth, true);//18点
			this.drawY(color, 0.5, "21", _startX+_oclock21Width+i*_oneDayWidth, true);//21点
			
			//x结束位置 再画一条竖线
			if (i == _len-1) {
				this.drawY(color, 2, "0", _startX+(i+1)*_oneDayWidth, false);//24点
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
		if (_drawYMoreFlag) {
			this.drawGridMoreY(color);
		} else {
			this.drawGridY(color);	//绘制y轴
		}
		
	};
	
	
	
	/**
	 * 绘制列车线
	 * public
	 * @param flag	//是否画起止箭头标识 true/flase
	 * @param color //线条颜色 #ffffff
	 * @param paramObj 单个车次信息
	 * 			{
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
		    	    startStn:"成都",//始发站名 为null时填入""（空串）
		    	    endStn:"北京西",//终到站名为null时填入""（空串）
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
		
		//绘制起止标记
		if (flag && _len > 0) {
			if(paramObj.startStn == paramObj.trainStns[0].stnName) {//列车经由第一站==始发站   则绘制开始标记
				this.drawTrainStartArrows(colorParam, paramObj.trainStns[0]);
			}
			
			if(paramObj.endStn == paramObj.trainStns[_len-1].stnName) {//列车经由最后一站==终到站   则绘制终到标记
				this.drawTrainEndArrows(colorParam, paramObj.trainStns[_len-1]);
			}
		}
		
		//绘制列车运行线
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

			//保存上一站出发点x y坐标
			_parentDeptStn = {x:_dptTimeX, y: _y};
		}
	};
	
	/**
	 * 绘制开始标记
	 * private
	 * @param stnObj 列车某个站点信息
	 * 			{runDays:0,runDate:"20140513",stnName:"北京西",arrTime:"08:35",dptTime:"08:35",stayTime:0}
	 */
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
	
	
	/**
	 * 绘制终到标记 三角形
	 * private
	 * @param 车站对象 {runDays:0,runDate:"20140513",stnName:"北京西",arrTime:"08:35",dptTime:"08:35",stayTime:0}
	 */
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
			myCanvasDrawLine(_context, 2, colorParam, fromX, fromY, fromX, fromY+offsetY);//接续左竖线
			myCanvasDrawLine(_context, 2, colorParam, fromX, fromY+offsetY, toX, fromY+offsetY);//接续横线
			myCanvasDrawLine(_context, 2, colorParam, toX, fromY+offsetY, toX, fromY);//接续右竖线
		}
	};
	
	
	/**
	 * 绘制每条交路起止标记
	 * public
	 * @param jlTrains 每条交路包含的所有车次信息
	 * 			[//该交路下所有车次数组
		    	          {
		    	        	  trainName:"K818",
		    	          	  startStn:"成都",//始发站名 为null时填入""（空串）
		    	          	  endStn:"北京西",//终到站名为null时填入""（空串）
		    	        	  trainStns:[
									{runDays:0,runDate:"20140511",stnName:"成都",arrTime:"19:18",dptTime:"19:18",stayTime:0},
									{runDays:0,runDate:"20140511",stnName:"遂宁",arrTime:"21:19",dptTime:"21:33",stayTime:14},
									{runDays:0,runDate:"20140511",stnName:"南充",arrTime:"22:15",dptTime:"22:22",stayTime:7},
									{runDays:0,runDate:"20140511",stnName:"蓬安",arrTime:"22:49",dptTime:"22:54",stayTime:5},
									{runDays:0,runDate:"20140511",stnName:"营山",arrTime:"23:06",dptTime:"23:11",stayTime:5},
									{runDays:1,runDate:"20140512",stnName:"达州",arrTime:"00:13",dptTime:"00:23",stayTime:10},
									{runDays:1,runDate:"20140512",stnName:"安康",arrTime:"03:12",dptTime:"03:32",stayTime:20},
									{runDays:1,runDate:"20140512",stnName:"华山",arrTime:"07:45",dptTime:"07:51",stayTime:6},
									{runDays:1,runDate:"20140512",stnName:"北京西",arrTime:"21:07",dptTime:"21:07",stayTime:0}
		    	        	  ]
		    	          },
		    	          {
		    	        	  trainName:"K817",
		    	          	  startStn:"北京西",//始发站名 为null时填入""（空串）
		    	          	  endStn:"成都",//终到站名为null时填入""（空串）
		    	        	  trainStns:[
									{runDays:0,runDate:"20140513",stnName:"北京西",arrTime:"08:35",dptTime:"08:35",stayTime:0},
									{runDays:0,runDate:"20140513",stnName:"华山",arrTime:"23:07",dptTime:"23:13",stayTime:6},
									{runDays:1,runDate:"20140514",stnName:"安康",arrTime:"03:59",dptTime:"04:13",stayTime:14},
									{runDays:1,runDate:"20140514",stnName:"达州",arrTime:"07:29",dptTime:"07:35",stayTime:6},
									{runDays:1,runDate:"20140514",stnName:"土溪",arrTime:"08:09",dptTime:"08:13",stayTime:4},
									{runDays:1,runDate:"20140514",stnName:"营山",arrTime:"08:45",dptTime:"08:49",stayTime:4},
									{runDays:1,runDate:"20140514",stnName:"蓬安",arrTime:"09:02",dptTime:"09:05",stayTime:3},
									{runDays:1,runDate:"20140514",stnName:"南充",arrTime:"09:32",dptTime:"09:36",stayTime:4},
									{runDays:1,runDate:"20140514",stnName:"遂宁",arrTime:"10:30",dptTime:"10:33",stayTime:3},
									{runDays:1,runDate:"20140514",stnName:"成都",arrTime:"12:30",dptTime:"12:30",stayTime:0}
		    	        	  ]
		    	          }
		    	  ]
	 */
	this.drawJlStartAndEnd = function(_color, jlTrains) {
		var _len = jlTrains.length;	//交路包含列车数
		if (jlTrains.length > 0) {
			if(jlTrains[0].trainStns.length>0 && jlTrains[0].startStn == jlTrains[0].trainStns[0].stnName) {//交路第一个列车经由第一站==始发站   则绘制开始标记
				this.drawTrainStartArrows(_color, jlTrains[0].trainStns[0]);
			}
			
			var _lenTrainStnLast = jlTrains[_len-1].trainStns.length;//该交路最后一个车次经由站长度
			if(_lenTrainStnLast>0 && jlTrains[_len-1].endStn == jlTrains[_len-1].trainStns[_lenTrainStnLast-1].stnName) {//交路最后一个车次经由最后一站==终到站   则绘制终到标记
				this.drawTrainEndArrows(_color, jlTrains[_len-1].trainStns[_lenTrainStnLast-1]);
			}
		}
		
	};
	
}
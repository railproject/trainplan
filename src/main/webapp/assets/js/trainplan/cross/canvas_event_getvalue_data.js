//界面测试专用
var dateArray = [];//[{runDate:"20140510",runDateText:"2014-06-10"},{runDate:"20140511",runDateText:"2014-06-11"}]
for(var i=0;i<4;i++) {
	dateArray.push({
//			dayIndex: i,
		runDate:"2014-06-1"+i
	});
};






/*==================================================
 * x\y坐标请求接口返回数据格式
 */
var gridData = {
			days:dateArray,//[{runDate:"20140510",runDateText:"2014-06-10"},{runDate:"20140511",runDateText:"2014-06-11"}]
			crossStns:[{stnName:"成都",isCurrentBureau:1},
			           {stnName:"遂宁",isCurrentBureau:1},
			           {stnName:"南充",isCurrentBureau:1},
			           {stnName:"蓬安",isCurrentBureau:1},
			           {stnName:"营山",isCurrentBureau:1},
			           {stnName:"土溪",isCurrentBureau:1},
			           {stnName:"达州",isCurrentBureau:1},
			           {stnName:"安康",isCurrentBureau:1},
			           {stnName:"华山"},
			           {stnName:"三门峡"},
			           {stnName:"洛阳"},
			           {stnName:"郑州"},
			           {stnName:"新乡"},
			           {stnName:"安阳"},
			           {stnName:"邯郸"},
			           {stnName:"邢台"},
			           {stnName:"石家庄"},
			           {stnName:"定州"},
			           {stnName:"保定"},
			           {stnName:"北京西"}
			]
};


/*==================================================
 *按交路绘图请求接口返回数据格式
 */
var myJlData = 
			[
		      {
		    	  crossName:"K818-K817-1",
		    	  jxgx:[//接续关系数组
		    	        {fromStnName:"北京西",fromTime:"2014-06-11 21:07",toStnName:"北京西",toTime:"2014-06-12 08:35"}

		    	        ],
		    	  trains:[//该交路下所有车次数组
		    	          {
		    	        	  trainName:"K818",
		    				  startStn:"成都",//始发站名 为null时填入""（空串）
		    				  endStn:"北京西",//终到站名为null时填入""（空串）
		    				  trainStns:[
		    						{runDays:0,stnName:"成都",arrTime:"2014-06-10 19:18",dptTime:"2014-06-10 19:18",stayTime:0},
		    						{runDays:0,stnName:"遂宁",arrTime:"2014-06-10 21:19",dptTime:"2014-06-10 21:33",stayTime:14},
		    						{runDays:0,stnName:"南充",arrTime:"2014-06-10 22:15",dptTime:"2014-06-10 22:22",stayTime:7},
		    						{runDays:0,stnName:"蓬安",arrTime:"2014-06-10 22:49",dptTime:"2014-06-10 22:54",stayTime:5},
		    						{runDays:0,stnName:"营山",arrTime:"2014-06-10 23:06",dptTime:"2014-06-10 23:11",stayTime:5},
		    						{runDays:1,stnName:"达州",arrTime:"2014-06-11 00:13",dptTime:"2014-06-11 00:23",stayTime:10},
		    						{runDays:1,stnName:"安康",arrTime:"2014-06-11 03:12",dptTime:"2014-06-11 03:32",stayTime:20},
		    						{runDays:1,stnName:"华山",arrTime:"2014-06-11 07:45",dptTime:"2014-06-11 07:51",stayTime:6},
		    						{runDays:1,stnName:"三门峡",arrTime:"2014-06-11 09:39",dptTime:"2014-06-11 09:41",stayTime:2},
		    						{runDays:1,stnName:"洛阳",arrTime:"2014-06-11 11:20",dptTime:"2014-06-11 11:23",stayTime:3},
		    						{runDays:1,stnName:"郑州",arrTime:"2014-06-11 12:54",dptTime:"2014-06-11 13:08",stayTime:14},
		    						{runDays:1,stnName:"新乡",arrTime:"2014-06-11 14:02",dptTime:"2014-06-11 14:12",stayTime:10},
		    						{runDays:1,stnName:"安阳",arrTime:"2014-06-11 15:21",dptTime:"2014-06-11 15:23",stayTime:2},
		    						{runDays:1,stnName:"邯郸",arrTime:"2014-06-11 15:58",dptTime:"2014-06-11 16:02",stayTime:4},
		    						{runDays:1,stnName:"邢台",arrTime:"2014-06-11 16:33",dptTime:"2014-06-11 16:37",stayTime:4},
		    						{runDays:1,stnName:"石家庄",arrTime:"2014-06-11 17:48",dptTime:"2014-06-11 17:56",stayTime:8},
		    						{runDays:1,stnName:"定州",arrTime:"2014-06-11 18:43",dptTime:"2014-06-11 18:45",stayTime:2},
		    						{runDays:1,stnName:"保定",arrTime:"2014-06-11 19:22",dptTime:"2014-06-11 19:26",stayTime:4},
		    						{runDays:1,stnName:"北京西",arrTime:"2014-06-11 21:07",dptTime:"2014-06-11 21:07",stayTime:0}
		    						
		    						
		    						
		    				  ]
		    	          },
		    	          {
		    	        	  trainName:"K817",
		    	          	  startStn:"北京西",//始发站名 为null时填入""（空串）
		    	          	  endStn:"成都",//终到站名为null时填入""（空串）
		    	        	  trainStns:[
									{runDays:0,stnName:"北京西",arrTime:"2014-06-12 08:35",dptTime:"2014-06-12 08:35",stayTime:0},
									{runDays:0,stnName:"华山",arrTime:"2014-06-12 23:07",dptTime:"2014-06-12 23:13",stayTime:6},
									{runDays:1,stnName:"安康",arrTime:"2014-06-13 03:59",dptTime:"2014-06-13 04:13",stayTime:14},
									{runDays:1,stnName:"达州",arrTime:"2014-06-13 07:29",dptTime:"2014-06-13 07:35",stayTime:6},
									{runDays:1,stnName:"土溪",arrTime:"2014-06-13 08:09",dptTime:"2014-06-13 08:13",stayTime:4},
									{runDays:1,stnName:"营山",arrTime:"2014-06-13 08:45",dptTime:"2014-06-13 08:49",stayTime:4},
									{runDays:1,stnName:"蓬安",arrTime:"2014-06-13 09:02",dptTime:"2014-06-13 09:05",stayTime:3},
									{runDays:1,stnName:"南充",arrTime:"2014-06-13 09:32",dptTime:"2014-06-13 09:36",stayTime:4},
									{runDays:1,stnName:"遂宁",arrTime:"2014-06-13 10:30",dptTime:"2014-06-13 10:33",stayTime:3},
									{runDays:1,stnName:"成都",arrTime:"2014-06-13 12:30",dptTime:"2014-06-13 12:30",stayTime:0}
		    	        	  ]
		    	          }
		    	  ]
		      }
		      //end 一条交路
		      
		      
		
		];



var canvasData = {
		grid:gridData,
		jlData:myJlData
};


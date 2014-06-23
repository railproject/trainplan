package org.railway.com.trainplan.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.railway.com.trainplan.web.dto.PlanLineGrid;
import org.railway.com.trainplan.web.dto.PlanLineGridX;
import org.railway.com.trainplan.web.dto.PlanLineGridY;

public class StattionUtils {

	
	public static void main(String[] args){
		/**
		 *  List<LinkedList<Station>> list1 = new ArrayList<LinkedList<Station>>();
		 *  
		 * *****************************
		 * 
		 * 
白银市|2014-06-23 07:18:00|0
白银西|2014-06-23 07:30:00|0
*****************************
白银西|2014-06-23 08:34:00|0
白银市|2014-06-23 08:45:00|0
*****************************
白银市|2014-06-23 10:50:00|0
白银西|2014-06-23 11:02:00|0
*****************************
白银西|2014-06-23 20:35:00|0
白银市|2014-06-23 20:46:00|0
		 */
		Station station1 = new Station("白银市","2014-06-23 07:18:00");
		Station station2 = new Station("白银西","2014-06-23 07:30:00");
		
		Station station3 = new Station("白银西","2014-06-23 08:34:00");
		Station station4 = new Station("白银市","2014-06-23 08:45:00");
		
		Station station5 = new Station("白银市","2014-06-23 10:50:00");
		Station station6 = new Station("白银西","2014-06-23 11:02:00");
		
		Station station7 = new Station("白银西","2014-06-23 20:35:00");
		Station station8 = new Station("白银市","2014-06-23 20:46:00");
		
		LinkedList<Station>  trainList1 = new LinkedList<Station>();
		trainList1.add(station1);
		trainList1.add(station2);
		
		LinkedList<Station>  trainList2 = new LinkedList<Station>();
		trainList2.add(station3);
		trainList2.add(station4);
		
		LinkedList<Station>  trainList3 = new LinkedList<Station>();
		trainList3.add(station5);
		trainList3.add(station6);
		
		LinkedList<Station>  trainList4 = new LinkedList<Station>();
		trainList4.add(station7);
		trainList4.add(station8);
		
		List<LinkedList<Station>> list1 = new ArrayList<LinkedList<Station>>();
		
		list1.add(trainList1);
		list1.add(trainList2);
		list1.add(trainList3);
		list1.add(trainList4);
		
		PlanLineGrid grid = getPlanLineGridForAll(list1,"2014-06-23","2014-06-24");
		//System.err.println(grid.getCrossStns());
		List<PlanLineGridY> yList = grid.getCrossStns();
		for(PlanLineGridY y : yList ){
			System.err.println("" + y.getStnName());
		}
		
		/**
		 * *****************************
北京|2014-06-20 14:41:00|0
北京南运转|2014-06-20 14:44:00|BT
北京南普速场|2014-06-20 14:51:00|BT
柳村线路所|2014-06-20 14:55:30|BT
丰台|2014-06-20 15:01:00|BT
西信号|2014-06-20 15:06:00|BT
西道口|2014-06-20 15:10:00|BT
石景山南|2014-06-20 15:18:00|TZ
养马场|2014-06-20 15:25:00|BT
养三|2014-06-20 15:29:00|BT
三家店|2014-06-20 15:36:00|TZ
斜河涧|2014-06-20 15:47:00|TZ
落坡岭|2014-06-20 15:59:00|TZ
安家庄|2014-06-20 16:43:00|TZ
雁翅|2014-06-20 16:54:00|TZ
珠窝东|2014-06-20 17:12:00|TZ
沿河城西|2014-06-20 17:29:00|TZ
旧庄窝(东)|2014-06-20 17:50:00|TZ
官厅(西)|2014-06-20 18:09:00|TZ
邢家堡|2014-06-20 18:23:00|TZ
沙城|2014-06-20 18:42:00|TZ
新保安|2014-06-20 18:54:00|TZ
西八里|2014-06-20 19:03:00|TZ
下花园|2014-06-20 19:14:00|TZ
辛庄子|2014-06-20 19:28:00|TZ
宣化|2014-06-20 19:43:00|TZ
沙岭子|2014-06-20 19:58:00|TZ
张家口南|2014-06-20 20:12:00|0
*****************************
张家口南|2014-06-21 07:40:00|0
沙岭子|2014-06-21 07:54:00|TZ
宣化|2014-06-21 08:12:00|TZ
辛庄子|2014-06-21 08:26:00|TZ
下花园|2014-06-21 08:41:00|TZ
西八里|2014-06-21 08:51:00|TZ
新保安|2014-06-21 09:03:00|TZ
沙城|2014-06-21 09:29:00|TZ
邢家堡|2014-06-21 09:43:00|TZ
官厅(西)|2014-06-21 09:57:00|TZ
旧庄窝(东)|2014-06-21 10:14:00|TZ
沿河城西|2014-06-21 10:32:00|TZ
珠窝东|2014-06-21 10:47:00|TZ
雁翅|2014-06-21 11:05:00|TZ
安家庄|2014-06-21 11:15:00|TZ
落坡岭|2014-06-21 11:22:00|TZ
斜河涧|2014-06-21 11:34:00|TZ
三家店|2014-06-21 11:46:00|TZ
养三|2014-06-21 11:51:00|BT
养马场|2014-06-21 11:55:00|BT
石景山南|2014-06-21 12:10:00|TZ
西道口|2014-06-21 12:17:00|BT
西信号|2014-06-21 12:21:00|BT
丰台|2014-06-21 12:22:00|BT
柳村线路所|2014-06-21 12:27:00|BT
北京南普速场|2014-06-21 12:30:00|BT
北京南运转|2014-06-21 12:37:00|BT
北京|2014-06-21 12:42:00|0
		 */
		/*Station  station1 = new Station("北京","2014-06-20 12:10:00",30);
		Station  station2 = new Station("北京","2014-06-20 12:10:00",20);
		Station  station3 = new Station("北京","2014-06-20 12:10:00",40);
		Station  station4 = new Station("北京","2014-06-20 12:10:00",10);
		//System.err.println(station1.equals(station2));
		List<Station> list = new ArrayList<Station>();
		list.add(station1);
		list.add(station2);
		list.add(station3);
		list.add(station4);
		ComparatorStation comparator = new ComparatorStation();
		Collections.sort(list, comparator);
		for(Station station: list){
			System.err.println("" + station.getMinites());
		}*/
		
		LinkedList<Station> longList = new LinkedList<Station>();
		LinkedList<Station> shortList = new LinkedList<Station>();
		/*Station station1 = new Station("A","2014-06-20 12:10:00");
		Station station2 = new Station("B","2014-06-20 12:50:00");
		Station station3 = new Station("C","2014-06-21 20:10:00");
		Station station4 = new Station("D","2014-06-21 22:10:00");
		Station station5 = new Station("E","2014-06-21 23:10:00");
		Station station6 = new Station("M","2014-06-22 02:10:00");
		Station station7 = new Station("N","2014-06-22 04:10:00");
		Station station8 = new Station("Q","2014-06-22 06:10:00");
		Station station9 = new Station("G","2014-06-22 08:10:00");
		longList.add(station1);
		longList.add(station2);
		longList.add(station3);
		longList.add(station4);
		longList.add(station5);
		longList.add(station6);
		longList.add(station7);
		longList.add(station8);
		longList.add(station9);
		Collections.reverse(longList);*/
		/*for(Station station :longList){
			System.err.println("stnName==" + station.getStnName());
			
		}*/
		Station test = new Station("z","2012-06-20 12:10:00");
		System.err.println("isContains==" + longList.contains(test));
		
		//Station station11 = new Station("A","2014-06-20 12:10:00");
		//Station station21 = new Station("B","2014-06-20 12:50:00");
		//Station station31 = new Station("C","2014-06-21 20:10:00");
		//Station station41 = new Station("D","2014-06-21 22:10:00");
		//Station station51 = new Station("F","2014-06-21 23:10:00");
		Station station61 = new Station("R","2014-06-22 02:10:00");
		Station station71 = new Station("Z","2014-06-22 04:10:00");
		Station station91 = new Station("G","2014-06-22 08:10:00");
		
		
		//shortList.add(station11);
		//shortList.add(station21);
		//shortList.add(station31);
		//shortList.add(station41);
		//shortList.add(station51);
		shortList.add(station61);
		shortList.add(station71);
		shortList.add(station91);
		
		/*List<Station> list = mergeStationTheSameDirection(longList,shortList);
		for(Station station : list){
			System.err.println(station.getStnName() + "|" + station.getDptTime()+"|" + station.getMinites());
		}
		*/
	}
	
	/**
	 * 判断两列车的list对象是否是一个顺序的
	 * @param stationList1
	 * @param stationList2
	 * @return
	 */
	public static boolean isSameDirectionTrainList(List<Station> stationList1,List<Station> stationList2){
		boolean isSame = false;
		int list1Index1=0;
		int list1Index2=0;
		int list2Index1=0;
		int list2Index2=0;
		int count = 0;
		for(Station  station : stationList1){
			if(count==2){
				break;
			}
			if(stationList2.contains(station)){
				if(count==0){
					list1Index1 = stationList1.indexOf(station);
					list2Index1 = stationList2.indexOf(station);
				}else{
					list1Index2 = stationList1.indexOf(station);
					list2Index2 = stationList2.indexOf(station);
				}
				
				count++;
			}
		}
		
		int temp1 = list1Index1 - list1Index2;
		int temp2 = list2Index1 - list2Index2;
		if(temp1 <0 && temp2<0){
			isSame = true;
		}else if(temp1>0&& temp2>0){
			isSame = true;
		}else{
			isSame = false;
		}
		return isSame;
	}
	
	/**
	 * 判断列车是否是同一个方向
	 * @return
	 */
	public static boolean isSameDirection(List<Station> stationList1,List<Station> stationList2){
		boolean result = true;
		int sameStationCount = 0;
		for(int i = 0;i<stationList1.size();i++){
			if(sameStationCount >=2){
				break;
			}
			Station station1 = stationList1.get(i);
			for(int j = 0;j<stationList2.size();j++){
				Station station2 = stationList2.get(j);
				if(station1.equals(station2)){
					sameStationCount++;
					break;
				}
			}
		}
		if(sameStationCount > 1){
			result = false;
		}
		return result;
	}
	/**
	 * 合并同方向的两车的经由站
	 * @param longList
	 * @param shortList
	 * @return
	 */
	public static List<Station> mergeStationTheSameDirection(List<Station> longList,List<Station> shortList){
		List<Station> result = new LinkedList<Station>();
		result.addAll(longList);
		for(Station station :shortList){
			if(!result.contains(station)){
				result.add(station);
			}
		}
		return result;
		
	}
	
	/**
	 * 合并反方向的两列列车的经由站
	 * @param longList
	 * @param shortList
	 * @return
	 */
	public static List<Station> mergeStation(List<Station> longList,List<Station> shortList)
	{
		if(!isSameDirectionTrainList(longList, shortList)){
			//方向调整成一样的
			Collections.reverse(shortList);
		}
		
		List<Station> result = new LinkedList<Station>();
		
		Station longListStation = null;
		
		Station shortListStaion = null;
		//记录当发现longList中有元素在shortList中没有时，result中最后一个元素的index
	    int resultIndex = 0;
	  //存放shortList中有，而longList中没有的元素
		List<Station> tempShortList = new LinkedList<Station>();
		//存放shortList中无，而longList中有的元素
		List<Station> tempLongList = new LinkedList<Station>();
		/**********************************/
		for(int i = 0;i<longList.size();i++){
			Station current = longList.get(i);
			
			if(!shortList.contains(current) ){
				if(result.size() != 0){
					resultIndex = 	result.size() -1 ;
					
				}
				//longListStation = current;
				tempLongList.add(current);
			}else{
				
				int indexCurrent = shortList.indexOf(current);
				
				//在shortList中的当前被匹配到的元素
				Station currentShort = shortList.get(indexCurrent);
				if(tempLongList.size()==0){
					longListStation = current;
				}
				if(indexCurrent == 0){
					result.add(currentShort);
					shortListStaion = currentShort;
					shortList.remove(indexCurrent);
					tempLongList.clear();
					tempShortList.clear();
				}else{ 
					
					for(int k = 0;k<indexCurrent;k++){
						tempShortList.add(shortList.get(k));
					}
					
					
					//排序tempLongList和tempShortList中的数据，然后将排好循序的列表加在result的resultIndex后面
					List<Station> sortedList = sortStations(tempLongList,tempShortList,longListStation,shortListStaion);
					result.addAll(sortedList);
					result.add(current);
					shortList.removeAll(tempShortList);
					shortList.remove(currentShort);
					
					longListStation = current;
					shortListStaion = currentShort;
					tempLongList.clear();
					tempShortList.clear();
				}
	
			}
		}
		/*if(shortList != null && shortList.size() > 0 ){
			for(int j=0;j<shortList.size();j++){
				result.add(shortList.get(j));
			}
		}
		*/
		
		/**********************************/
		
		return result;
	}
	
	

	
	
	private static List<Station>   sortStations(List<Station> longList,List<Station> shortList,Station longStation,Station shortStation){
		List<Station> result = new LinkedList<Station>();
		if(longList != null && longList.size() > 0){
			String dpttime = longStation.getDptTime();
			for(Station stationLong : longList){
				int minites =  Math.abs(DateUtil.getBetweenMinite(stationLong.getDptTime(), dpttime));
				stationLong.setMinites(minites);
				result.add(stationLong);
			}
		}
		
		if(shortList != null && shortList.size() > 0){
			String dpttime = shortStation.getDptTime();
			for(Station  stationShort : shortList){
				int minites =  Math.abs(DateUtil.getBetweenMinite(stationShort.getDptTime(), dpttime));
				stationShort.setMinites(minites);	
				result.add(stationShort);
			}
		}
		//排序
		ComparatorStation comparator = new ComparatorStation();
		Collections.sort(result, comparator);
		return result;
	}
	
	private static PlanLineGrid getPlanLineGridForAll(List<LinkedList<Station>> list1,String crossStartDate,String crossEndDate){
		//纵坐标
		 List<PlanLineGridY> planLineGridYList = new ArrayList<PlanLineGridY>();
		 //横坐标
		 List<PlanLineGridX> gridXList = new ArrayList<PlanLineGridX>(); 
		 
		 /****组装纵坐标****/
		 int trainSize = list1.size();
		 List<Station> mergeList = new LinkedList<Station>();
		 for(int i = 0;i<trainSize;i++){
			
			if(i==0){
				LinkedList<Station> listStation1 = list1.get(0);
				LinkedList<Station> listStation2 = list1.get(1);
				boolean isSameDirection = StattionUtils.isSameDirection(listStation1, listStation2);
				if(isSameDirection){
					mergeList = StattionUtils.mergeStationTheSameDirection(listStation1, listStation2);
				}else{
					
					if(listStation1.size() >=listStation2.size()){
						mergeList=StattionUtils.mergeStation(listStation1, listStation2);
					}else{
						mergeList=StattionUtils.mergeStation(listStation2, listStation1);
					}
					
				}
			}else if(i>1){
				LinkedList<Station> trainStationCurrentList = list1.get(i);
				boolean isSameDirection = StattionUtils.isSameDirection(trainStationCurrentList, mergeList);
				if(isSameDirection){
					mergeList = StattionUtils.mergeStationTheSameDirection(mergeList, trainStationCurrentList);
				}else{
					if(mergeList.size() >= trainStationCurrentList.size()){
						mergeList=StattionUtils.mergeStation(mergeList, trainStationCurrentList);
					}else{
						mergeList=StattionUtils.mergeStation(trainStationCurrentList, mergeList);	
					}
				}
				
			}
			
		 }
		  
			for(Station station : mergeList){
			    if(station != null){
			    	//planLineGridYList.add(new PlanLineGridY(stationName));
			    	planLineGridYList.add(new PlanLineGridY(
			    			station.getStnName(),
			    			0,
			    			station.getStationType()));
			    }
				
			}
					
			
		 
		/*****组装横坐标  *****/
		
		 LocalDate start = DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(crossStartDate);
	     LocalDate end = new LocalDate(DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(crossEndDate));
	     while(!start.isAfter(end)) {
	            gridXList.add(new PlanLineGridX(start.toString("yyyy-MM-dd")));
	            start = start.plusDays(1);
	        }
	     
		 return new PlanLineGrid(gridXList, planLineGridYList);
	}
	
}

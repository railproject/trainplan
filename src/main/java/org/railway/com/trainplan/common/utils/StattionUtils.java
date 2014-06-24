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
		/******第一种情况:02677-2678/5-2676/7-02678 ***************************/
		/*Station station11 = new Station("桂林北一场","2014-06-24 08:36:00");
		Station station12 = new Station("桂林北二场","2014-06-24 08:40:00");
		Station station13 = new Station("桂林","2014-06-24 08:46:00");
		
		LinkedList<Station>  trainList1 = new LinkedList<Station>();
		trainList1.add(station11);
		trainList1.add(station12);
		trainList1.add(station13);
		
		Station station21 = new Station("桂林","2014-06-24 09:25:00");
		Station station22 = new Station("桂林北二场","2014-06-24 09:32:00");
		Station station23 = new Station("桂林北一场","2014-06-24 09:34:00");
		Station station24 = new Station("灵川","2014-06-24 09:39:00");
		Station station26 = new Station("井山","2014-06-24 18:22:00");
		Station station27 = new Station("钟山","2014-06-24 18:41:00");
		Station station28 = new Station("贺州","2014-06-24 18:57:00");
		
		LinkedList<Station>  trainList2 = new LinkedList<Station>();
		trainList2.add(station21);
		trainList2.add(station22);
		trainList2.add(station23);
		trainList2.add(station24);
		trainList2.add(station26);
		trainList2.add(station27);
		trainList2.add(station28);
		
		
		Station station31 = new Station("贺州","2014-06-25 08:00:00");
		Station station32 = new Station("钟山","2014-06-25 08:18:00");
		Station station33 = new Station("井山","2014-06-25 08:32:00");
		Station station34 = new Station("灵川","2014-06-25 16:40:00");
		Station station35 = new Station("桂林北一场","2014-06-25 17:10:00");
		Station station36 = new Station("桂林北二场","2014-06-25 17:14:00");
		Station station37 = new Station("桂林","2014-06-25 17:20:00");
		
		LinkedList<Station>  trainList3 = new LinkedList<Station>();
		trainList3.add(station31);
		trainList3.add(station32);
		trainList3.add(station33);
		trainList3.add(station34);
		trainList3.add(station35);
		trainList3.add(station36);
		trainList3.add(station37);
		
		Station station41 = new Station("桂林","2014-06-25 18:12:00");
		Station station42 = new Station("桂林北二场","2014-06-25 18:19:00");
		Station station43 = new Station("桂林北一场","2014-06-25 18:22:00");
	
		LinkedList<Station>  trainList4 = new LinkedList<Station>();
		trainList4.add(station41);
		trainList4.add(station42);
		trainList4.add(station43);
		
		List<LinkedList<Station>> list1 = new ArrayList<LinkedList<Station>>();
		list1.add(trainList1);
		list1.add(trainList2);
		list1.add(trainList3);
		list1.add(trainList4);*/
		
		
		/******第二种情况05505-5505-5506-05506   **************/
		Station station11 = new Station("桂林北一场","2014-06-24 08:15:00");
		Station station12 = new Station("桂林北二场","2014-06-24 08:19:00");
		Station station13 = new Station("桂林","2014-06-24 08:25:00");
		
		LinkedList<Station>  trainList1 = new LinkedList<Station>();
		trainList1.add(station11);
		trainList1.add(station12);
		trainList1.add(station13);
		
		Station station21 = new Station("桂林","2014-06-24 09:10:00");
		Station station22 = new Station("二塘","2014-06-24 09:19:00");
		Station station23 = new Station("横山","2014-06-24 09:27:00");
		Station station24 = new Station("大溪河","2014-06-24 09:33:00");
		Station station26 = new Station("塘堡","2014-06-24 09:38:00");
		Station station27 = new Station("化州","2014-06-24 19:31:00");
		Station station28 = new Station("山底岭","2014-06-24 20:07:00");
		Station station29 = new Station("茂名","2014-06-24 20:20:00");
		
		LinkedList<Station>  trainList2 = new LinkedList<Station>();
		trainList2.add(station21);
		trainList2.add(station22);
		trainList2.add(station23);
		trainList2.add(station24);
		trainList2.add(station26);
		trainList2.add(station27);
		trainList2.add(station28);
		trainList2.add(station29);
		
		
		Station station31 = new Station("茂名","2014-06-25 08:45:00");
		Station station32 = new Station("山底岭","2014-06-25 08:57:00");
		Station station33 = new Station("化州","2014-06-25 09:09:00");
		Station station34 = new Station("塘堡","2014-06-25 18:34:00");
		Station station35 = new Station("大溪河","2014-06-25 18:42:00");
		Station station36 = new Station("横山","2014-06-25 19:06:00");
		Station station37 = new Station("二塘","2014-06-25 19:15:00");
		Station station38 = new Station("桂林","2014-06-25 19:25:00");
		
		LinkedList<Station>  trainList3 = new LinkedList<Station>();
		trainList3.add(station31);
		trainList3.add(station32);
		trainList3.add(station33);
		trainList3.add(station34);
		trainList3.add(station35);
		trainList3.add(station36);
		trainList3.add(station37);
		trainList3.add(station38);
		
		
		Station station41 = new Station("桂林","2014-06-25 20:02:00");
		Station station42 = new Station("桂林北二场","2014-06-25 20:09:00");
		Station station43 = new Station("桂林北一场","2014-06-25 20:12:00");
	
		LinkedList<Station>  trainList4 = new LinkedList<Station>();
		trainList4.add(station41);
		trainList4.add(station42);
		trainList4.add(station43);
		
		List<LinkedList<Station>> list1 = new ArrayList<LinkedList<Station>>();
		list1.add(trainList1);
		list1.add(trainList2);
		list1.add(trainList3);
		list1.add(trainList4);
		
		
		
		
		PlanLineGrid grid = getPlanLineGridForAll(list1,"2014-06-23","2014-06-25");
		List<PlanLineGridY> yList = grid.getCrossStns();
		for(PlanLineGridY y : yList ){
			System.err.println("" + y.getStnName() );
		}
		
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
					if(tempLongList !=null && tempLongList.size() > 0){
						result.addAll(tempLongList);
					}
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
		if(tempLongList != null && tempLongList.size() > 0 ){
			result.addAll(tempLongList);
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

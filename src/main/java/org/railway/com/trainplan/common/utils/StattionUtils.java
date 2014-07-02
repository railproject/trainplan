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
		/*Station station11 = new Station("桂林北一场","2014-06-24 08:15:00");
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
		list1.add(trainList4);*/
		
		
		/**
		 * *****************************
齐齐哈尔|2014-06-24 17:44:00|0
齐齐哈尔南场|2014-06-24 17:48:30|BT
大民屯|2014-06-24 17:53:30|BT
榆树屯|2014-06-24 18:09:00|TZ
三间房东场|2014-06-24 18:19:00|BT
辽中|2014-06-25 05:37:00|BT
台安|2014-06-25 05:54:00|BT
盘锦北|2014-06-25 06:26:00|TZ
锦州南|2014-06-25 07:04:00|BT
葫芦岛北|2014-06-25 07:35:00|TZ
绥中北|2014-06-25 08:14:00|BT
东戴河|2014-06-25 08:47:00|BT
龙家营线路所|2014-06-25 09:16:00|BT
龙家营西咽喉|2014-06-25 09:20:00|BT
天津四号楼|2014-06-25 13:51:30|BT
天津普速场|2014-06-25 14:01:00|0
*****************************
天津普速场|2014-06-25 15:51:00|0
天津四号楼|2014-06-25 15:59:00|BT
龙家营|2014-06-25 19:31:00|BT
山海关运转场|2014-06-25 20:00:00|BT
万家屯|2014-06-25 20:09:00|BT
葫芦岛|2014-06-25 22:03:00|TZ
锦州|2014-06-25 22:49:00|TZ
锦州上直通场|2014-06-25 22:54:00|BT
大成|2014-06-26 02:14:00|BT
三间房西场|2014-06-26 12:59:00|TZ
榆树屯|2014-06-26 13:51:00|TZ
大民屯|2014-06-26 14:01:00|BT
齐齐哈尔南场|2014-06-26 14:05:00|BT
齐齐哈尔|2014-06-26 14:11:00|0
*****************************
齐齐哈尔|2014-06-26 16:42:00|0
齐齐哈尔南场|2014-06-26 16:48:30|BT
上苏北|2014-06-27 04:41:00|BT
沙河口|2014-06-27 09:49:00|BT
大连|2014-06-27 09:55:00|0
*****************************
大连|2014-06-27 14:29:00|0
沙河口|2014-06-27 14:35:00|BT
苏家屯下到场|2014-06-27 19:27:00|BT
下苏北|2014-06-27 19:29:00|BT
齐齐哈尔南场|2014-06-28 07:05:00|BT
齐齐哈尔|2014-06-28 07:12:00|0
		 */
		
		/*********第三种情况：1052-1051-2210-2209************/
		
		Station station11 = new Station("齐齐哈尔","2014-06-24 17:44:00");
		Station station12 = new Station("齐齐哈尔南场","2014-06-24 17:48:30");
		Station station13 = new Station("大民屯","2014-06-24 17:53:30");
		Station station14 = new Station("榆树屯","2014-06-24 18:09:00");
		Station station15 = new Station("三间房东场","2014-06-24 18:19:00");
		Station station16 = new Station("辽中","2014-06-25 05:37:00");
		Station station17 = new Station("台安","2014-06-25 05:54:00");
		Station station18 = new Station("盘锦北","2014-06-25 06:26:00");
		Station station19 = new Station("锦州南","2014-06-25 07:04:00");
		Station station110 = new Station("葫芦岛北","2014-06-25 07:35:00");
		Station station111 = new Station("绥中北","2014-06-25 08:14:00");
		Station station112 = new Station("东戴河","2014-06-25 08:47:00");
		Station station113 = new Station("龙家营线路所","2014-06-25 09:16:00");
		Station station114 = new Station("龙家营西咽喉","2014-06-25 09:20:00");
		Station station115 = new Station("天津四号楼","2014-06-25 13:51:30");
		Station station116 = new Station("天津普速场","2014-06-25 14:01:00");
	
		
		
		LinkedList<Station>  trainList1 = new LinkedList<Station>();
		trainList1.add(station11);
		trainList1.add(station12);
		trainList1.add(station13);
		trainList1.add(station14);
		trainList1.add(station15);
		trainList1.add(station16);
		trainList1.add(station17);
		trainList1.add(station18);
		trainList1.add(station19);
		trainList1.add(station110);
		trainList1.add(station111);
		trainList1.add(station112);
		trainList1.add(station113);
		trainList1.add(station114);
		trainList1.add(station115);
		trainList1.add(station116);
		
		
		Station station21 = new Station("天津普速场","2014-06-25 15:51:00");
		Station station22 = new Station("天津四号楼","2014-06-25 15:59:00");
		Station station23 = new Station("龙家营","2014-06-25 19:31:00");
		Station station24 = new Station("山海关运转场","2014-06-25 20:00:00");
		Station station26 = new Station("万家屯","2014-06-25 20:09:00");
		Station station27 = new Station("葫芦岛","2014-06-25 22:03:00");
		Station station28 = new Station("锦州","2014-06-25 22:49:00");
		Station station29 = new Station("锦州上直通场","2014-06-25 22:54:00");
		Station station210 = new Station("大成","2014-06-26 02:14:00");
		Station station211 = new Station("三间房西场","2014-06-26 12:59:00");
		Station station212 = new Station("榆树屯","2014-06-26 13:51:00");
		Station station213 = new Station("大民屯","2014-06-26 14:01:00");
		Station station214 = new Station("齐齐哈尔南场","2014-06-26 14:05:00");
		Station station215 = new Station("齐齐哈尔","2014-06-26 14:11:00");
	
		LinkedList<Station>  trainList2 = new LinkedList<Station>();
		trainList2.add(station21);
		trainList2.add(station22);
		trainList2.add(station23);
		trainList2.add(station24);
		trainList2.add(station26);
		trainList2.add(station27);
		trainList2.add(station28);
		trainList2.add(station29);
		trainList2.add(station210);
		trainList2.add(station211);
		trainList2.add(station212);
		trainList2.add(station213);
		trainList2.add(station214);
		trainList2.add(station215);
		
		
		Station station31 = new Station("齐齐哈尔","2014-06-26 16:42:00");
		Station station32 = new Station("齐齐哈尔南场","2014-06-26 16:48:30");
		Station station33 = new Station("上苏北","2014-06-27 04:41:00");
		Station station34 = new Station("沙河口","2014-06-27 09:49:00");
		Station station35 = new Station("大连","2014-06-27 09:55:00");
		
		
		LinkedList<Station>  trainList3 = new LinkedList<Station>();
		trainList3.add(station31);
		trainList3.add(station32);
		trainList3.add(station33);
		trainList3.add(station34);
		trainList3.add(station35);
	
		
		
		Station station41 = new Station("大连","2014-06-27 14:29:00");
		Station station42 = new Station("沙河口","2014-06-27 14:35:00");
		Station station43 = new Station("苏家屯下到场","2014-06-27 19:27:00");
		Station station44 = new Station("下苏北","2014-06-27 19:29:00");
		Station station45 = new Station("齐齐哈尔南场","2014-06-28 07:05:00");
		Station station46 = new Station("齐齐哈尔","2014-06-28 07:12:00");
	
	
		LinkedList<Station>  trainList4 = new LinkedList<Station>();
		trainList4.add(station41);
		trainList4.add(station42);
		trainList4.add(station43);
		trainList4.add(station44);
		trainList4.add(station45);
		trainList4.add(station46);
	
		
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
		//int count =1;
		for(int i = 0;i<longList.size();i++){
			Station current = longList.get(i);
			
			//System.err.println(count+"---" + current.getStnName()+"|"+current.getDptTime());
			//count++;
			if(!shortList.contains(current) ){
				if(result.size() != 0){
					resultIndex = 	result.size() -1 ;
					
				}
			
				tempLongList.add(current);
			}else{
				boolean isRevert = false;
				int indexCurrent = shortList.indexOf(current);
				
				//在shortList中的当前被匹配到的元素
				Station currentShort = shortList.get(indexCurrent);
				if(tempLongList.size()==0){
					longListStation = current;
				}else{
					longListStation = current;
					isRevert = true;
				}
				
				
				if(tempShortList.size()==0){
					shortListStaion = currentShort;
				}
				//如果shortList中的第一个元素在longList中存在
				if(indexCurrent == 0){
					if(tempLongList !=null && tempLongList.size() > 0){
						result.addAll(tempLongList);
					}
					String stationTypeCurrent = current.getStationType();
					String stationTypeCurrentShort = currentShort.getStationType();
					//0：表示始发站和终到站
					if(!"0".equals(stationTypeCurrent) && "0".equals(stationTypeCurrentShort)){
						result.add(currentShort);
					}else {
						result.add(current);	
					}
					
					//print(result);
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
					if(isRevert){
						Collections.reverse(sortedList);
					}
					result.addAll(sortedList);
					result.add(current);
					shortList.removeAll(tempShortList);
					shortList.remove(currentShort);
					
					longListStation = current;
					shortListStaion = currentShort;
					tempLongList.clear();
					tempShortList.clear();
					//print(result);
				}
	
			}
	
		}
		if(tempLongList != null && tempLongList.size() > 0 ){
			result.addAll(tempLongList);
			//print(result);
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
	
	

	private static void print(List<Station> result){
		if(result != null && result.size()>0){
			List<Station> testResult = new LinkedList<Station>();
			//Collections.copy(testResult, result);
			for(Station station :result){
				testResult.add(station);
			}
			Collections.reverse(testResult);
			for(Station station : testResult){
				System.err.println(""+station.getStnName()+"|" + station.getDptTime() + "**");
			}	
		}
		
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
	
	public static PlanLineGrid getPlanLineGridForAll(List<LinkedList<Station>> list1,String crossStartDate,String crossEndDate){
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
			    			0,station.getDptTime(),
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

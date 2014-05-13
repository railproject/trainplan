package org.railway.com.trainplan.service;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.utils.ExcelUtil;
import org.railway.com.trainplan.entity.CrossInfo;
import org.railway.com.trainplan.entity.CrossTrainInfo;
import org.railway.com.trainplan.entity.Ljzd;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
 
@Service
public class CrossService{
	private static final Logger logger = Logger.getLogger(CommonService.class);
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private BaseDao baseDao;
	
	public static void main(String[] args) throws IOException, IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		InputStream is = new FileInputStream(
				"C:\\Users\\Administrator\\Desktop\\work\\交路相关\\对数表模板1.xls");
		
		CrossService a = new CrossService();
		a.actionExcel(is); 
//		System.out.println("G11(".substring(0,"G11(".indexOf('(')));
	}

	/**
	 * 查询cross信息
	 * @param reqMap
	 * @return
	 */
	public List<CrossInfo>  getCrossInfo(Map<String,Object> reqMap){
		List<CrossInfo>  list = baseDao.selectListBySql(Constants.CROSSDAO_GET_CROSS_INFO, reqMap);
		return list;
	}
	
	/**
	 * 通过crossid查询crossinfo信息
	 * @param crossId
	 * @return
	 */
	public CrossInfo getCrossInfoForCrossid(String crossId){
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("crossId", crossId);
		return (CrossInfo)baseDao.selectOneBySql(Constants.CROSSDAO_GET_CROSS_INFO_FOR_CROSSID, paramMap);
	    
	}
	
	/**
	 * 通过crossid查询crosstrainInfo信息
	 * @param crossId
	 * @return
	 */
	public List<CrossTrainInfo> getCrossTrainInfoForCrossid(String crossId){
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("crossId", crossId);
		return  baseDao.selectListBySql(Constants.CROSSDAO_GET_CROSS_TRAIN_INFO_FOR_CROSSID, paramMap);
	}
	
	
	public void actionExcel(InputStream inputStream) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// TODO Auto-generated method stub
		LinkedHashMap<String, String> pm = new LinkedHashMap<String, String>();
		pm.put("crossId", "");
		pm.put("crossName", "");
		pm.put("crossSpareName", "");
		pm.put("alterNateDate", "");
		pm.put("alterNateTranNbr", "");
		pm.put("spareFlag", ""); 
		pm.put("cutOld", ""); 
		pm.put("groupTotalNbr", "");
		pm.put("pairNbr", ""); 
		pm.put("highlineFlag", ""); 
		pm.put("highlineRule", "");
		pm.put("commonlineRule", "");
		pm.put("appointWeek", "");  
		pm.put("appointDay", ""); 
		pm.put("crossSection", ""); 
		pm.put("throughline", "");  
		pm.put("startBureau", "");  
		pm.put("tokenVehDept", ""); 
		pm.put("tokenVehDepot", ""); 
		pm.put("tokenPsgBureau", "");  
		pm.put("tokenPsgDept", "");  
		pm.put("locoType", "");
		pm.put("crhType", ""); 
		pm.put("elecSupply", "");
		pm.put("dejCollect", "");
		pm.put("airCondition", "");
		pm.put("note", "");
		
		
		Map<String,  Map<String, String>> valuesMap = new HashMap<String, Map<String, String>>();
		
		//可以从数据库中获取
		Map<String, String> tokenPsgDeptValuesMap = new HashMap<String, String>();
		//路局字典信息
		List<Ljzd> lizdList = commonService.getFullStationInfo();
		if(lizdList !=null && lizdList.size() > 0){
			for(Ljzd dto : lizdList){
				tokenPsgDeptValuesMap.put(dto.getLjpym(), dto.getLjjc());
			}
		}
		System.err.println("tokenPsgDeptValuesMap==" + tokenPsgDeptValuesMap);
		valuesMap.put("tokenPsgBureau", tokenPsgDeptValuesMap); 
		 
		
		// TODO Auto-generated method stub
		try{
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			int num = workbook.getNumberOfSheets();		
			List<CrossInfo> alllist = new ArrayList<CrossInfo>();
			for(int i = 0; i < num; i++){
				HSSFSheet sheet = workbook.getSheetAt(i);
				ExcelUtil<CrossInfo> test = new ExcelUtil<CrossInfo>(pm, sheet, CrossInfo.class);
				test.setValueMapping(valuesMap);
				List<CrossInfo> list = test.getEntities(-1);
				alllist.addAll(list);  
			}
			
			ExecutorService service=Executors.newFixedThreadPool(10);
			CompletionService<List<CrossTrainInfo>> completion=new ExecutorCompletionService<List<CrossTrainInfo>>(service);
			 
			ArrayList<CrossTrainInfo> crossTrains = new ArrayList<CrossTrainInfo>();
			
			for(int i = 0; i < alllist.size(); i++){
				completion.submit(new CrossCompletionService(alllist.get(i)));
			}
			
			for(int i = 0; i < alllist.size(); i++){
				try {
					crossTrains.addAll(completion.take().get());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
			service.shutdown();
			
			//fortest
			BeanInfo  beaninfo = Introspector.getBeanInfo(CrossInfo.class);
			 
			PropertyDescriptor[] pds = beaninfo.getPropertyDescriptors();
			for(CrossInfo cross: alllist){
				for(String key : pm.keySet()){
					for(int i = 0; i < pds.length; i++){
						PropertyDescriptor pd = pds[i];
						String propertyName = pd.getName();  
						if(key.equals(propertyName)){
							//Method method = pd.getReadMethod();
							//System.out.print(propertyName + "=" + method.invoke(cross, null) + ",");
							break; 
						}
					}
				}
				System.out.println();
			}
			 
			beaninfo = Introspector.getBeanInfo(CrossTrainInfo.class);
//			 
			pds = beaninfo.getPropertyDescriptors();
			for(CrossTrainInfo cross: crossTrains){
				for(String key : pm.keySet()){
					for(int i = 0; i < pds.length; i++){
						
						PropertyDescriptor pd = pds[i];
						Method method = pd.getReadMethod();
						//System.out.print(pd.getName() + "=" + method.invoke(cross, null) + ",");
//						String propertyName = pd.getName();  
//						if(key.equals(propertyName)){
//							Method method = pd.getReadMethod();
//							System.out.print(propertyName + "=" + method.invoke(cross, null) + ",");
//							break; 
//						}
					}
				}
				logger.debug("");
			}
			
			////////////////////////
			
			if(alllist != null && alllist.size() > 0){
				//保存交路信息
				baseDao.insertBySql(Constants.CROSSDAO_ADD_CROSS_INFO,alllist);
				
			}
        	//保存列车
			if(crossTrains != null && crossTrains.size() > 0 ){
				baseDao.insertBySql(Constants.CROSSDAO_ADD_CROSS_TRAIN_INFO, crossTrains);
			}
	
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		
	} 

	
	private void setDayGapForTrains(LinkedList<CrossTrainInfo> crossTrains){
		for(int i = 0; i < crossTrains.size(); i++){
			 
			setDayGap(crossTrains.get(i), crossTrains.get(i - 1 < 0 ? crossTrains.size() - 1 : i - 1));
			 
		}
	} 
	/**
	 * 计算列车间隔时间
	 * @param train1 需要设置间隔时间的列车
	 * @param train2 前置列车
	 */
	private void setDayGap(CrossTrainInfo train1, CrossTrainInfo train2){ 
		try { 
			logger.debug(train1.getSourceTargetTime());
			logger.debug(train1.getSourceTargetTime() + "-" + train1.getSourceTargetTime().substring(train1.getSourceTargetTime().indexOf(":") + 1));
			Date sourceTime = format.parse("1977-01-01 " + train1.getSourceTargetTime().substring(train1.getSourceTargetTime().indexOf(":") + 1));
			Date targetTime = train2 != null ?  format.parse("1977-01-01 " + train2.getTargetTime().substring(train2.getTargetTime().indexOf(":") + 1)) :  format.parse("1977-01-01 " + train1.getTargetTime().substring(train1.getTargetTime().indexOf(":") + 1));
			if(sourceTime.compareTo(targetTime) < 0){
				logger.debug(train1.getSourceTargetTime() + "----------------------------------123123----------------------------------" + train2.getTargetTime());
				train1.setDayGap(1);
			}else{
				logger.debug(train1.getSourceTargetTime() + "----------------------------------123123----------------------------------" + train2.getTargetTime());
				train1.setDayGap(0);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		 
	}
	




	/**
	 * 用于并行处理交路
	 * @author Administrator
	 *
	 */
	class CrossCompletionService implements Callable<List<CrossTrainInfo>> {
		
		/**
		 * 并行处理列车基本信息
		 * @author Administrator
		 *
		 */
		class GetTrainInfoCompletionService implements Callable<CrossTrainInfo> {
			
			private CrossTrainInfo train;
			
			public GetTrainInfoCompletionService(CrossTrainInfo train ){
			   this.train = train;
			}
			
			public CrossTrainInfo call() throws Exception {
				trainInfoFromPain(this.train);
			    return this.train;
			}
			
			private void trainInfoFromPain(CrossTrainInfo train){
				String result = "{\"code\":\"201\",\"name\":null,\"dataSize\":1,\"data\":[{\"id\":\"942be8cd-7df1-42c9-a5e0-3aaa82561a97\",\"name\":\"G11\",\"pinyinCode\":null,\"description\":null,\"versionDto\":null,\"state\":\"SYNCHRONIZED\",\"index\":0,\"resourceId\":\"7eeb336a-f024-4ec6-bf55-5fee5bd00c97\",\"resourceName\":\"基础资料\",\"typeId\":\"0019cb5f-509a-42f5-afc4-e43e4a7eafc0\",\"typeName\":\"高速动车组旅客列车\",\"sourceNodeId\":\"fc4812cc-6659-4556-8f34-e933bf3a1b33\",\"sourceNodeName\":\"北京南高速场\",\"targetNodeId\":null,\"targetNodeName\":null,\"sourceTime\":null,\"sourceTime1\":null,\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"targetTime\":null,\"targetTime1\":null,\"dataSourceDto\":{\"source\":\"southwest\",\"id\":\"0336fdb6-c008-45da-bff2-3ba405e65b29\",\"name\":null,\"handleTime\":null,\"manager\":null},\"scheduleDto\":{\"sourceItemDto\":{\"id\":\"09f5fcf2-b83b-4c5f-aae0-603134482d48\",\"name\":\"北京南高速场\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":0,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"fc4812cc-6659-4556-8f34-e933bf3a1b33\",\"nodeName\":\"北京南高速场\",\"trackName\":\"10\",\"sourceTimeDto2\":\"0:8:0:0\",\"targetTimeDto2\":\"0:8:0:0\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"timeDto\":null,\"timeDto1\":{\"day\":0,\"hour\":8,\"minute\":0,\"second\":0}},\"routeItemDtos\":[{\"id\":\"1c0814fa-2d67-47d9-93d2-68b1f319afcd\",\"name\":\"津沪线路所\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":3,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"e57ac46a-8cf2-41f2-b616-48148b835da1\",\"nodeName\":\"津沪线路所\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:29:48\",\"targetTimeDto2\":\"0:8:29:48\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":29,\"second\":48},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":29,\"second\":48}},{\"id\":\"8fe3ec66-b042-4a14-a9cc-79ca0c0bc8ad\",\"name\":\"济南西\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":7,\"bureauId\":\"d03a250a-b06a-425f-83f2-28f4314f5623\",\"bureauName\":\"济南铁路局\",\"nodeId\":\"59bfd98e-a895-44b6-a879-088e09798a9b\",\"nodeName\":\"济南西\",\"trackName\":\"3\",\"sourceTimeDto2\":\"0:9:32:10\",\"targetTimeDto2\":\"0:9:34:10\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":9,\"minute\":32,\"second\":10},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":9,\"minute\":34,\"second\":10}},{\"id\":\"aad1ed10-ea06-449c-a809-1aaca9a64457\",\"name\":\"廊坊\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":1,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"4493a648-e2ee-4fd8-8b8c-c49fe8dc1c3e\",\"nodeName\":\"廊坊\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:18:20\",\"targetTimeDto2\":\"0:8:18:20\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":18,\"second\":20},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":18,\"second\":20}},{\"id\":\"3863a9b4-d17c-45d8-887a-bfb1e5bca91a\",\"name\":\"德州东\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":6,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"da2c01a8-5593-4c53-b2af-f9a465cb5245\",\"nodeName\":\"德州东\",\"trackName\":\"5\",\"sourceTimeDto2\":\"0:9:10:2\",\"targetTimeDto2\":\"0:9:10:2\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":9,\"minute\":10,\"second\":2},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":9,\"minute\":10,\"second\":2}},{\"id\":\"34f24822-8037-4ee2-a295-7ffd2fbf6a39\",\"name\":\"天津南\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":4,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"e6a4321b-ed5c-44dc-a10d-2117fde8c937\",\"nodeName\":\"天津南\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:31:5\",\"targetTimeDto2\":\"0:8:31:5\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":31,\"second\":5},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":31,\"second\":5}},{\"id\":\"f9a92718-0d07-409e-b6e8-82843bd4b906\",\"name\":\"沧州西\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":5,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"601f327b-54db-4b96-8c18-b724e2aaac98\",\"nodeName\":\"沧州西\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:48:56\",\"targetTimeDto2\":\"0:8:48:56\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":48,\"second\":56},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":48,\"second\":56}},{\"id\":\"6d1e8b33-7e00-49be-9e72-bc343538d627\",\"name\":\"京津线路所\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":2,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"35f4333a-12e4-48ca-b8f3-6b1665b22887\",\"nodeName\":\"京津线路所\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:28:13\",\"targetTimeDto2\":\"0:8:28:13\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":28,\"second\":13},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":28,\"second\":13}}],\"targetItemDto\":{\"id\":\"09f5fcf2-b83b-4c5f-aae0-603134482d48\",\"name\":\"北京南高速场\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":0,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"fc4812cc-6659-4556-8f34-e933bf3a1b33\",\"nodeName\":\"北京南高速场\",\"trackName\":\"10\",\"sourceTimeDto2\":\"0:8:0:0\",\"targetTimeDto2\":\"0:6:0:0\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"timeDto\":null,\"timeDto1\":{\"day\":0,\"hour\":8,\"minute\":0,\"second\":0}}},\"trainlineWorkDto\":null,\"routeDto\":{\"directionalRailwayLineSegmentSiteDtos\":[{\"id\":\"9898d998-a1fc-4525-86a0-511668b6a019\",\"name\":\"铁路线[京沪高速下行]-[北京南高速场-济南西]区段\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":0,\"railwayLineId\":\"7354bd2d-cc29-4e2a-a73f-da21a274a9eb\",\"railwayLineName\":\"京沪高速\",\"directionalRailwayLineId\":\"06103c4d-28c2-47fb-9a28-7dccc1d24263\",\"directionalRailwayLineName\":\"京沪高速下行\",\"sourceSegmentId\":\"d97b6821-62b8-47a6-a963-1bf6258e6538\",\"sourceSegmentName\":\"北京南高速场\",\"targetSegmentId\":\"2d219a35-9ee4-4cd2-850e-722e6e39d16e\",\"targetSegmentName\":\"济南西\"}],\"directionalRailwayLineKilometerMarkSiteDtos\":[],\"lineSiteDtos\":[],\"nodeSiteDtos\":[]},\"schemeId\":\"0336fdb6-c008-45da-bff2-3ba405e65b29\",\"schemeName\":\"测试3-京沪高铁-30列\",\"vehicleCycleId\":null}],\"exceptionSize\":0,\"exceptions\":[]}";
				//http://10.1.191.135:7003/rail/template/TrainlineTemplates?name=G11 
				try {
					Client client = Client.create();
					
					client.setConnectTimeout(60*1000);
					String trainNbr = train.getTrainNbr();
					String stn = null;
					if(trainNbr.indexOf("(") >= 0){
						trainNbr = trainNbr.substring(0, trainNbr.indexOf("(")).trim();
						
						stn = trainNbr.substring(trainNbr.indexOf("(") + 1, trainNbr.indexOf(")")).trim();
					}
					WebResource webResource = client.resource("http://10.1.191.135:7003/rail/template/TrainlineTemplates?name=" + trainNbr); 
					
//					webResource.method(this.method, GenericType)
					
					ClientResponse response = webResource.type("application/json")
							.accept("application/json").method("GET", ClientResponse.class); 
				    
					//将返回结果转换为指定对象 
					result = response.getEntity(String.class);  
					 logger.debug(result);
					JSONArray obj = JSONObject.fromObject(result).getJSONArray("data");
					if(obj.size() > 0){
						
						JSONObject curTrain = null;
						JSONObject scheduleDto = null;
						JSONObject sourceItemDto = null;
						JSONObject targetItemDto = null;   
						
						//findFlag
						boolean findFlag = false;
						for(int i = 0; i < obj.size(); i++){ 
							curTrain = obj.getJSONObject(i);
							scheduleDto = curTrain.getJSONObject("scheduleDto");
							sourceItemDto = scheduleDto.getJSONObject("sourceItemDto");
							targetItemDto = scheduleDto.getJSONObject("targetItemDto"); 
							//如果车次中有站名，如果不匹配就返回
							if(stn != null){  
								if(stn.equals(sourceItemDto.getString("nodeName")) 
												|| stn.equals(targetItemDto.getString("nodeName"))){
									findFlag = true;
									break;
								}
							}else{//如果没有站名做标示就取第一个列车，应该只有一辆吧 
								if(obj.size() > 1){
									logger.error(trainNbr + ",有两辆列车从计划平台陪查询出来，默认取了第一辆");
								}
								findFlag = true;
								break;
							}
						} 
						if(findFlag){
							//设置始发时间
							train.setSourceTargetTime(sourceItemDto.getString("sourceTimeDto2"));
							//设置终到时间
							train.setTargetTime(targetItemDto == null ? null : targetItemDto.getString("targetTimeDto2"));
							//设置列车在计划平台ID
							train.setBaseTrainId(curTrain.getString("id"));
							//设置始局
							train.setStartBureau(sourceItemDto.getString("bureauName"));
							//设置始发站
							train.setStartStn(sourceItemDto.getString("nodeName"));
							//设置终到局
							train.setEndBureau(targetItemDto.getString("bureauName"));
							//设置中档站
							train.setEndStn(targetItemDto.getString("nodeName"));
						}
					}
					//间隔天数
					 
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}   
			} 
		}
			
		private CrossInfo cross;
		
		public CrossCompletionService(CrossInfo cross ){
		   this.cross = cross;
		}
		
		public List<CrossTrainInfo> call() throws Exception {
			 
		    return this.createTrainsForCross(this.cross);
		}
		
		private LinkedList<CrossTrainInfo> createTrainsForCross(CrossInfo cross){
			logger.debug("");
			String crossName = cross.getCrossName();
			String[] crossSpareNames = cross.getCrossSpareName() == null ? null : cross.getCrossSpareName().split("-");
			String[] alertNateTrains = cross.getAlterNateTranNbr() == null ? null : cross.getAlterNateTranNbr().split("-");
			String[] alertNaateDate = cross.getAlterNateDate() == null ? null : cross.getAlterNateDate().split("-");
			String[] spareFlag = cross.getSpareFlag() == null ? null : cross.getSpareFlag().split("-");
			String[] trains = crossName.split("-");
			LinkedList<CrossTrainInfo> crossTrains = new LinkedList<CrossTrainInfo>();
			CrossTrainInfo train = null;
			for(int i = 0; i < trains.length; i++){
				train = new CrossTrainInfo();
				train.setTrainSort(i);
				train.setCrossId(cross.getCrossId());
				train.setTrainNbr(trains[i]); 
				//
				if(alertNateTrains != null){
					train.setAlertNateTrainNbr(alertNateTrains[i]);
				}
				//
				if(alertNaateDate != null){
					train.setAlertNateTime(alertNaateDate[i] + " 02:00:00");
				}
				//
				if(spareFlag != null){
					if(spareFlag.length == 1){
						train.setSpareFlag(Integer.parseInt(spareFlag[0]));
					}else{
						train.setSpareFlag(Integer.parseInt(spareFlag[i]));
					}
				}  
				crossTrains.add(train);
			}
			
		   
		   ExecutorService service=Executors.newCachedThreadPool();
		   CompletionService<CrossTrainInfo> completion=new ExecutorCompletionService<CrossTrainInfo>(service);
		   
		   for(int i=0; i < crossTrains.size(); i++){
			   completion.submit(new GetTrainInfoCompletionService(crossTrains.get(i)));
		   }
		   for(int i=0;i < crossTrains.size();i++){
			   try {
				CrossTrainInfo crossTrain = completion.take().get();
				if(cross.getCrossName().startsWith(crossTrain.getTrainNbr())){
					cross.setStartBureau(crossTrain.getStartBureau());
					cross.setCrossStartDate(crossTrain.getSourceTargetTime());
				}
				if(cross.getCrossName().endsWith(crossTrain.getTrainNbr())){ 
					cross.setCrossEndDate(crossTrain.getTargetTime());
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   }
		   service.shutdown();
				 
		   setDayGapForTrains(crossTrains); 
			
			
		LinkedList<CrossTrainInfo> crossSpareTrains = new LinkedList<CrossTrainInfo>(); 
		if(crossSpareNames != null){
			for(int i = 0; i < crossSpareNames.length; i++){
				train = new CrossTrainInfo();
				train.setCrossId(cross.getCrossId());
				train.setTrainSort(crossTrains.size() + i);
				train.setTrainNbr(crossSpareNames[i]); 
				train.setSpareApplyFlage(1); 
				//
				if(alertNateTrains != null){
					train.setAlertNateTrainNbr(alertNateTrains[i]);
				}
				//
				if(alertNaateDate != null){
					train.setAlertNateTime(alertNaateDate[i] + " 02:00:00");
				}
				//
				if(spareFlag != null){
					if(spareFlag.length == 1){
						train.setSpareFlag(Integer.parseInt(spareFlag[0]));
					}else{
						train.setSpareFlag(Integer.parseInt(spareFlag[i]));
					}
				}  
				crossSpareTrains.add(train);
			} 
			
		   completion=new ExecutorCompletionService<CrossTrainInfo>(service); 
		   for(int i=0; i < crossSpareTrains.size(); i++){
			   completion.submit(new GetTrainInfoCompletionService(crossSpareTrains.get(i)));
		   }
		   for(int i=0;i < crossTrains.size();i++){
			   try {
				completion.take().get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   }
		   service.shutdown();
			 
		   setDayGapForTrains(crossSpareTrains);
			
		   crossTrains.addAll(crossSpareTrains);
		}
			 
//			String trains = crossName.split("-");
		logger.debug(this.cross.getCrossName() + "==crossTrains=" + crossTrains.size());
		
		return crossTrains; 
	}
		
		
	
	}
}

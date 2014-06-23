package org.railway.com.trainplan.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.Ljzd;
import org.railway.com.trainplan.entity.TrainType;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.repository.mybatis.LjzdMybatisDao;
import org.railway.com.trainplan.service.dto.ParamDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;




/**
 * 基本的service业务功能
 * @author join
 *
 */
@Component
@Transactional
@Monitored
public class CommonService {
	private static final Logger logger = Logger.getLogger(CommonService.class);
	
	private static Map<String, Ljzd> map = new HashMap<String, Ljzd>(); 
	private static Map<String, String> jcMap = new HashMap<String, String>();
	
	@Autowired
	private LjzdMybatisDao ljzdDao;
	
	@Autowired
	private BaseDao baseDao;
	/**
	 * 通过路局全称查询路基基本信息
	 */
	public Ljzd getLjInfo(String ljqc) {
		if(map.get(ljqc) != null){
			return map.get(ljqc);
		}
		Ljzd dto = (Ljzd)baseDao.selectOneBySql(Constants.LJZDDAO_GET_LJ_INFO, ljqc);
		map.put(ljqc, dto);
		return dto;
	}

	/**
	 * 通过id查询列车类型信息
	 * @param id
	 * @return
	 */
	public  TrainType getTrainType(String id){
		TrainType trainType = (TrainType)baseDao.selectOneBySql(Constants.LJZDDAO_GET_TRAIN_TYPE, id);
		return trainType;
	}
	
	/**
    * 从基础数据库中获取18个路局的基本信息
    * @return
    */ 
    public List<Ljzd> getFullStationInfo(){
    	List<Ljzd> list = baseDao.selectListBySql(Constants.LJZDDAO_GET_FULL_STATION_INFO, null);
    	return list;
    }
    
    /**
     * 从基础数据库中获取18个路局的基本信息
     * @return
     */ 
     public Map<String, String> getStationJCMapping(){ 
    	 if(jcMap.isEmpty()){
    		 List<Ljzd> list = getFullStationInfo();
	    	 for(Ljzd ljzd : list){
	    		 jcMap.put(ljzd.getLjpym(), ljzd.getLjjc());
	    	 }
    	 }
    	 return jcMap;
     }
     
     
     /**
      * 从基础数据库中获取18个路局的基本信息
      * @return
      */ 
      public String getStationJC(String py){ 
     	 if(jcMap.isEmpty()){
     		 List<Ljzd> list = getFullStationInfo();
 	    	 for(Ljzd ljzd : list){
 	    		 jcMap.put(ljzd.getLjpym(), ljzd.getLjjc());
 	    	 }
     	 }
     	 return jcMap.get(py);
     	
      
      }
      
      /**
       * 从基础数据库中获取18个路局的基本信息
       * @return
       */ 
       public String getStationPy(String jc){ 
	      	 if(jcMap.isEmpty()){
	      		 List<Ljzd> list = getFullStationInfo();
	  	    	 for(Ljzd ljzd : list){
	  	    		 jcMap.put(ljzd.getLjpym(), ljzd.getLjjc());
	  	    	 }
	      	 }
	      	for(String p : jcMap.keySet()){
	     		if(jc.equals(jcMap.get(p))){
	     			return p;
	     		}
	    	 }
	      	 return null;
       }
    
    
       /**
   	 * 组装发送到rabbit_MQ的字符串
   	 * @param listDto
   	 * @return
   	 */
   	public  String  combinationMessage(List<ParamDto> listDto) throws Exception{
   	
   		    JSONArray  jsonArray = new JSONArray();
               if(listDto != null && listDto.size() > 0){
   				
   				for(ParamDto dto : listDto){
   					
   					//TODO 判断表plan_train中字段CHECK_LEV1_TYPE，当CHECK_LEV1_TYPE=2时才能生成运行线
   					
   					//组装发往rabbit的报文
   					JSONObject  temp = new JSONObject();
   					temp.put("sourceEntityId", dto.getSourceEntityId());
   					temp.put("planTrainId", dto.getPlanTrainId());
   					temp.put("time", dto.getTime());
   					temp.put("source", dto.getSource());
   					temp.put("action", dto.getAction());
   					jsonArray.add(temp);
   				}
   			}
             //组装发送报文
   			JSONObject  json = new JSONObject();
   			JSONObject head = new JSONObject();
   			head.put("event", "trainlineEvent");
   			head.put("requestId", UUID.randomUUID().toString());
   			head.put("batch", 1);
   			head.put("user", "test");
   			json.put("head", head);
   			json.put("param", jsonArray);
   			
   			return json.toString();
   	}
   	
	    
}

package org.railway.com.trainplan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.entity.HighLineCrewInfo;
import org.railway.com.trainplan.entity.QueryResult;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.repository.mybatis.HighLineCrewDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 乘务计划服务
 * Created by speeder on 2014/6/27.
 */
@Monitored
@Component
@Transactional
public class HighLineCrewService {

    private static Log logger = LogFactory.getLog(HighLineCrewService.class);

    @Autowired
    private HighLineCrewDao highLineCrewDao;
    
    @Autowired
    private BaseDao baseDao;

    public HighLineCrewInfo findHighLineCrew(Map<String, Object> map) {
        return highLineCrewDao.findOne(map);
    }

    public List<HighLineCrewInfo> findList(String crewDate,String crewType) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	if("all".equals(crewType)){
    		crewType = null;
    	}
    	map.put("crewDate",crewDate);
    	map.put("crewType",crewType);
        return highLineCrewDao.findList(map);
    }

    public void addCrew(HighLineCrewInfo crewHighlineInfo) {
        highLineCrewDao.addCrew(crewHighlineInfo);
    }

    public void update(HighLineCrewInfo crewHighlineInfo) {
        highLineCrewDao.update(crewHighlineInfo);
    }

    public void delete(String crewHighlineId) {
    	Map<String,String> reqMap = new HashMap<String,String>();
    	reqMap.put("crewHighlineId", crewHighlineId);
        highLineCrewDao.delete(reqMap);
    }
    
    /**
	 * 查询PLAN_TRAIN信息
	 * @param 
	 * @return
	 * @throws Exception 
	 */
	public QueryResult  getRunLineListForRunDate(String runDate,String trainNbr,String rownumstart,String rownumend ) throws Exception{
		Map<String,String> reqMap = new HashMap<String,String>();
		reqMap.put("runDate",runDate );
		reqMap.put("trainNbr",trainNbr );
		reqMap.put("rownumstart",rownumstart );
		reqMap.put("rownumend",rownumend );
		return baseDao.selectListForPagingBySql(Constants.HIGHLINECREWDAO_FIND_RUNPLAN_LIST,reqMap);
	}
    
	
	 /**
	 * 查询乘务计划信息
	 * @param 
	 * @return
	 * @throws Exception 
	 */
	public QueryResult  getHighlineCrewListForRunDate(String crewDate,String crewType,String trainNbr,String rownumstart,String rownumend ) throws Exception{
		Map<String,String> reqMap = new HashMap<String,String>();
		if("all".equals(crewType)){
			crewType = null;
		}
		reqMap.put("crewDate",crewDate );
		reqMap.put("crewType",crewType );
		reqMap.put("rownumstart",rownumstart );
		reqMap.put("rownumend",rownumend );
		if(trainNbr != null && !"".equals(trainNbr)){
			reqMap.put("trainNbr","%" +trainNbr + "%");
		}else if("".equals(trainNbr)){
			reqMap.put("trainNbr",null);
		}
		return baseDao.selectListForPagingBySql(Constants.HIGHLINECREWDAO_FIND_HIGHLINE_CREW_LIST,reqMap);
	}
    
	/**
	 * 更新submitType字段值为1
	 * @param crewDate 格式yyyy-MM-dd
	 * @param crewType 乘务类型（1车长、2司机、3机械师）
	 * @return
	 */
	public int updateSubmitType(String crewDate,String crewType){
		Map<String,String> reqMap = new HashMap<String,String>();
		reqMap.put("crewDate",crewDate );
		reqMap.put("crewType", crewType);
		return baseDao.updateBySql(Constants.HIGHLINECREWDAO_UPDATE_SUBMIT_TYPE, reqMap);
	}
	
	/**
	 * 根据crewDate和crewType删除表highline_crew表中数据
	 * @param crewDate 格式：yyyyMMdd
	 * @param crewType
	 * @param recordPepole 
	 * @return 成功删除数据的条数
	 */
	public int deleteHighlineCrewForCrewDate(String crewDate,String crewType,String recordPepole ){
		Map<String,Object> reqMap = new HashMap<String,Object>();
		reqMap.put("crewDate",crewDate);
		reqMap.put("crewType", crewType);
		reqMap.put("recordPepole", recordPepole);
		
		return baseDao.deleteBySql(Constants.HIGHLINECREWDAO_DELETE_HIGHLINE_CREW_FOR_CREWDATE, reqMap);
		
	}
	
	public List<String> getRecordPeopleOrgList(){
		baseDao.selectListBySql(Constants.HIGHLINECREWDAO_GET_RECORD_PEOPLE_ORG, "");
	}
	
	
	
}

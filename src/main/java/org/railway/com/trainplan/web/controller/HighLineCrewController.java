package org.railway.com.trainplan.web.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.HighLineCrewInfo;
import org.railway.com.trainplan.entity.QueryResult;
import org.railway.com.trainplan.service.HighLineCrewService;
import org.railway.com.trainplan.service.ShiroRealm;
import org.railway.com.trainplan.service.dto.PagingResult;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;

/**
 * Created by speeder on 2014/6/27.
 */
@RestController
@RequestMapping(value = "/crew/highline")
public class HighLineCrewController {

    private static Log logger = LogFactory.getLog(HighLineCrewController.class);

    @Autowired
    private HighLineCrewService highLineCrewService;

    @RequestMapping(value = "getHighLineCrew", method = RequestMethod.POST)
    public Result getHighLineCrew(@RequestBody Map<String,Object> reqMap) {
        logger.debug("getHighLineCrew:::::::");
        Result result = new Result(); 
        try{
        	Map<String, Object> params = Maps.newHashMap();
            params.put("crewHighlineId", StringUtil.objToStr(reqMap.get("crewHighLineId")));
            HighLineCrewInfo highLineCrewInfo = highLineCrewService.findHighLineCrew(params);	
            result.setData(highLineCrewInfo);
        }catch(Exception e){
        	logger.error(e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
        }
        
        return result;
    }

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public Result getHighLineCrewList() {
        logger.debug("getHighLineCrewList:::::::");
        Result result = new Result(); 
        try{
        	 Map<String, Object> params = Maps.newHashMap();
             params.put("id", null);
             List<HighLineCrewInfo> list = highLineCrewService.findList(params);
             result.setData(list);
        }catch(Exception e){
        	logger.error(e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
        }
       
        return result;
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result addHighLineCrewInfo(@RequestBody HighLineCrewInfo highLineCrewInfo) {
        logger.debug("addHighLineCrewInfo:::::::");
        Result result = new Result(); 
        try{
        	ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        	 highLineCrewInfo.setCrewHighlineId(UUID.randomUUID().toString());
             String crewDate = DateUtil.getFormateDayShort(highLineCrewInfo.getCrewDate());
             highLineCrewInfo.setCrewDate(crewDate);
             //所属局简称
             highLineCrewInfo.setCrewBureau(user.getBureauShortName());
         	 highLineCrewInfo.setRecordPeople(user.getName());
        	 highLineCrewInfo.setRecordPeopleOrg(user.getDeptName());
             highLineCrewService.addCrew(highLineCrewInfo);
        }catch(Exception e){
        	logger.error(e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
        }
        return result;
        
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT)
    public Result updateHighLineCrewInfo(@RequestBody HighLineCrewInfo highLineCrewInfo) {
        logger.debug("updateHighLineCrewInfo:::::::" + highLineCrewInfo.getCrewDate() + "|" + highLineCrewInfo.getCrewCross());
        Result result = new Result(); 
        try{
        	ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        	//所属局简称
        	highLineCrewInfo.setCrewBureau(user.getBureauShortName());
        	highLineCrewInfo.setRecordPeople(user.getName());
        	highLineCrewInfo.setRecordPeopleOrg(user.getDeptName());
        	String crewDate = DateUtil.getFormateDayShort(highLineCrewInfo.getCrewDate());
            highLineCrewInfo.setCrewDate(crewDate);
        	highLineCrewService.update(highLineCrewInfo);
        }catch(Exception e){
        	logger.error(e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
        }
        return result;

    }

    /**
     * 批量删除highline_crew表中数据
     * @param reqMap
     * @return
     */
    @RequestMapping(value = "deleteHighLineCrewInfo", method = RequestMethod.DELETE)
    public Result deleteHighLineCrewInfo(@RequestBody Map<String,Object> reqMap ) {
        logger.debug("deleteHighLineCrewInfo:::::::");
        Result result = new Result(); 
        try{
            String crewHighLineId = StringUtil.objToStr(reqMap.get("crewHighLineId"));
            logger.debug("crewHighLineId==" + crewHighLineId);
            highLineCrewService.delete(crewHighLineId);
        }catch(Exception e){
        	logger.error(e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
        }
        return result;
    }
    
    /**
     * 更新字段submitType字段的值为1
     * @param reqMap
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "updateSubmitType", method = RequestMethod.POST)
    public Result updateSubmitType(@RequestBody Map<String,Object> reqMap){
    	Result result = new Result(); 
    	try{
    		logger.debug("updateSubmitType~~~~~reqMap="+reqMap);
    		String crewDate = StringUtil.objToStr(reqMap.get("crewDate"));
    		crewDate = DateUtil.getFormateDayShort(crewDate);
    		String crewType =  StringUtil.objToStr(reqMap.get("crewType"));
    		highLineCrewService.updateSubmitType(crewDate,crewType);
    	}catch(Exception e){
    		logger.error(e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());		
    	}
    	return result;
    }
    /**
	 * 获取运行线信息
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getRunLineListForRunDate", method = RequestMethod.POST)
	public Result getRunLineListForRunDate(@RequestBody Map<String,Object> reqMap){
		Result result = new Result(); 
	    try{
	    	logger.debug("getRunLineListForRunDate~~~~~reqMap="+reqMap);
	    	String runDate = StringUtil.objToStr(reqMap.get("runDate"));
	    	//格式化时间
	    	runDate = DateUtil.getFormateDayShort(runDate);
	    	String trainNbr =  StringUtil.objToStr(reqMap.get("trainNbr"));
	    	String rownumstart =  StringUtil.objToStr(reqMap.get("rownumstart"));
	    	String rownumend =  StringUtil.objToStr(reqMap.get("rownumend"));
	    	QueryResult queryResult = highLineCrewService.getRunLineListForRunDate(runDate, "".equals(trainNbr)?null:trainNbr, rownumstart, rownumend);
	    	PagingResult page = new PagingResult(queryResult.getTotal(), queryResult.getRows());
	    	result.setData(page);
	    }catch(Exception e){
			logger.error(e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
		}
	
		return result;
	}
	
	
	/**
	 * 根据日期获取乘务计划列表信息
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getHighlineCrewListForRunDate", method = RequestMethod.POST)
	public Result getHighlineCrewListForRunDate(@RequestBody Map<String,Object> reqMap){
		Result result = new Result(); 
	    try{
	    	logger.debug("getHighlineCrewListForRunDate~~~~~reqMap="+reqMap);
	    	String crewDate = StringUtil.objToStr(reqMap.get("crewDate"));
	    	//格式化时间
	    	crewDate = DateUtil.getFormateDayShort(crewDate);
	    	String crewType = StringUtil.objToStr(reqMap.get("crewType"));
	    	String rownumstart =  StringUtil.objToStr(reqMap.get("rownumstart"));
	    	String rownumend =  StringUtil.objToStr(reqMap.get("rownumend"));
	    	QueryResult queryResult = highLineCrewService.getHighlineCrewListForRunDate(crewDate,crewType, rownumstart, rownumend);
	    	PagingResult page = new PagingResult(queryResult.getTotal(), queryResult.getRows());
	    	result.setData(page);
	    }catch(Exception e){
			logger.error(e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
		}
	
		return result;
	}
}

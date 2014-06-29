package org.railway.com.trainplan.web.controller;

import com.google.common.collect.Maps;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.HighLineCrewInfo;
import org.railway.com.trainplan.entity.QueryResult;
import org.railway.com.trainplan.service.HighLineCrewService;
import org.railway.com.trainplan.service.dto.PagingResult;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by speeder on 2014/6/27.
 */
@RestController
@RequestMapping(value = "/crew/highline")
public class HighLineCrewController {

    private static Log logger = LogFactory.getLog(HighLineCrewController.class);

    @Autowired
    private HighLineCrewService highLineCrewService;

    @RequestMapping(value = "{highLineId}", method = RequestMethod.GET)
    public ResponseEntity<HighLineCrewInfo> getHighLineCrew(@PathVariable String crewHighLineId) {
        logger.debug("getHighLineCrew:::::::");
        Map<String, Object> params = Maps.newHashMap();
        params.put("crewHighLineId", crewHighLineId);
        HighLineCrewInfo highLineCrewInfo = highLineCrewService.findHighLineCrew(params);
        return new ResponseEntity<HighLineCrewInfo>(highLineCrewInfo, HttpStatus.OK);
    }

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public ResponseEntity<List<HighLineCrewInfo>> getHighLineCrewList() {
        logger.debug("getHighLineCrewList:::::::");
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", null);
        List<HighLineCrewInfo> list = highLineCrewService.findList(params);
        return new ResponseEntity<List<HighLineCrewInfo>>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "add", method = RequestMethod.PUT)
    public ResponseEntity addHighLineCrewInfo(@RequestBody HighLineCrewInfo highLineCrewInfo) {
        logger.debug("addHighLineCrewInfo:::::::");
        highLineCrewInfo.setCrewHighlineId(UUID.randomUUID().toString());
        highLineCrewService.addCrew(highLineCrewInfo);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ResponseEntity updateHighLineCrewInfo(@RequestBody HighLineCrewInfo highLineCrewInfo) {
        logger.debug("updateHighLineCrewInfo:::::::");
        highLineCrewService.update(highLineCrewInfo);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "{highLineId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteHighLineCrewInfo(@PathVariable String crewHighLineId) {
        logger.debug("deleteHighLineCrewInfo:::::::");
        highLineCrewService.delete(crewHighLineId);
        return new ResponseEntity(HttpStatus.OK);
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
	    	String trainNbr =  StringUtil.objToStr(reqMap.get("trainNbr"));
	    	String rownumstart =  StringUtil.objToStr(reqMap.get("rownumstart"));
	    	String rownumend =  StringUtil.objToStr(reqMap.get("rownumend"));
	    	QueryResult queryResult = highLineCrewService.getRunLineListForRunDate(runDate, trainNbr, rownumstart, rownumend);
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
	    	String rownumstart =  StringUtil.objToStr(reqMap.get("rownumstart"));
	    	String rownumend =  StringUtil.objToStr(reqMap.get("rownumend"));
	    	QueryResult queryResult = highLineCrewService.getHighlineCrewListForRunDate(crewDate, rownumstart, rownumend);
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

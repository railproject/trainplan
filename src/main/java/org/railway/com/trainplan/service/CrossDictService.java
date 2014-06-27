package org.railway.com.trainplan.service;

import java.util.Map;

import org.apache.log4j.Logger;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.entity.QueryResult;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrossDictService {
	private static final Logger logger = Logger.getLogger(CrossDictService.class);

	@Autowired
	private BaseDao baseDao;
	
	/**
	 * 查询cross信息
	 * @param reqMap
	 * @return
	 * @throws Exception 
	 */
	public QueryResult  getCrossDictInfo(Map<String,Object> reqMap) throws Exception{
		return baseDao.selectListForPagingBySql(Constants.SQL_ID_CROSSDICT_INFO, reqMap);
	}
}

package org.railway.com.trainplan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.entity.CrossDictInfo;
import org.railway.com.trainplan.entity.CrossDictStnInfo;
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
	
	
	/**
	 * 对表DRAW_GRAPH添加一条数据
	 * @param crossDictInfo
	 * @return
	 */
	public int  addCrossDictInfo(CrossDictInfo crossDictInfo){
		return baseDao.insertBySql(Constants.SQL_ADD_CROSS_DIC_INFO, crossDictInfo);
	}
	
	/**
	 * 批量对表DRAW_GRAPH_STN添加数据
	 * @param list
	 * @return
	 */
	public int batchAddCrossDictStnInfo(List<CrossDictStnInfo> list){
		Map<String,Object> reqMap = new HashMap<String,Object>();
		reqMap.put("trainCrossList",list );
		return baseDao.insertBySql(Constants.SQL_BATCH_ADD_CROSS_DIC_STN_INFO, reqMap);
	}
	
	
	/**
	 * 通过baseCrossId查询表DRAW_GRAPH的对象
	 * @param baseCrossId
	 * @return
	 */
	public CrossDictInfo  getDrawGraphForBaseCrossId(String baseCrossId){
		return (CrossDictInfo) baseDao.selectOneBySql(Constants.SQL_GET_DRAW_GRAPH_FOR_BASECROSSID, baseCrossId);
	}
	
	/**
	 * 通过baseCrossId查询
	 * @param baseCrossId
	 * @return
	 */
	public List<CrossDictStnInfo> getCrossDictStnForBaseCrossId(String baseCrossId){
		return baseDao.selectListBySql(Constants.SQL_GET_CROSS_DIC_STN_FOR_BASECROSSID, baseCrossId);
	}
}

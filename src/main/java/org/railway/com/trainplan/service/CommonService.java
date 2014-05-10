package org.railway.com.trainplan.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.Ljzd;
import org.railway.com.trainplan.entity.TrainType;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.repository.mybatis.LjzdMybatisDao;
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
	
	@Autowired
	private LjzdMybatisDao ljzdDao;
	
	@Autowired
	private BaseDao baseDao;
	/**
	 * 通过路局全称查询路基基本信息
	 */
	public Ljzd getLjInfo(String ljqc) {
		//Ljzd  dto = new Ljzd();
		//dto = ljzdDao.getLjInfo(ljqc);
		Ljzd dto = (Ljzd)baseDao.selectOneBySql("org.railway.com.trainplan.repository.mybatis.LjzdMybatisDao.getLjInfo", ljqc);
		//fortest
		System.err.println("dm=" + dto.getLjdm());
		System.err.println("jc=" + dto.getLjjc());
		System.err.println("pym=" + dto.getLjpym());
		return dto;
	}

	/**
	 * 通过id查询列车类型信息
	 * @param id
	 * @return
	 */
	public  TrainType getTrainType(String id){
		TrainType trainType = new TrainType();
		String sql = String.format("select_train_type_id", id);
		List<Map<String,Object>> listMap = null;//jdbcDao.queryList(sql);
		//只有一条数据
		if(listMap != null){
			Map<String,Object> map = listMap.get(0);
			trainType.setCheciBiaoshi(StringUtil.objToStr(map.get("CHECI_BIAOSHI")));
			trainType.setId(StringUtil.objToStr(map.get("id")));
			trainType.setLiecheDengji(StringUtil.objToStr(map.get("LIECHE_DENGJI")));
			trainType.setName(StringUtil.objToStr(map.get("name")));
			trainType.setPinYinCode(StringUtil.objToStr(map.get("PINYINCODE")));
			trainType.setShortName(StringUtil.objToStr(map.get("SHORT_NAME")));
			trainType.setZuoyeDengji(StringUtil.objToStr(map.get("ZUOYE_DENGJI")));
			
		}
		
		return trainType;
	}
}

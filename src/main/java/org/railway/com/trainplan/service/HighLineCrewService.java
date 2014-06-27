package org.railway.com.trainplan.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.entity.HighLineCrewInfo;
import org.railway.com.trainplan.repository.mybatis.HighLineCrewDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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

    public HighLineCrewInfo findHighLineCrew(Map<String, Object> map) {
        return highLineCrewDao.findOne(map);
    }

    public List<HighLineCrewInfo> findList(Map<String, Object> map) {
        return highLineCrewDao.findList(map);
    }

    public void addCrew(HighLineCrewInfo crewHighlineInfo) {
        highLineCrewDao.addCrew(crewHighlineInfo);
    }

    public void update(HighLineCrewInfo crewHighlineInfo) {
        highLineCrewDao.update(crewHighlineInfo);
    }

    public void delete(String crewHighLineId) {
        highLineCrewDao.delete(crewHighLineId);
    }
}

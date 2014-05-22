package org.railway.com.trainplan.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.repository.mybatis.RunLineDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by star on 5/13/14.
 */
@Component
@Monitored
public class RunLineService {

    private final static Log logger = LogFactory.getLog(RunLineService.class);

    @Autowired
    private RunLineDao runLineDao;

    public List<Map<String, Object>> findLineTimeTableByLineId(String lineId) {
        logger.debug("findLineTimeTableByLineId::::");
        return runLineDao.findLineTimeTableByLineId(lineId);
    }

    public Map<String, Object> findLineInfoByLineId(String lineId) {
        logger.debug("findLineInfoByLineId::::");
        return runLineDao.findLineInfoByLineId(lineId);
    }
}

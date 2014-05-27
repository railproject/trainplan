package org.railway.com.trainplan.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.repository.mybatis.ChartDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Created by speeder on 2014/5/27.
 */
@Component
@Transactional
@Monitored
public class ChartService {

    private static final Log logger = LogFactory.getLog(ChartService.class);

    @Autowired
    private ChartDao chartDao;

    public Map<String, Object> getPlanTypeChart(String date) {
        logger.debug("getPlanTypeChart::::");
        return chartDao.getPlanTypeCount(date);
    }

    public Map<String, Object> getPlanLineCount(String date) {
        logger.debug("getPlanLineCount::");
        return chartDao.getPlanLineCount(date);
    }
}

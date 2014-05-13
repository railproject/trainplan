package org.railway.com.trainplan.service;

import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.repository.mybatis.BaseDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by star on 5/13/14.
 */
@Component
@Monitored
public class BaseDataService {

    @Autowired
    private BaseDataDao baseDataDao;

    public List<Map<String, Object>> getFJCDic(String bureauCode) {
        baseDataDao.getFJKDicByBureauCode(bureauCode);
        return null;
    }
}

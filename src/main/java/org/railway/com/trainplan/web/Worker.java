package org.railway.com.trainplan.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.railway.com.trainplan.entity.User;
import org.railway.com.trainplan.service.AccountService;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by star on 5/9/14.
 */
public class Worker implements Runnable {

    private Log logger = LogFactory.getLog(Worker.class);

    private AccountService accountService;

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void run() {
        Map<String, Object> params = new TreeMap<String, Object>();
        List<User> list = accountService.searchUser(params);
        logger.info(list.size());
    }
}

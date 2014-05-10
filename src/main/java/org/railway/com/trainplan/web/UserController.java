/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package org.railway.com.trainplan.web;

import com.google.common.collect.Maps;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.railway.com.trainplan.entity.User;
import org.railway.com.trainplan.service.AccountEffectiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping(value = "/account/user")
public class UserController {

    private Log logger = LogFactory.getLog(UserController.class);

	private static Map<String, String> allStatus = Maps.newHashMap();

	static {
		allStatus.put("enabled", "有效");
		allStatus.put("disabled", "无效");
	}


    @Autowired
    private AccountEffectiveService accountEffectiveService;

	// 特别设定多个ReuireRoles之间为Or关系，而不是默认的And.
//	@RequiresRoles(value = { "Admin", "User" }, logical = Logical.OR)
	@RequestMapping(value = "")
	public String list(Model model, ServletRequest request) {
        logger.info("######## begin #########");
		return "account/userList";
	}

//	@RequiresRoles("Admin")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model) {
		return "account/userForm";
	}


	/**
	 * 不自动绑定对象中的roleList属性，另行处理。
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("roleList");
	}

    @RequestMapping(value = "save", method=RequestMethod.POST)
    public String save(@Valid User user) {
        System.out.println("save user: " + user);
        accountEffectiveService.saveUser(user);
        return "account/userList";
    }

    @RequestMapping(value = "register", method=RequestMethod.GET)
    public String register() {
        return "account/register";
    }
}

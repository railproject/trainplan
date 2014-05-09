/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package org.railway.com.trainplan.web;

import com.google.common.collect.Maps;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.railway.com.trainplan.entity.Role;
import org.railway.com.trainplan.entity.User;
import org.railway.com.trainplan.service.AccountEffectiveService;
import org.railway.com.trainplan.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.util.List;
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
	private AccountService accountService;

    @Autowired
    private AccountEffectiveService accountEffectiveService;

	// 特别设定多个ReuireRoles之间为Or关系，而不是默认的And.
//	@RequiresRoles(value = { "Admin", "User" }, logical = Logical.OR)
	@RequestMapping(value = "")
	public String list(Model model, ServletRequest request) {
        logger.info("######## begin #########");
        Worker worker = new Worker();
        worker.setAccountService(accountService);
        worker.run();

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		List<User> users = accountService.searchUser(searchParams);
		model.addAttribute("users", users);
		model.addAttribute("allStatus", allStatus);
		return "account/userList";
	}

//	@RequiresRoles("Admin")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("user", accountService.getUser(id));
		model.addAttribute("allStatus", allStatus);
		model.addAttribute("allRoles", accountService.getAllRole());
		return "account/userForm";
	}

	/**
	 * 演示自行绑定表单中的checkBox roleList到对象中.
	 */
//	@RequiresPermissions("user:edit")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("user") User user,
			@RequestParam(value = "roleList") List<Long> checkedRoleList, RedirectAttributes redirectAttributes) {

		// bind roleList
		user.getRoleList().clear();
		for (Long roleId : checkedRoleList) {
			Role role = new Role(roleId);
			user.getRoleList().add(role);
		}

		accountService.saveUser(user);

		redirectAttributes.addFlashAttribute("message", "保存用户成功");
		return "redirect:/account/user";
	}

	@RequestMapping(value = "checkLoginName")
	@ResponseBody
	public String checkLoginName(@RequestParam("oldLoginName") String oldLoginName,
			@RequestParam("loginName") String loginName) {
		if (loginName.equals(oldLoginName)) {
			return "true";
		} else if (accountService.findUserByLoginName(loginName) == null) {
			return "true";
		}

		return "false";
	}

	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void getUser(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("user", accountService.getUser(id));
		}
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

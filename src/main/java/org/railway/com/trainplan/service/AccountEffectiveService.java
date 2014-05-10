/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package org.railway.com.trainplan.service;

import com.google.common.collect.Maps;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.entity.User;
import org.railway.com.trainplan.repository.mybatis.UserMybatisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;

import java.util.List;
import java.util.Map;

/**
 * 更高效的AccountService实现，基于MyBatis + Memcached的方案，以JSON格式存储Memcached中的内容。
 * 
 * @author calvin
 */
@Component
@Transactional
@Monitored
public class AccountEffectiveService {
	@Autowired
	private UserMybatisDao userDao;

//	@Autowired(required = false)
//	private SpyMemcachedClient memcachedClient;

	private final JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();

	public User getUser(Long id) {
//		if (memcachedClient != null) {
//			return getUserWithMemcached(id);
//		} else {
			return userDao.get(id);
//		}
	}
/*

	*/
/**
	 * 先访问Memcached, 使用JSON字符串存放对象以节约空间.
	 *//*

	private User getUserWithMemcached(Long id) {
		String key = MemcachedObjectType.USER.getPrefix() + id;

		String jsonString = memcachedClient.get(key);

		if (jsonString != null) {
			return jsonMapper.fromJson(jsonString, User.class);
		} else {
			User user = userDao.get(id);
			if (user != null) {
				jsonString = jsonMapper.toJson(user);
				memcachedClient.set(key, MemcachedObjectType.USER.getExpiredTime(), jsonString);
			}
			return user;
		}
	}
*/

	public List<User> searchUser(String loginName, String name) {
		Map<String, Object> parameters = Maps.newHashMap();
		parameters.put("loginName", loginName);
		parameters.put("name", name);
		return userDao.search(parameters);
	}

	public void saveUser(User user) {
		userDao.save(user);
	}

	public void deleteUser(Long id) {
		userDao.delete(id);
	}
}

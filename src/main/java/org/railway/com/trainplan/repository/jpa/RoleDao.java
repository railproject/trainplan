/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package org.railway.com.trainplan.repository.jpa;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.railway.com.trainplan.entity.Role;

public interface RoleDao extends PagingAndSortingRepository<Role, Long> {

}

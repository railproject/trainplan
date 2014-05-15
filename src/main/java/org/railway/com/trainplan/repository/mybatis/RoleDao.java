package org.railway.com.trainplan.repository.mybatis;

import org.railway.com.trainplan.entity.Role;

import java.util.List;

/**
 * Created by star on 5/15/14.
 */
@MyBatisRepository
public interface RoleDao {

    List<Role> getRoleByAccId(int accId);
}

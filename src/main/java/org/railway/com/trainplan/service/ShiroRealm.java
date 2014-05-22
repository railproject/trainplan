package org.railway.com.trainplan.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.railway.com.trainplan.entity.Role;
import org.railway.com.trainplan.entity.User;
import org.railway.com.trainplan.repository.mybatis.RoleDao;
import org.railway.com.trainplan.repository.mybatis.UserDao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by star on 5/15/14.
 */
public class ShiroRealm extends AuthorizingRealm {

    private final static Log logger = LogFactory.getLog(ShiroRealm.class);

    private UserDao userDao;

    private RoleDao roleDao;

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public RoleDao getRoleDao() {
        return roleDao;
    }

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        logger.info("Authorization::::" + shiroUser.getUsername());
        if(shiroUser != null){
            int accId = shiroUser.getAccId();
            List<Role> roleList = roleDao.getRoleByAccId(accId);
            if(roleList != null && !roleList.isEmpty()) {
                SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
                for(Role role: roleList) {
                    info.addRole(role.getName());
                }
                return info;
            }
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();
        logger.info("Authentication:::::" + username);
        String[] logininfo = username.split("@");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", logininfo[0]);
        params.put("accId", Integer.parseInt(logininfo[1]));
        try {
            User user = userDao.getUserByUsernameAndAccId(params);
            if (user != null) {
                if (0 == user.getId() || "".equals(user.getUsername()) || "".equals(user.getName())) {
                    throw new DisabledAccountException();
                }

                ShiroUser shiroUser = new ShiroUser(user.getUsername(), user.getName(), Integer.parseInt(logininfo[1]), user.getLjpym(), user.getLjjc(), user.getLjqc());
                return new SimpleAuthenticationInfo(shiroUser, user.getPassword(), getName());
            }
        }catch(Exception e) {
            logger.error(e);
        }
        return null;
    }

    public static class ShiroUser implements Serializable {

        private String username;

        private String name;

        private int accId;

        private String bureau;

        private String bureauShortName;

        private String bureauFullName;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getAccId() {
            return accId;
        }

        public void setAccId(int accId) {
            this.accId = accId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBureau() {
            return bureau;
        }

        public void setBureau(String bureau) {
            this.bureau = bureau;
        }

        public String getBureauShortName() {
            return bureauShortName;
        }

        public void setBureauShortName(String bureauShortName) {
            this.bureauShortName = bureauShortName;
        }

        public String getBureauFullName() {
            return bureauFullName;
        }

        public void setBureauFullName(String bureauFullName) {
            this.bureauFullName = bureauFullName;
        }

        public ShiroUser(String username, String name, int accId) {
            this.username = username;
            this.name = name;
            this.accId = accId;
        }

        public ShiroUser(String username, String name, int accId, String bureau, String bureauShortName, String bureauFullName) {
            this.username = username;
            this.name = name;
            this.accId = accId;
            this.bureau = bureau;
            this.bureauShortName = bureauShortName;
            this.bureauFullName = bureauFullName;
        }

        @Override
        public String toString() {
            return this.name;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.username) + accId;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            ShiroUser other = (ShiroUser) obj;
            if (this.username == null) {
                if (other.getUsername() != null) {
                    return false;
                }
            } else if (!this.username.equals(other.getUsername())) {
                return false;
            }
            if (this.accId == 0) {
                if (other.getUsername() != null) {
                    return false;
                }
            } else if (this.accId != other.getAccId()) {
                return false;
            }
            return true;
        }
    }
}

package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.Exception.LoginException;
import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    public User login(String loginAct, String loginPwd, String ip) throws LoginException {

        Map<String,String> map =  new HashMap<String, String>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);

        User user = userDao.login(map);

        if (user == null){
            throw new LoginException("账号密码错误");
        }

        //执行到此处说明账号密码正确
        //取得失效时间
        String expireTime = user.getExpireTime();
        //当前系统时间
        String sysTime = DateTimeUtil.getSysTime();
        if(expireTime.compareTo(sysTime)<0){
            throw new LoginException("账号已失效");
        }

        //取得锁定状态
        String lockState = user.getLockState();
        if("0".equals(lockState)){
            throw new LoginException("账号已锁定");
        }

        //取ip地址
        String ips = user.getAllowIps();
        if (!ips.contains(ip)){
            throw new LoginException("ip地址受限");
        }

        return user;
    }

    public List<User> getUserList() {

        List<User> userList = userDao.getUserList();

        return userList;
    }

}










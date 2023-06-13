package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.Exception.LoginException;
import com.bjpowernode.crm.settings.domain.User;

import java.util.List;

public interface UserService {
    public List<User> getUserList();

    User login(String loginAct, String loginPwd, String ip) throws LoginException;
}

package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.MD5Util;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到用户控制器");

        String path = request.getServletPath();

        if ("/settings/user/login.do".equals(path)){

            login(request,response);

        }else if("/settings/user/xxx.do".equals(path)){

//            xxx(request,request);

        }

    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        //接受参数
        String loginAct = request.getParameter("loginAct");
        String _loginPwd = request.getParameter("loginPwd");
        //将密码用MD5加密
        String  loginPwd= MD5Util.getMD5(_loginPwd);

        //获取id
        String ip = request.getRemoteAddr();
        System.out.println("--------------ip:"+ip);

        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
//        UserService userService = new UserServiceImpl();

        try {
            User user = userService.login(loginAct,loginPwd,ip);
            request.getSession().setAttribute("user",user);

            //如果程序执行到此处，说明业务层没有为controller抛出任何的异常
            //表示登录成功
            PrintJson.printJsonFlag(response,true);


        }catch (Exception e){
            e.printStackTrace();

            //一旦程序执行了catch块的信息，说明业务层为我们验证登录失败，为controller抛出了异常
            //表示登录失败

            //获取异常信息
            String msg = e.getMessage();

            Map<String,Object> map = new HashMap<String, Object>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);

        }


    }

}




























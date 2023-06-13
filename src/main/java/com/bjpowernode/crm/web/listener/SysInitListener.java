package com.bjpowernode.crm.web.listener;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.impl.DicServiceImpl;
import com.bjpowernode.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("取字典开始");

        ServletContext application = event.getServletContext();

        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List<DicValue>> map = ds.getAll();

        Set<String> set = map.keySet();
        for (String key:set){
            application.setAttribute(key,map.get(key));
        }

        System.out.println("取字典结束");

        //-------------------------------------------------
        //解析Stage2Possibility.properties文件
        //将该属性文件的键值对关系处理成map
        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");

        Map<String,String> pMap = new HashMap<String, String>();
        Enumeration<String> e = rb.getKeys();
        while (e.hasMoreElements()){

            //阶段
            String key = e.nextElement();
            //可能性
            String value = rb.getString(key);

            pMap.put(key,value);

        }

        //将pMap保存到服务器缓冲中
        application.setAttribute("pMap",pMap);

    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}

package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {

    ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    public boolean save(Activity a) {
        boolean flag = true;

        int count = activityDao.save(a);
        if(count!=1){
            flag = false;
        }

        return flag;
    }

    public PaginationVo<Activity> pageList(Map<String, Object> map) {

        //取得total
        int total = activityDao.getTotalByCondition(map);

        //取得dataList
        List<Activity> dataList = activityDao.getActivityListByCondition(map);

        //将total和dataList封装到vo
        PaginationVo<Activity> vo = new PaginationVo<Activity>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        //返回vo
        return vo;
    }

    public Boolean delete(String[] ids) {
        boolean flag = true;

        //查询出需要删除的备注数量
        int count1 = activityRemarkDao.getCountByAids(ids);

        //删除备注，返回所受到影响到的条数
        int count2 = activityRemarkDao.deleteByAids(ids);

        if (count1!=count2){
            flag=false;
        }

        //删除市场活动
        int count3 = activityDao.delete(ids);
        if (count3!=ids.length){
            flag = false;
        }

        return flag;
    }


    public Map<String, Object> getUserListAndActivity(String id) {

        //获取用户列表
        List<User> userList = userDao.getUserList();
        //获取activity
        Activity a =activityDao.getById(id);

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("uList",userList);
        map.put("a",a);

        return map;
    }

    public boolean update(Activity a) {
        boolean flag = true;

        int count = activityDao.update(a);
        if(count!=1){
            flag = false;
        }

        return flag;
    }

    public Activity detail(String id) {

        Activity a = activityDao.getDetail(id);

        return a;
    }

    public List<ActivityRemark> getRemarkListByAid(String activityId) {

        List<ActivityRemark> arList = activityRemarkDao.getRemarkListByAid(activityId);

        return arList;
    }

    public Boolean deleteRemark(String id) {
        Boolean flag = true;

        int count = activityRemarkDao.deleteRemarkById(id);
        if (count != 1){
            flag = false;
        }

        return flag;
    }

    public Boolean saveRemark(ActivityRemark ar) {
        Boolean flag = true;

        int count = activityRemarkDao.saveRemark(ar);
        if (count!=1){
            flag = false;
        }

        return flag;
    }

    public Boolean updateRemark(ActivityRemark ar) {
        Boolean flag = true;

        int count = activityRemarkDao.updateRemark(ar);
        if (count!=1){
            flag = false;
        }

        return flag;
    }

    public List<Activity> getActivityListByClueId(String id) {

        List<Activity> aList = activityDao.getActivityListByClueId(id);

        return aList;
    }

    public List<Activity> getActivityListByNameNotByClueId(Map<String, String> map) {

        List<Activity> aList = activityDao.getActivityListByNameNotByClueId(map);

        return aList;
    }

    public List<Activity> getActivityListByName(String aname) {

        List<Activity> aList = activityDao.getActivityListByName(aname);

        return aList;
    }


}














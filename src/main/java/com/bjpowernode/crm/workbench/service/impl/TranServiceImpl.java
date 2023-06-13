package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UuidUtil;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    public Boolean save(Tran t, String customerName) {
        Boolean flag = true;

        Customer cus = customerDao.getCustomerByName(customerName);
        if (cus==null){
            cus = new Customer();
            cus.setId(UuidUtil.getUuid());
            cus.setOwner(t.getOwner());
            cus.setName(customerName);
            cus.setCreateBy(t.getCreateBy());
            cus.setCreateTime(t.getCreateTime());
            cus.setContactSummary(t.getContactSummary());
            cus.setNextContactTime(t.getNextContactTime());
            int count1 = customerDao.save(cus);
            if (count1!=1){
                flag = false;
            }
        }
        //给交易表设置外键--客户id
        t.setCustomerId(cus.getId());

        //添加交易
        int count2 = tranDao.save(t);
        if (count2!=1){
            flag = false;
        }

        //添加交易记录历史
        TranHistory th = new TranHistory();
        th.setId(UuidUtil.getUuid());
        th.setStage(t.getStage());
        th.setMoney(t.getMoney());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setCreateBy(t.getCreateBy());
        th.setTranId(t.getId());
        int count3 = tranHistoryDao.save(th);
        if (count3!=1){
            flag = false;
        }

        return flag;
    }

    public List<Tran> pageList() {

        List<Tran> t = tranDao.pageList();

        return t;
    }

    public Tran detail(String id) {

        Tran t = tranDao.detail(id);

        return t;
    }

    public List<TranHistory> showPossibilityListByTranId(String tranId) {

        List<TranHistory> thList = tranHistoryDao.showPossibilityListByTranId(tranId);

        return thList;
    }

    public Boolean changeStage(Tran t) {
        Boolean flag = true;

        //修改交易信息
        int count = tranDao.changeStage(t);
        if (count!=1){
            flag = false;
        }

        //修改交易信息后，创建交易阶段历史
        TranHistory th = new TranHistory();
        th.setId(UuidUtil.getUuid());
        th.setStage(t.getStage());
        th.setMoney(t.getMoney());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setCreateBy(t.getEditBy());
        th.setTranId(t.getId());

        int count2 = tranHistoryDao.save(th);
        if (count2!=1){
            flag = false;
        }

        return flag;
    }

    public Map<String, Object> getECharts() {

        int total = tranDao.getTotal();

        List<Map<String,Object>> dataList = tranDao.getECharts();

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("total",total);
        map.put("dataList",dataList);

        return map;
    }

}

















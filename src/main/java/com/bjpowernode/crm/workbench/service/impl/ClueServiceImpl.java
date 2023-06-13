package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UuidUtil;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.ClueService;

import java.util.List;

public class ClueServiceImpl implements ClueService {

    //线索相关表
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

    //客户相关的表
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);


    public List<Clue> getClueList() {
        List<Clue> cList = clueDao.getClueList();
        return cList;
    }

    public Boolean save(Clue c) {
        Boolean flag = true;

        int count = clueDao.save(c);
        if (count!=1){
            flag = false;
        }

        return flag;
    }

    public Clue getClueById(String id) {

        Clue c = clueDao.getClueById(id);

        return c;
    }

    public Boolean unbund(String id) {
        Boolean flag = true;


        int count = clueActivityRelationDao.unbund(id);
        if (count != 1){
            flag = false;
        }

        return flag;
    }

    public Boolean bund(String cid, String[] aids) {
        Boolean flag = true;

        for (String aid: aids) {
            ClueActivityRelation car  = new ClueActivityRelation();
            car.setId(UuidUtil.getUuid());
            car.setClueId(cid);
            car.setActivityId(aid);

            int count = clueActivityRelationDao.bund(car);
            if (count != 1){
                flag = false;
            }
        }

        return flag;
    }

    public Boolean convert(String clueId, Tran t, String createBy) {
        Boolean flag = true;

        String createTime = DateTimeUtil.getSysTime();

        //(1) 获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
        Clue c = clueDao.getClueById2(clueId);

        //(2) 通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        String company = c.getCompany();//获取公司名
        Customer cus = customerDao.getCustomerByName(company);
        if (cus==null){
            cus = new Customer();
            cus.setId(UuidUtil.getUuid());
            cus.setOwner(c.getOwner());
            cus.setName(company);
            cus.setWebsite(c.getWebsite());
            cus.setPhone(c.getPhone());
            cus.setCreateBy(createBy);
            cus.setCreateTime(createTime);
            cus.setContactSummary(c.getContactSummary());
            cus.setNextContactTime(c.getNextContactTime());
            cus.setDescription(c.getDescription());
            cus.setAddress(c.getAddress());

            //创建客户
            int count2 = customerDao.save(cus);
            if (count2!=1){
                flag = false;
            }
        }

        //(3) 通过线索对象提取联系人信息，保存联系人
        Contacts con = new Contacts();
        con.setId(UuidUtil.getUuid());
        con.setOwner(c.getOwner());
        con.setSource(c.getSource());
        con.setCustomerId(cus.getId());
        con.setFullname(c.getFullname());
        con.setAppellation(c.getAppellation());
        con.setEmail(c.getEmail());
        con.setMphone(c.getMphone());
        con.setJob(c.getJob());
        con.setCreateBy(createBy);
        con.setCreateTime(createTime);
        con.setDescription(c.getDescription());
        con.setContactSummary(c.getContactSummary());
        con.setNextContactTime(c.getNextContactTime());
        con.setAddress(c.getAddress());

        //创建联系人
        int count3 = contactsDao.save(con);
        if (count3!=1){
            flag = false;
        }

        //(4) 线索备注转换到客户备注以及联系人备注
        //查询当前线索备注的信息
        List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClueId(clueId);
        for (ClueRemark clueRemark:clueRemarkList){
            String noteContent = clueRemark.getNoteContent();

            //创建客户，转换备注信息
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UuidUtil.getUuid());
            customerRemark.setNoteContent(noteContent);
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setEditFlag("0");
            customerRemark.setCustomerId(cus.getId());
            int count4_1 = customerRemarkDao.save(customerRemark);
            if (count4_1!=1){
                flag = false;
            }

            //创建联系人，转换备注信息
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UuidUtil.getUuid());
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setEditFlag("0");
            contactsRemark.setContactsId(con.getId());
            int count4_2 = contactsRemarkDao.save(contactsRemark);
            if (count4_2!=1){
                flag = false;
            }
        }

        //(5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        //查询当前线索和市场活动关联
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);
        for (ClueActivityRelation clueActivityRelation:clueActivityRelationList) {
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UuidUtil.getUuid());
            contactsActivityRelation.setContactsId(con.getId());
            contactsActivityRelation.setActivityId(clueActivityRelation.getActivityId());
            int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
            if (count5!=1){
                flag = false;
            }
        }

        //(6) 如果有创建交易需求，创建一条交易
        if (t!=null){
            t.setOwner(c.getOwner());
            t.setCustomerId(cus.getId());
            t.setSource(c.getSource());
            t.setContactsId(con.getId());
            t.setDescription(c.getDescription());
            t.setContactSummary(c.getContactSummary());
            t.setNextContactTime(c.getNextContactTime());
            int count6 = tranDao.save(t);
            if (count6!=1){
                flag = false;
            }

            //(7) 如果创建了交易，则创建一条该交易下的交易历史
            TranHistory th = new TranHistory();
            th.setCreateBy(createBy);
            th.setCreateTime(createTime);
            th.setId(UuidUtil.getUuid());
            th.setMoney(t.getMoney());
            th.setTranId(t.getId());
            th.setExpectedDate(t.getExpectedDate());
            th.setStage(t.getStage());

            int count7 = tranHistoryDao.save(th);
            if (count7!=1){
                flag = false;
            }

        }

        //(8) 删除线索备注
        for (ClueRemark clueRemark:clueRemarkList) {
            int count8 = clueRemarkDao.delete(clueRemark);
            if (count8!=1){
                flag = false;
            }
        }

        //(9) 删除线索和市场活动的关系
        for (ClueActivityRelation clueActivityRelation:clueActivityRelationList){
            int count9 = clueActivityRelationDao.unbund(clueActivityRelation.getId());
            if (count9!=1){
                flag = false;
            }
        }

        //(10) 删除线索
        int count10 = clueDao.deleteById(clueId);
        if (count10!=1){
            flag = false;
        }

        return flag;
    }
}



































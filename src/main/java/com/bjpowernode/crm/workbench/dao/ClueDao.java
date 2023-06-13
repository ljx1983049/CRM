package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.List;

public interface ClueDao {

    List<Clue> getClueList();

    int save(Clue c);

    Clue getClueById(String id);//查询的owner为名称

    Clue getClueById2(String clueId);//查询的owner为id

    int deleteById(String clueId);

}

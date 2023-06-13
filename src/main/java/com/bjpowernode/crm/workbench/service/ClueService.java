package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.Tran;

import java.util.List;

public interface ClueService {
    List<Clue> getClueList();

    Boolean save(Clue c);

    Clue getClueById(String id);

    Boolean unbund(String id);

    Boolean bund(String cid, String[] aids);

    Boolean convert(String clueId, Tran t, String createBy);
}

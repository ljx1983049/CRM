package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {
    Boolean save(Tran t, String customerName);

    List<Tran> pageList();

    Tran detail(String id);

    List<TranHistory> showPossibilityListByTranId(String tranId);

    Boolean changeStage(Tran t);

    Map<String, Object> getECharts();
}

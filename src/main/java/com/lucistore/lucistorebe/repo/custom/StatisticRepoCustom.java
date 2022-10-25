package com.lucistore.lucistorebe.repo.custom;

import java.util.Date;
import java.util.List;

import com.lucistore.lucistorebe.controller.payload.dto.statistic.OrderStatistic;

public interface StatisticRepoCustom {
    List<OrderStatistic> getOrderStatistic(
        List<Long> idProducts,
        List<Long> idProductVariations,
        List<Long> idBuyers,
        List<Long> idAdmins,
        Date importDateFrom,
        Date importDateTo
    );
    
    void test();
}

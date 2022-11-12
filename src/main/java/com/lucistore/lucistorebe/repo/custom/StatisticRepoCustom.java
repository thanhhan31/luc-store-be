package com.lucistore.lucistorebe.repo.custom;
import java.util.List;

import com.lucistore.lucistorebe.controller.payload.dto.statistic.StatisticDTO;
import com.lucistore.lucistorebe.utility.EStatisticType;
public interface StatisticRepoCustom {
    List<StatisticDTO> statistic(
        List<Long> idBuyers,
        List<Long> idAdmins,
        Integer month,
        Integer quarter,
        Integer year,
        EStatisticType type);
}

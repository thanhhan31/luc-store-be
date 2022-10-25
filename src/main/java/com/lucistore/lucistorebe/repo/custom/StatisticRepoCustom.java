package com.lucistore.lucistorebe.repo.custom;
import java.util.List;

import com.lucistore.lucistorebe.controller.payload.dto.statistic.StatisticDTO;
public interface StatisticRepoCustom {
    List<StatisticDTO> statistic(
        List<Long> idBuyers,
        List<Long> idAdmins,
        Integer month,
        Integer year);
}

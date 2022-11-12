package com.lucistore.lucistorebe.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.controller.payload.dto.statistic.StatisticDTO;
import com.lucistore.lucistorebe.controller.payload.response.ListResponse;
import com.lucistore.lucistorebe.entity.user.User;
import com.lucistore.lucistorebe.repo.ProductRepo;
import com.lucistore.lucistorebe.repo.ProductVariationRepo;
import com.lucistore.lucistorebe.repo.custom.StatisticRepoCustom;
import com.lucistore.lucistorebe.service.util.ServiceUtils;
import com.lucistore.lucistorebe.utility.EStatisticType;
import com.lucistore.lucistorebe.utility.EUserRole;

@Service
public class StatisticService {
	@Autowired
	ProductRepo productRepo;
	
	@Autowired
	ProductVariationRepo productVariationRepo;
	
	@Autowired
	StatisticRepoCustom statisticRepo;
	
	@Autowired
	ServiceUtils serviceUtils;
	
	public ListResponse<StatisticDTO> statistic(
			List<Long> idBuyers,
			List<Long> idAdmins,
			Integer month,
			Integer quarter,
			Integer year,
			EStatisticType type,
			User user) {

		if(EUserRole.valueOf(user.getRole().getName()) != EUserRole.ADMIN){
			idAdmins.clear();
			idAdmins.add(user.getId());
		}

		if(month == null && quarter == null && type == null){
			throw new InvalidInputDataException("You must specify type of statistic if statistic by year");
		}

		List<StatisticDTO> os = statisticRepo.statistic(idBuyers, idAdmins, month, quarter, year, type);
		return serviceUtils.convertToListResponse(os, StatisticDTO.class);
	}
}

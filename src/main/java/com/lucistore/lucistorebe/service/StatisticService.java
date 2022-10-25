package com.lucistore.lucistorebe.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.payload.dto.statistic.StatisticDTO;
import com.lucistore.lucistorebe.controller.payload.response.ListResponse;
import com.lucistore.lucistorebe.entity.user.User;
import com.lucistore.lucistorebe.repo.ProductRepo;
import com.lucistore.lucistorebe.repo.ProductVariationRepo;
import com.lucistore.lucistorebe.repo.custom.StatisticRepoCustom;
import com.lucistore.lucistorebe.service.util.ServiceUtils;
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
			Integer year,
			User user) {

		// if(EUserRole.valueOf(user.getRole().getName()) != EUserRole.ADMIN){
		// 	idAdmins.clear();
		// 	idAdmins.add(user.getId());
		// }
		List<StatisticDTO> os = statisticRepo.statistic(idBuyers, idAdmins, null, null);
		return serviceUtils.convertToListResponse(os, StatisticDTO.class);
	}
}

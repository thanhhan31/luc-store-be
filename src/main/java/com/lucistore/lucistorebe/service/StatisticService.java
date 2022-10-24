package com.lucistore.lucistorebe.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.payload.dto.statistic.OrderStatistic;
import com.lucistore.lucistorebe.controller.payload.dto.statistic.StatisticDTO;
import com.lucistore.lucistorebe.controller.payload.response.DataResponse;
import com.lucistore.lucistorebe.entity.user.User;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
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
	
	public DataResponse<StatisticDTO> statistic(
			List<Long> idProducts,
			List<Long> idProductVariations,
			List<Long> idBuyers,
			List<Long> idAdmins,
			Date importDateFrom,
			Date importDateTo,
			User user) {

		if(EUserRole.valueOf(user.getRole().getName()) != EUserRole.ADMIN){
			idAdmins.clear();
			idAdmins.add(user.getId());
		}
		List<OrderStatistic> os = statisticRepo.getOrderStatistic(idProducts, idProductVariations, idBuyers, idAdmins, importDateFrom, importDateTo);

		StatisticDTO result = new StatisticDTO();

		List<Buyer> buyers = new ArrayList<>();
		os.stream().forEach(o -> {
			result.setNProduct(result.getNProduct() + o.getNProduct());
			result.setIncome(result.getIncome() + o.getOrder().getPayPrice());
			if(!buyers.contains(o.getOrder().getBuyer())){
				buyers.add(o.getOrder().getBuyer());
			}
		});
		result.setNBuyer(Long.valueOf(buyers.size()));
		result.setNOrder(Long.valueOf(os.size()));

		return serviceUtils.convertToDataResponse( result, StatisticDTO.class);
	}
}

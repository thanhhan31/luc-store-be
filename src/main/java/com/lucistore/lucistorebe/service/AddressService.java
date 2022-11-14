package com.lucistore.lucistorebe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.payload.dto.address.forward.AddressDistrictDTO;
import com.lucistore.lucistorebe.controller.payload.dto.address.forward.AddressDistrictDetailDTO;
import com.lucistore.lucistorebe.controller.payload.dto.address.forward.AddressProvinceCityDTO;
import com.lucistore.lucistorebe.controller.payload.dto.address.forward.AddressProvinceCityDetailDTO;
import com.lucistore.lucistorebe.controller.payload.response.DataResponse;
import com.lucistore.lucistorebe.controller.payload.response.ListResponse;
import com.lucistore.lucistorebe.repo.address.AddressDistrictRepo;
import com.lucistore.lucistorebe.repo.address.AddressProvinceCityRepo;
import com.lucistore.lucistorebe.repo.address.AddressWardRepo;
import com.lucistore.lucistorebe.service.util.ServiceUtils;

@Service
public class AddressService {
	
	@Autowired
	AddressProvinceCityRepo addressProvinceCityRepo;
	
	@Autowired
	AddressDistrictRepo addressDistrictRepo;
	
	@Autowired
	AddressWardRepo addressWardRepo;
	
	@Autowired
	ServiceUtils serviceUtils;
	
	public ListResponse<AddressProvinceCityDTO> getProvinceCityList() {
		return serviceUtils.convertToListResponse(
				addressProvinceCityRepo.findAll(),
				AddressProvinceCityDTO.class
			);
	}
	
	public DataResponse<AddressProvinceCityDetailDTO> getProvinceCityById(Long id) {
		return serviceUtils.convertToDataResponse(
				addressProvinceCityRepo.findById(id),
				AddressProvinceCityDetailDTO.class
			);
	}
	
	public DataResponse<AddressDistrictDetailDTO> getDistrictById(Long id) {
		return serviceUtils.convertToDataResponse(
				addressDistrictRepo.findById(id),
				AddressDistrictDetailDTO.class
			);
	}
}

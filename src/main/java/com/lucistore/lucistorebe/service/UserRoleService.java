package com.lucistore.lucistorebe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.payload.dto.UserRoleDTO;
import com.lucistore.lucistorebe.controller.payload.request.UpdateUserRolePermissionRequest;
import com.lucistore.lucistorebe.controller.payload.response.DataResponse;
import com.lucistore.lucistorebe.entity.user.UserRole;
import com.lucistore.lucistorebe.repo.UserRoleRepo;
import com.lucistore.lucistorebe.service.util.ServiceUtils;
import com.lucistore.lucistorebe.utility.EUserRole;

@Service
public class UserRoleService {
	@Autowired
	UserRoleRepo userRoleRepo;
	
	@Autowired
	ServiceUtils serviceUtils;
	
	public DataResponse<UserRoleDTO> getSaleAdminPermission() {
		return serviceUtils.convertToDataResponse(userRoleRepo.getReferenceById(EUserRole.SALE_ADMIN.toString()), UserRoleDTO.class);
	}
	
	public DataResponse<UserRoleDTO> updateSaleAdminPermission(UpdateUserRolePermissionRequest data) { // update sale admin permission
		UserRole saleAdminRole = userRoleRepo.getReferenceById(EUserRole.SALE_ADMIN.toString());
		saleAdminRole.setPermissions(data.getPermissions());
		return serviceUtils.convertToDataResponse(userRoleRepo.save(saleAdminRole), UserRoleDTO.class);
	}
}

package com.lucistore.lucistorebe.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.payload.dto.UserRoleDTO;
import com.lucistore.lucistorebe.controller.payload.request.UpdateUserRolePermissionRequest;
import com.lucistore.lucistorebe.controller.payload.response.DataResponse;
import com.lucistore.lucistorebe.entity.user.UserRole;
import com.lucistore.lucistorebe.repo.UserRoleRepo;
import com.lucistore.lucistorebe.service.util.ServiceUtils;
import com.lucistore.lucistorebe.utility.ERolePermission;
import com.lucistore.lucistorebe.utility.EUserRole;

@Service
public class UserRoleService {
	@Autowired
	LogService logService;
	
	@Autowired
	UserRoleRepo userRoleRepo;
	
	@Autowired
	ServiceUtils serviceUtils;
	
	public DataResponse<UserRoleDTO> getSaleAdminPermission() {
		return serviceUtils.convertToDataResponse(userRoleRepo.getReferenceById(EUserRole.SALE_ADMIN.toString()), UserRoleDTO.class);
	}
	
	public DataResponse<UserRoleDTO> updateSaleAdminPermission(Long idUser, UpdateUserRolePermissionRequest data) { // update sale admin permission
		UserRole saleAdminRole = userRoleRepo.getReferenceById(EUserRole.SALE_ADMIN.toString());
		
		String oldPermissions = permissionsToString(saleAdminRole.getPermissions());
		String newPermissions = permissionsToString(data.getPermissions());
		saleAdminRole.setPermissions(data.getPermissions());
		
		logService.logInfo(idUser, 
				String.format("Change sale admin permission from (%s) to (%s)", oldPermissions, newPermissions));
		
		return serviceUtils.convertToDataResponse(userRoleRepo.save(saleAdminRole), UserRoleDTO.class);
	}
	
	private String permissionsToString(Set<ERolePermission> permissions) {
		String ret = "";
		for (var permission : permissions) {
			ret = ret.concat(permission.toString() + ", ");
		}
		return ret.substring(0, ret.length() - 2);
	}
}

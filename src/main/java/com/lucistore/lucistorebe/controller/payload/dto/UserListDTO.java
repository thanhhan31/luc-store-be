package com.lucistore.lucistorebe.controller.payload.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserListDTO {
	private List<UserDTO> admins;
	private List<SaleAdminDTO> saleAdmins;
	private List<BuyerDTO> buyers;
}

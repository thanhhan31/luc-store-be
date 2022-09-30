package com.lucistore.lucistorebe.controller.payload.request;

import java.util.Set;

import javax.validation.constraints.NotNull;

import com.lucistore.lucistorebe.utility.EAdministrativePermission;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class UpdateUserRolePermissionRequest {
	@NotNull
	private Set<EAdministrativePermission> permissions;
}

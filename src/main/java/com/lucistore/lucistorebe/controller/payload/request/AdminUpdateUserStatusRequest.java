package com.lucistore.lucistorebe.controller.payload.request;

import javax.validation.constraints.NotNull;
import com.lucistore.lucistorebe.utility.EUserStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class AdminUpdateUserStatusRequest {
	@NotNull
	private EUserStatus status;
}

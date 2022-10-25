package com.lucistore.lucistorebe.controller.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class NotificationDTO {
	private Long id;
	private Long idUser;
	
	private String title;
	private String content;
	private Boolean seen;
}
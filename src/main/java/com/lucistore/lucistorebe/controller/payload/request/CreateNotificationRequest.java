package com.lucistore.lucistorebe.controller.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class CreateNotificationRequest {
	private Long idUser;
	private String title;
	private String content;
}

package com.lucistore.lucistorebe.controller.payload.dto;

import java.util.Date;

import com.lucistore.lucistorebe.utility.ELogType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LogDTO {
	private Long id;
	private String username;
	private Date date;
	private ELogType logType;
	private String content;
}

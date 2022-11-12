package com.lucistore.lucistorebe.utility.filter;

import java.util.Date;

import com.lucistore.lucistorebe.utility.ELogType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class LogFilter {
	Long idUser;
	Date beginDate;
	Date endDate;
	ELogType logType;
	String searchContent;
	
	public LogFilter(Long idUser, Date beginDate, Date endDate, ELogType logType, String searchContent) {
		this.idUser = idUser;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.logType = logType;
		this.searchContent = searchContent;
	}
}

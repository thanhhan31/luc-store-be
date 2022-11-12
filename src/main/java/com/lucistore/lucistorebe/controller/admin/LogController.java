package com.lucistore.lucistorebe.controller.admin;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lucistore.lucistorebe.service.LogService;
import com.lucistore.lucistorebe.utility.ELogType;
import com.lucistore.lucistorebe.utility.filter.LogFilter;
import com.lucistore.lucistorebe.utility.filter.PagingInfo;

@RestController
@RequestMapping("/api/admin/log")
public class LogController {
	@Autowired
	LogService logService;
	
	@GetMapping
	public ResponseEntity<?> get(
			@RequestParam(required = false) 
				Long idUser,
			@RequestParam(required = false) 
				Date beginDate,
			@RequestParam(required = false) 
				Date endDate,
			@RequestParam(required = false)
				ELogType logType,
			@RequestParam(required = false) 
				String searchContent,
			
			@RequestParam(required = false) 
				Integer page,
			@RequestParam(required = false) 
				Integer size,
			@RequestParam(required = false)
				Boolean sortByDateDescending) {
		
		return ResponseEntity.ok(
			logService.search(
					new LogFilter(
						idUser,
						beginDate,
						endDate, 
						logType, 
						searchContent
					),
					new PagingInfo(page, size, null, sortByDateDescending)
				)
			);
	}
}

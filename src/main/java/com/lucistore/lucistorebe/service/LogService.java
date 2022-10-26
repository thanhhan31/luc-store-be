package com.lucistore.lucistorebe.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.payload.dto.LogDTO;
import com.lucistore.lucistorebe.controller.payload.dto.ProductGeneralDetailDTO;
import com.lucistore.lucistorebe.controller.payload.response.ListWithPagingResponse;
import com.lucistore.lucistorebe.entity.Log;
import com.lucistore.lucistorebe.repo.LogRepo;
import com.lucistore.lucistorebe.repo.UserRepo;
import com.lucistore.lucistorebe.service.util.ServiceUtils;
import com.lucistore.lucistorebe.utility.ELogType;
import com.lucistore.lucistorebe.utility.EProductStatus;
import com.lucistore.lucistorebe.utility.PageWithJpaSort;

@Service
public class LogService {
	@Autowired
	LogRepo logRepo;
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	ServiceUtils serviceUtils;
	
	public void logInfo(Long idUser, String content) {		
		logRepo.save(new Log(
				userRepo.getReferenceById(idUser),
				content,
				ELogType.INFORMATION
			)
		);
	}
	
	public void logWarning(Long idUser, String content) {		
		logRepo.save(new Log(
				userRepo.getReferenceById(idUser),
				content,
				ELogType.WARNING
			)
		);
	}
	
	public ListWithPagingResponse<LogDTO> search(Long idUser, Date beginDate, Date endDate, ELogType logType, String searchContent, 
			Integer currentPage, Integer size, Sort sort) {
		
		int count = logRepo.searchCount(idUser, beginDate, endDate, logType, searchContent).intValue();
		PageWithJpaSort page = new PageWithJpaSort(currentPage, size, count, sort);
		
		return serviceUtils.convertToListResponse(
				logRepo.search(idUser, beginDate, endDate, logType, searchContent, page),
				LogDTO.class, 
				page
			);
	}
}

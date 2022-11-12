package com.lucistore.lucistorebe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.payload.dto.LogDTO;
import com.lucistore.lucistorebe.controller.payload.response.ListWithPagingResponse;
import com.lucistore.lucistorebe.entity.Log;
import com.lucistore.lucistorebe.repo.LogRepo;
import com.lucistore.lucistorebe.repo.UserRepo;
import com.lucistore.lucistorebe.service.util.ServiceUtils;
import com.lucistore.lucistorebe.utility.ELogType;
import com.lucistore.lucistorebe.utility.filter.LogFilter;
import com.lucistore.lucistorebe.utility.filter.PagingInfo;

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
	
	public ListWithPagingResponse<LogDTO> search(LogFilter filter, PagingInfo pagingInfo) {
		return serviceUtils.convertToListResponse(
				logRepo.search(filter, pagingInfo),
				LogDTO.class
			);
	}
}

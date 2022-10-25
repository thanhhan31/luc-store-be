package com.lucistore.lucistorebe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.controller.payload.dto.NotificationDTO;
import com.lucistore.lucistorebe.controller.payload.response.DataResponse;
import com.lucistore.lucistorebe.controller.payload.response.ListResponse;
import com.lucistore.lucistorebe.entity.Notification;
import com.lucistore.lucistorebe.repo.NotificationRepo;
import com.lucistore.lucistorebe.repo.UserRepo;
import com.lucistore.lucistorebe.service.util.ServiceUtils;


@Service
public class NotificationService {
    @Autowired
    NotificationRepo notificationRepo;

    @Autowired
    UserRepo userRepo;
	
	@Autowired
	ServiceUtils serviceUtils;
	
	public ListResponse<NotificationDTO> getAllByIdUser(Long idUser) {
		if(!userRepo.existsById(idUser)) {
			throw new InvalidInputDataException("No user found with given id " + idUser);
		}

        return serviceUtils.convertToListResponse(
                    notificationRepo.findAllByUserId(idUser),NotificationDTO.class);
	}
	
	public DataResponse<NotificationDTO> create(Long idUser, String title, String content) {

        Notification notification = new Notification(
            userRepo.getReferenceById(idUser),
            title,
            content,
            false
        );
		
		return serviceUtils.convertToDataResponse(notificationRepo.save(notification), NotificationDTO.class);
	}
}

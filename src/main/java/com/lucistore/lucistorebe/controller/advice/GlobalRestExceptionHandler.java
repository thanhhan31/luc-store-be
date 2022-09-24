package com.lucistore.lucistorebe.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.controller.advice.exception.LoginException;
import com.lucistore.lucistorebe.controller.advice.exception.RemoteUploadException;
import com.lucistore.lucistorebe.controller.payload.response.BaseResponse;

@RestControllerAdvice
public class GlobalRestExceptionHandler {
	@ExceptionHandler(InvalidInputDataException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public BaseResponse invalidInputDataHandler(InvalidInputDataException e) {
		return new BaseResponse(false, e.getMessage());
	}
	
	@ExceptionHandler(RemoteUploadException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public BaseResponse remoteUploadExceptionHandler(RemoteUploadException e) {
		return new BaseResponse(false, e.getMessage());
	}
	
	@ExceptionHandler(LoginException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public BaseResponse loginExceptionHandler(LoginException e) {
		return new BaseResponse(false, e.getMessage());
	}
	
}

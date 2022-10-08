package com.lucistore.lucistorebe.controller.advice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.core.JsonParseException;
import com.lucistore.lucistorebe.controller.advice.exception.DataConflictException;
import com.lucistore.lucistorebe.controller.advice.exception.InvalidInputDataException;
import com.lucistore.lucistorebe.controller.advice.exception.LoginException;
import com.lucistore.lucistorebe.controller.advice.exception.RemoteUploadException;
import com.lucistore.lucistorebe.controller.payload.response.BaseResponse;
import com.lucistore.lucistorebe.controller.payload.response.DataResponse;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
	
	@ExceptionHandler(DataConflictException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public BaseResponse dataConflictException(InvalidInputDataException e) {
		return new BaseResponse(false, e.getMessage());
	}
	
	@ExceptionHandler(InvalidInputDataException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public BaseResponse invalidInputDataHandler(InvalidInputDataException e) {
		return new BaseResponse(false, e.getMessage());
	}
	
	@ExceptionHandler(JsonParseException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public BaseResponse jsonParseExceptionHandler(JsonParseException e) {
		return new BaseResponse(false, "Json format is invalid");
		//return new BaseResponse(false, e.getMessage());
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
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public DataResponse<Map<String, String>> invalidDataFormat(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		List<FieldError> fieldErrors = ex.getFieldErrors();
		for (FieldError fieldError : fieldErrors) {
			errors.put(fieldError.getField(), fieldError.getDefaultMessage());
		}
		return new DataResponse<>(false, "Data format invalid and violate constrains", errors);
	}
}

package com.lucistore.lucistorebe.controller.advice.exception;

public class DataConflictException extends RuntimeException {
	private static final long serialVersionUID = -3782289578177020342L;

	public DataConflictException(String message) {
		super(message);
	}
}

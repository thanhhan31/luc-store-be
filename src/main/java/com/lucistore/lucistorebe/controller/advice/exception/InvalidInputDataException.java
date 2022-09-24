package com.lucistore.lucistorebe.controller.advice.exception;

public class InvalidInputDataException extends RuntimeException {
	private static final long serialVersionUID = -3782289578177020342L;

	public InvalidInputDataException(String message) {
		super(message);
	}
}

package com.carbon.todobackend.exception;

import java.io.Serial;


public class NotFoundException extends RuntimeException {
 
	@Serial
	private static final long serialVersionUID = 1L;

	public NotFoundException(final String message) {
		super(message);
	}
}

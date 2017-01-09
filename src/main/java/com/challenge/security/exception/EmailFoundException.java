package com.challenge.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason = "E-mail já existente")
public class EmailFoundException extends AuthenticationException {
	
	private static final long serialVersionUID = 1L;

	public EmailFoundException(String msg) {
		super(msg);
	}

	public EmailFoundException(String msg, Throwable t) {
		super(msg, t);
	}
}
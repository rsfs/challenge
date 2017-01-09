package com.challenge.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.REQUEST_TIMEOUT, reason = "Sessão inválida")
public class InvalidSessionException extends AuthenticationException {
	
	private static final long serialVersionUID = 1L;

	public InvalidSessionException(String msg) {
		super(msg);
	}

	public InvalidSessionException(String msg, Throwable t) {
		super(msg, t);
	}
}
package com.challenge.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason = "Usuário e/ou senha inválidos")
public class EmailNotFoundException extends AuthenticationException {
	
	private static final long serialVersionUID = 1L;

	public EmailNotFoundException(String msg) {
		super(msg);
	}

	public EmailNotFoundException(String msg, Throwable t) {
		super(msg, t);
	}
}
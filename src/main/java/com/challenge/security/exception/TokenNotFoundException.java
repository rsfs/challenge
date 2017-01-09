package com.challenge.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason = "Não autorizado")
public class TokenNotFoundException extends AuthenticationException {
	
	private static final long serialVersionUID = 1L;

	public TokenNotFoundException(String msg) {
		super(msg);
	}

	public TokenNotFoundException(String msg, Throwable t) {
		super(msg, t);
	}
}
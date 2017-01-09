package com.challenge.security.service;

import java.io.Serializable;

import com.challenge.security.JwtUser;

public class JwtUserAuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private final JwtUser jwtUser;

    public JwtUserAuthenticationResponse(JwtUser jwtUser) {
        this.jwtUser = jwtUser;
    }

	public JwtUser getJwtUser() {
		return jwtUser;
	}
}
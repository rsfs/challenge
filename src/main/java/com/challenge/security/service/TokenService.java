package com.challenge.security.service;

import java.util.Date;
import java.util.List;

import com.challenge.model.security.Token;
import com.challenge.model.security.User;

public interface TokenService {

	Token saveToken(String token, Date date, User user);
	
	Token loadTokenByNumber(String token);

	Token loadTokenByUser(User user);
	
	Token loadLastTokenCreated(List<Token> t);

}

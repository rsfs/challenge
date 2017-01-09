package com.challenge.security.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.challenge.model.security.Token;
import com.challenge.model.security.User;
import com.challenge.security.repository.TokenRepository;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenRepository tokenRepository;

	@Override
	public Token saveToken(String token, Date date, User user) {
		return tokenRepository.save(new Token(token,date,user));
	}

	@Override
	public Token loadTokenByNumber(String token) {
		List<Token> t = tokenRepository.findByNumber(token);
		return loadLastTokenCreated(t);
	}

	public Token loadLastTokenCreated(List<Token> t) {
		if(t == null || t.isEmpty()){
			return null;
		}
		
		Set<Token> orderedList = new HashSet<Token>();
		
		t
        .stream()
        .sorted((e1, e2) -> e1.getCreationDate()
                .compareTo(e2.getCreationDate())).forEachOrdered(orderedList::add);
		
		Stream<Token> stream = orderedList.stream();
		Token lastToken = stream.reduce((a, b) -> b).orElse(null);
		return lastToken;
	}

	@Override
	public Token loadTokenByUser(User user) {
		List<Token> t = tokenRepository.findByUser(user);
		return (t == null || t.isEmpty() ) ? null : t.get(0);
	}
    
}

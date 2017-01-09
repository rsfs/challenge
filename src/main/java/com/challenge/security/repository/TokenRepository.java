package com.challenge.security.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.challenge.model.security.Token;
import com.challenge.model.security.User;

@RepositoryRestResource
public interface TokenRepository extends CrudRepository<Token, Long> {

	@SuppressWarnings("unchecked")
	Token save(Token token);
	
	List<Token> findByNumber(String number);
	
	List<Token> findByUser(User user);
	
}

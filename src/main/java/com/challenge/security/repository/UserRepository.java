package com.challenge.security.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.challenge.model.security.User;

@RepositoryRestResource
public interface UserRepository extends CrudRepository<User, Long> {

	User findByEmail(String email);
	
	User findByPassword(String password);

	@Query("select u from User u where u.email = ?1 and u.password = ?2")
	User findByEmailAndPassword(String email, String password);

	User findByUsername(String username);
	
}

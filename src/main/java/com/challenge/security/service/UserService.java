/**
 * 
 */
package com.challenge.security.service;

import java.util.List;

import com.challenge.model.security.User;
import com.challenge.security.exception.EmailFoundException;
import com.challenge.security.exception.EmailNotFoundException;
import com.challenge.security.exception.PasswordNotFoundException;

/**
 * @author rafael
 *
 */
public interface UserService {
	
	void validateUserByEmail(String email) throws EmailFoundException, EmailNotFoundException;

	List<User> loadUsers();
	
	User saveUser(User user);

	void validateUserByPassword(String password) throws PasswordNotFoundException;
	
	User loadUserById(Long id);

	User loadUserByEmailAndPassword(String email, String password);

	User loadUserByEmail(String email);
	
}

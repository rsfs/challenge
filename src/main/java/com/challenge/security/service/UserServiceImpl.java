package com.challenge.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.challenge.model.security.User;
import com.challenge.security.exception.EmailFoundException;
import com.challenge.security.exception.EmailNotFoundException;
import com.challenge.security.exception.PasswordNotFoundException;
import com.challenge.security.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void validateUserByEmail(String email) throws EmailFoundException, EmailNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            throw new EmailFoundException(String.format("E-mail já existente '%s'.", email));
        } else{
        	throw new EmailNotFoundException(String.format("Usuário e/ou senha inválidos '%s'.", email));
        }         
    }

	@Override
	public void validateUserByPassword(String password) throws PasswordNotFoundException {
        User user = userRepository.findByPassword(password);
        if (user == null) {
            throw new PasswordNotFoundException(String.format("Usuário e/ou senha inválidos '%s'.", password));
        } 
	}    
    
    
    @Override
    public List<User> loadUsers() {
	    List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }


	@Override
	public User saveUser(User user) {
		return userRepository.save(user);
	}


	@Override
	public User loadUserById(Long id) {
		return userRepository.findOne(id);
	}


	@Override
	public User loadUserByEmailAndPassword(String email, String password) {
		return userRepository.findByEmailAndPassword(email, password);
	}

	@Override
	public User loadUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

    
}

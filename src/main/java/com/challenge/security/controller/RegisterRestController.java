package com.challenge.security.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.model.security.Authority;
import com.challenge.model.security.AuthorityName;
import com.challenge.model.security.Token;
import com.challenge.model.security.User;
import com.challenge.security.JwtTokenUtil;
import com.challenge.security.JwtUserFactory;
import com.challenge.security.exception.EmailFoundException;
import com.challenge.security.exception.EmailNotFoundException;
import com.challenge.security.service.JwtUserAuthenticationResponse;
import com.challenge.security.service.UserService;

@RestController
public class RegisterRestController {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserService userService;
	
	private final Log logger = LogFactory.getLog(this.getClass());

	@RequestMapping(value = "cadastro", method = RequestMethod.POST)
	public ResponseEntity<?> createUser(@RequestBody User user, Device device) throws EmailFoundException {

		logger.info("creating token for username: " + user.getUsername());
		logger.info("creating token for password: " + user.getPassword());

		validateUserEmail(user);
		
		if(user.getUsername() == null)
			setUserUsername(user);
		
		if(user.getAuthorities() == null || user.getAuthorities().isEmpty())
			setUserAuthorities(user);

		String tokenNumber = jwtTokenUtil.generateTokenByUsername(user.getUsername(), device);
		
		setUserToken(user, tokenNumber);
		
		encodePassword(user);
		
		User userSaved = userService.saveUser(prepareUserToSave(user));
		
		return ResponseEntity.ok(new JwtUserAuthenticationResponse(JwtUserFactory.create(userSaved, tokenNumber)));
	}

	private void setUserToken(User user, String tokenNumber) {
		Set<Token> tokens = new HashSet<Token>();
		tokens.add(new Token(tokenNumber, Calendar.getInstance().getTime(), user));
		user.setTokens(tokens);
	}

	private void encodePassword(User user) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(hashedPassword);
	}

	private void setUserAuthorities(User user) {
		List<Authority> authorities = new ArrayList<Authority>();
		authorities.add(new Authority(AuthorityName.ROLE_MASTER));
		authorities.add(new Authority(AuthorityName.ROLE_USER));
	}

	private void setUserUsername(User user) {
		int index = user.getEmail().indexOf("@");
		user.setUsername(user.getEmail().substring(0, index));
	}

	private void validateUserEmail(User user) throws EmailFoundException {
		try {
			userService.validateUserByEmail(user.getEmail());
		} catch (EmailFoundException e) {
			throw e;
		} catch (EmailNotFoundException e) {
			logger.info("new user email: " + user.getEmail());
		}
	}

	private User prepareUserToSave(User user) {
		user.setEnabled(Boolean.TRUE);
		user.setCreationDate(Calendar.getInstance().getTime());
		user.setModificationDate(Calendar.getInstance().getTime());
		user.setLastLoginDate(Calendar.getInstance().getTime());
		return user;
	}

}

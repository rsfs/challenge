package com.challenge.security.controller;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.model.security.Token;
import com.challenge.model.security.User;
import com.challenge.security.JwtAuthenticationRequest;
import com.challenge.security.JwtUser;
import com.challenge.security.JwtUserFactory;
import com.challenge.security.exception.EmailFoundException;
import com.challenge.security.exception.PasswordNotFoundException;
import com.challenge.security.exception.TokenNotFoundException;
import com.challenge.security.exception.UserNotFoundException;
import com.challenge.security.service.JwtUserAuthenticationResponse;
import com.challenge.security.service.TokenService;
import com.challenge.security.service.UserService;

@RestController
public class LoginRestController {

	@Value("${jwt.header}")
	private String tokenHeader;

	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenService tokenService;

	private final Log logger = LogFactory.getLog(this.getClass());

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestBody JwtAuthenticationRequest authenticationRequest, Device device)
			throws AuthenticationException {

		logger.info("creating authentication for email: " + authenticationRequest.getEmail());
		logger.info("creating authentication for password: " + authenticationRequest.getPassword());

		validateUserEmail(authenticationRequest);
		
		User user = userService.loadUserByEmail(authenticationRequest.getEmail());
		
		validateUserPassword(authenticationRequest, user);
		
		return ResponseEntity.ok(new JwtUserAuthenticationResponse(JwtUserFactory.create(user,returnTokenNumber(user))));
	}

	private void validateUserPassword(JwtAuthenticationRequest authenticationRequest, User user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();  
		if(!encoder.matches(authenticationRequest.getPassword(), user.getPassword()))
			throw new PasswordNotFoundException(String.format("Usuário e/ou senha inválidos '%s'.", user.getPassword()));
	}

	private void validateUserEmail(JwtAuthenticationRequest authenticationRequest) {
		try {
			userService.validateUserByEmail(authenticationRequest.getEmail());
		} catch (EmailFoundException e) {
			logger.info("usuario localizado para email: " + authenticationRequest.getEmail());
		}
	}
	
	private String returnTokenNumber(User user) {
		if(user.getTokens() == null && user.getTokens().isEmpty())
			return "";
		
		Token t = tokenService.loadLastTokenCreated(new ArrayList<Token>(user.getTokens()) );
		return t != null ? t.getNumber() : "";
	}

	@RequestMapping(value = "perfil", method = RequestMethod.POST)
	public JwtUser getAuthenticatedUser(HttpServletRequest request, @PathParam("idUsuario") Long idUsuario) {
		String tokenNumber = request.getHeader(tokenHeader);
		logger.info("token header: " + tokenNumber);
		Token tokenLoad = tokenService.loadTokenByNumber(tokenNumber);
		String tokenModelo = tokenLoad != null ? tokenLoad.getNumber() : null;
		if (tokenNumber == null || tokenModelo == null)
			throw new TokenNotFoundException(String.format("Não autorizado"));

		User user = userService.loadUserById(idUsuario);
		if(user == null)
			throw new UserNotFoundException(String.format("Não localizado"));

		if (!tokenNumber.equals(tokenModelo)) {
			throw new TokenNotFoundException(String.format("Não autorizado"));
		} else {
			validateTokenSession(user.getLastLoginDate());
		}

		return JwtUserFactory.create(user, tokenNumber);
	}

	private void validateTokenSession(Date lastLoginDate) {
		Date iInicial = lastLoginDate;
		Date iFinal = Calendar.getInstance().getTime();
		Duration duracao = Duration.between(iInicial.toInstant(), iFinal.toInstant());
		if (duracao.toMinutes() >= 30)
			throw new TokenNotFoundException(String.format("Sessão inválida"));
	}

}

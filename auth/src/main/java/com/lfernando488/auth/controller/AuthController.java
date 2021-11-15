package com.lfernando488.auth.controller;

import static org.springframework.http.ResponseEntity.ok;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lfernando488.auth.DTO.UserDTO;
import com.lfernando488.auth.jwt.JwtTokenProvider;
import com.lfernando488.auth.repositories.UserRepository;

@RestController
@RequestMapping("/login")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;
	
	@Autowired
	public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
			UserRepository userRepository) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
		this.userRepository = userRepository;
	}
	
	@RequestMapping("/testeSecurity")
	public String teste(){
		return"testado";
	}
	
	@PostMapping(produces = {"application/json", "application/xml", "application/x-yaml"},
			consumes = {"application/json", "application/xml", "application/x-yaml"})
	public ResponseEntity<?> login (@RequestBody UserDTO userDTO) {
		try {
			var username = userDTO.getUserName();
			var password = userDTO.getPassword();
			
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO, password));
			
			var user = userRepository.findByUserName(username);
			var token = "";
			
			if(user!=null){
				token = jwtTokenProvider.createToken(username,user.getRoles());
			}else {
				throw new UsernameNotFoundException("Username not found00");
			}
			
			Map<Object, Object> model = new HashMap<>();
			model.put("username", username);
			model.put("token", token);
			
			return ok(model);
			
		}catch (AuthenticationException e) {
			throw new BadCredentialsException("Invalid username or password");
		}
	}
	
}

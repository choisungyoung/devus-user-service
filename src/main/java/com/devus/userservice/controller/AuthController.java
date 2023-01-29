package com.devus.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devus.userservice.common.exception.AuthenticationException;
import com.devus.userservice.common.exception.ValidationException;
import com.devus.userservice.controller.dto.ReqAuthLoinDTO;
import com.devus.userservice.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final UserService userService;

	@PostMapping("login")
	public ResponseEntity<?> login(@Validated @RequestBody ReqAuthLoinDTO dto, BindingResult bindingResult)
			throws Exception {
		if (bindingResult.hasErrors()) {
			throw new ValidationException(bindingResult);
		}

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPwd()));

		if (authentication == null) {
			throw new AuthenticationException("존재하지 않는 사용자입니다.");
		}
		
		return (ResponseEntity<?>) ResponseEntity.ok();
	}
}

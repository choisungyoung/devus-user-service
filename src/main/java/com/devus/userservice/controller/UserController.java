package com.devus.userservice.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devus.userservice.common.exception.ValidationException;
import com.devus.userservice.controller.dto.ReqUserSaveDTO;
import com.devus.userservice.controller.dto.ResUserFindDTO;
import com.devus.userservice.service.UserService;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

	private final UserService userService;

    private final PasswordEncoder passwordEncoder;
	
	//@GetMapping("")
	public ResponseEntity<?> findUserAll(@PageableDefault Pageable pageable) throws Exception {
		return ResponseEntity.ok(userService.findUser(pageable).map(p -> {
			return new ResUserFindDTO(p);
		}));
	}
	
	@GetMapping("/{email}")
	public ResponseEntity<?> findUser(@PathVariable @NotNull String email, BindingResult bindingResult) throws Exception {
		if (bindingResult.hasErrors()) {
			throw new ValidationException(bindingResult);
        }
		
		return ResponseEntity.ok(new ResUserFindDTO(userService.findUserByEmail(email)));
	}

	@PostMapping
	public ResponseEntity<?> saveUser(@Validated @RequestBody ReqUserSaveDTO dto, BindingResult bindingResult) throws Exception {
		if (bindingResult.hasErrors()) {
			throw new ValidationException(bindingResult);
        }
		
		dto.setPwd(passwordEncoder.encode(dto.getPwd()));
		
		return ResponseEntity.ok(new ResUserFindDTO(userService.saveUser(dto.toModel())));
	}
}

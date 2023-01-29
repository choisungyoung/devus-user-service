package com.devus.userservice.common.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.security.auth.login.AccountException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** 공통오류처리 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionControllerAdvice {

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<ErrorResponse> noSuchElementException(HttpServletRequest request, NoSuchElementException e) {
		log.error(e.toString());
		
		return new ResponseEntity<ErrorResponse>(
				ErrorResponse.builder()
					.code("E0005")
					.title("찾을 수 없음")
					.message(e.toString())
					.details(Collections.<String>emptyList())
					.build()
				, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(HttpServletRequest request, ValidationException e) {
		log.error(e.toString());
		BindingResult br = e.getBindingResult();
		String message = "입력값 검증 에러가 발생하였습니다.";
		List<String> details = new ArrayList<>();
		
		if (br.hasFieldErrors()) {
			message = br.getFieldErrors().get(0).getDefaultMessage();
			for (int i = 0; i < br.getFieldErrors().size(); i++) {
				details.add(br.getFieldErrors().get(i).getDefaultMessage());
			}
		}
		
		return new ResponseEntity<ErrorResponse>(
				ErrorResponse.builder()
					.code("E0004")
					.title("입력값 검증 에러")
					.message(message)
					.details(details)
					.build()
				, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorResponse> handleAuthenticationException(HttpServletRequest request, AuthenticationException e) {
		log.error(e.toString());

		return new ResponseEntity<ErrorResponse>(
				ErrorResponse.builder()
					.code(e.getCode())
					.title("인증에러 발생")
					.message(e.getMessage())
					.details(Arrays.stream(e.getStackTrace()).map(st -> {
						return st.toString();
					}).collect(Collectors.toList()))
					.build()
				, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(HttpServletRequest request, BusinessException e) {
		log.error(e.toString());

		return new ResponseEntity<ErrorResponse>(
				ErrorResponse.builder()
					.code(e.getCode())
					.title("업무처리 중 발생")
					.message(e.getMessage())
					.details(Arrays.stream(e.getStackTrace()).map(st -> {
						return st.toString();
					}).collect(Collectors.toList()))
					.build()
				, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(HttpServletRequest request, Exception e) {
		log.error(e.toString());

		return new ResponseEntity<ErrorResponse>(
				ErrorResponse.builder()
					.code("E0001")
					.title("에러 발생")
					.message(e.toString())
					.details(Arrays.stream(e.getStackTrace()).map(st -> {
						return st.toString();
					}).collect(Collectors.toList()))
					.build()
				, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
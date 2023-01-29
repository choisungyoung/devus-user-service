package com.devus.userservice.common.exception;

import lombok.Getter;

/**
 * 입력값검증 예외클래스
 */
@Getter
public class AuthenticationException extends Exception {

    private static final long serialVersionUID = 7654485645884452721L;

    private String code = "E0003";
    private String message;

    public AuthenticationException(String message) {
        this.message = message;
    }
    public AuthenticationException(String code, String message) {
    	this.code = code;
        this.message = message;
    }

}

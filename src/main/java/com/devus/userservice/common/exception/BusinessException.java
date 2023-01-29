package com.devus.userservice.common.exception;

import lombok.Getter;

/**
 * 입력값검증 예외클래스
 */
@Getter
public class BusinessException extends Exception {

    private static final long serialVersionUID = 7654485645884452721L;

    private String code = "E0002";
    private String message;

    public BusinessException(String message) {
        this.message = message;
    }
    public BusinessException(String code, String message) {
    	this.code = code;
        this.message = message;
    }

}

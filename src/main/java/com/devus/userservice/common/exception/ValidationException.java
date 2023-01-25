package com.devus.userservice.common.exception;

import org.springframework.validation.BindingResult;

import lombok.Getter;

/**
 * 입력값검증 예외클래스
 */

public class ValidationException extends Exception {

    private static final long serialVersionUID = 7654485645884452721L;

    @Getter
    private BindingResult bindingResult;

    public ValidationException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

}

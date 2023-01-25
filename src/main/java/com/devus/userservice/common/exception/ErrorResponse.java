package com.devus.userservice.common.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Data
public class ErrorResponse {

    private String code;
    private String title;
    private String message;
    private List<String> details;

    public ErrorResponse(String code, String title, String message, String detail) {
        this.code = code;
        this.title = title;
        this.message = message;
        this.details = new ArrayList<>();
        this.details.add(detail);
    }

    public ErrorResponse(String code, String title, String message, List<String> details) {
        this.code = code;
        this.title = title;
        this.message = message;
        this.details = details;
    }
}
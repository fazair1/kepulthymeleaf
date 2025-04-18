package com.juaracoding.kepulthymeleaf.handler;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class FeignCustomException extends RuntimeException {
    private HttpStatus status;
    private Map<String, Object> errorBody;

    public FeignCustomException(String message, HttpStatus status, Map<String, Object> errorBody) {
        super(message);
        this.status = status;
        this.errorBody = errorBody;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Map<String, Object> getErrorBody() {
        return errorBody;
    }

}

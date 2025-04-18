package com.juaracoding.kepulthymeleaf.handler;


public class ApiSubError {

    private String field;
    private String message;
    private Object rejectedValue;

    public ApiSubError(String field, String message, Object rejectedValue) {
        this.field = field;
        this.message = message;
        this.rejectedValue = rejectedValue;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }
}

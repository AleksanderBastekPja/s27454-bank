package com.example.s27454bank.exception;

import lombok.Getter;

@Getter
public class ValidationError extends RuntimeException {
    private final String field;
    private final String message;


    public ValidationError(String field, String message) {
        this.field = field;
        this.message = message;
    }
}

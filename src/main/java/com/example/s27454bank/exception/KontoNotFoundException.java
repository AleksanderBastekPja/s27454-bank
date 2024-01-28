package com.example.s27454bank.exception;

public class KontoNotFoundException extends RuntimeException {
    public KontoNotFoundException(String message) {
        super(message);
    }
}

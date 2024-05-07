package com.example.cloudservice.exception;

public class IllegalArgumentException extends RuntimeException {
    public IllegalArgumentException(String message) {
        super(message);
    }
    public IllegalArgumentException(String message,Exception e) {
        super(message);
    }
}

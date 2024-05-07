package com.example.cloudservice.exception;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException(String message) {
        super(message);
    }

    public UsernameNotFoundException(String localMessage, String message) {
        super(localMessage + " : " + message);
    }
}

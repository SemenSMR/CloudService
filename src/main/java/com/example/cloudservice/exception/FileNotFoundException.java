package com.example.cloudservice.exception;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException(String localMessage, String message) {
        super(localMessage + " : " + message);
    }
}
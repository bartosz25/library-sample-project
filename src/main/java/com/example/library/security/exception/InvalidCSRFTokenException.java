package com.example.library.security.exception;

public class InvalidCSRFTokenException extends Exception {

    public InvalidCSRFTokenException(String message) {
        super(message);
    }
}
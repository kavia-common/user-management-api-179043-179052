package com.example.usermanagementbackend.exception;

/**
 * Exception thrown for bad request errors (400).
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}

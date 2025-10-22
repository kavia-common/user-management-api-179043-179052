package com.example.usermanagementbackend.exception;

/**
 * Exception thrown when a requested resource is not found (404).
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}

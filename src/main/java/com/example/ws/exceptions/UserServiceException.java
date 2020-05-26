package com.example.ws.exceptions;

public class UserServiceException extends RuntimeException {
    private static final long serialVersionUID = -1341191274866387397L;

    public UserServiceException(String message) {
        super(message);
    }
}

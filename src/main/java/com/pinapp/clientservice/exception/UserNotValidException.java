package com.pinapp.clientservice.exception;

public class UserNotValidException extends RuntimeException {

    public UserNotValidException(String message) {
        super(message);
    }
}

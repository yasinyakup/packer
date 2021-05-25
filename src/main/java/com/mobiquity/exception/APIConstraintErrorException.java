package com.mobiquity.exception;

public class APIConstraintErrorException extends RuntimeException {
    public APIConstraintErrorException(String message) {
        super(message);
    }
}

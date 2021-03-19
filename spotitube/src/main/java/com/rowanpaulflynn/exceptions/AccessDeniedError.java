package com.rowanpaulflynn.exceptions;

public class AccessDeniedError extends RuntimeException{
    public AccessDeniedError(String message) {
        super(message);
    }
}

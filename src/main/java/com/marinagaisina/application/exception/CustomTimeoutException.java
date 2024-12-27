package com.marinagaisina.application.exception;

public class CustomTimeoutException extends RuntimeException {
    public CustomTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}

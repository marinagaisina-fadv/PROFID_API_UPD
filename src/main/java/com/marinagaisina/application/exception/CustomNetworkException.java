package com.marinagaisina.application.exception;

public class CustomNetworkException extends RuntimeException {
    public CustomNetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}

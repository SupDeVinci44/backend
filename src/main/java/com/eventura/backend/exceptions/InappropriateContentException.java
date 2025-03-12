package com.eventura.backend.exceptions;

public class InappropriateContentException extends RuntimeException {

    public InappropriateContentException(String message) {
        super(message);
    }

    public InappropriateContentException(String message, Throwable cause) {
        super(message, cause);
    }
}
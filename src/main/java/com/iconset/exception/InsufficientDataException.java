package com.iconset.exception;

/**
 * Exception thrown when there are insufficient icons to satisfy generation request.
 */
public class InsufficientDataException extends RuntimeException {

    public InsufficientDataException(String message) {
        super(message);
    }

    public InsufficientDataException(String message, Throwable cause) {
        super(message, cause);
    }
}

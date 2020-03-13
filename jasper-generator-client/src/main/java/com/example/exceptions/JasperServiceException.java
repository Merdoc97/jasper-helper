package com.example.exceptions;

/**
 */
public class JasperServiceException extends RuntimeException{

    public JasperServiceException() {
    }

    public JasperServiceException(String message) {
        super(message);
    }

    public JasperServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

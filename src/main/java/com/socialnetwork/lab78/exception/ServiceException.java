package com.socialnetwork.lab78.exception;

public class ServiceException extends RuntimeException {
    public ServiceException(String errorMessage, Throwable err) {
        super("ServiceException: " + errorMessage, err);
    }

    public ServiceException(String errorMessage) {
        super("ServiceException: " + errorMessage);
    }
}

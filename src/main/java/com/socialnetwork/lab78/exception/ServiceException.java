package com.socialnetwork.lab78.exception;

/**
 * Custom exception class for service-related errors.
 * Extends RuntimeException to indicate that it is an unchecked exception.
 */
public class ServiceException extends RuntimeException {

    /**
     * Constructs a new ServiceException with the specified error message and a cause.
     *
     * @param errorMessage The error message associated with the exception.
     * @param err The cause of the exception.
     */
    public ServiceException(String errorMessage, Throwable err) {
        // Call the constructor of the superclass (RuntimeException) with a formatted error message and the cause.
        super("ServiceException: " + errorMessage, err);
    }

    /**
     * Constructs a new ServiceException with the specified error message.
     *
     * @param errorMessage The error message associated with the exception.
     */
    public ServiceException(String errorMessage) {
        // Call the constructor of the superclass (RuntimeException) with a formatted error message.
        super("ServiceException: " + errorMessage);
    }
}
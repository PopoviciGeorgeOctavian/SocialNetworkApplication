package com.socialnetwork.lab78.exception;

/**
 * Custom exception class for repository-related errors.
 * Extends RuntimeException to indicate that it is an unchecked exception.
 */
public class RepositoryException extends RuntimeException {

    /**
     * Constructs a new RepositoryException with the specified error message.
     *
     * @param errorMessage The error message associated with the exception.
     */
    public RepositoryException(String errorMessage) {
        // Call the constructor of the superclass (RuntimeException) with a formatted error message.
        super("RepositoryException: " + errorMessage);
    }
}
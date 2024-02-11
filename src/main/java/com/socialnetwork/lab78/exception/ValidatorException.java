package com.socialnetwork.lab78.exception;

/**
 * Custom exception class for validation-related errors.
 * Extends RuntimeException to indicate that it is an unchecked exception.
 */
public class ValidatorException extends RuntimeException {

    /**
     * Constructs a new ValidatorException with the specified error message.
     *
     * @param errorMessage The error message associated with the exception.
     */
    public ValidatorException(String errorMessage) {
        // Call the constructor of the superclass (RuntimeException) with the provided error message.
        super(errorMessage);
    }
}
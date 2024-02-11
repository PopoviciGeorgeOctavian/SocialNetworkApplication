package com.socialnetwork.lab78.validators;

/**
 * The ValidationException class is a custom runtime exception used for handling validation errors.
 * It extends RuntimeException and provides constructors for different use cases.
 */
public class ValidationException extends RuntimeException {

    /**
     * Constructs a new ValidationException with no specified detail message.
     */
    public ValidationException() {
    }

    /**
     * Constructs a new ValidationException with the specified detail message.
     *
     * @param message The detail message.
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructs a new ValidationException with the specified detail message and cause.
     *
     * @param message The detail message.
     * @param cause   The cause (which is saved for later retrieval by the getCause() method).
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new ValidationException with the specified cause and a detail message of (cause==null ? null : cause.toString()) (which typically contains the class and detail message of cause).
     *
     * @param cause The cause (which is saved for later retrieval by the getCause() method).
     */
    public ValidationException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new ValidationException with the specified detail message, cause, suppression enabled or disabled, and writable stack trace enabled or disabled.
     *
     * @param message            The detail message.
     * @param cause              The cause (which is saved for later retrieval by the getCause() method).
     * @param enableSuppression  Whether or not suppression is enabled or disabled.
     * @param writableStackTrace Whether or not the stack trace should be writable.
     */
    public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
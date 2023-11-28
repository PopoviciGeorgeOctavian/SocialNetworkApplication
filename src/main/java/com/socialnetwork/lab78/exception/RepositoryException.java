package com.socialnetwork.lab78.exception;

public class RepositoryException extends RuntimeException {
    public RepositoryException(String errorMessage) {
        super("RepositoryException: " + errorMessage);
    }
}
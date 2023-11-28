package com.socialnetwork.lab78.validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
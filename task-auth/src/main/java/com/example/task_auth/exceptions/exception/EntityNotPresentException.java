package com.example.task_auth.exceptions.exception;

public class EntityNotPresentException extends RuntimeException {
    public EntityNotPresentException(String message) {
        super(message);
    }
}

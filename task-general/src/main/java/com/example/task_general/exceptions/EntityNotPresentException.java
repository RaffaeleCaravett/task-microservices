package com.example.task_general.exceptions;

public class EntityNotPresentException extends RuntimeException {
    public EntityNotPresentException(String message) {
        super(message);
    }
}

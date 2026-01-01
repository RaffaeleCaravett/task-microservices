package com.example.task_company.exceptions;

import org.springframework.validation.ObjectError;

import java.util.List;

public class WrongDTOException extends RuntimeException {
    public WrongDTOException(String message) {
        super(message);
    }

    List<ObjectError> messages;

    public WrongDTOException(List<ObjectError> messages) {
        this.messages = messages;
    }
}

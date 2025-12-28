package com.example.task_auth.exceptions.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.ObjectError;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SignupException extends RuntimeException {
    private String message;
    private List<ObjectError> messages;

    public SignupException(String message) {
        super(message);
    }

    public SignupException(List<ObjectError> messages) {
        this.messages = messages;
    }
}

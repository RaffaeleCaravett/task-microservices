package com.example.task_auth.exceptions;

import com.example.task_auth.dto.exceptionsDTO.ErrorWithListDTO;
import com.example.task_auth.exceptions.exception.SignupException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ExceptionsHandler {


    @ExceptionHandler(SignupException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorWithListDTO handleSignup(SignupException e) {
        if (e.getMessage().isEmpty()) {
            return new ErrorWithListDTO("", e.getMessages().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        }
        return new ErrorWithListDTO(e.getMessage(), new ArrayList<>());
    }
}

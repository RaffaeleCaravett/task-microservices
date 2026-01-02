package com.example.task_company.exceptions;

import com.example.task_company.dtos.exceptionsDTOS.ErrorWithListDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;

@RestControllerAdvice
public class ExceptionsHandler {


    @ExceptionHandler(SignupException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorWithListDTO handleSignup(SignupException e) {
        return new ErrorWithListDTO(e.getMessage(), new ArrayList<>());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorWithListDTO handleSignup(UnauthorizedException e) {
        return new ErrorWithListDTO(e.getMessage(), new ArrayList<>());
    }
}

package com.example.task_general.exceptions;

import com.example.task_general.dtos.exceptionsDTOS.ErrorWithListDTO;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;

@RestControllerAdvice
public class ExceptionsHandler {


    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorWithListDTO handleSignup(UnauthorizedException e) {
        return new ErrorWithListDTO(e.getMessage(), new ArrayList<>());
    }

    @ExceptionHandler(EntityNotPresentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorWithListDTO handleSignup(EntityNotPresentException e) {
        return new ErrorWithListDTO(e.getMessage(), new ArrayList<>());
    }
}

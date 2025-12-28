package com.example.task_general.dtos.exceptionsDTOS;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.ObjectError;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorWithListDTO {
    private String message;
    private List<String> messages;
}

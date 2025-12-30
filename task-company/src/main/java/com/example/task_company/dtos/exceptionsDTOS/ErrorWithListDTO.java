package com.example.task_company.dtos.exceptionsDTOS;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorWithListDTO {
    private String message;
    private List<String> messages;
}

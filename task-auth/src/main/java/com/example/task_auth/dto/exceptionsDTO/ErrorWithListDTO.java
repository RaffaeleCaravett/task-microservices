package com.example.task_auth.dto.exceptionsDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorWithListDTO {

    private String message;
    private List<String> messages;
}

package com.example.task_general.dtos.entitiesDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FirstUserLoginDTO {
    private String email;
    private String password;
    private String nome;
    private String cognome;
}

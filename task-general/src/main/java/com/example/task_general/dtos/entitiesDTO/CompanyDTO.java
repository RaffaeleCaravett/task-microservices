package com.example.task_general.dtos.entitiesDTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {
    @NotNull(message = "Campo necessario: Id")
    private Long id;
    @NotNull(message = "Campo necessario: Email")
    private String email;
    @NotNull(message = "Campo necessario: Nome")
    private String nomeAzienda;
}

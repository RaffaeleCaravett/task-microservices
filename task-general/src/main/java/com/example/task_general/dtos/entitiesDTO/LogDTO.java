package com.example.task_general.dtos.entitiesDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LogDTO(
        Long companyId,
        Long userId,
        @NotEmpty(message = "Tipo di log mancante")
        @NotBlank(message = "Tipo di log inappropriato")
        String logType,
        @NotNull(message="Id risorsa mancante")
        Long idRisorsa,
        @NotEmpty(message = "Nome risorsa mancante")
        @NotBlank(message = "Nome risorse inappropriato")
        String nomeRisorsa
) {
}

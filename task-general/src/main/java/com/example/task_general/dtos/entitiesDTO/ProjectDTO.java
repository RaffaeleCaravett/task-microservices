package com.example.task_general.dtos.entitiesDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    @NotEmpty(message = "Manca il titolo")
    private String title;
    @NotEmpty(message = "Manca la descrizione")
    private String description;
    private List<Long> developers;
    @NotNull(message = "Manca il manager")
    private Long managerId;
    @NotNull(message = "Mana l'azienda")
    private Long companyId;
    private Long typeId;
    private String state;
}

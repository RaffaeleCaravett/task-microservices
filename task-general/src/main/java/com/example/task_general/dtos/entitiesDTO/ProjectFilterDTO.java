package com.example.task_general.dtos.entitiesDTO;

public record ProjectFilterDTO(
        String projectName,
        String state,
        Long type,
        String manager,
        String description,
        String date
) {
}

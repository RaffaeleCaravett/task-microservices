package com.example.task_general.dtos.entitiesDTO;

import lombok.NoArgsConstructor;


public record UserLightFilters(
        String email,
        String fullname,
        String status
) {
}

package com.example.task_general.dtos.entitiesDTO;

public record LogFilters(
        Long resourceId,
        Long milestoneId,
        String text,
        String resourceName,
        String creationDateFrom,
        String creationDateTo,
        String creationTimeFrom,
        String creationTimeTo,
        String logType
) {
}

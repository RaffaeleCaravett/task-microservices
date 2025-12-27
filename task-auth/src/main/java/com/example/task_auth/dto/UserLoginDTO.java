package com.example.task_auth.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserLoginDTO(
        @NotEmpty(message = "Email necessaria")
        String email,
        @NotEmpty(message = "Password necessaria")
        String password
) {
}

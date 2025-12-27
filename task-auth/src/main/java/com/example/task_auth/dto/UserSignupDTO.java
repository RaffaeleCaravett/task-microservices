package com.example.task_auth.dto;

public record UserSignupDTO(
        String nome,
        String cognome,
        String email,
        String password){
}

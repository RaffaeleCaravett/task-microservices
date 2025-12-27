package com.example.task_auth.dto;

import com.example.task_auth.image.Image;
import com.example.task_auth.utils.Role;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String nome;
    private String cognome;
    private String email;
    private List<Image> image;
    private LocalDate createdAt;
    private LocalDate deletedAt;
    private Boolean isActive;
    private Boolean isConfirmed;
    private Role role;
}

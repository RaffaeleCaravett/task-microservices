package com.example.task_auth.dto;

import com.example.task_auth.utils.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private String password;
    private Boolean isActive;
    private Boolean isConfirmed;
    private Role role;
}

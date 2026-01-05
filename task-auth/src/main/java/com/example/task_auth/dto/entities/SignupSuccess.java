package com.example.task_auth.dto.entities;

import com.example.task_auth.company.Company;
import com.example.task_auth.user.User;
import com.example.task_auth.utils.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupSuccess {
    private Token token;
    private Company company;
    private User user;
}

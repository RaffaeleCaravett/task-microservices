package com.example.task_company.auth;

import com.example.task_company.dtos.entitiesDTOS.CompanyDTO;
import com.example.task_company.dtos.entitiesDTOS.CompanySignupDTO;
import com.example.task_company.exceptions.SignupException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;


    @PostMapping("/company/signup")
    public CompanyDTO signup(@RequestBody @Valid CompanySignupDTO companySignupDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new SignupException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }
        return authService.signup(companySignupDTO);
    }
}

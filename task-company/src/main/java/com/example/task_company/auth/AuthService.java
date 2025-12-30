package com.example.task_company.auth;

import com.example.task_company.dtos.entitiesDTOS.CompanyDTO;
import com.example.task_company.dtos.entitiesDTOS.CompanySignupDTO;
import com.example.task_company.gateway.GeneralGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
public class AuthService {
    private final GeneralGateway generalGateway;



    public CompanyDTO signup(CompanySignupDTO companySignupDTO) {
        //TODO
        return generalGateway.signupCompany(companySignupDTO);
    }
}

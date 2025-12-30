package com.example.task_company.gateway;

import com.example.task_company.dtos.entitiesDTOS.CompanyDTO;
import com.example.task_company.dtos.entitiesDTOS.CompanySignupDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeneralGateway {
    private final RestTemplate restTemplate;
    @Value("${spring.endpoint.main}")
    private String endpoint;

    public CompanyDTO signupCompany(CompanySignupDTO companySignupDTO) {
        Map<String, Object> body = Map.of(
                "email", companySignupDTO
        );
        return restTemplate.postForEntity(endpoint + "/auth/signup",
                body, CompanyDTO.class).getBody();
    }
}

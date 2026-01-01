package com.example.task_auth.gateway;

import com.example.task_auth.exceptions.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CompanyGateway {

    private final RestTemplate restTemplate;
    @Value("${spring.endpoint.company}")
    private String endpointCompany;


    public Long findCompanyByEmail(String email, String password) {
        Map<String, String> body = Map.of(
                "email", email,
                "password", password
        );
        return restTemplate.postForEntity(endpointCompany + "/auth/email",
                body, Long.class).getBody();
    }

    public String findAccessCodeByCompanyId(Long id) {
        Map<String, Long> params = new HashMap<>();
        params.put("id", id);
        return restTemplate.getForEntity(endpointCompany + "/auth/accessCode/{id}", String.class, params).getBody();
    }

    public void createAccessCodeByCompanyId(Long id) {
        Map<String, Long> params = new HashMap<>();
        params.put("id", id);
        restTemplate.getForEntity(endpointCompany + "/auth/accessCode/create/{id}", String.class, params).getBody();
    }

    public void deleteCode(Long id) {
        Map<String, Long> params = new HashMap<>();
        params.put("id", id);
        try {
            restTemplate.getForEntity(endpointCompany + "/auth/accessCode/delete/{id}",
                    Boolean.class, params).getBody();
        } catch (Exception e) {
            throw new UnauthorizedException("E' successo qualcosa nell'elaborazione della richiesta. Contatta l'assistenza");
        }
    }
}

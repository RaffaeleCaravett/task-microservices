package com.example.task_auth.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserGateway {

    private final RestTemplate restTemplate;
    @Value("${spring.endpoint.main}")
    private String endpoint;


    public Long findUserByEmail(String email, String password) {
        Map<String, String> body = Map.of(
                "email", email,
                "password", password
        );
        return restTemplate.postForEntity(endpoint + "/auth/email",
                body, Long.class).getBody();
    }
}

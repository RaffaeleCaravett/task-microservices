package com.example.task_auth.gateway;

import com.example.task_auth.exceptions.exception.UnauthorizedException;
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

    public String findAccessCodeByUserId(Long id) {
        Map<String, Long> body = Map.of(
                "id", id
        );
        return restTemplate.postForEntity(endpoint + "/auth/accessCode/{id}",
                body, String.class).getBody();
    }

    public void createAccessCodeByUserId(Long id) {
        Map<String, Long> body = Map.of(
                "id", id
        );
        restTemplate.postForEntity(endpoint + "/auth/accessCode/create/{id}",
                body, String.class).getBody();
    }

    public void deleteCode(Long id) {
        Map<String, Long> body = Map.of(
                "id", id
        );
        try {
            restTemplate.postForEntity(endpoint + "/auth/accessCode/delete/{id}",
                    body, String.class).getBody();
        } catch (Exception e) {
            throw new UnauthorizedException("E' successo qualcosa nell'elaborazione della richiesta. Contatta l'assistenza");
        }
    }
}

package com.example.task_company.mailgun;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Value;


public class MGSamples {
    @Value("${spring.endpoint.mgApiKey}")
    private String apiKey;

    public JsonNode sendSimpleMessage(String companyName, String email, String subject, String content) throws UnirestException {

        String apiKey = System.getenv("API_KEY");
        if (apiKey == null) {
            apiKey = "API_KEY";
        }

        HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/sandboxb74f6882e6e04d4baf3a5091cf143f6a.mailgun.org/messages")
                .basicAuth("api", apiKey)
                .queryString("from", "Taskmaster <postmaster@sandboxb74f6882e6e04d4baf3a5091cf143f6a.mailgun.org>")
                .queryString("to", companyName + " " + email)
                .queryString("subject", subject)
                .queryString("text", content)
                .asJson();
        return request.getBody();
    }
}
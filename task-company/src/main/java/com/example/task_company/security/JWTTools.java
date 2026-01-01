package com.example.task_company.security;

import com.example.task_company.company.Company;
import com.example.task_company.company.CompanyRepository;
import com.example.task_company.exceptions.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class JWTTools {
    @Value("${spring.jwt.secret}")
    private String secret;
    private final CompanyRepository companyRepository;

    public Company verifyToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build().parse(token).getBody();
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            if (!"ACCESS".equals(claims.get("type")) && !claims.getIssuer().equals("task-auth") && !"COMPANY".equals(claims.get("userType"))) {
                throw new UnauthorizedException("Devi effettuare di nuovo l'accesso.");
            }
            String userId = claims.getSubject();
            return companyRepository.findById(Long.valueOf(userId)).get();
        } catch (Exception e) {
            throw new UnauthorizedException("Il token non è valido! Per favore effettua nuovamente il login o refresha la pagina!");
        }
    }

    public String extractIdFromToken(String token){
        return  Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build().parseClaimsJws(token).getBody().getSubject();
    }
}

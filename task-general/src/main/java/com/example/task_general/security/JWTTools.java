package com.example.task_general.security;

import com.example.task_general.exceptions.UnauthorizedException;
import com.example.task_general.user.User;
import com.example.task_general.user.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTTools {
    @Value("${spring.jwt.secret}")
    private String secret;
    @Autowired
    UserRepository userRepository;


    public User verifyToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build().parse(token).getBody();
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            if (!"ACCESS".equals(claims.get("type")) && !claims.getIssuer().equals("task-auth")) {
                throw new UnauthorizedException("Devi effettuare di nuovo l'accesso.");
            }
            String userId = claims.getSubject();
            return userRepository.findById(Long.valueOf(userId)).get();
        } catch (Exception e) {
            throw new UnauthorizedException("Il token non è valido! Per favore effettua nuovamente il login o refresha la pagina!");
        }
    }

    public String extractIdFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build().parseClaimsJws(token).getBody().getSubject();
    }
}

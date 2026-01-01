package com.example.task_auth.security;

import com.example.task_auth.exceptions.exception.UnauthorizedException;
import com.example.task_auth.utils.Token;
import com.example.task_auth.utils.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Jwts;

@Component
public class JWTTools {
    @Value("${spring.jwt.secret}")
    private String secret;

    public Token createTokens(Long id, String type) {

        Map<String, Object> accessMap = new HashMap<>();
        accessMap.put("type", TokenType.ACCESS);
        accessMap.put("userType", type);
        Map<String, Object> refreshMap = new HashMap<>();
        accessMap.put("userType", type);
        refreshMap.put("type", TokenType.REFRESH);

        String accessToken = Jwts.builder().setSubject(String.valueOf(id))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .addClaims(accessMap)
                .setIssuer("task-auth")
                .signWith(Keys.hmacShaKeyFor(secret.getBytes())).compact();

        String refreshToken = Jwts.builder().setSubject(String.valueOf(id))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .addClaims(refreshMap)
                .setIssuer("task-auth")
                .signWith(Keys.hmacShaKeyFor(secret.getBytes())).compact();

        return new Token(accessToken, refreshToken);
    }


    public Token verifyRefreshToken(String token, String type) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            if (!TokenType.REFRESH.name().equals(claims.get("type")) || !"task-auth".equals(claims.getIssuer())
                    || !type.equals(claims.get("userType"))) {
                throw new UnauthorizedException("Accedi nuovamente.");
            }
            String entityId = claims.getSubject();

            Token token1 = this.createTokens(Long.valueOf(entityId), type);
            token1.setRefreshToken(token);
            return token1;
        } catch (Exception e) {
            throw new UnauthorizedException("Accedi nuovamente.");
        }
    }
}

package com.example.task_auth.security;

import com.example.task_auth.company.Company;
import com.example.task_auth.company.CompanyService;
import com.example.task_auth.dto.entities.SignupSuccess;
import com.example.task_auth.exceptions.exception.UnauthorizedException;
import com.example.task_auth.user.User;
import com.example.task_auth.user.UserService;
import com.example.task_auth.utils.Token;
import com.example.task_auth.utils.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Jwts;

@Component
@RequiredArgsConstructor
public class JWTTools {
    @Value("${spring.jwt.secret}")
    private String secret;
    private final CompanyService companyService;
    private final UserService userService;

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


    public SignupSuccess verifyRefreshToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            if (!TokenType.REFRESH.name().equals(claims.get("type")) || !"task-auth".equals(claims.getIssuer())) {
                throw new UnauthorizedException("Accedi nuovamente.");
            }
            String entityId = claims.getSubject();

            Token token1 = this.createTokens(Long.valueOf(entityId), claims.get("userType").toString().toUpperCase());
            token1.setRefreshToken(token);
            Company company = null;
            User user = null;
            if ("COMPANY".equals(claims.get("userType"))) {
                company = companyService.findById(Long.valueOf(entityId));
            } else {
                user = userService.findById(Long.valueOf(entityId));
            }
            return new SignupSuccess(token1, company, user);
        } catch (Exception e) {
            throw new UnauthorizedException("Accedi nuovamente.");
        }
    }

    public SignupSuccess verifyAccessToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            if (!TokenType.ACCESS.name().equals(claims.get("type")) || !"task-auth".equals(claims.getIssuer())) {
                throw new UnauthorizedException("Accedi nuovamente.");
            }
            String entityId = claims.getSubject();

            Company company = null;
            User user = null;
            if ("COMPANY".equals(claims.get("userType"))) {
                company = companyService.findById(Long.valueOf(entityId));
            } else {
                user = userService.findById(Long.valueOf(entityId));
            }
            return new SignupSuccess(null, company, user);
        } catch (Exception e) {
            throw new UnauthorizedException("Accedi nuovamente.");
        }
    }
}

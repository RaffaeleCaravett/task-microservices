package com.example.task_auth.auth;

import com.example.task_auth.dto.UserLoginDTO;
import com.example.task_auth.exceptions.exception.UnauthorizedException;
import com.example.task_auth.security.JWTTools;
import com.example.task_auth.user.UserGateway;
import com.example.task_auth.utils.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserGateway userGateway;
    private final JWTTools jwtTools;

    public Token login(UserLoginDTO userLoginDTO) {
        try {
            Long id = userGateway.findUserByEmail(userLoginDTO.email(), userLoginDTO.password());
            return jwtTools.createTokens(id);
        } catch (Exception e) {
            throw new UnauthorizedException("Credenziali non valide");        }
    }

    public Token refreshAccessToken(String token) {
        return jwtTools.verifyRefreshToken(token);
    }
}

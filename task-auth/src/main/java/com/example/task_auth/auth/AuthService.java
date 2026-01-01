package com.example.task_auth.auth;

import com.example.task_auth.dto.UserLoginDTO;
import com.example.task_auth.exceptions.exception.UnauthorizedException;
import com.example.task_auth.gateway.CompanyGateway;
import com.example.task_auth.security.JWTTools;
import com.example.task_auth.gateway.UserGateway;
import com.example.task_auth.utils.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserGateway userGateway;
    private final CompanyGateway companyGateway;
    private final JWTTools jwtTools;

    public Boolean login(UserLoginDTO userLoginDTO) {
        try {
            Long id = userGateway.findUserByEmail(userLoginDTO.email(), userLoginDTO.password());
            userGateway.createAccessCodeByUserId(id);
            return true;
        } catch (Exception e) {
            throw new UnauthorizedException("Credenziali non valide");
        }
    }

    public Boolean companyLogin(UserLoginDTO userLoginDTO) {
        try {
            Long id = companyGateway.findCompanyByEmail(userLoginDTO.email(), userLoginDTO.password());
            companyGateway.createAccessCodeByCompanyId(id);
            return true;
        } catch (Exception e) {
            throw new UnauthorizedException("Credenziali non valide");
        }
    }

    public Token validateCompanyCode(String code, Long id) {
        try {
            String remoteCode = companyGateway.findAccessCodeByCompanyId(id);
            if (remoteCode.equals(code)) {
                companyGateway.deleteCode(id);
                return jwtTools.createTokens(id, "COMPANY");
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new UnauthorizedException("Le informazioni che hai fornito non sono valide. Se sei sicuro che siano corrette manda un messaggio all'assistenza.");
        }
    }

    public Token validateUserCode(String code, Long id) {
        try {
            String remoteCode = userGateway.findAccessCodeByUserId(id);
            if (remoteCode.equals(code)) {
                userGateway.deleteCode(id);
                return jwtTools.createTokens(id, "USER");
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new UnauthorizedException("Le informazioni che hai fornito non sono valide. Se sei sicuro che siano corrette manda un messaggio all'assistenza.");
        }
    }

    public Token refreshAccessToken(String token, String type) {
        return jwtTools.verifyRefreshToken(token, type);
    }
}

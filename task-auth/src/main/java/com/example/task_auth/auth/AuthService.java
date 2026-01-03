package com.example.task_auth.auth;

import com.example.task_auth.codiceAccesso.CodiceAccesso;
import com.example.task_auth.codiceAccesso.CodiceAccessoRepository;
import com.example.task_auth.company.Company;
import com.example.task_auth.company.CompanyService;
import com.example.task_auth.dto.entities.SignupSuccess;
import com.example.task_auth.dto.entities.UserLoginDTO;
import com.example.task_auth.exceptions.exception.UnauthorizedException;
import com.example.task_auth.security.JWTTools;
import com.example.task_auth.user.User;
import com.example.task_auth.user.UserService;
import com.example.task_auth.utils.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JWTTools jwtTools;
    private final PasswordEncoder bcrypt;
    private final CodiceAccessoRepository codiceAccessoRepository;
    private final CompanyService companyService;

    public Boolean login(UserLoginDTO userLoginDTO) {
        try {
            User user = userService.findByEmail(userLoginDTO.email());
            if (bcrypt.matches(userLoginDTO.password(), user.getPassword())) {
                createUserAccessCode(user.getId());
                return true;
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new UnauthorizedException("Credenziali non valide");
        }
    }

    public Boolean companyLogin(UserLoginDTO userLoginDTO) {
        try {
            Company company = companyService.findByEmail(userLoginDTO.email());
            if (bcrypt.matches(userLoginDTO.password(), company.getPassword())) {
                if (codiceAccessoRepository.findByCompany_Id(company.getId()).isPresent()) {
                    throw new Exception("Hai già ricevuto un codice di accesso sulla tua email. Aspetta 10 minuti per riceverne un altro.");
                }
                companyService.createAccessCode(company.getId(), company);
                return true;
            } else {
                throw new Exception("Credenziali non valide");
            }
        } catch (Exception e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }

    public SignupSuccess validateCompanyCode(String code, Long id) {
        try {
            Optional<CodiceAccesso> codiceAccesso = codiceAccessoRepository.findByCompany_Id(id);
            var company = companyService.findById(id);
            String remoteCode = null;
            if (codiceAccesso.isPresent()) {
                remoteCode = codiceAccesso.get().getCode();
            } else {
                throw new Exception();
            }
            if (remoteCode.equals(code)) {
                companyService.deleteAccessCode(id);
                return new SignupSuccess(jwtTools.createTokens(id, "COMPANY"), company, null);
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new UnauthorizedException("Le informazioni che hai fornito non sono valide. Se sei sicuro che siano corrette manda un messaggio all'assistenza.");
        }
    }

    public SignupSuccess validateUserCode(String code, Long id) {
        try {
            Optional<CodiceAccesso> codiceAccesso = codiceAccessoRepository.findByUser_Id(id);
            var user = userService.findById(id);
            String remoteCode = null;
            if (codiceAccesso.isPresent()) {
                remoteCode = codiceAccesso.get().getCode();
            } else {
                throw new Exception();
            }
            if (remoteCode.equals(code)) {
                deleteAccessCode(id);
                return new SignupSuccess(jwtTools.createTokens(id, "USER"), null, user);
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

    public void createUserAccessCode(Long id) {
        CodiceAccesso codiceAccesso = new CodiceAccesso();
        codiceAccesso.setCreationTime(Instant.now());
        codiceAccesso.setUser(userService.findById(id));
        codiceAccesso.setIsUsed(false);
        codiceAccesso.setCompany(null);
        codiceAccesso.setCode(createAccessCode());
        codiceAccessoRepository.save(codiceAccesso);
    }

    public String createAccessCode() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }

    public Boolean deleteAccessCode(Long id) {
        Optional<CodiceAccesso> codiceAccesso = codiceAccessoRepository.findByUser_Id(id);
        if (codiceAccesso.isPresent()) {
            codiceAccessoRepository.delete(codiceAccesso.get());
            return true;
        } else {
            return false;
        }
    }
}

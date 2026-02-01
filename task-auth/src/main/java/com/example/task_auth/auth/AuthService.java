package com.example.task_auth.auth;

import com.example.task_auth.codiceAccesso.CodiceAccesso;
import com.example.task_auth.codiceAccesso.CodiceAccessoRepository;
import com.example.task_auth.company.Company;
import com.example.task_auth.company.CompanyService;
import com.example.task_auth.dto.entities.SignupSuccess;
import com.example.task_auth.dto.entities.UserLoginDTO;
import com.example.task_auth.exceptions.exception.UnauthorizedException;
import com.example.task_auth.mailgun.MGSamples;
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
    private final MGSamples mgSamples;

    public SignupSuccess login(UserLoginDTO userLoginDTO, String code) {
        try {
            User user = userService.findByEmail(userLoginDTO.email());
            if (bcrypt.matches(userLoginDTO.password(), user.getPassword())) {
                if (!user.getIsConfirmed()) {
                    var accessCode = codiceAccessoRepository.findByUser_Email(userLoginDTO.email());
                    if (accessCode.isPresent() && accessCode.get().getCode().equals(code)) {
                        codiceAccessoRepository.delete(accessCode.get());
                    } else {
                        throw new UnauthorizedException("Non hai i permessi per accedere");
                    }
                }
            } else {
                throw new UnauthorizedException("Credenziali non valide");
            }
            Token token = jwtTools.createTokens(user.getId(), "USER");
            return new SignupSuccess(token, null, user);
        } catch (
                Exception e) {
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
                var code = companyService.createAccessCode(company.getId(), company);
                mgSamples.sendSimpleMessage(company.getNomeAzienda(), company.getEmail(), "Codice di accesso login - TaskMaster",
                        "Hai effettuato il login, \n" +
                                "questo è il codice di accesso : \n" +
                                code.getCode() + "\n" +
                                "Buon lavoro!");

                return true;
            } else {
                throw new Exception("Credenziali non valide");
            }
        } catch (Exception e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }

    public SignupSuccess validateCompanyCode(String code, String email) {
        try {
            Optional<CodiceAccesso> codiceAccesso = codiceAccessoRepository.findByCompany_Email(email);
            var company = companyService.findByEmail(email);
            String remoteCode = null;
            if (codiceAccesso.isPresent()) {
                remoteCode = codiceAccesso.get().getCode();
            } else {
                throw new Exception();
            }
            if (remoteCode.equals(code)) {
                companyService.deleteAccessCode(company.getId());
                return new SignupSuccess(jwtTools.createTokens(company.getId(), "COMPANY"), company, null);
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new UnauthorizedException("Le informazioni che hai fornito non sono valide. Se sei sicuro che siano corrette manda un messaggio all'assistenza.");
        }
    }

    public SignupSuccess validateUserCode(String code, String email) {
        try {
            Optional<CodiceAccesso> codiceAccesso = codiceAccessoRepository.findByUser_Email(email);
            var user = userService.findByEmail(email);
            String remoteCode = null;
            if (codiceAccesso.isPresent()) {
                remoteCode = codiceAccesso.get().getCode();
            } else {
                throw new Exception();
            }
            if (remoteCode.equals(code)) {
                deleteAccessCode(user.getId());
                return new SignupSuccess(jwtTools.createTokens(user.getId(), "USER"), null, user);
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new UnauthorizedException("Le informazioni che hai fornito non sono valide. Se sei sicuro che siano corrette manda un messaggio all'assistenza.");
        }
    }

    public SignupSuccess refreshRefreshToken(String token) {
        return jwtTools.verifyRefreshToken(token);
    }

    public SignupSuccess verifyAccessToken(String token) {
        return jwtTools.verifyAccessToken(token);
    }

    public CodiceAccesso createUserAccessCode(Long id) {
        CodiceAccesso codiceAccesso = new CodiceAccesso();
        codiceAccesso.setCreationTime(Instant.now());
        codiceAccesso.setUser(userService.findById(id));
        codiceAccesso.setIsUsed(false);
        codiceAccesso.setCompany(null);
        codiceAccesso.setCode(createAccessCode());
        return codiceAccessoRepository.save(codiceAccesso);
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

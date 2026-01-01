package com.example.task_general.auth;

import com.example.task_general.codiceAccesso.CodiceAccesso;
import com.example.task_general.codiceAccesso.CodiceAccessoRepository;
import com.example.task_general.exceptions.UnauthorizedException;
import com.example.task_general.user.User;
import com.example.task_general.user.UserRepository;
import com.example.task_general.dtos.entitiesDTO.UserLoginDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CodiceAccessoRepository codiceAccessoRepository;

    public Long findByEmail(UserLoginDTO userLoginDTO) {

        User user = userRepository.findByEmail(userLoginDTO.getEmail()).orElseThrow(() -> new UnauthorizedException("Credenziali errate"));
        if (passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            return user.getId();
        }
        throw new UnauthorizedException("Credenziali errate");
    }

    public void createUserAccessCode(Long id) {
        CodiceAccesso codiceAccesso = new CodiceAccesso();
        codiceAccesso.setCreationTime(Instant.now());
        codiceAccesso.setUser(userRepository.findById(id).orElseThrow(() -> new UnauthorizedException("Accesso negato")));
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

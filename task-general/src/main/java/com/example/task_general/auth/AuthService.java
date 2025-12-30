package com.example.task_general.auth;

import com.example.task_general.company.Company;
import com.example.task_general.company.CompanyRepository;
import com.example.task_general.dtos.entitiesDTO.CompanyDTO;
import com.example.task_general.dtos.entitiesDTO.CompanySignupDTO;
import com.example.task_general.exceptions.SignupException;
import com.example.task_general.exceptions.UnauthorizedException;
import com.example.task_general.user.Role;
import com.example.task_general.user.User;
import com.example.task_general.user.UserRepository;
import com.example.task_general.user.dto.UserLoginDTO;
import com.example.task_general.user.dto.UserSignupDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public Long findByEmail(UserLoginDTO userLoginDTO) {

        User user = userRepository.findByEmail(userLoginDTO.getEmail()).orElseThrow(() -> new UnauthorizedException("Credenziali errate"));
        if (passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            return user.getId();
        }
        throw new UnauthorizedException("Credenziali errate");
    }

    public CompanyDTO signup(CompanySignupDTO companySignupDTO) {

        if (companyRepository.findByEmail(companySignupDTO.getEmail()).isPresent()) {
            throw new SignupException("L'email è già presente in db");
        }
        try {
            Company company = companyRepository.save(companySignupDTO);
            CompanyDTO companyDTO = new CompanyDTO();
            companyDTO.setEmail(companySignupDTO.getEmail());
            companyDTO.setId(company.getId());
            companyDTO.setNome(companySignupDTO.getNomeAzienda());
        return companyDTO;
        } catch (Exception e) {
            throw new SignupException(e.getMessage());
        }
    }
}

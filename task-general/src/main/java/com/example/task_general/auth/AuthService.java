package com.example.task_general.auth;

import com.example.task_general.codiceAccesso.CodiceAccesso;
import com.example.task_general.codiceAccesso.CodiceAccessoRepository;
import com.example.task_general.company.Company;
import com.example.task_general.company.CompanyRepository;
import com.example.task_general.exceptions.BadRequestException;
import com.example.task_general.exceptions.UnauthorizedException;
import com.example.task_general.user.Role;
import com.example.task_general.user.User;
import com.example.task_general.user.UserRepository;
import com.example.task_general.dtos.entitiesDTO.UserLoginDTO;
import com.example.task_general.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {


    public final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CodiceAccessoRepository codiceAccessoRepository;
    private final UserService userService;
    private final CompanyRepository companyRepository;

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

    public List<User> addUserToCompany(UserLoginDTO userLoginDTO, Company company) {
        if (userService.findByEmail(userLoginDTO.getEmail()) != null) {
            throw new BadRequestException("User già presente in db");
        }
        User user = new User();
        user.setCreatedAt(LocalDate.now().toString());
        user.setRole(Role.USER);
        user.setIsConfirmed(false);
        if (user.getCompanies().isEmpty()) {
            var companies = new ArrayList<Company>();
            companies.add(company);
            user.setCompanies(companies);
        } else {
            user.getCompanies().add(company);
            user.setCompanies(user.getCompanies());
        }
        user.setEmail(userLoginDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userLoginDTO.getPassword()));
        company.getUsers().add(user);
        company.setUsers(company.getUsers());
        return companyRepository.save(company).getUsers();

    }
}

package com.example.task_general.auth;

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

    public Long findByEmail(UserLoginDTO userLoginDTO) {

        User user = userRepository.findByEmail(userLoginDTO.getEmail()).orElseThrow(() -> new UnauthorizedException("Credenziali errate"));
        if (passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            return user.getId();
        }
        throw new UnauthorizedException("Credenziali errate");
    }

    public User signup(UserSignupDTO userSignupDTO) {

        if (userRepository.findByEmail(userSignupDTO.getEmail()).isPresent()) {
            throw new SignupException("L'email è già presente in db");
        }
        User user = new User();
        user.setEmail(userSignupDTO.getEmail());
        user.setRole(Role.COMPANY);
        user.setPassword(passwordEncoder.encode(userSignupDTO.getPassword()));
        return userRepository.save(user);
    }
}

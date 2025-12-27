package com.example.task_auth.auth;

import com.example.task_auth.dto.UserDTO;
import com.example.task_auth.dto.UserSignupDTO;
import com.example.task_auth.exceptions.exception.SignupException;
import com.example.task_auth.mapper.UserMapper;
import com.example.task_auth.user.User;
import com.example.task_auth.user.UserRepository;
import com.example.task_auth.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class AuthService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder bCrypt;
    private final UserMapper userMapper;

    public UserDTO signup(UserSignupDTO userSignupDTO) {
        User user = userService.findByEmail(userSignupDTO.email());
        if (!bCrypt.matches(userSignupDTO.password(), user.getPassword())) {
            throw new SignupException("La password non combacia con quella nel database");
        }

        if (userSignupDTO.nome() != null) {
            user.setNome(userSignupDTO.nome());
        }
        if (userSignupDTO.cognome() != null) {
            user.setCognome(userSignupDTO.cognome());
        }
        return userMapper.toUserDTO(new UserDTO(), userRepository.save(user));
    }
}

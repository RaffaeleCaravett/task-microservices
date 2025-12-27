package com.example.task_auth.user;

import com.example.task_auth.exceptions.exception.SignupException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new SignupException("Nessun utente corrisponde a questa mail"));
    }
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new SignupException("Nessun utente corrisponde a questa mail"));
    }
}

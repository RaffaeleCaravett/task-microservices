package com.example.task_company.user;

import com.example.task_company.exceptions.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UnauthorizedException("Utente non trovato, id: " + id));
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UnauthorizedException("Utente non trovato, email: " + email));
    }
}

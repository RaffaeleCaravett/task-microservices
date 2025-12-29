package com.example.task_general.user;

import com.example.task_general.exceptions.SignupException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new SignupException("Utente non trovato, id: " + id));
    }
}

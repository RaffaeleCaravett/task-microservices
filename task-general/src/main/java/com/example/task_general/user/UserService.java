package com.example.task_general.user;

import com.example.task_general.exceptions.BadRequestException;
import com.example.task_general.exceptions.SignupException;
import lombok.RequiredArgsConstructor;
import org.mapstruct.control.MappingControl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new SignupException("Utente non trovato, id: " + id));
    }

    public List<User> findAllById(List<Long> ids) {
        if (ids.isEmpty()) {
            return new ArrayList<>();
        }
        return userRepository.findAllById(ids);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException("Utente non trovato, email: " + email));
    }

    public void delete(User user) {
        try {
            userRepository.delete(user);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
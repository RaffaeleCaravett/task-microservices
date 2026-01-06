package com.example.task_general.user;

import com.example.task_general.dtos.entitiesDTO.UserLightFilters;
import com.example.task_general.exceptions.BadRequestException;
import com.example.task_general.exceptions.InternalServerException;
import com.example.task_general.exceptions.SignupException;
import lombok.RequiredArgsConstructor;
import org.mapstruct.control.MappingControl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    public Page<User> filterByCompanyId(Long id, UserLightFilters userLightFilters, Pageable pageable) {
        try {
            return userRepository.findAll(Specification.where(UserRepository.emailContains(userLightFilters.email())
                    .and(UserRepository.fullnameContains(userLightFilters.fullname()))
                    .and(UserRepository.statusEquals(userLightFilters.status().equals("ACTIVE")))
            ), pageable);
        } catch (Exception e) {
            throw new InternalServerException("Qualcosa è successo internamente. Risolviamo subito.");
        }
    }
}
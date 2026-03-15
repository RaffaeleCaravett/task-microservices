package com.example.task_general.user;

import com.example.task_general.dtos.entitiesDTO.UserLightFilters;
import com.example.task_general.exceptions.BadRequestException;
import com.example.task_general.exceptions.InternalServerException;
import com.example.task_general.exceptions.SignupException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.mapstruct.control.MappingControl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

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
            Specification<User> spec = new Specification<User>() {
                @Override
                public @Nullable Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    return null;
                }
            };
            if (userLightFilters.email() != null) {
                spec = UserRepository.emailContains(userLightFilters.email());
            }
            if (userLightFilters.fullname() != null) {
                spec = spec.and(UserRepository.fullnameContains(userLightFilters.fullname()));
            }
            if (userLightFilters.status() != null) {
                spec = spec.and(UserRepository.statusEquals(userLightFilters.status().equals("ACTIVE")));
            }
            if (id != null) {
                spec = spec.and(UserRepository.companyIdEquals(id));
            }
            return userRepository.findAll(spec, pageable);
        } catch (Exception e) {
            throw new InternalServerException("Qualcosa è successo internamente. Risolviamo subito.");
        }
    }
    public List<User> filterByCompanyId(Long id) {
        try {
            if(null == id){
                throw new BadRequestException("L'id della company non può essere null");
            }
            return userRepository.findAll(Specification.where(UserRepository.companyIdEquals(id)));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException("Qualcosa è successo internamente. Risolviamo subito.");
        }
    }
}
package com.example.task_general.company;

import com.example.task_general.dtos.entitiesDTO.UserLightFilters;
import com.example.task_general.dtos.entitiesDTO.UserLoginDTO;
import com.example.task_general.exceptions.BadRequestException;
import com.example.task_general.exceptions.EntityNotPresentException;
import com.example.task_general.exceptions.UnauthorizedException;
import com.example.task_general.project.Project;
import com.example.task_general.project.ProjectService;
import com.example.task_general.user.Role;
import com.example.task_general.user.User;
import com.example.task_general.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserService userService;

    public Company findById(Long id) {
        return companyRepository.findById(id).orElseThrow(() -> new EntityNotPresentException("Company " + id + " non presente in db"));
    }

    public Page<User> getUsersByCompanyId(Long id, Pageable pageable) {
        return userService.filterByCompanyId(id, new UserLightFilters(null, null, null), pageable);
    }

    public List<User> removeUserFromCompany(Long userId, Company company) {
        var user = userService.findById(userId);
        if (user.getCompanies().getFirst().equals(company)) {
            userService.delete(user);
            return company.getUsers();
        } else {
            throw new UnauthorizedException("Non puoi eliminare lo user");
        }
    }

    public List<User> suspendUser(Long userId, Company company) {
        var user = userService.findById(userId);
        if (user.getCompanies().getFirst().equals(company)) {
            user.setIsActive(false);
            userService.save(user);
            return company.getUsers();
        } else {
            throw new UnauthorizedException("Non puoi sospendere lo user");
        }
    }
}


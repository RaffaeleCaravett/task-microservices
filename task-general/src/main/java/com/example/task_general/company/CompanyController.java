package com.example.task_general.company;

import com.example.task_general.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAuthority('COMPANY')")
    public List<User> getUsers(@PathVariable Long id) {
        return companyService.getUsersByCompanyId(id);
    }
}

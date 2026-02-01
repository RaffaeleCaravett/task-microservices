package com.example.task_general.company;

import com.example.task_general.auth.AuthService;
import com.example.task_general.codiceAccesso.CodiceAccesso;
import com.example.task_general.dtos.entitiesDTO.FirstUserLoginDTO;
import com.example.task_general.dtos.entitiesDTO.UserLoginDTO;
import com.example.task_general.exceptions.BadRequestException;
import com.example.task_general.user.User;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('COMPANY')")
public class CompanyController {

    private final CompanyService companyService;
    private final AuthService authService;

    @GetMapping("/users/{id}")
    public Page<User> getUsers(@PathVariable Long id, @PageableDefault Pageable pageable) {
        return companyService.getUsersByCompanyId(id, pageable);
    }

    @PostMapping("/user")
    public CodiceAccesso addUserToCompany(@RequestBody @Valid FirstUserLoginDTO userLoginDTO, BindingResult bindingResult, @AuthenticationPrincipal Company company) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }
        return authService.addUserToCompany(userLoginDTO, company);
    }

    @DeleteMapping("/userFromCompany/{id}")
    public List<User> removeUserFromCompany(@PathVariable Long id, @AuthenticationPrincipal Company company) {
        return companyService.removeUserFromCompany(id, company);
    }

    @DeleteMapping("/userSuspend/{id}")
    public List<User> suspendUser(@PathVariable Long id, @AuthenticationPrincipal Company company) {
        return companyService.suspendUser(id, company);
    }
}

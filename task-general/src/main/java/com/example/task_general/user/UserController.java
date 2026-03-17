package com.example.task_general.user;

import com.example.task_general.codiceAccesso.CodiceAccesso;
import com.example.task_general.company.Company;
import com.example.task_general.dtos.entitiesDTO.UserLightFilters;
import com.example.task_general.exceptions.BadRequestException;
import com.example.task_general.secretCode.SecretCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/byCompanyAndFilters/{id}")
    public Page<User> find(@PathVariable Long id, @RequestBody @Valid UserLightFilters userLightFilters, BindingResult bindingResult, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }
        return userService.filterByCompanyId(id, userLightFilters, pageable);
    }

    @GetMapping("/canRequestNewSecretCode/{userId}")
    public Boolean canRequestSecretCode(@PathVariable Long userId){
        return userService.canRequestCode(userId);
    }

    @GetMapping("/updateCode/{userId}")
    public SecretCode updateCode(@PathVariable Long userId, @AuthenticationPrincipal User user, @AuthenticationPrincipal Company company){
        return userService.updateCode(userId,user, company);
    }
}

package com.example.task_general.auth;

import com.example.task_general.dtos.entitiesDTO.CompanyDTO;
import com.example.task_general.dtos.entitiesDTO.CompanySignupDTO;
import com.example.task_general.exceptions.UnauthorizedException;
import com.example.task_general.user.User;
import com.example.task_general.user.UserService;
import com.example.task_general.user.dto.UserLoginDTO;
import com.example.task_general.user.dto.UserSignupDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public CompanyDTO signup(@RequestBody @Valid CompanySignupDTO companySignupDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UnauthorizedException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }
        return authService.signup(companySignupDTO);
    }

    @PostMapping("/email")
    public Long getUserByEmail(@RequestBody @Valid UserLoginDTO userLoginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UnauthorizedException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }
        return authService.findByEmail(userLoginDTO);
    }
}

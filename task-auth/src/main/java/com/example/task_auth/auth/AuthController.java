package com.example.task_auth.auth;

import com.example.task_auth.dto.entities.SignupSuccess;
import com.example.task_auth.dto.entities.UserLoginDTO;
import com.example.task_auth.exceptions.exception.UnauthorizedException;
import com.example.task_auth.utils.Token;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public Boolean login(@RequestBody @Valid UserLoginDTO userLoginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UnauthorizedException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }
        return authService.login(userLoginDTO);
    }

    @PostMapping("/company/login")
    public Boolean companyLogin(@RequestBody @Valid UserLoginDTO userLoginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UnauthorizedException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }
        return authService.companyLogin(userLoginDTO);
    }

    @GetMapping("/validate/code")
    public SignupSuccess validateCode(@RequestParam String code, @RequestParam String email, @RequestParam String type) {
        try {
            return switch (type) {
                case "COMPANY" -> authService.validateCompanyCode(code, email);
                case "USER" -> authService.validateUserCode(code, email);
                default -> throw new Exception();
            };
        } catch (Exception e) {
            throw new UnauthorizedException("Impossibile verificare il codice di accesso");
        }
    }

    @GetMapping("/refreshAccessToken/{refreshToken}/{type}")
    public Token refreshAccess(@PathVariable String refreshToken, @PathVariable String type) {
        return authService.refreshAccessToken(refreshToken, type);
    }
}

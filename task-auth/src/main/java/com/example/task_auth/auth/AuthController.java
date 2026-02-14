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


    @PostMapping("/login/{accessCode}")
    public SignupSuccess login(@RequestBody @Valid UserLoginDTO userLoginDTO, BindingResult bindingResult, @PathVariable String accessCode) {
        if (bindingResult.hasErrors()) {
            throw new UnauthorizedException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }
        return authService.login(userLoginDTO, accessCode);
    }

    @PostMapping("/company/login")
    public Boolean companyLogin(@RequestBody @Valid UserLoginDTO userLoginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UnauthorizedException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }
        return authService.companyLogin(userLoginDTO);
    }

    @PostMapping("/user/login")
    public SignupSuccess userLogin(@RequestBody @Valid UserLoginDTO userLoginDTO, @RequestParam String code, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UnauthorizedException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }
        return authService.login(userLoginDTO, code);
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

    @GetMapping("/verifyRefreshToken/{refreshToken}")
    public SignupSuccess refreshAccess(@PathVariable String refreshToken) {
        return authService.refreshRefreshToken(refreshToken);
    }

    @GetMapping("/verifyAccessToken/{token}")
    public SignupSuccess verifyAccessToken(@PathVariable String token) {
        return authService.verifyAccessToken(token);
    }
}


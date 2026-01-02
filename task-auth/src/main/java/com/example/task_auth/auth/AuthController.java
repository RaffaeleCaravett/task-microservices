package com.example.task_auth.auth;

import com.example.task_auth.dto.UserLoginDTO;
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

    @PostMapping("/validate/code/{code}/{id}/{type}")
    public Token validateCode(@PathVariable String code, @PathVariable Long id, @PathVariable String type) {
        try {
            switch (type) {
                case "COMPANY":
                    return authService.validateCompanyCode(code, id);
                case "USER":
                    return authService.validateUserCode(code, id);
                default:
                    throw new Exception();
            }
        } catch (Exception e) {
            throw new UnauthorizedException("Impossibile verificare il codice di accesso");
        }
    }

    @PostMapping("/refreshAccessToken/{refreshToken}/{type}")
    public Token refreshAccess(@PathVariable String refreshToken, @PathVariable String type) {
        return authService.refreshAccessToken(refreshToken, type);
    }
}

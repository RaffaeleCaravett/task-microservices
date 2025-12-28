package com.example.task_auth.auth;

import com.example.task_auth.dto.UserDTO;
import com.example.task_auth.dto.UserLoginDTO;
import com.example.task_auth.exceptions.exception.SignupException;
import com.example.task_auth.utils.Token;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public Token update(@RequestBody @Valid UserLoginDTO userLoginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new SignupException(bindingResult.getAllErrors());
        }

        return authService.login(userLoginDTO);
    }

    @PostMapping("/refreshAccessToken/{refreshToken}")
    public Token refreshAccess(@PathVariable String refreshToken) {
        return authService.refreshAccessToken(refreshToken);
    }
}

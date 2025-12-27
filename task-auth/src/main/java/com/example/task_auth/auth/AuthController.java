package com.example.task_auth.auth;

import com.example.task_auth.dto.UserDTO;
import com.example.task_auth.dto.UserSignupDTO;
import com.example.task_auth.exceptions.exception.SignupException;
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
    public UserDTO signup (@RequestBody @Valid UserSignupDTO userSignupDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new SignupException(bindingResult.getAllErrors());
        }
       return authService.signup(userSignupDTO);
    }
}

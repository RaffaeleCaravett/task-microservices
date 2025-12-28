package com.example.task_general.user;

import com.example.task_general.exceptions.UnauthorizedException;
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
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/email")
    public Long getUserByEmail(@RequestBody @Valid UserLoginDTO userLoginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UnauthorizedException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }
        return userService.findByEmail(userLoginDTO);
    }

    @PostMapping("/signup")
    public User getUserByEmail(@RequestBody @Valid UserSignupDTO userSignup, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UnauthorizedException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }
        return userService.signup(userSignup);
    }

}

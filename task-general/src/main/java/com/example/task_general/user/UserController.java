package com.example.task_general.user;

import com.example.task_general.auth.AuthService;
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

}

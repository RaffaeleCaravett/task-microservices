package com.example.task_general.auth;

import com.example.task_general.codiceAccesso.CodiceAccesso;
import com.example.task_general.codiceAccesso.CodiceAccessoRepository;
import com.example.task_general.exceptions.UnauthorizedException;
import com.example.task_general.dtos.entitiesDTO.UserLoginDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final CodiceAccessoRepository codiceAccessoRepository;

    @PostMapping("/email")
    public Long getUserByEmail(@RequestBody @Valid UserLoginDTO userLoginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UnauthorizedException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }
        return authService.findByEmail(userLoginDTO);
    }

    @GetMapping("/accessCodee/{id}")
    public String findAccessCodeByUSerId(@PathVariable Long id) {
        Optional<CodiceAccesso> codiceAccesso = codiceAccessoRepository.findByUser_Id(id);
        if (codiceAccesso.isPresent() && !codiceAccesso.get().getIsUsed()) {
            return codiceAccessoRepository.findByUser_Id(id).get().getCode();
        } else {
            throw new UnauthorizedException("Accesso negato");
        }
    }

    @GetMapping("/accessCode/createe/{id}")
    public void createAccessCodeByUSerId(@PathVariable Long id) {
        authService.createUserAccessCode(id);
    }

    @GetMapping("/accessCode/delete/{id}")
    public Boolean deleteAccessCodeByUSerId(@PathVariable Long id) {
        return authService.deleteAccessCode(id);
    }
}

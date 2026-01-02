package com.example.task_company.auth;

import com.example.task_company.codiceAccesso.CodiceAccesso;
import com.example.task_company.codiceAccesso.CodiceAccessoRepository;
import com.example.task_company.dtos.entitiesDTOS.CompanyDTO;
import com.example.task_company.dtos.entitiesDTOS.CompanyLoginDTO;
import com.example.task_company.dtos.entitiesDTOS.CompanySignupDTO;
import com.example.task_company.exceptions.SignupException;
import com.example.task_company.exceptions.UnauthorizedException;
import jakarta.transaction.Transactional;
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
    public Long getUserByEmail(@RequestBody @Valid CompanyLoginDTO companyLoginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UnauthorizedException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }
        return authService.findByEmail(companyLoginDTO);
    }

    @PostMapping("/company/signup")
    public CompanyDTO signup(@RequestBody @Valid CompanySignupDTO companySignupDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new SignupException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }
        return authService.signup(companySignupDTO);
    }

    @GetMapping("/accessCode/{id}")
    public String findAccessCodeByUSerId(@PathVariable Long id) {
        Optional<CodiceAccesso> codiceAccesso = codiceAccessoRepository.findByCompany_Id(id);
        if (codiceAccesso.isPresent() && !codiceAccesso.get().getIsUsed()) {
            return codiceAccessoRepository.findByCompany_Id(id).get().getCode();
        } else {
            throw new UnauthorizedException("Accesso negato");
        }
    }

    @GetMapping("/accessCode/create/{id}")
    public void createAccessCodeByUSerId(@PathVariable Long id) {
        authService.createAccessCode(id, null);
    }

    @Transactional
    @GetMapping("/accessCode/delete/{id}")
    public Boolean deleteAccessCodeByUSerId(@PathVariable Long id) {
        return authService.deleteAccessCode(id);
    }
}

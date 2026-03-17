package com.example.task_general.log;

import com.example.task_general.company.Company;
import com.example.task_general.dtos.entitiesDTO.LogFilters;
import com.example.task_general.exceptions.BadRequestException;
import com.example.task_general.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/logs")
public class LogController {

    private final LogService logService;


    @GetMapping("")
    public List<Log> getAll(@AuthenticationPrincipal Company company, @RequestBody @Validated LogFilters logFilters, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }
            return logService.findAllByCompanyId(company.getId(), logFilters);
    }

    @GetMapping("/user")
    public List<Log> getAll(@AuthenticationPrincipal User user, @RequestBody @Validated LogFilters logFilters, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }
        return logService.findAllByUserId(user.getId(), logFilters);
    }
}

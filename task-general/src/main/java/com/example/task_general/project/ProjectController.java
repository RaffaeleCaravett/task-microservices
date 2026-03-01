package com.example.task_general.project;

import com.example.task_general.company.Company;
import com.example.task_general.dtos.entitiesDTO.ProjectDTO;
import com.example.task_general.exceptions.BadRequestException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/{id}")
    public Page<Project> getProjects(@PathVariable Long id, @PageableDefault Pageable pageable, @RequestParam (required = false) String projectName) {
        return projectService.filterByCompanyIdAndName(id, projectName, pageable);
    }

    @PostMapping("")
    public Project createProject(@RequestBody @Valid ProjectDTO projectDTO, BindingResult bindingResult, @AuthenticationPrincipal Company company) {
        if(bindingResult.hasErrors()){
            throw new BadRequestException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }
        return projectService.create(projectDTO, company);
    }
}

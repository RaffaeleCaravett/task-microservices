package com.example.task_general.project;

import com.example.task_general.company.Company;
import com.example.task_general.dtos.entitiesDTO.ProjectDTO;
import com.example.task_general.dtos.entitiesDTO.ProjectFilterDTO;
import com.example.task_general.exceptions.BadRequestException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/{id}")
    public Page<Project> getProjects(@PathVariable Long id, @PageableDefault Pageable pageable, @RequestBody ProjectFilterDTO projectFilterDTO) {
        return projectService.filterByFilter(id, projectFilterDTO, pageable);
    }

    @PostMapping("")
    public Project createProject(@RequestBody @Valid ProjectDTO projectDTO, BindingResult bindingResult, @AuthenticationPrincipal Company company) {
        if(bindingResult.hasErrors()){
            throw new BadRequestException(bindingResult.getAllErrors().getFirst().getDefaultMessage());
        }
        return projectService.create(projectDTO, company);
    }

    @GetMapping("/unfavourite/{id}")
    @PreAuthorize("hasAuthority('COMPANY')")
    public Project unmarkAsFavourite(@PathVariable Long id, @AuthenticationPrincipal Company company){
        return projectService.unmarkAsFavourite(id,company.getId());
    }
    @GetMapping("/favourite/{id}")
    @PreAuthorize("hasAuthority('COMPANY')")
    public Project markAsFavourite(@PathVariable Long id, @AuthenticationPrincipal Company company){
        return projectService.markAsFavourite(id,company.getId());
    }
}

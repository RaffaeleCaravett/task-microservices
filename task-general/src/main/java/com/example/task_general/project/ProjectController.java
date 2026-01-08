package com.example.task_general.project;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
}

package com.example.task_general.projectType;

import com.example.task_general.exceptions.BadRequestException;
import com.example.task_general.project.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectTypeService {
    private final ProjectTypeRepository projectTypeRepository;

    public ProjectType findById(Long id) {
        return projectTypeRepository.findById(id).orElseThrow(() -> new BadRequestException("Tipo progetto non trovato : " + id));
    }

}

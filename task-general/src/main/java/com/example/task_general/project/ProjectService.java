package com.example.task_general.project;

import com.example.task_general.company.Company;
import com.example.task_general.company.CompanyService;
import com.example.task_general.dtos.entitiesDTO.ProjectDTO;
import com.example.task_general.exceptions.BadRequestException;
import com.example.task_general.exceptions.EntityNotPresentException;
import com.example.task_general.exceptions.InternalServerException;
import com.example.task_general.exceptions.UnauthorizedException;
import com.example.task_general.projectType.ProjectTypeService;
import com.example.task_general.user.UserService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final CompanyService companyService;
    private final UserService userService;
    private final ProjectTypeService projectTypeService;

    public Project create(ProjectDTO projectDTO, Company company) {
        var foundCompany = companyService.findById(projectDTO.getCompanyId());
        if(!foundCompany.getId().equals(company.getId())){
            throw new BadRequestException("Non puoi creare progetti per altre companie");
        }

        Project project = new Project();
        project.setCreatedAt(LocalDate.now());
        project.setProjectState(ProjectState.valueOf(projectDTO.getState()));
        project.setTitle(projectDTO.getTitle());
        project.setDescription(projectDTO.getDescription());
        project.setCompany(companyService.findById(projectDTO.getCompanyId()));
        project.setManager(userService.findById(projectDTO.getManagerId()));
        project.setUsers(userService.findAllById(projectDTO.getDevelopers()));
        project.setTasks(new ArrayList<>());
        project.setProjectType(projectTypeService.findById(projectDTO.getTypeId()));
        return projectRepository.save(project);
    }

    public Project addManagerToProject(Long projectId, Long userId, Company company) {
        var project = findById(projectId);
        if (project.getProjectState().equals(ProjectState.COMPLETED)) {
            throw new UnauthorizedException("Il progetto è stato già completato");
        }
        if (project.getCompany().equals(company)) {
            var user = userService.findById(userId);
            project.setManager(user);
            return projectRepository.save(project);
        } else {
            throw new UnauthorizedException("Non puoi aggiungere un manager");
        }
    }

    public Project addUserToProject(Long projectId, Long userId, Long id, String type) {
        Object executor = findExecutor(type, id);
        var project = findById(projectId);
        if (project.getProjectState().equals(ProjectState.COMPLETED)) {
            throw new UnauthorizedException("Il progetto è stato già completato");
        }
        if (("COMPANY".equals(type) && project.getCompany().equals(executor)) ||
                ("USER".equals(type) && project.getManager().equals(executor))) {
            var user = userService.findById(userId);
            if (project.getUsers().isEmpty()) {
                project.setUsers(new ArrayList<>());
                project.getUsers().add(user);
                project.setUsers(project.getUsers());
            } else {
                project.getUsers().add(user);
                project.setUsers(project.getUsers());
            }
            return projectRepository.save(project);
        } else {
            throw new UnauthorizedException("Non puoi aggiungere developers.");
        }
    }

    public Project removeUserFromProject(Long projectId, Long userId, Long id, String type) {
        Object executor = findExecutor(type, id);
        var project = findById(projectId);
        if (project.getProjectState().equals(ProjectState.COMPLETED)) {
            throw new UnauthorizedException("Il progetto è stato già completato");
        }
        if (("COMPANY".equals(type) && project.getCompany().equals(executor)) ||
                ("USER".equals(type) && project.getManager().equals(executor))) {
            var user = userService.findById(userId);
            if (project.getUsers().isEmpty()) {
                project.setUsers(new ArrayList<>());
                project.getUsers().add(user);
                project.setUsers(project.getUsers());
            } else {
                project.getUsers().add(user);
                project.setUsers(project.getUsers());
            }
            return projectRepository.save(project);
        } else {
            throw new UnauthorizedException("Non puoi rimuovere utenti.");
        }
    }

    public Project findById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new EntityNotPresentException("Progetto " + id + " non trovato in db"));
    }

    public Object findExecutor(String type, Long id) {
        Object executor = null;
        if (type.equals("COMPANY")) {
            executor = companyService.findById(id);
        } else {
            executor = userService.findById(id);
        }
        return executor;
    }

    public Project changeProjectState(Long projectId, String state, Company company) {
        ProjectState projectState = ProjectState.valueOf(state);
        var project = findById(projectId);
        if (!project.getCompany().equals(company)) {
            throw new UnauthorizedException("Non pui cambiare stato a questo progetto");
        }
        project.setProjectState(projectState);
        return projectRepository.save(project);
    }

    public Page<Project> filterByCompanyIdAndName(Long id, String name, Pageable pageable){
        try {
            Specification<Project> spec = new Specification<Project>() {
                @Override
                public @Nullable Predicate toPredicate(Root<Project> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    return null;
                }
            };
            if (id != null) {
                spec = ProjectRepository.companyIdEquals(id);
            }
            if (name != null) {
                spec = spec.and(ProjectRepository.nameContains(name));
            }
            return projectRepository.findAll(spec, pageable);
        } catch (Exception e) {
            throw new InternalServerException("Qualcosa è successo internamente. Risolviamo subito.");
        }
    }
}

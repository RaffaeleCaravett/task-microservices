package com.example.task_general.project;

import com.example.task_general.company.Company;
import com.example.task_general.company.CompanyService;
import com.example.task_general.dtos.entitiesDTO.ProjectDTO;
import com.example.task_general.dtos.entitiesDTO.ProjectFilterDTO;
import com.example.task_general.exceptions.BadRequestException;
import com.example.task_general.exceptions.EntityNotPresentException;
import com.example.task_general.exceptions.InternalServerException;
import com.example.task_general.exceptions.UnauthorizedException;
import com.example.task_general.projectType.ProjectType;
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
        if (!foundCompany.getId().equals(company.getId())) {
            throw new BadRequestException("Non puoi creare progetti per altre companie");
        }
        try {
            Project project = new Project();
            project.setCreatedAt(LocalDate.now());
            project.setProjectState(ProjectState.valueOf(projectDTO.getState()));
            project.setTitle(projectDTO.getTitle());
            project.setDescription(projectDTO.getDescription());
            project.setCompany(companyService.findById(projectDTO.getCompanyId()));
            project.setManager(userService.findById(projectDTO.getManagerId()));
            project.setFavourite(false);
            if (null != projectDTO.getDevelopers()) {
                project.setUsers(userService.findAllById(projectDTO.getDevelopers()));
            }
            project.setTasks(new ArrayList<>());
            project.setProjectType(projectTypeService.findById(projectDTO.getTypeId()));
            return projectRepository.save(project);
        } catch (Exception e) {
            throw new InternalServerException("Something bad happened, we're working on it to solve as soon as possible.");
        }

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

    public Page<Project> filterByFilter(Long id, ProjectFilterDTO projectFilterDTO, Pageable pageable) {
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
            if (projectFilterDTO.projectName() != null) {
                spec = spec.and(ProjectRepository.nameContains(projectFilterDTO.projectName()));
            }
            if (projectFilterDTO.description() != null) {
                spec = spec.and(ProjectRepository.descriptionContains(projectFilterDTO.description()));
            }
            if (projectFilterDTO.manager() != null) {
                spec = spec.and(ProjectRepository.managerNameContains(projectFilterDTO.manager()));
            }
            if (projectFilterDTO.type() != null) {
                ProjectType projectType = projectTypeService.findById(projectFilterDTO.type());
                spec = spec.and(ProjectRepository.typeEquals(projectType.getId()));
            }
            if (projectFilterDTO.state() != null) {
                try {
                    ProjectState projectState = ProjectState.valueOf(projectFilterDTO.state());
                    spec = spec.and(ProjectRepository.stateEquals(projectState));
                } catch (Exception e) {
                    throw new BadRequestException("Lo stato inserito non è corretto");
                }
            }
            if (projectFilterDTO.date() != null && !projectFilterDTO.date().isBlank() && !projectFilterDTO.date().isEmpty()) {
                try {
                    LocalDate date = LocalDate.parse(projectFilterDTO.date());
                    spec = spec.and(ProjectRepository.createdAtGreaterThanOrEqualTo(date));
                } catch (Exception e) {
                    throw new BadRequestException("La data inserita non è corretta");
                }
            }
            return projectRepository.findAll(spec, pageable);
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException("Qualcosa è successo internamente. Risolviamo subito.");
        }
    }

    public Project markAsFavourite(Long id , Long companyId){
        Project project = findById(id);
        if(project.getCompany().getId().equals(companyId)){
            project.setFavourite(true);
        }else{
            throw new BadRequestException("Non puoi modificare i progetti di altre aziende.");
        }
        return projectRepository.save(project);
    }

    public Project unmarkAsFavourite(Long id , Long companyId){
        Project project = findById(id);
        if(project.getCompany().getId().equals(companyId)){
            project.setFavourite(false);
        }else{
            throw new BadRequestException("Non puoi modificare i progetti di altre aziende.");
        }
        return projectRepository.save(project);
    }
}

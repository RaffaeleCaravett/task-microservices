package com.example.task_general.log;

import com.example.task_general.company.Company;
import com.example.task_general.company.CompanyService;
import com.example.task_general.dtos.entitiesDTO.LogDTO;
import com.example.task_general.dtos.entitiesDTO.LogFilters;
import com.example.task_general.exceptions.BadRequestException;
import com.example.task_general.exceptions.InternalServerException;
import com.example.task_general.project.Project;
import com.example.task_general.project.ProjectRepository;
import com.example.task_general.project.ProjectState;
import com.example.task_general.projectType.ProjectType;
import com.example.task_general.user.User;
import com.example.task_general.user.UserService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;
    private final CompanyService companyService;
    private final UserService userService;


    public Log save(LogDTO logDTO) {
        Company company = new Company();
        User user = new User();
        if (null == logDTO.userId() && null == logDTO.companyId()) {
            throw new BadRequestException("Almeno uno fra company e user id deve essere presente");
        }
        if (null != logDTO.companyId()) {
            company = companyService.findById(logDTO.companyId());
        }
        if (null != logDTO.userId()) {
            user = userService.findById(logDTO.companyId());
        }

        Log log = new Log();
        log.setLogType(LogType.valueOf(logDTO.logType()));
        log.setResourceId(logDTO.idRisorsa());
        log.setResourceName(logDTO.nomeRisorsa());
        log.setCreationDate(LocalDate.now());
        log.setCreationTime(Instant.now());
        if (null != company.getId()) {
            log.setCompany(company);
        }
        if (null != user.getId()) {
            log.setUser(user);
        }
        createText(log);
        return logRepository.save(log);
    }


    public List<Log> findAllByCompanyId(Long companyId, LogFilters logFilters) {
        try {
            Specification<Log> spec = new Specification<Log>() {
                @Override
                public @Nullable Predicate toPredicate(Root<Log> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    return null;
                }
            };
            if (companyId != null) {
                spec = LogRepository.companyIdEquals(companyId);
            }
            if (logFilters.logType() != null) {
                LogType logType = LogType.valueOf(logFilters.logType());
                spec = LogRepository.logTypeEquals(logType);
            }
            if (null != logFilters.creationDateFrom() && null != logFilters.creationDateTo()) {
                LocalDate from = LocalDate.parse(logFilters.creationDateFrom());
                LocalDate to = LocalDate.parse(logFilters.creationDateTo());
                spec = LogRepository.creationDateBetween(from, to);
            } else if (logFilters.creationDateFrom() != null) {
                LocalDate from = LocalDate.parse(logFilters.creationDateFrom());
                spec = LogRepository.creationDateFrom(from);
            } else if (logFilters.creationDateTo() != null) {
                LocalDate to = LocalDate.parse(logFilters.creationDateTo());
                spec = LogRepository.creationDateTo(to);
            }

            if (null != logFilters.creationTimeFrom() && null != logFilters.creationTimeTo()) {
                Instant from = Instant.parse(logFilters.creationTimeFrom());
                Instant to = Instant.parse(logFilters.creationTimeTo());
                spec = LogRepository.creationTimeBetween(from, to);
            } else if (logFilters.creationTimeFrom() != null) {
                Instant from = Instant.parse(logFilters.creationTimeFrom());
                spec = LogRepository.creationTimeFrom(from);
            } else if (logFilters.creationTimeTo() != null) {
                Instant to = Instant.parse(logFilters.creationTimeTo());
                spec = LogRepository.creationTimeTo(to);
            }

            return logRepository.findAll(spec);
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException("Qualcosa è successo internamente. Risolviamo subito.");
        }
    }

    private void createText(Log log) {

        var type = log.getLogType();


        switch (type) {
            case TASK_CREATION -> log.setText("Creato task " + log.getResourceName());
            case TASK_DELETION -> log.setText("Cancellato task " + log.getResourceName());
            case TASK_COMPLETION -> log.setText("Completato task " + log.getResourceName());
            case PROJECT_CREATION -> log.setText("Creato progetto " + log.getResourceName());
            case PROJECT_DELETION -> log.setText("Cancellato progetto " + log.getResourceName());
            case PROJECT_COMPLETION -> log.setText("Completato progetto " + log.getResourceName());
            case USER_CREATION -> log.setText("Creato user " + log.getResourceName());
            case USER_DELETION -> log.setText("Cancellato user " + log.getResourceName());
        }
    }
}

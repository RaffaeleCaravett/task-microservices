package com.example.task_general.log;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;

@Repository
public interface LogRepository extends JpaRepository<Log, Long>, JpaSpecificationExecutor<Log> {

    static Specification<Log> companyIdEquals(Long id) {
        if (id == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("company").get("id"), id);
    }

    static Specification<Log> logTypeEquals(LogType logType) {
        if (logType == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("logType"), logType);
    }

    static Specification<Log> creationDateBetween(LocalDate from, LocalDate to) {
        if (from == null || to == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("creationDate"), from, to);
    }

    static Specification<Log> creationTimeBetween(Instant from, Instant to) {
        if (from == null || to == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("creationTime"), from, to);
    }

    static Specification<Log> creationTimeFrom(Instant from) {
        if (from == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("creationTime"), from);
    }

    static Specification<Log> creationTimeTo(Instant to) {
        if (to == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("creationTime"), to);
    }

    static Specification<Log> creationDateFrom(LocalDate from) {
        if (from == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("creationDate"), from);
    }

    static Specification<Log> creationDateTo(LocalDate to) {
        if (to == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("creationDate"), to);
    }
}

package com.example.task_general.secretCode;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface SecretCodeRepository extends JpaRepository<SecretCode,Long>, JpaSpecificationExecutor<SecretCode> {

    static Specification<SecretCode> createdAtMinusThan(LocalDate createdAt){
        if(createdAt == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"),createdAt);
    }
    static Specification<SecretCode> notifies(Boolean notified){
        if(notified == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("notified"),notified);
    }

    SecretCode findByUser_Id(Long userId);
}

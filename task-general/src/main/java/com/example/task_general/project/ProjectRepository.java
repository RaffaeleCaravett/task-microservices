package com.example.task_general.project;

import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {

    static Specification<Project> companyIdEquals(Long id) {
        if (id == null) {
            return null;
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("company").get("id"), id);
    }

    static Specification<Project> nameContains(String name) {
        if (name == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("title")), "%" + name.toUpperCase() + "%");
    }

    static Specification<Project> descriptionContains(String description) {
        if (description == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("description")), "%" + description.toUpperCase() + "%");
    }

    static Specification<Project> managerNameContains(String name) {
        if (name == null) {
            return null;
        }

        return (root, query, criteriaBuilder) -> {
            Expression<String> e = criteriaBuilder.concat(root.get("manager").get("nome"), " ");
            e = criteriaBuilder.concat(e , root.get("manager").get("cognome"));
          return criteriaBuilder.like(criteriaBuilder.upper(e), "%" + name.toUpperCase() + "%");
        };
    }

    static Specification<Project> stateEquals(ProjectState state) {
        if (state == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("projectState"), state);
    }

    static Specification<Project> typeEquals(Long type) {
        if (type == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("projectType").get("id"), type);
    }

    static Specification<Project> createdAtGreaterThanOrEqualTo(LocalDate createdAt) {
        if (createdAt == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), createdAt);
    }
}

package com.example.task_general.project;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> , JpaSpecificationExecutor<Project> {

    static Specification<Project> companyIdEquals(Long id){
        if(id == null){
            return null;
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("company").get("id"),id);
    }
    static Specification<Project> nameContains(String name){
        if(name==null || name.isBlank() || name.isEmpty()){
            return null;
        }
        return  (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("title")),"%" + name.toUpperCase()+"%");
    }
}

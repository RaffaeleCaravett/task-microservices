package com.example.task_general.user;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    static Specification<User> emailContains(String email) {
        if (email == null ) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("email")), "%" + email.toUpperCase() + "%");
    }


    static Specification<User> fullnameContains(String nome) {
        if (nome == null) return null;
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.concat(criteriaBuilder.concat(root.get("nome"), " "), root.get("cognome")), "%" + nome.toUpperCase() + "%");
    }

    static Specification<User> statusEquals(Boolean status) {
        if (status == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isConfirmed"), status);
    }
}

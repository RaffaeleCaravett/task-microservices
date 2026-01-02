package com.example.task_general.codiceAccesso;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodiceAccessoRepository extends JpaRepository<CodiceAccesso,Long>, JpaSpecificationExecutor<CodiceAccesso> {
    Optional<CodiceAccesso> findByUser_Id(Long id);
}

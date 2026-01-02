package com.example.task_auth.indirizzo.regione;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegioneRepository extends JpaRepository<Regione, Long>, JpaSpecificationExecutor<Regione> {
    List<Regione> findAllByNazione_Id(Long id);
}

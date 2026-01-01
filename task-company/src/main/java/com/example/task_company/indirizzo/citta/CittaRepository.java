package com.example.task_company.indirizzo.citta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CittaRepository extends JpaRepository<Citta, Long>, JpaSpecificationExecutor<Citta> {
    List<Citta> findAllByRegione_Id(Long id);
}